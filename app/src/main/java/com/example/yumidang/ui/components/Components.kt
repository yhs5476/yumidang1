package com.example.yumidang.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yumidang.data.model.PostStatus
import com.example.yumidang.data.model.RequestStatus
import com.example.yumidang.ui.theme.CoralPrimary
import com.example.yumidang.ui.theme.MannerWarm
import com.example.yumidang.ui.theme.SafetyBg
import com.example.yumidang.ui.theme.SafetyRed
import com.example.yumidang.ui.theme.StarGold
import com.example.yumidang.ui.theme.VerifiedGreen

@Composable
fun TrustKycBadge(
    isVerified: Boolean,
    modifier: Modifier = Modifier
) {
    if (isVerified) {
        Surface(
            modifier = modifier.testTag("kyc_verified_badge"),
            shape = RoundedCornerShape(12.dp),
            color = Color(0xFFE8F5E9),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF81C784))
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Security,
                    contentDescription = "KYC 인증 완료",
                    tint = VerifiedGreen,
                    modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    text = "KYC 인증",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = VerifiedGreen
                )
            }
        }
    } else {
        Surface(
            modifier = modifier.testTag("kyc_unverified_badge"),
            shape = RoundedCornerShape(12.dp),
            color = Color(0xFFF3F4F6)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.Shield,
                    contentDescription = "휴대폰 인증 완료",
                    tint = Color(0xFF6B7280),
                    modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    text = "기본인증",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF6B7280)
                )
            }
        }
    }
}

@Composable
fun MannerScoreBar(
    score: Double,
    modifier: Modifier = Modifier
) {
    val progress = (score / 100.0).coerceIn(0.0, 1.0).toFloat()
    val color = when {
        score >= 40.0 -> MannerWarm
        score >= 36.5 -> CoralPrimary
        else -> Color(0xFF6B7280)
    }

    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Thermostat,
                    contentDescription = "당도",
                    tint = color,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = "당도",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                text = "${"%.1f".format(score)} Brix",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = color,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
    }
}

@Composable
fun PostStatusBadge(
    status: PostStatus,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor) = when (status) {
        PostStatus.RECRUITING -> CoralPrimary to Color.White
        PostStatus.CLOSED -> Color(0xFF9CA3AF) to Color.White
        PostStatus.CANCELLED -> Color(0xFFEF4444) to Color.White
    }

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(6.dp),
        color = bgColor
    ) {
        Text(
            text = status.label,
            color = textColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        )
    }
}

@Composable
fun RequestStatusBadge(
    status: RequestStatus,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor) = when (status) {
        RequestStatus.PENDING -> Color(0xFFFEF3C7) to Color(0xFF92400E)
        RequestStatus.ACCEPTED -> Color(0xFFD1FAE5) to Color(0xFF065F46)
        RequestStatus.REJECTED -> Color(0xFFFEE2E2) to Color(0xFF991B1B)
        RequestStatus.CANCELLED -> Color(0xFFF3F4F6) to Color(0xFF6B7280)
    }

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(6.dp),
        color = bgColor
    ) {
        Text(
            text = status.label,
            color = textColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        )
    }
}

@Composable
fun SafetyNoticeCard(
    onViewRulesClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("safety_notice_card"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF3E5F5))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(Color(0xFFE1BEE7), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Security,
                    contentDescription = null,
                    tint = Color(0xFF7B1FA2),
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "유미당 안심 동행 수칙",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color(0xFF4A148C)
                )
                Text(
                    text = "낮 시간, 공개된 공공장소에서 만나요. 사전 송금 요구나 외부 연락처 유도는 거절하세요.",
                    fontSize = 12.sp,
                    color = Color(0xFF6A1B9A),
                    lineHeight = 16.sp
                )
            }
            Spacer(modifier = Modifier.width(6.dp))
            TextButton(
                onClick = onViewRulesClick,
                modifier = Modifier.testTag("safety_rules_button")
            ) {
                Text(
                    text = "수칙보기",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = Color(0xFF7B1FA2)
                )
            }
        }
    }
}

@Composable
fun SafetyRulesDialog(
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Default.Security,
                contentDescription = null,
                tint = CoralPrimary,
                modifier = Modifier.size(32.dp)
            )
        },
        title = {
            Text(
                text = "만남 전 5대 안심 안전수칙",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                val rules = listOf(
                    "1. 첫 만남은 카페·역 로비 등 밝고 사람이 많은 공공장소에서 갖습니다.",
                    "2. 플랫폼 외 메신저로의 이동이나 계좌 직접 송금 요구는 단호히 거절하세요.",
                    "3. 만남 전 지인이나 가족에게 동행 일정 및 장소를 미리 공유해 두세요.",
                    "4. 상대방의 공개 프로필, KYC 인증 마크 및 당도(Brix)를 사전에 확인하세요.",
                    "5. 위급 상황 발생 시 즉시 112 경찰 또는 119로 신고하세요."
                )
                rules.forEach { rule ->
                    Text(
                        text = rule,
                        fontSize = 13.sp,
                        lineHeight = 18.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
                Card(
                    colors = CardDefaults.cardColors(containerColor = SafetyBg),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = SafetyRed,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "※ 서비스는 공공 긴급기관을 대체하지 않습니다. 긴급 위험 시 즉시 112로 신고하세요.",
                            fontSize = 11.sp,
                            color = SafetyRed,
                            lineHeight = 15.sp
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = CoralPrimary)
            ) {
                Text("확인했습니다")
            }
        }
    )
}

@Composable
fun KycModalDialog(
    onDismiss: () -> Unit,
    onSimulateKyc: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Default.Security,
                contentDescription = null,
                tint = VerifiedGreen,
                modifier = Modifier.size(36.dp)
            )
        },
        title = {
            Text(
                text = "선택형 KYC 신원 인증 안내",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "유미당에서는 사용자의 신뢰도를 높이고 안심할 수 있는 1:1 만남 환경을 위해 공인 인증 기관 연동 선택형 KYC 인증을 제공합니다.",
                    fontSize = 13.sp,
                    lineHeight = 18.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(10.dp))

                Card(
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF3F4F6))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "• 비용 안내: 건당 약 2,000~3,000원 (사용자 직접 결제)",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "• 개인정보 보호: 서비스는 주민등록번호 등 민감 정보를 보관하지 않으며, 외부 인증기관으로부터 '인증 성공 여부'만 수신합니다.",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "• 완료 시 혜택: 공개 프로필에 신뢰를 상징하는 [KYC 인증] 엠블럼이 표시되어 매칭 성사율이 대폭 상승합니다.",
                            fontSize = 12.sp,
                            color = VerifiedGreen,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "※ 본 프로토타입에서는 간편 시뮬레이션을 통해 결제 없이 즉시 KYC 인증 마크를 발급받으실 수 있습니다.",
                    fontSize = 11.sp,
                    color = Color(0xFF6B7280)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onSimulateKyc,
                colors = ButtonDefaults.buttonColors(containerColor = VerifiedGreen),
                modifier = Modifier.testTag("simulate_kyc_button")
            ) {
                Text("KYC 인증 완료하기 (무료 체험)")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("다음에 하기")
            }
        }
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MutualReviewDialog(
    targetUserName: String,
    onDismiss: () -> Unit,
    onSubmit: (rating: Int, tags: List<String>, comment: String) -> Unit
) {
    var rating by remember { mutableIntStateOf(5) }
    val availableTags = listOf(
        "시간 약속을 잘 지켜요",
        "대화가 편안했어요",
        "배려심이 깊어요",
        "친절하고 유쾌해요",
        "다음에 또 함께하고 싶어요"
    )
    val selectedTags = remember { mutableStateListOf<String>() }
    var comment by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "${targetUserName}님과의 동행 상호 평가",
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp
            )
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "동행이 즐거우셨나요? 실제 만남 경험을 솔직하게 평가해 주세요. (양측 모두 평가를 완료해야 상세 리뷰가 공개됩니다)",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 16.sp
                )
                Spacer(modifier = Modifier.height(12.dp))

                // Star rating
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    for (i in 1..5) {
                        Icon(
                            imageVector = if (i <= rating) Icons.Default.Star else Icons.Default.StarBorder,
                            contentDescription = "$i 점",
                            tint = if (i <= rating) StarGold else Color.Gray,
                            modifier = Modifier
                                .size(36.dp)
                                .clickable { rating = i }
                                .padding(4.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "동행 태그 선택 (다중 선택 가능)",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(6.dp))

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    availableTags.forEach { tag ->
                        val isSelected = tag in selectedTags
                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                if (isSelected) selectedTags.remove(tag) else selectedTags.add(tag)
                            },
                            label = { Text(tag, fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = CoralPrimary,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = comment,
                    onValueChange = { comment = it },
                    label = { Text("상세 후기 남기기 (선택)") },
                    placeholder = { Text("상대방에게 전하고 싶은 감사 인사나 동행 소감을 적어주세요.") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onSubmit(rating, selectedTags.toList(), comment) },
                colors = ButtonDefaults.buttonColors(containerColor = CoralPrimary),
                modifier = Modifier.testTag("submit_review_button")
            ) {
                Text("평가 제출하기")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("취소")
            }
        }
    )
}

@Composable
fun ReportDialog(
    targetSummary: String,
    onDismiss: () -> Unit,
    onSubmit: (category: String, details: String) -> Unit
) {
    val categories = listOf(
        "사칭 및 허위 정보 프로필",
        "부적절한 언행 및 불쾌감 조성",
        "상업적 광고 및 외부 거래 유도",
        "약속 일방적 파기 및 연락 두절",
        "기타 안전 위협 우려"
    )
    var selectedCategory by remember { mutableStateOf(categories[0]) }
    var details by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = null,
                tint = SafetyRed,
                modifier = Modifier.size(32.dp)
            )
        },
        title = {
            Text(
                text = "안전 신고 접수",
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp
            )
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "신고 대상: $targetSummary",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(10.dp))

                categories.forEach { cat ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedCategory = cat }
                            .padding(vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (selectedCategory == cat),
                            onClick = { selectedCategory = cat }
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = cat, fontSize = 13.sp)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = details,
                    onValueChange = { details = it },
                    label = { Text("구체적인 사유 입력 (선택)") },
                    placeholder = { Text("상황을 자세히 적어주시면 신속한 조치에 큰 도움이 됩니다.") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onSubmit(selectedCategory, details) },
                colors = ButtonDefaults.buttonColors(containerColor = SafetyRed),
                modifier = Modifier.testTag("submit_report_button")
            ) {
                Text("신고하기")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("취소")
            }
        }
    )
}
