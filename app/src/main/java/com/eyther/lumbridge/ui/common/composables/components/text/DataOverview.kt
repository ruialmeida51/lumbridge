package com.eyther.lumbridge.ui.common.composables.components.text

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.eyther.lumbridge.ui.common.composables.text.buildAnnotatedStringTextWithLabel

@Composable
fun DataOverview(
    modifier: Modifier = Modifier,
    label: String,
    text: String,
    textAlign: TextAlign = TextAlign.Start
) {
    Text(
        modifier = modifier,
        text = buildAnnotatedStringTextWithLabel(
            label = label.plus(": "),
            remainingText = text
        ),
        style = MaterialTheme.typography.bodyMedium,
        textAlign = textAlign
    )
}
