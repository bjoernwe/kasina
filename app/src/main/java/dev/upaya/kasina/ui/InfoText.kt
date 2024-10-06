package dev.upaya.kasina.ui

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
internal fun InfoText() {
    Row(
        modifier = Modifier
            .wrapContentHeight()
            .padding(12.dp),
    ) {
        Icon(
            painter = painterResource(R.drawable.baseline_info_24),
            contentDescription = "Info",
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(
            modifier = Modifier.padding(4.dp),
        )
        Text(
            text = "Press/hold physical buttons to start",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}
