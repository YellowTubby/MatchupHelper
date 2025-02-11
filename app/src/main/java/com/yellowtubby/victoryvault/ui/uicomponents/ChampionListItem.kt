package com.yellowtubby.victoryvault.ui.uicomponents

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.yellowtubby.victoryvault.R
import com.yellowtubby.victoryvault.model.Champion

@OptIn(ExperimentalGlideComposeApi::class)
@Preview
@Composable
fun ChampionListItem(champion: Champion = Champion("Ahri"), onItemClicked: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(8.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .clickable(onClick = onItemClicked),
        horizontalArrangement = Arrangement.Absolute.Left,
        verticalAlignment = Alignment.CenterVertically
    ) {
        GlideImage(
            modifier = Modifier
                .size(48.dp)
                .padding(8.dp)
                .clip(CircleShape),
            loading = placeholder(R.drawable.logo),
            model = champion.iconUri, contentDescription = "icon_champion_${champion.name}"
        )
        Text(
            champion.name,
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight()
        )
        Spacer(Modifier.weight(1f))
        Icon(
            modifier = Modifier,
            imageVector = Icons.Filled.ArrowDropDown, contentDescription = "DropDown Arrow"
        )
    }

}
