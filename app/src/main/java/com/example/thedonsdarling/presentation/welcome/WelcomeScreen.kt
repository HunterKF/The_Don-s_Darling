package com.example.thedonsdarling.presentation.welcome

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.thedonsdarling.R
import com.example.thedonsdarling.Screen
import com.example.thedonsdarling.domain.models.OnBoardingPage
import com.example.thedonsdarling.presentation.util.CustomTextButton
import com.example.thedonsdarling.presentation.util.HyperlinkText
import com.google.accompanist.pager.*

@OptIn(ExperimentalPagerApi::class, ExperimentalAnimationApi::class)
@Composable
fun WelcomeScreen(
    navController: NavController,
    welcomeViewModel: WelcomeViewModel = hiltViewModel(),
) {
    val pages = listOf(
        OnBoardingPage.First,
        OnBoardingPage.Second,
        OnBoardingPage.Third
    )
    val pagerState = rememberPagerState()

    Surface(
        color = MaterialTheme.colors.primary
    ) {

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            HorizontalPager(
                modifier = Modifier.weight(10f),
                count = 3,
                state = pagerState
            ) { position ->
                PagerScreen(onBoardingPage = pages[position])
            }
            HorizontalPagerIndicator(
                modifier = Modifier
                    .align(CenterHorizontally)
                    .weight(1f),
                pagerState = pagerState
            )
            FinishButton(modifier = Modifier.weight(1f), pagerState = pagerState) {
                welcomeViewModel.saveOnBoardingState(completed = true)
                navController.popBackStack()
                navController.navigate(Screen.Home.route)
            }
        }
    }
}

@Composable
fun PagerScreen(
    onBoardingPage: OnBoardingPage,
    textColor: Color = MaterialTheme.colors.onPrimary,
) {

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        if (onBoardingPage.image == R.drawable._12_512_b) {
            Image(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .fillMaxHeight(0.7f),
                painter = painterResource(id = onBoardingPage.image),
                contentDescription = "Pager Image"
            )
        } else {
            Icon(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .fillMaxHeight(0.7f),
                painter = painterResource(id = onBoardingPage.image),
                contentDescription = "Pager Image",
                tint = Color.White
            )
        }
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = onBoardingPage.title,
            fontSize = MaterialTheme.typography.h4.fontSize,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = textColor

        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp)
                .padding(top = 20.dp),
            text = onBoardingPage.description,
            fontSize = MaterialTheme.typography.subtitle1.fontSize,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            color = textColor
        )
        if (onBoardingPage == OnBoardingPage.Third) {

            HyperlinkText(
                modifier = Modifier
                    .padding(horizontal = 40.dp)
                    .padding(top = 20.dp),
                fullText = stringResource(R.string.agree_to),
                linkText = listOf(stringResource(R.string.privacy_policy),
                    stringResource(
                        R.string.terms_of_use)),
                hyperlinks = listOf("https://github.com/HunterKF/Terms-and-Policies/blob/main/Privacy%20Policy%20for%20The%20Don's%20Darling%20-%20TermsFeed.pdf",
                    "https://github.com/HunterKF/Terms-and-Policies/blob/main/The%20Don's%20Darling%20-%20Terms%20of%20Use.pdf"),
                fontSize = MaterialTheme.typography.subtitle1.fontSize,
                textColor = textColor
            )
        }
    }
}

@ExperimentalAnimationApi
@ExperimentalPagerApi
@Composable
fun FinishButton(
    modifier: Modifier,
    pagerState: PagerState,
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .padding(horizontal = 40.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.Center
    ) {
        AnimatedVisibility(
            modifier = Modifier.fillMaxWidth(),
            visible = pagerState.currentPage == 2
        ) {
            CustomTextButton(
                enabled = true,
                onClick = onClick,
                text = stringResource(R.string.finish)
            )
        }
    }
}