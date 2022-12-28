package com.example.loveletter.presentation.game.util

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.loveletter.domain.Avatar
import com.example.loveletter.domain.Player
import com.example.loveletter.ui.theme.*

@Composable
fun Table(
    modifier: Modifier = Modifier
) {
    Box(
        modifier
            .fillMaxSize()
            .border(15.dp, Steel, RoundedCornerShape(150.dp))
            .clip(RoundedCornerShape(150.dp))
            .background(DarkNavy)
    )

}

@Composable
fun PlayerIcon(
    player: Player,
) {

    Box(
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .border(1.dp, Color.Red, RoundedCornerShape(25.dp))

                .clip(RoundedCornerShape(25.dp))
                .widthIn(min = 120.dp, max = 170.dp)
                .height(40.dp)
                .background(Steel)
                .align(Alignment.Center),
            verticalAlignment = Alignment.CenterVertically

        ) {

            Spacer(Modifier.width(54.dp))

            Column(
                modifier = Modifier.padding(4.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(text = player.nickName,
                    fontSize = 12.sp,
                    color = Color.Red,
                    fontWeight = FontWeight.Normal)
                Text(text = "Wins: ${player.wins}",
                    fontSize = 12.sp,
                    color = Color.Yellow,
                    fontWeight = FontWeight.Light)
            }
        }
        val avatar = Avatar.setAvatar(player.avatar)

        Box(
            modifier = Modifier.align(Alignment.CenterStart),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = avatar.avatar),
                contentDescription = avatar.description,
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(45.dp)
                    .zIndex(1f)
                    .clip(CircleShape)
            )
            Box(
                modifier = Modifier
                    .border(1.dp, Color.Red, CircleShape)
                    .align(Alignment.Center)
                    .size(55.dp)
                    .clip(CircleShape)
                    .background(DarkNavy)
            )
        }
        Box(
            modifier = Modifier
                .offset(y = 50.dp, x = 20.dp)

                .align(Alignment.Center)
                .size(10.dp)
                .border(1.dp, Color.Green)
        ) {

        }
    }


}


@Preview(showSystemUi = true)
@Composable
fun TablePreview() {
    val player = Player()
    player.nickName = "Bigballersho"
    player.avatar = 3
    player.wins = 2

    Box(
        modifier = Modifier
            .background(OffWhite)
            .fillMaxSize()
    ) {
        Table(
            modifier = Modifier.padding(30.dp)
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 60.dp, horizontal = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ){
            Row(
                modifier = Modifier.weight(1f).offset(y = (-15).dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically

            ) {
                Box(
                    modifier = Modifier
//                        .offset(x = (-15).dp, y = 100.dp)

                        .border(1.dp, Color.Blue, CircleShape)
                        .zIndex(2f)
                ) {
                    PlayerIcon(player = player)
                }
                Spacer(modifier = Modifier.weight(1f))
                Box(
                    modifier = Modifier
//                        .offset(x = (-15).dp, y = 100.dp)

                        .border(1.dp, Color.Blue, CircleShape)
                        .zIndex(2f)
                ) {
                    PlayerIcon(player = player)
                }
            }
            Spacer(modifier = Modifier
                .size(10.dp)
                .zIndex(1f)
                .border(1.dp, Color.Red)
                .weight(1f))
            Row(
                modifier = Modifier.weight(1f).offset(y = (-20).dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically

            ) {
                Box(
                    modifier = Modifier
//                        .offset(x = (-15).dp, y = 100.dp)

                        .border(1.dp, Color.Blue, CircleShape)
                        .zIndex(2f)
                ) {
                    PlayerIcon(player = player)
                }
                Spacer(modifier = Modifier.weight(1f))
                Box(
                    modifier = Modifier
//                        .offset(x = (-15).dp, y = 100.dp)

                        .border(1.dp, Color.Blue, CircleShape)
                        .zIndex(2f)
                ) {
                    PlayerIcon(player = player)
                }
            }


        }


    }
}