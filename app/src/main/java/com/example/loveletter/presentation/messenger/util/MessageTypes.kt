package com.example.loveletter.presentation.messenger.util

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.loveletter.domain.Avatar
import com.example.loveletter.ui.theme.Blue
import com.example.loveletter.ui.theme.LightGray
import com.example.loveletter.ui.theme.Steel

@Composable
fun PlayerMessage(
    message: String,
    avatar: Int,
    nickName: String,
    time: String,
    backgroundColor: Color = LightGray,

    ) {

    Row(
        modifier = Modifier
            .padding(top = 4.dp, bottom = 4.dp, end = 18.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {

        Column(
            modifier = Modifier.padding(end = 4.dp)
        ) {
            val avatar = Avatar.setAvatar(avatar)
            Image(
                painter = painterResource(id = avatar.avatar),
                contentDescription = avatar.description,
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
            )
        }

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = nickName,
                    style = MaterialTheme.typography.body1,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Left
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = time)
            }
            Spacer(modifier = Modifier.height(6.dp))

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(topStart = 0.dp,
                        bottomStart = 10.dp,
                        topEnd = 10.dp,
                        bottomEnd = 10.dp))
                    .background(backgroundColor)
            ) {
                Text(modifier = Modifier.padding(12.dp), text = message)
            }

        }
    }
}

@Composable
fun UserMessage(
    message: String,
    time: String,
    corner: Dp = 10.dp,
    backgroundColor: Color = Blue,
    textColor: Color = Color.White,
) {
    Row(
        modifier = Modifier
            .padding(top = 4.dp, bottom = 4.dp, start = 18.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.End
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = time,
                    textAlign = TextAlign.Right
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "You",
                    style = MaterialTheme.typography.body1,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Right
                )

            }
            Spacer(modifier = Modifier.height(6.dp))

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(topStart = corner,
                        bottomStart = corner,
                        topEnd = 0.dp,
                        bottomEnd = corner))
                    .background(color = backgroundColor)
            ) {
                Text(modifier = Modifier.padding(12.dp), text = message, color = textColor)
            }

        }
    }
}

@Composable
fun GameMessage(
    message: String,
    time: String,
) {
    Column(
        modifier = Modifier.fillMaxWidth(1f).padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = time,
            style = MaterialTheme.typography.subtitle2,
            fontSize = 14.sp,
        )
        Text(
            text = message,
            style = MaterialTheme.typography.subtitle2,
            fontSize = 14.sp,
            fontWeight = FontWeight.Light,
            textAlign = TextAlign.Center
        )
    }
}