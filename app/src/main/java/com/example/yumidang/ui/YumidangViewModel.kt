package com.example.yumidang.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yumidang.data.model.CompanionCategory
import com.example.yumidang.data.model.CompanionPost
import com.example.yumidang.data.model.JoinRequest
import com.example.yumidang.data.model.MutualReview
import com.example.yumidang.data.model.PostStatus
import com.example.yumidang.data.model.ProOffer
import com.example.yumidang.data.model.Report
import com.example.yumidang.data.model.ReportStatus
import com.example.yumidang.data.model.UserProfile
import com.example.yumidang.data.repository.YumidangRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class YumidangViewModel(
    private val repository: YumidangRepository = YumidangRepository()
) : ViewModel() {

    val currentUser: StateFlow<UserProfile?> = repository.currentUser
    val joinRequests: StateFlow<List<JoinRequest>> = repository.joinRequests
    val reviews: StateFlow<List<MutualReview>> = repository.reviews
    val proOffers: StateFlow<List<ProOffer>> = repository.proOffers
    val reports: StateFlow<List<Report>> = repository.reports
    val blockedUserIds: StateFlow<Set<String>> = repository.blockedUserIds

    // Filtering
    private val _selectedCategory = MutableStateFlow(CompanionCategory.ALL)
    val selectedCategory: StateFlow<CompanionCategory> = _selectedCategory.asStateFlow()

    private val _selectedArea = MutableStateFlow("전체")
    val selectedArea: StateFlow<String> = _selectedArea.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Filtered Posts taking into account category, area, blocked users
    val filteredPosts: StateFlow<List<CompanionPost>> = combine(
        repository.posts,
        _selectedCategory,
        _selectedArea,
        _searchQuery,
        repository.blockedUserIds
    ) { posts, category, area, query, blocked ->
        posts.filter { post ->
            val notBlocked = post.authorId !in blocked
            val categoryMatches = category == CompanionCategory.ALL || post.category == category
            val areaMatches = area == "전체" || post.area == area
            val queryMatches = query.isBlank() ||
                    post.title.contains(query, ignoreCase = true) ||
                    post.content.contains(query, ignoreCase = true) ||
                    post.location.contains(query, ignoreCase = true)
            notBlocked && categoryMatches && areaMatches && queryMatches
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // UI state popups & selections
    private val _selectedPost = MutableStateFlow<CompanionPost?>(null)
    val selectedPost: StateFlow<CompanionPost?> = _selectedPost.asStateFlow()

    private val _selectedOffer = MutableStateFlow<ProOffer?>(null)
    val selectedOffer: StateFlow<ProOffer?> = _selectedOffer.asStateFlow()

    private val _reviewingRequest = MutableStateFlow<JoinRequest?>(null)
    val reviewingRequest: StateFlow<JoinRequest?> = _reviewingRequest.asStateFlow()

    private val _reportingTarget = MutableStateFlow<Triple<String, String, String>?>(null) // type, id, summary
    val reportingTarget: StateFlow<Triple<String, String, String>?> = _reportingTarget.asStateFlow()

    private val _showSafetyDialog = MutableStateFlow(false)
    val showSafetyDialog: StateFlow<Boolean> = _showSafetyDialog.asStateFlow()

    private val _showKycSheet = MutableStateFlow(false)
    val showKycSheet: StateFlow<Boolean> = _showKycSheet.asStateFlow()

    private val _snackBarMessage = MutableStateFlow<String?>(null)
    val snackBarMessage: StateFlow<String?> = _snackBarMessage.asStateFlow()

    fun setCategory(category: CompanionCategory) {
        _selectedCategory.value = category
    }

    fun setArea(area: String) {
        _selectedArea.value = area
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun openPostDetail(post: CompanionPost) {
        _selectedPost.value = post
    }

    fun closePostDetail() {
        _selectedPost.value = null
    }

    fun openOfferDetail(offer: ProOffer) {
        _selectedOffer.value = offer
    }

    fun closeOfferDetail() {
        _selectedOffer.value = null
    }

    fun openReviewDialog(request: JoinRequest) {
        _reviewingRequest.value = request
    }

    fun closeReviewDialog() {
        _reviewingRequest.value = null
    }

    fun openReportDialog(targetType: String, targetId: String, summary: String) {
        _reportingTarget.value = Triple(targetType, targetId, summary)
    }

    fun closeReportDialog() {
        _reportingTarget.value = null
    }

    fun setSafetyDialogVisible(show: Boolean) {
        _showSafetyDialog.value = show
    }

    fun setKycSheetVisible(show: Boolean) {
        _showKycSheet.value = show
    }

    fun clearSnackBar() {
        _snackBarMessage.value = null
    }

    fun showMessage(msg: String) {
        _snackBarMessage.value = msg
    }

    // Auth & Profile Actions
    fun login(name: String, phone: String) {
        repository.login(name, phone)
        showMessage("${name}님, 로그인되었습니다.")
    }

    fun signup(
        name: String,
        gender: String,
        age: Int,
        phone: String,
        bio: String,
        interests: List<String>
    ) {
        repository.signup(name, gender, age, phone, bio, interests)
        showMessage("${name}님, 회원가입이 완료되었습니다!")
    }

    fun logout() {
        repository.logout()
        showMessage("로그아웃 되었습니다.")
    }

    fun completeKyc() {
        repository.completeKycVerification()
        _showKycSheet.value = false
        showMessage("선택형 KYC 인증이 완료되어 프로필에 안심 인증 마크가 표시됩니다.")
    }

    fun updateProfile(name: String, bio: String, interests: List<String>) {
        repository.updateProfile(name, bio, interests)
        showMessage("프로필이 수정되었습니다.")
    }

    // Post Actions
    fun createPost(
        category: CompanionCategory,
        title: String,
        content: String,
        dateTime: String,
        location: String,
        area: String,
        conditionGender: String,
        conditionAge: String,
        expenseType: String
    ) {
        val newPost = repository.createPost(
            category, title, content, dateTime, location, area,
            conditionGender, conditionAge, expenseType
        )
        showMessage("동행 공고가 등록되었습니다.")
    }

    fun updatePost(
        postId: String,
        title: String,
        content: String,
        dateTime: String,
        location: String,
        conditionGender: String,
        conditionAge: String,
        expenseType: String
    ) {
        repository.updatePost(
            postId, title, content, dateTime, location,
            conditionGender, conditionAge, expenseType
        )
        // update selected post if currently opened
        _selectedPost.value?.let { current ->
            if (current.id == postId) {
                _selectedPost.value = current.copy(
                    title = title,
                    content = content,
                    dateTime = dateTime,
                    location = location,
                    conditionGender = conditionGender,
                    conditionAge = conditionAge,
                    expenseType = expenseType
                )
            }
        }
        showMessage("공고가 수정되었습니다.")
    }

    fun closePost(postId: String) {
        repository.changePostStatus(postId, PostStatus.CLOSED)
        _selectedPost.value?.let {
            if (it.id == postId) _selectedPost.value = it.copy(status = PostStatus.CLOSED)
        }
        showMessage("공고가 마감되었습니다.")
    }

    fun cancelPost(postId: String) {
        repository.changePostStatus(postId, PostStatus.CANCELLED)
        _selectedPost.value?.let {
            if (it.id == postId) _selectedPost.value = it.copy(status = PostStatus.CANCELLED)
        }
        showMessage("공고가 취소되었습니다.")
    }

    // Requests Actions
    fun applyForPost(postId: String, message: String) {
        repository.submitJoinRequest(postId, message)
        showMessage("참여 신청이 전송되었습니다. 작성자 수락 시 동행이 확정됩니다.")
    }

    fun respondToJoinRequest(requestId: String, accept: Boolean) {
        repository.respondToRequest(requestId, accept)
        if (accept) {
            showMessage("동행 요청을 수락했습니다. 동행이 확정되었습니다!")
        } else {
            showMessage("동행 요청을 거절했습니다.")
        }
    }

    fun cancelJoinRequest(requestId: String) {
        repository.cancelRequest(requestId)
        showMessage("참여 요청이 취소되었습니다.")
    }

    // Mutual Review
    fun submitMutualReview(
        requestId: String,
        targetUserId: String,
        targetUserName: String,
        rating: Int,
        tags: List<String>,
        comment: String
    ) {
        repository.submitReview(requestId, targetUserId, targetUserName, rating, tags, comment)
        _reviewingRequest.value = null
        showMessage("${targetUserName}님과의 상호 평가가 완료되었습니다. 당도가 갱신되었습니다.")
    }

    // Pro Offers
    fun createProOffer(
        categoryTitle: String,
        title: String,
        description: String,
        priceKrw: Int,
        schedule: String,
        includes: String,
        excludes: String
    ) {
        repository.createProOffer(
            categoryTitle, title, description, priceKrw, schedule, includes, excludes
        )
        showMessage("유료 전문 동행 오퍼가 등록되었습니다.")
    }

    fun bookProOffer(offerId: String) {
        repository.bookProOffer(offerId)
        _selectedOffer.value?.let {
            if (it.id == offerId) _selectedOffer.value = it.copy(status = "BOOKED")
        }
        showMessage("전문 동행 예약이 접수되었습니다.")
    }

    fun cancelProOfferBooking(offerId: String) {
        repository.cancelProOfferBooking(offerId)
        _selectedOffer.value?.let {
            if (it.id == offerId) _selectedOffer.value = it.copy(status = "AVAILABLE")
        }
        showMessage("예약이 취소되었습니다.")
    }

    // Safety & Admin
    fun submitReport(reasonCategory: String, details: String) {
        val target = _reportingTarget.value ?: return
        repository.submitReport(
            targetType = target.first,
            targetId = target.second,
            targetSummary = target.third,
            reasonCategory = reasonCategory,
            description = details
        )
        _reportingTarget.value = null
        showMessage("신고가 접수되었습니다. 운영 관리팀에서 신속히 검토하겠습니다.")
    }

    fun resolveAdminReport(reportId: String, status: ReportStatus, note: String) {
        repository.resolveReport(reportId, status, note)
        showMessage("신고 건에 대해 ${status.label} 처리가 완료되었습니다.")
    }

    fun blockUser(targetUserId: String, targetUserName: String) {
        repository.blockUser(targetUserId)
        _selectedPost.value = null
        showMessage("${targetUserName}님을 차단했습니다. 더 이상 상호 노출되지 않습니다.")
    }

    fun unblockUser(targetUserId: String) {
        repository.unblockUser(targetUserId)
        showMessage("차단이 해제되었습니다.")
    }
}
