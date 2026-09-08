package com.example.yumidang.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yumidang.data.model.ReportStatus
import com.example.yumidang.ui.YumidangViewModel
import com.example.yumidang.ui.components.MannerScoreBar
import com.example.yumidang.ui.components.TrustKycBadge
import com.example.yumidang.ui.theme.CoralPrimary
import com.example.yumidang.ui.theme.SafetyRed
import com.example.yumidang.ui.theme.StarGold
import com.example.yumidang.ui.theme.VerifiedGreen

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ProfileScreen(
    viewModel: YumidangViewModel,
    modifier: Modifier = Modifier
) {
    val currentUser by viewModel.currentUser.collectAsState()
    val reviews by viewModel.reviews.collectAsState()
    val reports by viewModel.reports.collectAsState()
    val blockedUserIds by viewModel.blockedUserIds.collectAsState()

    var showEditProfileDialog by remember { mutableStateOf(false) }
    var showAdminDialog by remember { mutableStateOf(false) }
    var showBlockedUsersDialog by remember { mutableStateOf(false) }

    val user = currentUser ?: return

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("내 프로필 & 안심센터", fontWeight = FontWeight.Bold, fontSize = 20.sp) },
                actions = {
                    IconButton(
                        onClick = { showEditProfileDialog = true },
                        modifier = Modifier.testTag("edit_profile_button")
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = "프로필 수정")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // User Basic Info Card
            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = user.name.take(1),
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CoralPrimary
                                )
                            }
                            Spacer(modifier = Modifier.width(14.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = user.name,
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 18.sp
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    TrustKycBadge(isVerified = user.isKycVerified)
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "${user.gender} · ${user.age}세 · ${user.phone}",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        if (user.bio.isNotBlank()) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = user.bio,
                                fontSize = 13.sp,
                                lineHeight = 18.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        if (user.interests.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(10.dp))
                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                user.interests.forEach { tag ->
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                                    ) {
                                        Text(
                                            text = "#$tag",
                                            fontSize = 11.sp,
                                            color = CoralPrimary,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Manner Score Thermometer
                        MannerScoreBar(score = user.mannerScore)
                    }
                }
            }

            // KYC Promotion / Verification Status Card
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (user.isKycVerified) Color(0xFFF0FDF4) else Color(0xFFFEF2F2)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(
                                    if (user.isKycVerified) Color(0xFFDCFCE7) else Color(0xFFFEE2E2),
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Security,
                                contentDescription = null,
                                tint = if (user.isKycVerified) VerifiedGreen else CoralPrimary,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (user.isKycVerified) "선택형 KYC 인증 완료" else "선택형 KYC 신원 인증",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = if (user.isKycVerified) Color(0xFF166534) else Color(0xFF991B1B)
                            )
                            Text(
                                text = if (user.isKycVerified)
                                    "공인인증기관 안심 인증 완료 (${user.kycVerifiedDate})"
                                else
                                    "신뢰도 엠블럼 획득 (사용자 직접 결제 2~3천원 상당)",
                                fontSize = 11.sp,
                                color = if (user.isKycVerified) Color(0xFF15803D) else Color(0xFFB91C1C)
                            )
                        }
                        if (!user.isKycVerified) {
                            Button(
                                onClick = { viewModel.setKycSheetVisible(true) },
                                colors = ButtonDefaults.buttonColors(containerColor = CoralPrimary),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.testTag("open_kyc_sheet_button")
                            ) {
                                Text("인증하기", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // Received Reviews & Manner Feedback
            item {
                Text(
                    text = "받은 상호 평가 & 동행 후기 (${reviews.size}건)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            if (reviews.isEmpty()) {
                item {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surface,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(modifier = Modifier.padding(20.dp), contentAlignment = Alignment.Center) {
                            Text("아직 작성된 상호 평가가 없습니다.", color = Color.Gray, fontSize = 13.sp)
                        }
                    }
                }
            } else {
                items(reviews, key = { it.id }) { review ->
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "${review.reviewerName}님의 평가",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Row {
                                        repeat(review.rating) {
                                            Icon(
                                                imageVector = Icons.Default.Star,
                                                contentDescription = null,
                                                tint = StarGold,
                                                modifier = Modifier.size(13.dp)
                                            )
                                        }
                                    }
                                }
                            }

                            if (review.tags.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(6.dp))
                                FlowRow(
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    review.tags.forEach { t ->
                                        Surface(
                                            shape = RoundedCornerShape(6.dp),
                                            color = Color(0xFFF3F4F6)
                                        ) {
                                            Text(
                                                text = t,
                                                fontSize = 10.sp,
                                                color = Color(0xFF4B5563),
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                    }
                                }
                            }

                            if (review.comment.isNotBlank()) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "\"${review.comment}\"",
                                    fontSize = 13.sp,
                                    lineHeight = 18.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }

            // Safety Center Menu Items
            item {
                Text(
                    text = "안전 및 서비스 관리",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            item {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        // Safety rules item
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { viewModel.setSafetyDialogVisible(true) }
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Security, contentDescription = null, tint = CoralPrimary)
                                Spacer(modifier = Modifier.width(12.dp))
                                Text("유미당 5대 안심 만남 수칙 보기", fontSize = 14.sp)
                            }
                            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.Gray)
                        }

                        // Blocked users item
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { showBlockedUsersDialog = true }
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Block, contentDescription = null, tint = Color.Gray)
                                Spacer(modifier = Modifier.width(12.dp))
                                Text("차단한 사용자 목록 (${blockedUserIds.size})", fontSize = 14.sp)
                            }
                            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.Gray)
                        }

                        // Admin Mode item (PRD 5.3: Operations Admin Panel for review & restrictions)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { showAdminDialog = true }
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.AdminPanelSettings, contentDescription = null, tint = CoralPrimary)
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text("운영 관리자 모드 (신고 처리 및 이용 제재)", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                                    Text("접수된 신고 ${reports.size}건 확인", fontSize = 11.sp, color = Color.Gray)
                                }
                            }
                            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.Gray)
                        }
                    }
                }
            }

            // Logout Button
            item {
                OutlinedButton(
                    onClick = { viewModel.logout() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("logout_button"),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.ExitToApp, contentDescription = null, tint = SafetyRed)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("로그아웃", color = SafetyRed, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }

    // Edit Profile Dialog
    if (showEditProfileDialog) {
        var editName by remember { mutableStateOf(user.name) }
        var editBio by remember { mutableStateOf(user.bio) }
        val interestOptions = listOf("전시회", "맛집/카페", "러닝/운동", "원데이클래스", "산책", "여행", "독립서점", "영화")
        val editInterests = remember { mutableStateListOf<String>().apply { addAll(user.interests) } }

        AlertDialog(
            onDismissRequest = { showEditProfileDialog = false },
            title = { Text("프로필 수정", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    OutlinedTextField(
                        value = editName,
                        onValueChange = { editName = it },
                        label = { Text("이름") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = editBio,
                        onValueChange = { editBio = it },
                        label = { Text("한 줄 소개") },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 3
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(text = "관심사 선택", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(6.dp))
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        interestOptions.forEach { opt ->
                            val isSelected = opt in editInterests
                            FilterChip(
                                selected = isSelected,
                                onClick = {
                                    if (isSelected) editInterests.remove(opt) else editInterests.add(opt)
                                },
                                label = { Text(opt, fontSize = 10.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = CoralPrimary,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.updateProfile(editName, editBio, editInterests.toList())
                        showEditProfileDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CoralPrimary)
                ) {
                    Text("저장하기")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditProfileDialog = false }) {
                    Text("취소")
                }
            }
        )
    }

    // Admin Reports Dialog (PRD 5.3: Operational Action on Reports)
    if (showAdminDialog) {
        AlertDialog(
            onDismissRequest = { showAdminDialog = false },
            title = { Text("운영 관리자 - 신고 처리 센터", fontWeight = FontWeight.Bold) },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "접수된 안전 신고 내역을 검토하고 제재 조치를 집행할 수 있습니다.",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    if (reports.isEmpty()) {
                        Text("처리 대기 중인 신고 내역이 없습니다.", fontSize = 13.sp)
                    } else {
                        reports.forEach { rep ->
                            Card(
                                shape = RoundedCornerShape(8.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFF9FAFB)),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(text = "신고 사유: ${rep.reasonCategory}", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                        Text(text = rep.status.label, color = SafetyRed, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                    }
                                    Text(text = "대상: ${rep.targetSummary}", fontSize = 11.sp, color = Color.Gray)
                                    Text(text = "내용: ${rep.description}", fontSize = 11.sp)

                                    if (rep.status == ReportStatus.PENDING) {
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                            Button(
                                                onClick = {
                                                    viewModel.resolveAdminReport(rep.id, ReportStatus.WARNED, "경고 메시지 발송")
                                                },
                                                colors = ButtonDefaults.buttonColors(containerColor = CoralPrimary),
                                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                                            ) {
                                                Text("경고", fontSize = 11.sp)
                                            }
                                            Button(
                                                onClick = {
                                                    viewModel.resolveAdminReport(rep.id, ReportStatus.RESTRICTED, "공고 작성 7일 제한")
                                                },
                                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626)),
                                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                                            ) {
                                                Text("공고제한", fontSize = 11.sp)
                                            }
                                            Button(
                                                onClick = {
                                                    viewModel.resolveAdminReport(rep.id, ReportStatus.SUSPENDED, "영구 이용 정지")
                                                },
                                                colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                                            ) {
                                                Text("이용정지", fontSize = 11.sp)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showAdminDialog = false }) {
                    Text("닫기")
                }
            }
        )
    }

    // Blocked Users Dialog
    if (showBlockedUsersDialog) {
        AlertDialog(
            onDismissRequest = { showBlockedUsersDialog = false },
            title = { Text("차단한 사용자 목록", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    if (blockedUserIds.isEmpty()) {
                        Text("차단된 사용자가 없습니다.", fontSize = 13.sp, color = Color.Gray)
                    } else {
                        blockedUserIds.forEach { uid ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = "차단 ID: $uid", fontSize = 13.sp)
                                TextButton(onClick = { viewModel.unblockUser(uid) }) {
                                    Text("차단 해제", color = CoralPrimary)
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showBlockedUsersDialog = false }) {
                    Text("닫기")
                }
            }
        )
    }
}
