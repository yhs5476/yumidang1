package com.example.yumidang.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.yumidang.ui.components.KycModalDialog
import com.example.yumidang.ui.components.MutualReviewDialog
import com.example.yumidang.ui.components.ReportDialog
import com.example.yumidang.ui.components.SafetyRulesDialog
import com.example.yumidang.ui.screens.AuthScreen
import com.example.yumidang.ui.screens.CreatePostScreen
import com.example.yumidang.ui.screens.HomeScreen
import com.example.yumidang.ui.screens.MyMatchesScreen
import com.example.yumidang.ui.screens.PostDetailScreen
import com.example.yumidang.ui.screens.ProfileScreen
import com.example.yumidang.ui.screens.ProOffersScreen
import com.example.yumidang.ui.theme.CoralPrimary

@Composable
fun YumidangMainScreen(
    viewModel: YumidangViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val currentUser by viewModel.currentUser.collectAsState()
    val selectedPost by viewModel.selectedPost.collectAsState()
    val reviewingRequest by viewModel.reviewingRequest.collectAsState()
    val reportingTarget by viewModel.reportingTarget.collectAsState()
    val showSafetyDialog by viewModel.showSafetyDialog.collectAsState()
    val showKycSheet by viewModel.showKycSheet.collectAsState()
    val snackBarMsg by viewModel.snackBarMessage.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    var selectedNavIndex by remember { mutableIntStateOf(0) }
    var isCreatingPost by remember { mutableStateOf(false) }

    LaunchedEffect(snackBarMsg) {
        snackBarMsg?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearSnackBar()
        }
    }

    if (currentUser == null) {
        AuthScreen(viewModel = viewModel, modifier = modifier)
        return
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            if (selectedPost == null && !isCreatingPost) {
                NavigationBar(
                    containerColor = androidx.compose.material3.MaterialTheme.colorScheme.surface,
                    tonalElevation = 8.dp
                ) {
                    NavigationBarItem(
                        selected = selectedNavIndex == 0,
                        onClick = { selectedNavIndex = 0 },
                        icon = { Icon(Icons.Default.Home, contentDescription = "홈", modifier = Modifier.size(24.dp)) },
                        label = { Text("동행 찾기", fontSize = 11.sp, fontWeight = if (selectedNavIndex == 0) FontWeight.Bold else FontWeight.Normal) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = CoralPrimary,
                            selectedTextColor = CoralPrimary,
                            indicatorColor = Color(0xFFFFECEF)
                        ),
                        modifier = Modifier.testTag("nav_home")
                    )
                    NavigationBarItem(
                        selected = selectedNavIndex == 1,
                        onClick = { selectedNavIndex = 1 },
                        icon = { Icon(Icons.Default.Store, contentDescription = "전문 마켓", modifier = Modifier.size(24.dp)) },
                        label = { Text("전문 오퍼", fontSize = 11.sp, fontWeight = if (selectedNavIndex == 1) FontWeight.Bold else FontWeight.Normal) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = CoralPrimary,
                            selectedTextColor = CoralPrimary,
                            indicatorColor = Color(0xFFFFECEF)
                        ),
                        modifier = Modifier.testTag("nav_pro")
                    )
                    NavigationBarItem(
                        selected = selectedNavIndex == 2,
                        onClick = { selectedNavIndex = 2 },
                        icon = { Icon(Icons.Default.People, contentDescription = "나의 동행", modifier = Modifier.size(24.dp)) },
                        label = { Text("나의 동행", fontSize = 11.sp, fontWeight = if (selectedNavIndex == 2) FontWeight.Bold else FontWeight.Normal) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = CoralPrimary,
                            selectedTextColor = CoralPrimary,
                            indicatorColor = Color(0xFFFFECEF)
                        ),
                        modifier = Modifier.testTag("nav_matches")
                    )
                    NavigationBarItem(
                        selected = selectedNavIndex == 3,
                        onClick = { selectedNavIndex = 3 },
                        icon = { Icon(Icons.Default.Person, contentDescription = "프로필", modifier = Modifier.size(24.dp)) },
                        label = { Text("내 프로필", fontSize = 11.sp, fontWeight = if (selectedNavIndex == 3) FontWeight.Bold else FontWeight.Normal) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = CoralPrimary,
                            selectedTextColor = CoralPrimary,
                            indicatorColor = Color(0xFFFFECEF)
                        ),
                        modifier = Modifier.testTag("nav_profile")
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when {
                isCreatingPost -> {
                    CreatePostScreen(
                        viewModel = viewModel,
                        onBack = { isCreatingPost = false }
                    )
                }
                selectedPost != null -> {
                    PostDetailScreen(
                        post = selectedPost!!,
                        viewModel = viewModel,
                        onBack = { viewModel.closePostDetail() },
                        onNavigateToMyMatches = {
                            viewModel.closePostDetail()
                            selectedNavIndex = 2
                        }
                    )
                }
                else -> {
                    when (selectedNavIndex) {
                        0 -> HomeScreen(
                            viewModel = viewModel,
                            onCreatePostClick = { isCreatingPost = true }
                        )
                        1 -> ProOffersScreen(viewModel = viewModel)
                        2 -> MyMatchesScreen(viewModel = viewModel)
                        3 -> ProfileScreen(viewModel = viewModel)
                    }
                }
            }
        }
    }

    // Safety Rules Dialog
    if (showSafetyDialog) {
        SafetyRulesDialog(onDismiss = { viewModel.setSafetyDialogVisible(false) })
    }

    // KYC Verification Sheet Dialog
    if (showKycSheet) {
        KycModalDialog(
            onDismiss = { viewModel.setKycSheetVisible(false) },
            onSimulateKyc = { viewModel.completeKyc() }
        )
    }

    // Mutual Review Dialog
    reviewingRequest?.let { req ->
        val isRequester = req.requesterId == currentUser?.id
        val targetName = if (isRequester) "공고 작성자" else req.requesterName
        val targetId = if (isRequester) req.postAuthorId else req.requesterId

        MutualReviewDialog(
            targetUserName = targetName,
            onDismiss = { viewModel.closeReviewDialog() },
            onSubmit = { rating, tags, comment ->
                viewModel.submitMutualReview(
                    requestId = req.id,
                    targetUserId = targetId,
                    targetUserName = targetName,
                    rating = rating,
                    tags = tags,
                    comment = comment
                )
            }
        )
    }

    // Report Dialog
    reportingTarget?.let { target ->
        ReportDialog(
            targetSummary = target.third,
            onDismiss = { viewModel.closeReportDialog() },
            onSubmit = { reasonCategory, details ->
                viewModel.submitReport(reasonCategory, details)
            }
        )
    }
}
