package com.example.yumidang.data.repository

import com.example.yumidang.data.model.CompanionCategory
import com.example.yumidang.data.model.CompanionPost
import com.example.yumidang.data.model.JoinRequest
import com.example.yumidang.data.model.MutualReview
import com.example.yumidang.data.model.PostStatus
import com.example.yumidang.data.model.ProOffer
import com.example.yumidang.data.model.Report
import com.example.yumidang.data.model.ReportStatus
import com.example.yumidang.data.model.RequestStatus
import com.example.yumidang.data.model.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class YumidangRepository {

    // Current logged-in user
    private val _currentUser = MutableStateFlow<UserProfile?>(
        UserProfile(
            id = "user_me",
            name = "김민서",
            gender = "여성",
            age = 28,
            phone = "010-4829-1029",
            isPhoneVerified = true,
            isKycVerified = true,
            kycVerifiedDate = "2026.08.15",
            bio = "주말 미술관 전시 관람과 조용한 북카페 투어를 좋아합니다. 매너 있고 편안한 동행을 환영해요!",
            interests = listOf("전시회", "카페투어", "러닝", "필름카메라"),
            mannerScore = 38.2,
            completedCompanionsCount = 5,
            positiveReviewTags = listOf("시간 약속을 잘 지켜요", "대화가 편안해요", "배려심이 넘쳐요")
        )
    )
    val currentUser: StateFlow<UserProfile?> = _currentUser.asStateFlow()

    // Posts
    private val _posts = MutableStateFlow<List<CompanionPost>>(createInitialPosts())
    val posts: StateFlow<List<CompanionPost>> = _posts.asStateFlow()

    // Join Requests
    private val _joinRequests = MutableStateFlow<List<JoinRequest>>(createInitialRequests())
    val joinRequests: StateFlow<List<JoinRequest>> = _joinRequests.asStateFlow()

    // Reviews
    private val _reviews = MutableStateFlow<List<MutualReview>>(createInitialReviews())
    val reviews: StateFlow<List<MutualReview>> = _reviews.asStateFlow()

    // Pro Offers
    private val _proOffers = MutableStateFlow<List<ProOffer>>(createInitialProOffers())
    val proOffers: StateFlow<List<ProOffer>> = _proOffers.asStateFlow()

    // Reports
    private val _reports = MutableStateFlow<List<Report>>(createInitialReports())
    val reports: StateFlow<List<Report>> = _reports.asStateFlow()

    // Blocked users
    private val _blockedUserIds = MutableStateFlow<Set<String>>(emptySet())
    val blockedUserIds: StateFlow<Set<String>> = _blockedUserIds.asStateFlow()

    // Authentication & Profile
    fun login(name: String, phone: String): Boolean {
        val user = UserProfile(
            id = "user_me",
            name = name,
            gender = "여성",
            age = 27,
            phone = phone,
            isPhoneVerified = true,
            isKycVerified = false,
            bio = "새로운 동행을 기대하고 있어요!",
            interests = listOf("전시회", "맛집", "산책"),
            mannerScore = 36.5,
            completedCompanionsCount = 0
        )
        _currentUser.value = user
        return true
    }

    fun signup(
        name: String,
        gender: String,
        age: Int,
        phone: String,
        bio: String,
        interests: List<String>
    ): UserProfile {
        val newUser = UserProfile(
            id = "user_${UUID.randomUUID().toString().take(6)}",
            name = name,
            gender = gender,
            age = age,
            phone = phone,
            isPhoneVerified = true,
            isKycVerified = false,
            bio = bio,
            interests = interests,
            mannerScore = 36.5,
            completedCompanionsCount = 0
        )
        _currentUser.value = newUser
        return newUser
    }

    fun logout() {
        _currentUser.value = null
    }

    fun completeKycVerification() {
        val current = _currentUser.value ?: return
        _currentUser.value = current.copy(
            isKycVerified = true,
            kycVerifiedDate = "2026.09.08"
        )
    }

    fun updateProfile(name: String, bio: String, interests: List<String>) {
        val current = _currentUser.value ?: return
        _currentUser.value = current.copy(
            name = name,
            bio = bio,
            interests = interests
        )
    }

    // Companion Posts
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
    ): CompanionPost {
        val author = _currentUser.value ?: error("User not logged in")
        val post = CompanionPost(
            id = "post_${UUID.randomUUID().toString().take(6)}",
            authorId = author.id,
            authorName = author.name,
            authorGender = author.gender,
            authorAge = author.age,
            authorKycVerified = author.isKycVerified,
            authorMannerScore = author.mannerScore,
            category = category,
            title = title,
            content = content,
            dateTime = dateTime,
            location = location,
            area = area,
            conditionGender = conditionGender,
            conditionAge = conditionAge,
            expenseType = expenseType,
            status = PostStatus.RECRUITING
        )
        _posts.value = listOf(post) + _posts.value
        return post
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
        _posts.value = _posts.value.map { post ->
            if (post.id == postId) {
                post.copy(
                    title = title,
                    content = content,
                    dateTime = dateTime,
                    location = location,
                    conditionGender = conditionGender,
                    conditionAge = conditionAge,
                    expenseType = expenseType
                )
            } else post
        }
    }

    fun changePostStatus(postId: String, newStatus: PostStatus) {
        _posts.value = _posts.value.map {
            if (it.id == postId) it.copy(status = newStatus) else it
        }
    }

    // Join Requests & Confirmation
    fun submitJoinRequest(postId: String, message: String): JoinRequest {
        val user = _currentUser.value ?: error("User not logged in")
        val post = _posts.value.find { it.id == postId } ?: error("Post not found")
        val request = JoinRequest(
            id = "req_${UUID.randomUUID().toString().take(6)}",
            postId = post.id,
            postTitle = post.title,
            postDateTime = post.dateTime,
            postLocation = post.location,
            postAuthorId = post.authorId,
            requesterId = user.id,
            requesterName = user.name,
            requesterGender = user.gender,
            requesterAge = user.age,
            requesterKycVerified = user.isKycVerified,
            requesterMannerScore = user.mannerScore,
            message = message,
            status = RequestStatus.PENDING
        )
        _joinRequests.value = listOf(request) + _joinRequests.value
        return request
    }

    fun respondToRequest(requestId: String, accept: Boolean) {
        _joinRequests.value = _joinRequests.value.map { req ->
            if (req.id == requestId) {
                val newStatus = if (accept) RequestStatus.ACCEPTED else RequestStatus.REJECTED
                req.copy(status = newStatus)
            } else req
        }
    }

    fun cancelRequest(requestId: String) {
        _joinRequests.value = _joinRequests.value.map {
            if (it.id == requestId) it.copy(status = RequestStatus.CANCELLED) else it
        }
    }

    // Mutual Review
    fun submitReview(
        requestId: String,
        targetUserId: String,
        targetUserName: String,
        rating: Int,
        tags: List<String>,
        comment: String
    ) {
        val user = _currentUser.value ?: return
        val newReview = MutualReview(
            id = "rev_${UUID.randomUUID().toString().take(6)}",
            requestId = requestId,
            reviewerId = user.id,
            reviewerName = user.name,
            targetUserId = targetUserId,
            targetUserName = targetUserName,
            rating = rating,
            tags = tags,
            comment = comment,
            isBothSubmitted = true // Prototype marks as mutual for seamless feedback display
        )
        _reviews.value = listOf(newReview) + _reviews.value

        // Update join request evaluated flag
        _joinRequests.value = _joinRequests.value.map { req ->
            if (req.id == requestId) {
                if (req.requesterId == user.id) {
                    req.copy(isEvaluatedByRequester = true)
                } else {
                    req.copy(isEvaluatedByAuthor = true)
                }
            } else req
        }

        // Increase current user completed count & manner temperature
        _currentUser.value = user.copy(
            completedCompanionsCount = user.completedCompanionsCount + 1,
            mannerScore = kotlin.math.min(99.0, user.mannerScore + 0.3)
        )
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
    ): ProOffer {
        val user = _currentUser.value ?: error("Not logged in")
        val offer = ProOffer(
            id = "pro_${UUID.randomUUID().toString().take(6)}",
            providerId = user.id,
            providerName = user.name,
            providerGender = user.gender,
            providerAge = user.age,
            providerKycVerified = user.isKycVerified,
            providerMannerScore = user.mannerScore,
            categoryTitle = categoryTitle,
            title = title,
            description = description,
            priceKrw = priceKrw,
            schedule = schedule,
            includes = includes,
            excludes = excludes,
            status = "AVAILABLE"
        )
        _proOffers.value = listOf(offer) + _proOffers.value
        return offer
    }

    fun bookProOffer(offerId: String) {
        val user = _currentUser.value ?: return
        _proOffers.value = _proOffers.value.map {
            if (it.id == offerId) it.copy(status = "BOOKED", bookedByUserId = user.id) else it
        }
    }

    fun cancelProOfferBooking(offerId: String) {
        _proOffers.value = _proOffers.value.map {
            if (it.id == offerId) it.copy(status = "AVAILABLE", bookedByUserId = null) else it
        }
    }

    // Safety, Reports & Blocking
    fun submitReport(
        targetType: String,
        targetId: String,
        targetSummary: String,
        reasonCategory: String,
        description: String
    ): Report {
        val user = _currentUser.value ?: error("Not logged in")
        val report = Report(
            id = "rep_${UUID.randomUUID().toString().take(6)}",
            reporterId = user.id,
            reporterName = user.name,
            targetType = targetType,
            targetId = targetId,
            targetSummary = targetSummary,
            reasonCategory = reasonCategory,
            description = description,
            status = ReportStatus.PENDING
        )
        _reports.value = listOf(report) + _reports.value
        return report
    }

    fun resolveReport(reportId: String, newStatus: ReportStatus, note: String) {
        _reports.value = _reports.value.map {
            if (it.id == reportId) it.copy(status = newStatus, adminActionNotes = note) else it
        }
    }

    fun blockUser(targetUserId: String) {
        _blockedUserIds.value = _blockedUserIds.value + targetUserId
    }

    fun unblockUser(targetUserId: String) {
        _blockedUserIds.value = _blockedUserIds.value - targetUserId
    }

    // Seed Data Setup
    private fun createInitialPosts(): List<CompanionPost> {
        return listOf(
            CompanionPost(
                id = "post_1",
                authorId = "user_2",
                authorName = "박서연",
                authorGender = "여성",
                authorAge = 26,
                authorKycVerified = true,
                authorMannerScore = 39.1,
                category = CompanionCategory.HOBBY,
                title = "국립현대미술관 서울관 특별전 조용히 함께 관람해요",
                content = "이번 주말 삼청동 국립현대미술관 기획 전시 함께 보실 분 찾습니다! 평소 현대미술에 관심 많으신 분이면 더 반가워요. 관람 후 근처 조용한 카페에서 커피 한잔 가볍게 나눠요 :)",
                dateTime = "2026.09.13 (일) 14:30",
                location = "서울 종로구 삼청로 30 국립현대미술관 서울 로비",
                area = "종로구",
                conditionGender = "여성 전용",
                conditionAge = "20-30대",
                expenseType = "각자 부담",
                status = PostStatus.RECRUITING
            ),
            CompanionPost(
                id = "post_2",
                authorId = "user_3",
                authorName = "정다은",
                authorGender = "여성",
                authorAge = 31,
                authorKycVerified = true,
                authorMannerScore = 41.5,
                category = CompanionCategory.DINING,
                title = "성수동 새로 오픈한 화덕피자 맛집 2인 런치 동행",
                content = "평소 가보고 싶었던 성수동 나폴리 피자 맛집인데 1인 방문하기 애매해서 동행 요청드려요! 맛있는 점심 같이 먹고 편하게 수다 떨어요.",
                dateTime = "2026.09.14 (월) 12:00",
                location = "서울 성동구 연무장길 성수역 3번 출구 앞",
                area = "성동구",
                conditionGender = "여성 전용",
                conditionAge = "연령 무관",
                expenseType = "각자 부담",
                status = PostStatus.RECRUITING
            ),
            CompanionPost(
                id = "post_3",
                authorId = "user_4",
                authorName = "한수진",
                authorGender = "여성",
                authorAge = 29,
                authorKycVerified = false,
                authorMannerScore = 37.0,
                category = CompanionCategory.FITNESS,
                title = "퇴근 후 여의도 한강공원 5km 가벼운 시티런 함께해요",
                content = "혼자 뛰기 심심해서 1:1 페이스메이커로 함께 달려주실 분 구합니다! 페이스는 6분 30초 정도로 천천히 여유롭게 뛸 예정이에요. 초보 러너분 환영합니다.",
                dateTime = "2026.09.15 (화) 19:30",
                location = "서울 영등포구 여의나루역 2번 출구 광장",
                area = "영등포구",
                conditionGender = "성별 무관",
                conditionAge = "20-30대",
                expenseType = "작성자 부담 (음료 제공)",
                status = PostStatus.RECRUITING
            ),
            CompanionPost(
                id = "post_4",
                authorId = "user_5",
                authorName = "윤채원",
                authorGender = "여성",
                authorAge = 34,
                authorKycVerified = true,
                authorMannerScore = 42.0,
                category = CompanionCategory.TRAVEL,
                title = "강릉 당일치기 가을 바다 드라이브 & 안목 커피거리",
                content = "서울에서 출발해 당일치기로 강릉 안목해변과 정동진 둘러보실 분 모셔요. 자차로 이동하며 유류비는 각자 나누면 좋겠습니다. 맑은 공기 마시며 힐링해요!",
                dateTime = "2026.09.19 (토) 08:30",
                location = "서울 양재역 환승주차장 앞 집결",
                area = "서초구",
                conditionGender = "여성 전용",
                conditionAge = "20-40대",
                expenseType = "협의 (유류비 1/N)",
                status = PostStatus.RECRUITING
            ),
            CompanionPost(
                id = "post_5",
                authorId = "user_me",
                authorName = "김민서",
                authorGender = "여성",
                authorAge = 28,
                authorKycVerified = true,
                authorMannerScore = 38.2,
                category = CompanionCategory.HOBBY,
                title = "망원동 독립서점 북토크 & 산책 같이 가실 분",
                content = "제가 올린 공고입니다. 망원 한강공원 산책 겸 소규모 독립서점 투어 같이 가요!",
                dateTime = "2026.09.16 (수) 15:00",
                location = "서울 마포구 망원역 2번 출구",
                area = "마포구",
                conditionGender = "여성 전용",
                conditionAge = "20-30대",
                expenseType = "각자 부담",
                status = PostStatus.RECRUITING
            )
        )
    }

    private fun createInitialRequests(): List<JoinRequest> {
        return listOf(
            // Received request on user_me's post
            JoinRequest(
                id = "req_1",
                postId = "post_5",
                postTitle = "망원동 독립서점 북토크 & 산책 같이 가실 분",
                postDateTime = "2026.09.16 (수) 15:00",
                postLocation = "서울 마포구 망원역 2번 출구",
                postAuthorId = "user_me",
                requesterId = "user_6",
                requesterName = "송예린",
                requesterGender = "여성",
                requesterAge = 27,
                requesterKycVerified = true,
                requesterMannerScore = 40.2,
                message = "안녕하세요! 저도 망원동 서점들을 정말 좋아해서 꼭 함께 걷고 싶어요. 평소 에세이 즐겨 읽습니다 :)",
                status = RequestStatus.PENDING
            ),
            // User sent request to post_1
            JoinRequest(
                id = "req_2",
                postId = "post_1",
                postTitle = "국립현대미술관 서울관 특별전 조용히 함께 관람해요",
                postDateTime = "2026.09.13 (일) 14:30",
                postLocation = "서울 종로구 삼청로 30 국립현대미술관 서울 로비",
                postAuthorId = "user_2",
                requesterId = "user_me",
                requesterName = "김민서",
                requesterGender = "여성",
                requesterAge = 28,
                requesterKycVerified = true,
                requesterMannerScore = 38.2,
                message = "안녕하세요 서연님! 이번 전시 꼭 보고 싶었는데 마침 좋은 공고를 올려주셔서 신청합니다. 편안한 관람 약속드려요.",
                status = RequestStatus.ACCEPTED // Confirmed!
            )
        )
    }

    private fun createInitialReviews(): List<MutualReview> {
        return listOf(
            MutualReview(
                id = "rev_1",
                requestId = "req_past_1",
                reviewerId = "user_past",
                reviewerName = "최은지",
                targetUserId = "user_me",
                targetUserName = "김민서",
                rating = 5,
                tags = listOf("시간 약속을 잘 지켜요", "대화가 편안해요", "배려심이 넘쳐요"),
                comment = "약속 시간보다 10분 일찍 오시고 맛집도 같이 기분좋게 다녀왔어요. 다음에도 기회되면 또 만나요!",
                isBothSubmitted = true
            )
        )
    }

    private fun createInitialProOffers(): List<ProOffer> {
        return listOf(
            ProOffer(
                id = "pro_1",
                providerId = "user_pro_1",
                providerName = "오준석",
                providerGender = "남성",
                providerAge = 32,
                providerKycVerified = true,
                providerMannerScore = 45.3,
                categoryTitle = "스냅 사진 촬영 동행",
                title = "인생샷 전문 포토그래퍼의 서울 핫플 스냅 동행",
                description = "북촌 한옥마을, 성수동 등 감성적인 거리에서 자연스러운 고화질 사진을 촬영해 드립니다. 보정본 20장 포함.",
                priceKrw = 40000,
                schedule = "주말 2시간 기준 (사전 조율)",
                includes = "원본 전체 제공, 색감 보정 20장, 촬영 장소 동행",
                excludes = "개인 음료비, 입장료"
            ),
            ProOffer(
                id = "pro_2",
                providerId = "user_pro_2",
                providerName = "강민주",
                providerGender = "여성",
                providerAge = 30,
                providerKycVerified = true,
                providerMannerScore = 44.8,
                categoryTitle = "전시 도슨트 & 예술 해설",
                title = "국립중앙박물관 및 주요 미술관 1:1 맞춤 도슨트",
                description = "미술사 전공자가 알기 쉽고 재미있게 시대 배경과 작품의 숨은 이야기를 들려드립니다. 혼자서는 놓치기 쉬운 감상 포인트를 짚어드려요.",
                priceKrw = 35000,
                schedule = "평일/주말 1시간 30분",
                includes = "1:1 전문 해설, 감상 가이드 리플렛",
                excludes = "전시회 티켓비 (각자 예매)"
            ),
            ProOffer(
                id = "pro_3",
                providerId = "user_pro_3",
                providerName = "유지훈",
                providerGender = "남성",
                providerAge = 28,
                providerKycVerified = true,
                providerMannerScore = 42.6,
                categoryTitle = "러닝 & 페이스 코칭",
                title = "초보 러너를 위한 1:1 자세 교정 및 부상 방지 페이스메이커",
                description = "달리기 호흡법, 착지법 코칭과 함께 원하는 페이스로 안전하게 동행 페이스메이커 역할을 수행합니다.",
                priceKrw = 25000,
                schedule = "주 1회 60분 (야간 러닝 가능)",
                includes = "스트레칭 지도, 페이스 조절, 스포츠 이온음료",
                excludes = "개인 러닝화/운동복"
            )
        )
    }

    private fun createInitialReports(): List<Report> {
        return listOf(
            Report(
                id = "rep_1",
                reporterId = "user_2",
                reporterName = "박서연",
                targetType = "USER",
                targetId = "user_bad_1",
                targetSummary = "회원 '이상한사람' (신원 불명확)",
                reasonCategory = "사칭 및 허위 정보",
                description = "프로필 정보와 다른 목적으로 반복해서 연락을 시도합니다.",
                status = ReportStatus.PENDING
            )
        )
    }
}
