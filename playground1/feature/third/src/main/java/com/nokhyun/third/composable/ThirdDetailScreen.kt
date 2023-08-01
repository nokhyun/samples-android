package com.nokhyun.third.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.nokhyun.third.composable.chart.SampleChartScreen
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.style.ChartStyle
import com.patrykandpatrick.vico.compose.style.ProvideChartStyle
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun ThirdDetailScreen() {
    logger { "ThirdDetailScreen" }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {

        val lineChart = lineChart()

        ProvideChartStyle(remember(lineChart) {
            ChartStyle(
                axis = ChartStyle.Axis(
                    axisLabelColor = Color.Black,
                    axisGuidelineColor = Color.Transparent,
                    axisLineColor = Color.Black
                ),
                columnChart = ChartStyle.ColumnChart(
                    columns = emptyList(),
                ),
                lineChart = ChartStyle.LineChart(
                    lines = emptyList(),
                ),
                marker = ChartStyle.Marker(),
                elevationOverlayColor = Color.Transparent
            )
        }) {
            SampleChartScreen(valueFormatter = valueFormatterFromToday(), valueFormatterEntries = valueFormatterEntries())
        }
    }
}

@Preview
@Composable
fun ThirdDetailScreenPreview() {
    ThirdDetailScreen()
}

private fun <T: AxisPosition> valueFormatterEntries(): AxisValueFormatter<T>{
    return AxisValueFormatter { value, _ ->
        value.toInt().toString()
    }
}

private fun <T : AxisPosition> valueFormatterFromToday(): AxisValueFormatter<T> {
    return AxisValueFormatter { value, _ ->
        LocalDate.now().plusDays(value.toLong()).format(DateTimeFormatter.ofPattern("M월 d일"))
    }
}