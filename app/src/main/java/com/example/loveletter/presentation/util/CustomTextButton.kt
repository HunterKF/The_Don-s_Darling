package com.example.loveletter.presentation.util

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomTextButton(
    modifier: Modifier = Modifier,
    text: String = "",
    enabled: Boolean = false,
    onClick: () -> Unit,
    backgroundColor: Color = MaterialTheme.colors.onPrimary,
    contentColor: Color = MaterialTheme.colors.primary,
) {
    Button(
        enabled = enabled,
        shape = RoundedCornerShape(15.dp),
        modifier = modifier
            .fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = backgroundColor,
            contentColor = contentColor,


            ),
        onClick = { onClick() }) {
        Text(
            text = text,
            fontSize = 18.sp

        )
    }
}