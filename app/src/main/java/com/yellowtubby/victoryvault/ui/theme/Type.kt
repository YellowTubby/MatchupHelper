package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight

val AppTypography = Typography().copy(
    titleLarge = Typography().titleLarge.merge(
        TextStyle(
            fontWeight = FontWeight.Bold
        )
    )
)


