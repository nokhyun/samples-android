package com.nokhyun.third.composable.chart

import androidx.compose.runtime.Composable
import com.patrykandpatrick.vico.compose.axis.horizontal.bottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.startAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.chart.column.ColumnChart
import com.patrykandpatrick.vico.core.entry.ChartEntryModel

@Composable
fun SingleColumnChartScreen(
    columnChart: ColumnChart,
    entryModel: ChartEntryModel,
    axisValueFormatter: AxisValueFormatter<AxisPosition.Horizontal.Bottom>
) {
    Chart(
        chart = columnChart,
        model = entryModel,
        startAxis = startAxis(),
        bottomAxis = bottomAxis(
            valueFormatter = axisValueFormatter
        )
    )
}