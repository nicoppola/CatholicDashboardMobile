package ui.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ui.util.shimmerEffect

@Composable
fun RefreshContent() {
    Column {
        // Date
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Text(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp)).shimmerEffect(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Medium),
                color = Color.Transparent,
                text = "Today's Date",
            )
        }

        Spacer(Modifier.height(4.dp))

        // Season
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp)
                .padding(bottom = 48.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Text(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .shimmerEffect(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Normal,
                    fontSize = 20.sp,
                ),
                color = Color.Transparent,
                text = "Tuesday of the fifth week of Lent"
            )
        }

        // Readings
        RefreshHeader()
        RefreshCard(numRows = 4)

        // Liturgy of Hours
        RefreshHeader()
        RefreshCard()

        // Office of Readings
        RefreshHeader()
        RefreshCard()
    }
}

@Composable
private fun RefreshHeader(){
    Row(
        modifier = Modifier
            .wrapContentWidth()
            .padding(bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .shimmerEffect(),
            style = MaterialTheme.typography.titleSmall,
            text = "Daily Readingss",
            color = Color.Transparent
        )
    }

    Spacer(Modifier.height(4.dp))
}

@Composable
private fun RefreshCard(numRows: Int = 1){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp)
            .clip(RoundedCornerShape(8.dp))
            .shimmerEffect(),
        )
    {
        (0..numRows).forEach { _ ->
            Text(
                text = "test",
                color = Color.Transparent
            )
        }
    }
    Spacer(Modifier.height(8.dp))
}