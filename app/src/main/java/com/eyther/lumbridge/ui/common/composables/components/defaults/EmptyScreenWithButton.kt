package com.eyther.lumbridge.ui.common.composables.components.defaults

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.eyther.lumbridge.ui.theme.DefaultPadding
import com.eyther.lumbridge.ui.theme.runescapeTypography

@Composable
fun ColumnScope.EmptyScreenWithButton(
    text: String,
    buttonText: String,
    icon: @Composable (ColumnScope.() -> Unit)? = null,
    onButtonClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .align(Alignment.CenterHorizontally),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = text,
            style = runescapeTypography.titleSmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.padding(DefaultPadding))

        icon?.invoke(this)

        Spacer(modifier = Modifier.padding(DefaultPadding))

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
            onClick = onButtonClick
        ) {
            Text(text = buttonText)
        }
    }
}
