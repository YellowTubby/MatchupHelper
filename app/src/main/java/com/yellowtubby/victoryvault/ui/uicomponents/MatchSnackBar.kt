package com.yellowtubby.victoryvault.ui.uicomponents

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.yellowtubby.victoryvault.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


enum class SnackBarType {
    SUCCESS,
    ERROR,
    INFO,
}

data class SnackbarMessage(
    val stringRes: Int = 0,
    val type: SnackBarType = SnackBarType.ERROR
)

data class SnackbarManager(val snackbarHostState: SnackbarHostState,
                           val scope: CoroutineScope, val context: Context) {
    private var isSnackbarVisible = false

    fun showSuccessSnackbar(description: Int) {
        val message = SnackbarMessage(description, SnackBarType.ERROR)
        showSnackbar(message)
    }

    fun showErrorSnackbar(description: Int) {
        val message = SnackbarMessage(description, SnackBarType.ERROR)
        showSnackbar(message)
    }

    fun showInfoSnackbar(description: Int) {
        val message = SnackbarMessage(description, SnackBarType.INFO)
        showSnackbar(message)
    }

    private fun showSnackbar(snackbarMessage: SnackbarMessage) {
        if (!isSnackbarVisible) {
            scope.launch {
                isSnackbarVisible = true
                snackbarHostState.showSnackbar(
                    message = with(snackbarMessage) { "${SnackBarType.SUCCESS.name.lowercase().capitalize(
                        Locale.current
                    )}: ${ContextCompat.getString(context,stringRes)}" },
                    duration = SnackbarDuration.Short
                )
                isSnackbarVisible = false
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchSnackBar(title: String, description: String, type: SnackBarType) {
    val (backgroundColor, textColor) = when (type) {
        SnackBarType.ERROR -> Pair(
            MaterialTheme.colorScheme.errorContainer,
            MaterialTheme.colorScheme.error
        )

        SnackBarType.SUCCESS -> Pair(
            MaterialTheme.colorScheme.surface,
            MaterialTheme.colorScheme.primary
        )

        SnackBarType.INFO -> Pair(
            MaterialTheme.colorScheme.surface,
            MaterialTheme.colorScheme.secondary
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor,
        ),

        ) {
        Row(
            modifier = Modifier.height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.Center,
        ) {

            VerticalDivider(
                color = MaterialTheme.colorScheme.primary,
                thickness = 8.dp,
                modifier = Modifier
                    .padding(all = 0.dp)
                    .fillMaxHeight()
            )

            Column(modifier = Modifier.padding(all = 12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val (icon, tint) = when (type) {
                        SnackBarType.ERROR -> Pair(
                            Icons.Outlined.Info,
                            MaterialTheme.colorScheme.error
                        )

                        SnackBarType.SUCCESS -> Pair(
                            Icons.Filled.Check,
                            MaterialTheme.colorScheme.primary
                        )

                        SnackBarType.INFO -> Pair(
                            Icons.Filled.Info,
                            MaterialTheme.colorScheme.secondary
                        )
                    }
                    Icon(
                        imageVector = icon,
                        contentDescription = "Status Icon",
                        tint = tint
                    )

                    Spacer(modifier = Modifier.padding(all = 4.dp))
                    Text(modifier = Modifier, text = title, color = textColor)
                }
                Spacer(modifier = Modifier.padding(all = 4.dp))
                Text(
                    modifier = Modifier,
                    text = description,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 4
                )
            }
        }
    }
}