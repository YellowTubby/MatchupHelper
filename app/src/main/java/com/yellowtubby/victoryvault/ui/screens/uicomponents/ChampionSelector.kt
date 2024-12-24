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
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.yellowtubby.victoryvault.model.Champion

@Composable
fun ChampionSelector(
    championList: List<Champion>,
    initialChampion: Champion?,
    onSelected: (Champion) -> Unit,
) {
    val expanded = remember { mutableStateOf(false) }
    initialChampion?.let {
        val selectedIndex = remember { mutableIntStateOf(championList.indexOf(it)) }
        Box(modifier = Modifier.padding(8.dp), contentAlignment = Alignment.Center) {
            if(championList.isNotEmpty()){
                ChampionListItem(
                    championList[selectedIndex.intValue]
                ) {
                    expanded.value = true
                }
            }
            ChampionDropdown(
                championList,
                onSelected,
                expanded,
                selectedIndex,
            )
        }
    }

}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ChampionDropdown(
    championList: List<Champion>,
    onSelected: (Champion) -> Unit,
    expanded: MutableState<Boolean>,
    selectedIndex: MutableIntState,

    ){
    DropdownMenu(
        expanded = expanded.value, onDismissRequest = { expanded.value = false }) {
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
                    selectedIndex.intValue = index
                    onSelected(championList[selectedIndex.intValue])
                    expanded.value = false
                }
            )
        }
    }
}