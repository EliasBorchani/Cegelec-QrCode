package com.eborchani.station.qrcode.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed


@Composable
fun RadioGroup(
    modifier: Modifier = Modifier,
    selected: Int? = null,
    onSelectionChanged: (Int) -> Unit = { },
    contents: RadioGroupScope.() -> Unit,
) {
    val scope = remember(contents) { RadioGroupScopeImpl().apply { contents(this) } }
    val maxWith = remember(contents) { mutableStateOf(0) }
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        scope.list.fastForEachIndexed { index, content ->
            val border = if (selected == index) BorderStroke(2.dp, Color.Blue) else null
            Card(
                modifier = Modifier
                    .widthIn(min = with(LocalDensity.current) { maxWith.value.toDp() })
                    .onGloballyPositioned { maxWith.value = maxOf(maxWith.value, it.size.width) },
                border = border,
            ) {
                Box(
                    modifier = Modifier
                        .clickable { onSelectionChanged(index) }
                        .widthIn(min = with(LocalDensity.current) { maxWith.value.toDp() })
                        .padding(8.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    content()
                }
            }
        }
    }
}

interface RadioGroupScope {
    fun item(item: @Composable () -> Unit)
}

private class RadioGroupScopeImpl : RadioGroupScope {

    val list = mutableListOf<@Composable () -> Unit>()

    override fun item(item: @Composable () -> Unit) {
        list.add(item)
    }
}

@Preview
@Composable
private fun GenerateRadioGroup() {
    val selected = remember { mutableStateOf<Int?>(null) }
    RadioGroup(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        selected = selected.value,
        onSelectionChanged = { selected.value = it }
    ) {
        item {
            Text(text = "Test1")
        }

        item {
            Text(text = "Test2")
        }

        item {
            Text(text = "Test3AndMoreAndMore")
        }
    }
}
