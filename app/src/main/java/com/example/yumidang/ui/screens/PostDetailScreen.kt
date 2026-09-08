package com.example.yumidang.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Report
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.example.yumidang.data.model.CompanionPost
import com.example.yumidang.data.model.PostStatus
import com.example.yumidang.data.model.RequestStatus
import com.example.yumidang.ui.YumidangViewModel
import com.example.yumidang.ui.components.MannerScoreBar
import com.example.yumidang.ui.components.PostStatusBadge
import com.example.yumidang.ui.components.TrustKycBadge
import com.example.yumidang.ui.theme.CoralPrimary
import com.example.yumidang.ui.theme.SafetyRed
import com.example.yumidang.ui.theme.VerifiedGreen

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun PostDetailScreen(
    post: CompanionPost,
    viewModel: YumidangViewModel,
    onBack: () -> Unit,
    onNavigateToMyMatches: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentUser by viewModel.currentUser.collectAsState()
    val joinRequests by viewModel.joinRequests.collectAsState()

    var showMenu by remember { mutableStateOf(false) }
    var showApplyDialog by remember { mutableStateOf(false) }
    var applyMessage by remember { mutableStateOf("") }
    var showBlockConfirmDialog by remember { mutableStateOf(false) }

    val isAuthor = currentUser?.id == post.authorId
    val existingRequest = joinRequests.find {
        it.postId == post.id && it.requesterId == currentUser?.id
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("동행 공고 상세", fontSize = 18.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("detail_back_button")) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "뒤로가기")
                    }
                },
                actions = {
                    IconButton(onClick = { showMenu = true }, modifier = Modifier.testTag("detail_more_button")) {
                        Icon(Icons.Default.MoreVert, contentDescription = "더보기 메뉴")
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        if (isAuthor) {
                            if (post.status == PostStatus.RECRUITING) {
                                DropdownMenuItem(
                                    text = { Text("공고 마감하기") },
                                    onClick = {
                                        showMenu = false
                                        viewModel.closePost(post.id)
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("공고 취소하기", color = SafetyRed) },
                                    onClick = {
                                        showMenu = false
                                        viewModel.cancelPost(post.id)
                                    }
                                )
                            }
                        } else {
                            DropdownMenuItem(
                                text = { Text("신고하기", color = SafetyRed) },
                                leadingIcon = { Icon(Icons.Default.Report, contentDescription = null, tint = SafetyRed) },
                                onClick = {
                                    showMenu = false
                                    viewModel.openReportDialog(
                                        targetType = "POST",
                                        targetId = post.id,
                                        summary = "공고: '${post.title}' (작성자: ${post.authorName})"
                                    )
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("작성자 차단하기") },
                                leadingIcon = { Icon(Icons.Default.Block, contentDescription = null) },
                                onClick = {
                                    showMenu = false
                                    showBlockConfirmDialog = true
                                }
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        bottomBar = {
            Surface(
                tonalElevation = 6.dp,
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(modifier = Modifier.padding(16.dp)) {
                    when {
                        isAuthor -> {
                            Button(
                                onClick = onNavigateToMyMatches,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                                    .testTag("author_check_requests_button"),
                                colors = ButtonDefaults.buttonColors(containerColor = CoralPrimary),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("받은 참여 요청 확인하기", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            }
                        }
                        post.status != PostStatus.RECRUITING -> {
                            Button(
                                onClick = {},
                                enabled = false,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("모집이 마감된 공고입니다", fontSize = 15.sp)
                            }
                        }
                        existingRequest != null -> {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxSize(),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = CoralPrimary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "참여 신청 완료 (${existingRequest.status.label})",
                                        fontWeight = FontWeight.Bold,
                                        color = CoralPrimary
                                    )
                                }
                            }
                        }
                        else -> {
                            Button(
                                onClick = { showApplyDialog = true },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                                    .testTag("apply_companion_button"),
                                colors = ButtonDefaults.buttonColors(containerColor = CoralPrimary),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("1:1 동행 참여 신청하기", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            }
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Category & Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Text(
                        text = "${post.category.iconText} ${post.category.label}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = CoralPrimary,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
                PostStatusBadge(status = post.status)
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Post Title
            Text(
                text = post.title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = 26.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Schedule & Location Card
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = null,
                            tint = CoralPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(text = "희망 일시", fontSize = 11.sp, color = Color.Gray)
                            Text(text = post.dateTime, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = CoralPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(text = "만남 장소", fontSize = 11.sp, color = Color.Gray)
                            Text(text = post.location, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Conditions Row
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "성별 조건", fontSize = 11.sp, color = Color.Gray)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = post.conditionGender,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = if (post.conditionGender == "여성 전용") Color(0xFFE11D48) else MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "희망 연령", fontSize = 11.sp, color = Color.Gray)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(text = post.conditionAge, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "비용 분담", fontSize = 11.sp, color = Color.Gray)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(text = post.expenseType, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Post Content Body
            Text(
                text = "동행 상세 내용",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = post.content,
                    fontSize = 14.sp,
                    lineHeight = 22.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(16.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Author Public Profile Card (PRD requirement: Author profile with KYC badge & manner temp)
            Text(
                text = "동행 작성자 프로필",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))

            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = post.authorName.take(1),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    color = CoralPrimary
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "${post.authorName} (${post.authorGender}·${post.authorAge}세)",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    TrustKycBadge(isVerified = post.authorKycVerified)
                                }
                                Text(
                                    text = "동행 완료 ${if (isAuthor) 5 else 3}회",
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    MannerScoreBar(score = post.authorMannerScore)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Safety Warning Box
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBEB)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = null,
                            tint = Color(0xFFD97706),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "안전한 1:1 만남을 위한 약속",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFB45309)
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "• 첫 만남은 공개된 공공장소(지하철역 로비, 대형 카페 등)에서 만납니다.\n• 신원이 검증되지 않은 외부 메신저로의 이동은 자제하세요.\n• 긴급 위험 상황 시 즉시 112 경찰 또는 119로 신고해 주세요.",
                        fontSize = 11.sp,
                        color = Color(0xFF92400E),
                        lineHeight = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }

    // Apply Message Dialog
    if (showApplyDialog) {
        AlertDialog(
            onDismissRequest = { showApplyDialog = false },
            title = { Text("동행 참여 신청", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text(
                        text = "작성자에게 전달할 인사말과 동행 의사를 남겨주세요.",
                        fontSize = 13.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = applyMessage,
                        onValueChange = { applyMessage = it },
                        placeholder = { Text("예: 안녕하세요! 저도 이 전시 꼭 보고 싶었는데 함께 관람하고 싶어요 :)") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("apply_message_input"),
                        maxLines = 3
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val message = if (applyMessage.isNotBlank()) applyMessage else "함께 좋은 동행 만들고 싶습니다!"
                        viewModel.applyForPost(post.id, message)
                        showApplyDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CoralPrimary),
                    modifier = Modifier.testTag("confirm_apply_button")
                ) {
                    Text("신청하기")
                }
            },
            dismissButton = {
                TextButton(onClick = { showApplyDialog = false }) {
                    Text("취소")
                }
            }
        )
    }

    // Block User Dialog
    if (showBlockConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showBlockConfirmDialog = false },
            title = { Text("작성자 차단하기", fontWeight = FontWeight.Bold) },
            text = {
                Text(
                    text = "${post.authorName}님을 차단하시겠습니까? 차단 시 해당 사용자의 공고와 참여 요청이 더 이상 표시되지 않습니다.",
                    fontSize = 13.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showBlockConfirmDialog = false
                        viewModel.blockUser(post.authorId, post.authorName)
                        onBack()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SafetyRed)
                ) {
                    Text("차단하기")
                }
            },
            dismissButton = {
                TextButton(onClick = { showBlockConfirmDialog = false }) {
                    Text("취소")
                }
            }
        )
    }
}
