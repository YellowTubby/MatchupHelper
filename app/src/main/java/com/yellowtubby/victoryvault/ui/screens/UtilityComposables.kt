package com.yellowtubby.victoryvault.ui.screens

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.addPathNodes
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.yellowtubby.victoryvault.R
import com.yellowtubby.victoryvault.model.Role


@Composable
fun getIconPainerResource(role: Role?) : Painter {
    return when (role) {
        Role.TOP -> painterResource(R.drawable.top_icon)
        Role.JUNGLE -> painterResource(R.drawable.jungle_icon)
        Role.MID -> painterResource(R.drawable.mid_icon)
        Role.BOTTOM -> painterResource(R.drawable.bot_icon)
        Role.SUPPORT -> painterResource(R.drawable.support_icon)
        null -> painterResource(R.drawable.logo)
    }
}



