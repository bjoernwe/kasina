package dev.upaya.kasina.ui.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import dev.upaya.kasina.R


@Composable
internal fun ErrorText() {
    Row(
        modifier = Modifier
            .wrapContentHeight()
            .padding(12.dp),
    ) {
        Icon(
            painter = painterResource(R.drawable.baseline_error_24),
            contentDescription = "Error",
            tint = MaterialTheme.colorScheme.onErrorContainer,
        )
        Spacer(
            modifier = Modifier.padding(4.dp),
        )
        Text(
            text = "Error: No flashlight found on this device!",
            color = MaterialTheme.colorScheme.onErrorContainer,
        )
    }
}
