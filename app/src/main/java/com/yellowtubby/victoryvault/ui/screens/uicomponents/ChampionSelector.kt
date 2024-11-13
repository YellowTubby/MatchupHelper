package com.yellowtubby.victoryvault.ui.screens.uicomponents

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.yellowtubby.victoryvault.ui.model.Champion

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ChampionSelector(
    championList: List<Champion>,
    initialChampion: Champion? = championList[0],
    onSelected: (Champion) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableIntStateOf(championList.indexOf(initialChampion)) }
    Box(modifier = Modifier.padding(8.dp), contentAlignment = Alignment.Center) {
        if(championList.isNotEmpty()){
            ChampionListItem(
                championList[selectedIndex]
            ) {
                expanded = true
            }
        }
        DropdownMenu(
            expanded = expanded, onDismissRequest = { expanded = false }) {
            championList.forEachIndexed { index, champ ->
                val inlineContent = mapOf(
                    Pair(
                        // This tells the [CoreText] to replace the placeholder string "[icon]" by
                        // the composable given in the [InlineTextContent] object.
                        champ.name,
                        InlineTextContent(
                            // Placeholder tells text layout the expected size and vertical alignment of
                            // children composable.
                            Placeholder(
                                width = 14.sp,
                                height = 14.sp,
                                placeholderVerticalAlign = PlaceholderVerticalAlign.TextCenter
                            )
                        ) {
                            // This Icon will fill maximum size, which is specified by the [Placeholder]
                            // above. Notice the width and height in [Placeholder] are specified in TextUnit,
                            // and are converted into pixel by text layout.
                            GlideImage(
                                model = champ.iconUri, contentDescription = "icon_champion_dropdown${champ.name}"
                            )
                        }
                    )
                )
                DropdownMenuItem(
                    text = {
                        Text(
                            text =  buildAnnotatedString {
                                append(champ.name)
                                append("  ")
                                appendInlineContent(champ.name)
                            },
                            inlineContent = inlineContent,
                            modifier = Modifier
                                .wrapContentWidth()
                                .wrapContentHeight(),

                            )
                    },
                    onClick = {
                        selectedIndex = index
                        onSelected(championList[selectedIndex])
                        expanded = false
                    }
                )
            }
        }
    }
}