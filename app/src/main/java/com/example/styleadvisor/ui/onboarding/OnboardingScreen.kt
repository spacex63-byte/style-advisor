package com.example.styleadvisor.ui.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.styleadvisor.R
import com.example.styleadvisor.theme.*
import kotlinx.coroutines.launch

@Composable
fun OnboardingScreen(
    onGetStarted: () -> Unit,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(pageCount = { 3 })
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.radialGradient(
                    0.0f to Color(0xFFE7EEFF),
                    0.35f to Color(0xFFEEF4FF),
                    0.65f to Color(0xFFF7F9FF),
                    1.0f to Color(0xFFFFFFFF),
                    radius = 1500f
                )
            )
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        // Sparkle Icon
        Box(contentAlignment = Alignment.Center) {
            Icon(
                painter = painterResource(id = R.drawable.ic_sparkle),
                contentDescription = "Sparkle",
                tint = PrimaryBlue,
                modifier = Modifier.size(32.dp)
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_sparkle),
                contentDescription = null,
                tint = PrimaryBlue.copy(alpha = 0.5f),
                modifier = Modifier
                    .size(16.dp)
                    .offset(x = 18.dp, y = (-12).dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        HorizontalPager(
            state = pagerState,
            userScrollEnabled = false,
            modifier = Modifier.weight(1f)
        ) { page ->
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when (page) {
                    0 -> {
                        // Page 1
                        Text(
                            text = buildAnnotatedString {
                                withStyle(style = SpanStyle(color = TextNavyBlue)) {
                                    append("Style")
                                }
                                withStyle(style = SpanStyle(color = PrimaryBlue)) {
                                    append("Advisor")
                                }
                            },
                            fontSize = 40.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = (-1).sp
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "AI-powered fashion advice\nmade just for you.",
                            color = TextMuted,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center,
                            lineHeight = 20.sp
                        )

                        Spacer(modifier = Modifier.height(40.dp))

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.onboarding_outfit),
                                contentDescription = "Onboarding Outfit",
                                contentScale = ContentScale.Fit,
                                modifier = Modifier.fillMaxSize().scale(1.4f).offset(y = (-15).dp)
                            )
                        }
                    }
                    1 -> {
                        // Page 2
                        Text(
                            text = buildAnnotatedString {
                                withStyle(style = SpanStyle(color = TextNavyBlue)) {
                                    append("AI Analyzes\n")
                                }
                                withStyle(style = SpanStyle(color = PrimaryBlue)) {
                                    append("Your Outfit")
                                }
                            },
                            fontSize = 40.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = (-1).sp,
                            textAlign = TextAlign.Center,
                            lineHeight = 44.sp
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Get instant insights on style,\ncolors, fit and more.",
                            color = TextMuted,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center,
                            lineHeight = 20.sp
                        )

                        Spacer(modifier = Modifier.height(40.dp))

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.hero_fashion_man_photoroom),
                                contentDescription = "AI Analyzes",
                                contentScale = ContentScale.Fit,
                                modifier = Modifier.fillMaxSize().scale(1.05f).offset(y = (-25).dp)
                            )
                        }
                    }
                    2 -> {
                        // Page 3
                        Text(
                            text = buildAnnotatedString {
                                withStyle(style = SpanStyle(color = TextNavyBlue)) {
                                    append("You're ")
                                }
                                withStyle(style = SpanStyle(color = PrimaryBlue)) {
                                    append("All Set!")
                                }
                            },
                            fontSize = 40.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = (-1).sp
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Let's find your best look\nand elevate your style.",
                            color = TextMuted,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center,
                            lineHeight = 20.sp
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.onboarding_outfit_photoroom),
                                contentDescription = "Sample Outfit",
                                contentScale = ContentScale.Fit,
                                modifier = Modifier.fillMaxSize().scale(1.05f).offset(y = (-9).dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            FeatureItem(
                                icon = {
                                    Icon(
                                        imageVector = Icons.Outlined.PhotoCamera,
                                        contentDescription = "Camera",
                                        tint = Color(0xFF4A7DFF)
                                    )
                                },
                                title = "Upload",
                                subtitle = "Your Outfit",
                                bgColor = Color(0xFFF0F5FF)
                            )

                            FeatureItem(
                                icon = {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_sparkle),
                                        contentDescription = "AI",
                                        tint = Color(0xFF4CAF50),
                                        modifier = Modifier.size(24.dp)
                                    )
                                },
                                title = "Get AI",
                                subtitle = "Analysis",
                                bgColor = Color(0xFFF0FFF0)
                            )

                            FeatureItem(
                                icon = {
                                    Icon(
                                        imageVector = Icons.Outlined.Lightbulb,
                                        contentDescription = "Improve",
                                        tint = Color(0xFF9C27B0)
                                    )
                                },
                                title = "Improve",
                                subtitle = "Your Style",
                                bgColor = Color(0xFFFAF0FF)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Pager Indicators
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            for (i in 0 until 3) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(if (i == pagerState.currentPage) PrimaryBlue else Color.LightGray)
                )
                if (i < 2) {
                    Spacer(modifier = Modifier.width(12.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Get Started Button
        val buttonColor = if (pagerState.currentPage == 2) PrimaryBlue else TextNavyBlue
        Button(
            onClick = {
                if (pagerState.currentPage < 2) {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                } else {
                    onGetStarted()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            shape = RoundedCornerShape(30.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = buttonColor
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = if (pagerState.currentPage == 2) "Let's Go!" else "Next",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.width(12.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Arrow Forward",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun FeatureItem(
    icon: @Composable () -> Unit,
    title: String,
    subtitle: String,
    bgColor: Color
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(bgColor),
            contentAlignment = Alignment.Center
        ) {
            icon()
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = title,
            color = TextNavyBlue,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = subtitle,
            color = TextMuted,
            fontSize = 12.sp
        )
    }
}
