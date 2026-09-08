package com.example.yumidang.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
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
import com.example.yumidang.data.model.CompanionCategory
import com.example.yumidang.ui.YumidangViewModel
import com.example.yumidang.ui.theme.CoralPrimary

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun CreatePostScreen(
    viewModel: YumidangViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var category by remember { mutableStateOf(CompanionCategory.HOBBY) }
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var dateTime by remember { mutableStateOf("2026.09.20 (토) 14:00") }
    var location by remember { mutableStateOf("서울 종로구 인사동 쌈지길 앞") }
    var area by remember { mutableStateOf("종로구") }
    var conditionGender by remember { mutableStateOf("여성 전용") }
    var conditionAge by remember { mutableStateOf("20-30대") }
    var expenseType by remember { mutableStateOf("각자 부담") }

    val areas = listOf("종로구", "마포구", "성동구", "영등포구", "강남구", "서초구", "송파구")

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("1:1 동행 공고 등록", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("create_post_back_button")) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "취소")
                    }
                },
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
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Category Selection
            Text(text = "동행 카테고리", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                CompanionCategory.entries.filter { it != CompanionCategory.ALL }.forEach { cat ->
                    val isSelected = cat == category
                    FilterChip(
                        selected = isSelected,
                        onClick = { category = cat },
                        label = { Text("${cat.iconText} ${cat.label}", fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = CoralPrimary,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Title
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("공고 제목") },
                placeholder = { Text("예: 국립현대미술관 특별전 조용히 함께 관람해요") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("create_post_title_input"),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Content
            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                label = { Text("동행 내용 및 소개") },
                placeholder = { Text("어떤 활동을 함께 하고 싶으신지, 어떤 동행자를 찾으시는지 적어주세요.") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .testTag("create_post_content_input"),
                maxLines = 6
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Date & Time
            OutlinedTextField(
                value = dateTime,
                onValueChange = { dateTime = it },
                label = { Text("희망 일시") },
                placeholder = { Text("2026.09.20 (토) 14:00") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Location
            OutlinedTextField(
                value = location,
                onValueChange = { location = it },
                label = { Text("만남 장소 (구체적인 공공장소)") },
                placeholder = { Text("예: 안국역 1번 출구 앞, 삼청동 국립현대미술관 로비") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Area
            Text(text = "활동 지역 (구)", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                areas.forEach { a ->
                    val isSelected = a == area
                    FilterChip(
                        selected = isSelected,
                        onClick = { area = a },
                        label = { Text(a, fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = CoralPrimary,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Gender Condition
            Text(text = "성별 조건", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("여성 전용", "성별 무관").forEach { opt ->
                    val isSelected = opt == conditionGender
                    OutlinedButton(
                        onClick = { conditionGender = opt },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (isSelected) CoralPrimary else Color.Transparent
                        )
                    ) {
                        Text(
                            text = opt,
                            color = if (isSelected) Color.White else CoralPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Age Condition
            Text(text = "희망 연령대", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("20-30대", "30-40대", "연령 무관").forEach { opt ->
                    val isSelected = opt == conditionAge
                    FilterChip(
                        selected = isSelected,
                        onClick = { conditionAge = opt },
                        label = { Text(opt, fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = CoralPrimary,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Expense Type
            Text(text = "비용 분담 기준", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("각자 부담", "작성자 부담", "협의 가능").forEach { opt ->
                    val isSelected = opt == expenseType
                    FilterChip(
                        selected = isSelected,
                        onClick = { expenseType = opt },
                        label = { Text(opt, fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = CoralPrimary,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (title.isBlank()) {
                        viewModel.showMessage("제목을 입력해 주세요.")
                        return@Button
                    }
                    if (content.isBlank()) {
                        viewModel.showMessage("내용을 입력해 주세요.")
                        return@Button
                    }
                    viewModel.createPost(
                        category = category,
                        title = title,
                        content = content,
                        dateTime = dateTime,
                        location = location,
                        area = area,
                        conditionGender = conditionGender,
                        conditionAge = conditionAge,
                        expenseType = expenseType
                    )
                    onBack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("submit_create_post_button"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = CoralPrimary)
            ) {
                Text("공고 등록 완료", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
