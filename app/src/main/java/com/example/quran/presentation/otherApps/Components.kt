package com.example.quran.presentation.otherApps


import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.quran.ui.theme.thirdBackgroundColor

@Composable
fun IosSwitcher(
    isEnabled: Boolean ,
    padding: Dp = 2.dp,
    animationSpec: AnimationSpec<Dp> = tween(durationMillis = 300),
    onClick: (Boolean) -> Unit
) {
        val offset by animateDpAsState(
            targetValue = if (isEnabled) 0.dp else 20.dp,
            animationSpec = animationSpec, label = ""
        )

        Box(modifier = Modifier
            .width(51.dp)
            .height(31.dp)
            .clip(shape = CircleShape)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick(isEnabled) }
            .background(if (isEnabled) MaterialTheme.colors.primary else thirdBackgroundColor)
        ) {

            Card(
                modifier = Modifier
                    .size(31.dp)
                    .padding(all = padding)
                    .offset(x = offset ),
                backgroundColor = Color.White ,
                shape = CircleShape ,
                elevation = 3.dp
            ) {}
        }
}