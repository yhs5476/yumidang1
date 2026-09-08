package com.example.yumidang.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yumidang.data.model.JoinRequest
import com.example.yumidang.data.model.RequestStatus
import com.example.yumidang.ui.YumidangViewModel
import com.example.yumidang.ui.components.MannerScoreBar
import com.example.yumidang.ui.components.RequestStatusBadge
import com.example.yumidang.ui.components.TrustKycBadge
import com.example.yumidang.ui.theme.CoralPrimary
import com.example.yumidang.ui.theme.SafetyRed
import com.example.yumidang.ui.theme.VerifiedGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyMatchesScreen(
    viewModel: YumidangViewModel,
    modifier: Modifier = Modifier
) {
    val currentUser by viewModel.currentUser.collectAsState()
    val allRequests by viewModel.joinRequests.collectAsState()

    var selectedTab by remember { mutableIntStateOf(0) } // 0: 받은 요청, 1: 보낸 신청, 2: 확정 및 완료

    val receivedRequests = allRequests.filter { it.postAuthorId == currentUser?.id }
    val sentRequests = allRequests.filter { it.requesterId == currentUser?.id }
    val confirmedCompanions = allRequests.filter {
        it.status == RequestStatus.ACCEPTED &&
                (it.postAuthorId == currentUser?.id || it.requesterId == currentUser?.id)
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("나의 동행 관리", fontWeight = FontWeight.Bold, fontSize = 20.sp) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = CoralPrimary
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = {
                        Text(
                            "받은 요청 (${receivedRequests.size})",
                            fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 13.sp
                        )
                    }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = {
                        Text(
                            "보낸 신청 (${sentRequests.size})",
                            fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 13.sp
                        )
                    }
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = {
                        Text(
                            "확정·완료 (${confirmedCompanions.size})",
                            fontWeight = if (selectedTab == 2) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 13.sp
                        )
                    }
                )
            }

            when (selectedTab) {
                0 -> ReceivedRequestsTab(
                    requests = receivedRequests,
                    onAccept = { viewModel.respondToJoinRequest(it, true) },
                    onReject = { viewModel.respondToJoinRequest(it, false) }
                )
                1 -> SentRequestsTab(
                    requests = sentRequests,
                    onCancel = { viewModel.cancelJoinRequest(it) }
                )
                2 -> ConfirmedCompanionsTab(
                    requests = confirmedCompanions,
                    currentUserId = currentUser?.id ?: "",
                    onReviewClick = { req -> viewModel.openReviewDialog(req) },
                    onSafetyClick = { viewModel.setSafetyDialogVisible(true) }
                )
            }
        }
    }
}

@Composable
fun ReceivedRequestsTab(
    requests: List<JoinRequest>,
    onAccept: (String) -> Unit,
    onReject: (String) -> Unit
) {
    if (requests.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(40.dp),
            contentAlignment = Alignment.Center
        ) {
            Text("아직 접수된 동행 참여 요청이 없습니다.", color = Color.Gray, fontSize = 14.sp)
        }
    } else {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(requests, key = { it.id }) { req ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier.fillMaxWidth().testTag("received_req_${req.id}")
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "내 공고: ${req.postTitle}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                maxLines = 1,
                                modifier = Modifier.weight(1f)
                            )
                            RequestStatusBadge(status = req.status)
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Requester Profile Snippet
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(
                                            modifier = Modifier
                                                .size(32.dp)
                                                .background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = req.requesterName.take(1),
                                                fontWeight = FontWeight.Bold,
                                                color = CoralPrimary
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "${req.requesterName} (${req.requesterGender}·${req.requesterAge}세)",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        TrustKycBadge(isVerified = req.requesterKycVerified)
                                    }
                                    MannerScoreBar(score = req.requesterMannerScore, modifier = Modifier.width(90.dp))
                                }

                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "신청 메시지:",
                                    fontSize = 11.sp,
                                    color = Color.Gray,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = req.message,
                                    fontSize = 13.sp,
                                    lineHeight = 18.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }

                        if (req.status == RequestStatus.PENDING) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedButton(
                                    onClick = { onReject(req.id) },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text("거절하기", color = Color.Gray)
                                }
                                Button(
                                    onClick = { onAccept(req.id) },
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.buttonColors(containerColor = CoralPrimary),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text("수락하기 (동행 확정)", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SentRequestsTab(
    requests: List<JoinRequest>,
    onCancel: (String) -> Unit
) {
    if (requests.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(40.dp),
            contentAlignment = Alignment.Center
        ) {
            Text("보낸 동행 신청 내역이 없습니다.", color = Color.Gray, fontSize = 14.sp)
        }
    } else {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(requests, key = { it.id }) { req ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier.fillMaxWidth().testTag("sent_req_${req.id}")
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = req.postTitle,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                maxLines = 1,
                                modifier = Modifier.weight(1f)
                            )
                            RequestStatusBadge(status = req.status)
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.CalendarToday,
                                contentDescription = null,
                                tint = CoralPrimary,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = req.postDateTime, fontSize = 12.sp, color = Color.Gray)
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = CoralPrimary,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = req.postLocation, fontSize = 12.sp, color = Color.Gray, maxLines = 1)
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "내 신청 메시지: ${req.message}",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        if (req.status == RequestStatus.PENDING) {
                            Spacer(modifier = Modifier.height(10.dp))
                            OutlinedButton(
                                onClick = { onCancel(req.id) },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("신청 취소하기", color = SafetyRed, fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ConfirmedCompanionsTab(
    requests: List<JoinRequest>,
    currentUserId: String,
    onReviewClick: (JoinRequest) -> Unit,
    onSafetyClick: () -> Unit
) {
    if (requests.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(40.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("아직 확정된 동행이 없습니다.", color = Color.Gray, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(6.dp))
                Text("공고에 참여 신청을 보내거나 신청을 수락해 보세요!", color = CoralPrimary, fontSize = 12.sp)
            }
        }
    } else {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(requests, key = { it.id }) { req ->
                val isRequester = req.requesterId == currentUserId
                val partnerName = if (isRequester) "공고 작성자" else req.requesterName
                val hasEvaluated = if (isRequester) req.isEvaluatedByRequester else req.isEvaluatedByAuthor

                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth().testTag("confirmed_companion_${req.id}")
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = Color(0xFFD1FAE5)
                            ) {
                                Text(
                                    text = "동행 확정",
                                    color = Color(0xFF065F46),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                )
                            }
                            Text(text = "동행 상대: $partnerName", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = req.postTitle,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CalendarToday, contentDescription = null, tint = CoralPrimary, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = req.postDateTime, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LocationOn, contentDescription = null, tint = CoralPrimary, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = req.postLocation, fontSize = 13.sp, color = Color.Gray)
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Safety actions
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = onSafetyClick,
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(Icons.Default.Security, contentDescription = null, modifier = Modifier.size(14.dp), tint = CoralPrimary)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("안전수칙", fontSize = 12.sp, color = CoralPrimary)
                            }

                            if (!hasEvaluated) {
                                Button(
                                    onClick = { onReviewClick(req) },
                                    modifier = Modifier.weight(1.4f).testTag("open_eval_button_${req.id}"),
                                    colors = ButtonDefaults.buttonColors(containerColor = CoralPrimary),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Icon(Icons.Default.RateReview, contentDescription = null, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("상호 평가 작성", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            } else {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = Color(0xFFEFF6FF),
                                    modifier = Modifier.weight(1.4f).height(40.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxSize(),
                                        horizontalArrangement = Arrangement.Center,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(Icons.Default.Check, contentDescription = null, tint = Color(0xFF2563EB), modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("평가 제출 완료", color = Color(0xFF2563EB), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
