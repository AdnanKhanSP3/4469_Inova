package com.example.onboarding

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.onboarding.checkconnectivitypage.presentation.ConnectivityScreenWrapper
import com.example.onboarding.firstpage.OnboardingFirstScreen
import kotlinx.coroutines.launch


@OptIn(ExperimentalFoundationApi::class)
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun OnboardingScreen(
    modifier: Modifier = Modifier,
    onFinished: () -> Unit,
) {

    val snackbarHostState = remember { SnackbarHostState() }

    val coroutineScope = rememberCoroutineScope()

    val pagerState = rememberPagerState(
        pageCount ={
        2
    })

    Scaffold(
        containerColor = Color.Black,
        contentColor = Color.White,
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        }
    ) {paddingValues ->

        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ){
            HorizontalPager(
                state = pagerState,
                modifier = modifier
                    .testTag("HorizontalPager")
                    .fillMaxSize()
            ) {  page ->
                when (page) {
                    0 ->
                        OnboardingFirstScreen {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(1)
                            }
                        }

                    1 -> ConnectivityScreenWrapper(
                        onFinished = onFinished
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .align(Alignment.BottomCenter),
                contentAlignment = Alignment.Center
            ){
                //page indicator
                Row(
                    modifier = Modifier
                        .wrapContentHeight()
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {

                    repeat(pagerState.pageCount){iteration ->

                        // Animate color based on current page
                        val targetColor = if (pagerState.currentPage == iteration) Color.Yellow else Color.White
                        val animatedColor by animateColorAsState(targetValue = targetColor, label = "")

                        // Animate size for liquid-like effect
                        val targetSize = if (pagerState.currentPage == iteration) 12.dp else 12.dp
                        val animatedSize by androidx.compose.animation.core.animateDpAsState(
                            targetValue = targetSize,
                            label = ""
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(animatedColor)
                                .size(animatedSize)
                        )
                    }
                }
            }
        }
    }
}
