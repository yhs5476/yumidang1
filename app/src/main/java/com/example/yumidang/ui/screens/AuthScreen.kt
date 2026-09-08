package com.example.yumidang.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yumidang.R
import com.example.yumidang.ui.YumidangViewModel
import com.example.yumidang.ui.theme.CoralPrimary
import com.example.yumidang.ui.theme.PeachContainer
import com.example.yumidang.ui.theme.SafetyBg
import com.example.yumidang.ui.theme.VerifiedGreen
import kotlinx.coroutines.delay

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AuthScreen(
    viewModel: YumidangViewModel,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0: 로그인, 1: 회원가입

    // Input States
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("여성") }
    var ageText by remember { mutableStateOf("28") }
    var bio by remember { mutableStateOf("") }

    // Verification States
    var isCodeSent by remember { mutableStateOf(false) }
    var verificationCode by remember { mutableStateOf("") }
    var isPhoneVerified by remember { mutableStateOf(false) }
    var timerSeconds by remember { mutableIntStateOf(180) }

    val interestOptions = listOf("전시회", "맛집/카페", "러닝/운동", "원데이클래스", "산책", "여행", "독립서점", "영화")
    val selectedInterests = remember { mutableStateListOf<String>() }

    // Timer effect
    LaunchedEffect(isCodeSent, timerSeconds) {
        if (isCodeSent && timerSeconds > 0 && !isPhoneVerified) {
            delay(1000)
            timerSeconds -= 1
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        // Hero Illustration & Logo
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = PeachContainer),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.hero_companion),
                    contentDescription = "유미당 동행 일러스트",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "유미당 (너와 나의 동행)",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = CoralPrimary
                )
                Text(
                    text = "신뢰 기반 1:1 일상 동행 & 안심 매칭",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Prototype Notice Box (PRD requirement: Prototype phone verification notice & 1-won account transition)
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF6FF)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.Top
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = Color(0xFF2563EB),
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "※ [프로토타입 안내] 유미당은 현실적 제작 환경을 고려해 휴대폰 인증을 적용하고 있으며, 정식 출시 시 '1원 계좌 실명인증'으로 전환될 예정입니다.",
                    fontSize = 11.sp,
                    color = Color(0xFF1D4ED8),
                    lineHeight = 16.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = CoralPrimary,
            modifier = Modifier.clip(RoundedCornerShape(10.dp))
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("간편 로그인", fontWeight = FontWeight.Bold) }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("신규 회원가입", fontWeight = FontWeight.Bold) }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (selectedTab == 0) {
            // Login Form
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("이름 또는 닉네임") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("휴대폰 번호 (예: 01012345678)") },
                leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val finalName = if (name.isNotBlank()) name else "김민서"
                    val finalPhone = if (phone.isNotBlank()) phone else "010-4829-1029"
                    viewModel.login(finalName, finalPhone)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("login_button"),
                colors = ButtonDefaults.buttonColors(containerColor = CoralPrimary),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("로그인하기", fontSize = 15.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = {
                    viewModel.login("김민서", "010-4829-1029")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .testTag("demo_guest_button"),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("체험용 테스트 계정으로 바로 시작", color = CoralPrimary)
            }

        } else {
            // Signup Form with Phone Verification
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("실명 (신뢰 매칭 기준)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(10.dp))

            // Phone verification row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("휴대폰 번호") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    enabled = !isPhoneVerified
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        isCodeSent = true
                        timerSeconds = 180
                        viewModel.showMessage("인증번호 6자리가 발송되었습니다. (테스트용: 123456)")
                    },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = CoralPrimary),
                    enabled = phone.isNotBlank() && !isPhoneVerified
                ) {
                    Text(if (isCodeSent) "재발송" else "인증요청", fontSize = 13.sp)
                }
            }

            AnimatedVisibility(visible = isCodeSent && !isPhoneVerified) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = verificationCode,
                            onValueChange = { verificationCode = it },
                            label = { Text("인증번호 6자리") },
                            placeholder = { Text("123456") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                if (verificationCode == "123456" || verificationCode.length >= 4) {
                                    isPhoneVerified = true
                                    viewModel.showMessage("휴대폰 본인 인증이 성공적으로 완료되었습니다.")
                                } else {
                                    viewModel.showMessage("인증번호가 일치하지 않습니다. (테스트 번호: 123456)")
                                }
                            },
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = VerifiedGreen)
                        ) {
                            Text("인증확인", fontSize = 13.sp)
                        }
                    }
                    Text(
                        text = "남은 시간: ${timerSeconds / 60}:${"%02d".format(timerSeconds % 60)} (테스트 번호: 123456)",
                        fontSize = 11.sp,
                        color = Color(0xFFDC2626),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            AnimatedVisibility(visible = isPhoneVerified) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = VerifiedGreen,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "휴대폰 인증 완료 (가입 자격 획득)",
                        fontSize = 12.sp,
                        color = VerifiedGreen,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Gender selector
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = { gender = "여성" },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = if (gender == "여성") CoralPrimary else Color.Transparent
                    )
                ) {
                    Text("여성", color = if (gender == "여성") Color.White else CoralPrimary)
                }
                OutlinedButton(
                    onClick = { gender = "남성" },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = if (gender == "남성") CoralPrimary else Color.Transparent
                    )
                ) {
                    Text("남성", color = if (gender == "남성") Color.White else CoralPrimary)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = ageText,
                onValueChange = { ageText = it },
                label = { Text("나이") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = bio,
                onValueChange = { bio = it },
                label = { Text("한 줄 소개") },
                placeholder = { Text("좋아하는 활동이나 관심사를 적어주세요.") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "관심사 / 취미 선택",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.align(Alignment.Start)
            )
            Spacer(modifier = Modifier.height(6.dp))

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                interestOptions.forEach { opt ->
                    val isSelected = opt in selectedInterests
                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            if (isSelected) selectedInterests.remove(opt) else selectedInterests.add(opt)
                        },
                        label = { Text(opt, fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = CoralPrimary,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (name.isBlank()) {
                        viewModel.showMessage("이름을 입력해 주세요.")
                        return@Button
                    }
                    if (!isPhoneVerified) {
                        viewModel.showMessage("휴대폰 인증을 완료해 주세요.")
                        return@Button
                    }
                    val age = ageText.toIntOrNull() ?: 28
                    viewModel.signup(
                        name = name,
                        gender = gender,
                        age = age,
                        phone = phone,
                        bio = if (bio.isNotBlank()) bio else "만나서 반갑습니다!",
                        interests = selectedInterests.toList()
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("signup_submit_button"),
                colors = ButtonDefaults.buttonColors(containerColor = CoralPrimary),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("회원가입 완료하기", fontSize = 15.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}
