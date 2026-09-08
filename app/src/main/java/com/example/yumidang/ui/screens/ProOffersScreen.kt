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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
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
import com.example.yumidang.data.model.ProOffer
import com.example.yumidang.ui.YumidangViewModel
import com.example.yumidang.ui.components.MannerScoreBar
import com.example.yumidang.ui.components.TrustKycBadge
import com.example.yumidang.ui.theme.CoralPrimary
import com.example.yumidang.ui.theme.SafetyBg
import com.example.yumidang.ui.theme.SafetyRed
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProOffersScreen(
    viewModel: YumidangViewModel,
    modifier: Modifier = Modifier
) {
    val proOffers by viewModel.proOffers.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()

    var showCreateOfferDialog by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("전문 동행 마켓", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFFEF3C7)
                        ) {
                            Text(
                                text = "PRO",
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF92400E),
                                fontSize = 11.sp,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showCreateOfferDialog = true },
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("오퍼 등록하기", fontWeight = FontWeight.Bold) },
                containerColor = CoralPrimary,
                contentColor = Color.White,
                modifier = Modifier.testTag("create_pro_offer_fab")
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Service Introduction Banner
            item {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "특기 및 전문성을 나누는 유료 1:1 동행",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = CoralPrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "스냅 사진 촬영, 전시회 도슨트, 운동 페이스메이커 등 믿을 수 있는 인증 프로와의 특별한 동행을 경험해 보세요.",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 16.sp
                        )
                    }
                }
            }

            // Legal & Medical Policy Restriction Notice (PRD requirement 6.1)
            item {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = SafetyBg),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = SafetyRed,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "※ [이용 제한 안내] 전문 자격 면허가 필요한 의료·간호·전문 간병 영역은 서비스 제공 및 오퍼 등록이 엄격히 제한됩니다. 일상 생활 동행 및 취미 가이드에 한하여 이용 가능합니다.",
                            fontSize = 11.sp,
                            color = SafetyRed,
                            lineHeight = 15.sp
                        )
                    }
                }
            }

            // Pro Offer Cards
            items(proOffers, key = { it.id }) { offer ->
                val isBookedByMe = offer.bookedByUserId == currentUser?.id
                val isBooked = offer.status == "BOOKED"

                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("pro_offer_${offer.id}")
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = MaterialTheme.colorScheme.primaryContainer
                            ) {
                                Text(
                                    text = offer.categoryTitle,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CoralPrimary,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                            Text(
                                text = "${NumberFormat.getNumberInstance(Locale.KOREA).format(offer.priceKrw)}원",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = CoralPrimary
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = offer.title,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            lineHeight = 22.sp
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = offer.description,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 18.sp
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(text = "• 일정: ${offer.schedule}", fontSize = 11.sp)
                                Text(text = "• 포함: ${offer.includes}", fontSize = 11.sp)
                                Text(text = "• 불포함: ${offer.excludes}", fontSize = 11.sp, color = Color.Gray)
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Provider Info
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
                                        text = offer.providerName.take(1),
                                        fontWeight = FontWeight.Bold,
                                        color = CoralPrimary
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = "${offer.providerName} (${offer.providerGender}·${offer.providerAge}세)",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        TrustKycBadge(isVerified = offer.providerKycVerified)
                                    }
                                }
                            }
                            MannerScoreBar(score = offer.providerMannerScore, modifier = Modifier.width(90.dp))
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        when {
                            isBookedByMe -> {
                                OutlinedButton(
                                    onClick = { viewModel.cancelProOfferBooking(offer.id) },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text("예약 취소하기", color = SafetyRed)
                                }
                            }
                            isBooked -> {
                                Button(
                                    onClick = {},
                                    enabled = false,
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text("현재 예약 진행 중입니다")
                                }
                            }
                            else -> {
                                Button(
                                    onClick = { viewModel.bookProOffer(offer.id) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("book_offer_${offer.id}"),
                                    colors = ButtonDefaults.buttonColors(containerColor = CoralPrimary),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text("전문 동행 예약 신청하기", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Create Pro Offer Dialog
    if (showCreateOfferDialog) {
        var categoryTitle by remember { mutableStateOf("스냅 사진 촬영 동행") }
        var title by remember { mutableStateOf("") }
        var description by remember { mutableStateOf("") }
        var priceText by remember { mutableStateOf("30000") }
        var schedule by remember { mutableStateOf("주말 2시간") }
        var includes by remember { mutableStateOf("장소 동행 및 가이드") }
        var excludes by remember { mutableStateOf("개인 식사/음료비") }

        AlertDialog(
            onDismissRequest = { showCreateOfferDialog = false },
            title = { Text("유료 전문 동행 오퍼 등록", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    OutlinedTextField(
                        value = categoryTitle,
                        onValueChange = { categoryTitle = it },
                        label = { Text("전문 분야 카테고리") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("오퍼 제목") },
                        placeholder = { Text("예: 초보자를 위한 1:1 사진 촬영 동행") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("제공 내용 및 설명") },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 3
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = priceText,
                        onValueChange = { priceText = it },
                        label = { Text("제안 금액 (원)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = schedule,
                        onValueChange = { schedule = it },
                        label = { Text("소요 시간 및 일정") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (title.isBlank() || description.isBlank()) {
                            viewModel.showMessage("제목과 내용을 입력해 주세요.")
                            return@Button
                        }
                        val price = priceText.toIntOrNull() ?: 30000
                        viewModel.createProOffer(
                            categoryTitle = categoryTitle,
                            title = title,
                            description = description,
                            priceKrw = price,
                            schedule = schedule,
                            includes = includes,
                            excludes = excludes
                        )
                        showCreateOfferDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CoralPrimary),
                    modifier = Modifier.testTag("submit_pro_offer_button")
                ) {
                    Text("오퍼 등록 완료")
                }
            },
            dismissButton = {
                TextButton(onClick = { showCreateOfferDialog = false }) {
                    Text("취소")
                }
            }
        )
    }
}
