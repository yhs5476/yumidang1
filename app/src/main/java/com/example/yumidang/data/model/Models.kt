package com.example.yumidang.data.model

enum class CompanionCategory(val label: String, val iconText: String) {
    ALL("전체", "✨"),
    HOBBY("취미/전시", "🎨"),
    DINING("맛집/카페", "☕"),
    FITNESS("운동/산책", "🏃"),
    TRAVEL("여행/나들이", "✈️"),
    ASSISTANCE("생활/돌봄", "🤝")
}

enum class PostStatus(val label: String) {
    RECRUITING("모집중"),
    CLOSED("모집마감"),
    CANCELLED("취소됨")
}

enum class RequestStatus(val label: String) {
    PENDING("수락대기"),
    ACCEPTED("동행확정"),
    REJECTED("거절됨"),
    CANCELLED("취소됨")
}

enum class ReportStatus(val label: String) {
    PENDING("접수완료"),
    WARNED("경고조치"),
    RESTRICTED("공고제한"),
    SUSPENDED("이용정지"),
    DISMISSED("이상없음")
}

data class UserProfile(
    val id: String,
    val name: String,
    val gender: String, // "여성" | "남성"
    val age: Int,
    val phone: String,
    val isPhoneVerified: Boolean = true,
    val isKycVerified: Boolean = false,
    val kycVerifiedDate: String? = null,
    val bio: String = "",
    val interests: List<String> = emptyList(),
    val mannerScore: Double = 36.5, // 매너온도 (기본 36.5도)
    val completedCompanionsCount: Int = 0,
    val positiveReviewTags: List<String> = emptyList()
)

data class CompanionPost(
    val id: String,
    val authorId: String,
    val authorName: String,
    val authorGender: String,
    val authorAge: Int,
    val authorKycVerified: Boolean,
    val authorMannerScore: Double,
    val category: CompanionCategory,
    val title: String,
    val content: String,
    val dateTime: String, // e.g., "2026.09.12 (토) 14:00"
    val location: String, // e.g., "서울 종로구 삼청동 국립현대미술관"
    val area: String, // e.g., "종로구"
    val conditionGender: String = "성별 무관", // "여성 전용", "성별 무관"
    val conditionAge: String = "연령 무관", // "20-30대", "연령 무관"
    val expenseType: String = "각자 부담", // "각자 부담", "작성자 부담", "협의"
    val status: PostStatus = PostStatus.RECRUITING,
    val createdAt: Long = System.currentTimeMillis()
)

data class JoinRequest(
    val id: String,
    val postId: String,
    val postTitle: String,
    val postDateTime: String,
    val postLocation: String,
    val postAuthorId: String,
    val requesterId: String,
    val requesterName: String,
    val requesterGender: String,
    val requesterAge: Int,
    val requesterKycVerified: Boolean,
    val requesterMannerScore: Double,
    val message: String,
    val status: RequestStatus = RequestStatus.PENDING,
    val createdAt: Long = System.currentTimeMillis(),
    val isEvaluatedByRequester: Boolean = false,
    val isEvaluatedByAuthor: Boolean = false
)

data class MutualReview(
    val id: String,
    val requestId: String,
    val reviewerId: String,
    val reviewerName: String,
    val targetUserId: String,
    val targetUserName: String,
    val rating: Int, // 1 to 5
    val tags: List<String>,
    val comment: String,
    val isBothSubmitted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

data class ProOffer(
    val id: String,
    val providerId: String,
    val providerName: String,
    val providerGender: String,
    val providerAge: Int,
    val providerKycVerified: Boolean,
    val providerMannerScore: Double,
    val categoryTitle: String,
    val title: String,
    val description: String,
    val priceKrw: Int,
    val schedule: String,
    val includes: String,
    val excludes: String,
    val restrictedPolicyNotice: String = "※ 간병·의료 등 자격 및 법적 검토가 필요한 전문 의료/간호 영역은 서비스 제공이 엄격히 제한됩니다.",
    val status: String = "AVAILABLE", // AVAILABLE, BOOKED, COMPLETED
    val bookedByUserId: String? = null
)

data class Report(
    val id: String,
    val reporterId: String,
    val reporterName: String,
    val targetType: String, // "POST" | "USER"
    val targetId: String,
    val targetSummary: String,
    val reasonCategory: String,
    val description: String,
    val status: ReportStatus = ReportStatus.PENDING,
    val adminActionNotes: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)
