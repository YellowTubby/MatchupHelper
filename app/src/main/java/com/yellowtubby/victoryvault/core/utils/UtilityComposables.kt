package com.yellowtubby.victoryvault.core.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import com.yellowtubby.victoryvault.R
import com.yellowtubby.victoryvault.data.datamodels.Role


@Composable
fun getIconPainerResource(role: Role?) : Painter {
    return when (role) {
        Role.TOP -> painterResource(R.drawable.top_icon)
        Role.JUNGLE -> painterResource(R.drawable.jungle_icon)
        Role.MID -> painterResource(R.drawable.mid_icon)
        Role.BOTTOM -> painterResource(R.drawable.bot_icon)
        Role.SUPPORT -> painterResource(R.drawable.support_icon)
        Role.NAN -> painterResource(R.drawable.logo)
        null -> TODO()
    }
}



