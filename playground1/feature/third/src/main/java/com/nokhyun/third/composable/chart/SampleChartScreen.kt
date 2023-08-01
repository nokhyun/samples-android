package com.nokhyun.third.composable.chart

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.patrykandpatrick.vico.compose.axis.axisLabelComponent
import com.patrykandpatrick.vico.compose.axis.horizontal.bottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.startAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.component.shape.shader.fromBrush
import com.patrykandpatrick.vico.compose.style.ChartStyle
import com.patrykandpatrick.vico.compose.style.ProvideChartStyle
import com.patrykandpatrick.vico.compose.style.currentChartStyle
import com.patrykandpatrick.vico.core.DefaultAlpha
import com.patrykandpatrick.vico.core.DefaultColors
import com.patrykandpatrick.vico.core.DefaultDimens
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.axis.vertical.VerticalAxis
import com.patrykandpatrick.vico.core.chart.copy
import com.patrykandpatrick.vico.core.chart.line.LineChart
import com.patrykandpatrick.vico.core.component.shape.LineComponent
import com.patrykandpatrick.vico.core.component.shape.Shapes
import com.patrykandpatrick.vico.core.component.shape.shader.DynamicShaders
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.FloatEntry
import com.patrykandpatrick.vico.core.entry.composed.plus
import com.patrykandpatrick.vico.core.entry.entriesOf
import com.patrykandpatrick.vico.core.util.RandomEntriesGenerator

@Composable
fun SampleChartScreen(
    chartColors: List<Color> = listOf(Color.Yellow, Color.Blue),
    valueFormatter: AxisValueFormatter<AxisPosition.Horizontal.Bottom>,
    start: AxisValueFormatter<AxisPosition.Vertical.Start>
) {
    val values = listOf<Int>(4, 12, 8, 16)

    val chartEntryModelProducer1 = ChartEntryModelProducer(entriesOf(values))
    val chartEntryModelProducer2 = ChartEntryModelProducer(entriesOf(16, 8, 12, 4))

    val generator = RandomEntriesGenerator(
        xRange = 0..96,
        yRange = 2..20
    )

    val multiDataSetChartEntryModelProducer: ChartEntryModelProducer = ChartEntryModelProducer()
    multiDataSetChartEntryModelProducer.setEntries(
        entries = List(size = 3) {
            generator.generateRandomEntries()
        }
    )

    val lineChart = lineChart()
    ProvideChartStyle(remember(lineChart) {
        ChartStyle(
            ChartStyle.Axis(
                axisLabelColor = Color.Black,
                axisGuidelineColor = Color.Transparent,
                axisLineColor = Color.Black
            ),
            ChartStyle.ColumnChart(
                columns = chartColors.map {
                    LineComponent(
                        it.toArgb(),
                        DefaultDimens.COLUMN_WIDTH,
                        Shapes.roundedCornerShape(DefaultDimens.COLUMN_ROUNDNESS_PERCENT)
                    )
                },
            ),
            ChartStyle.LineChart(
                chartColors.map {
                    LineChart.LineSpec(
                        lineColor = it.toArgb(),
                        lineBackgroundShader = DynamicShaders.fromBrush(
                            Brush.verticalGradient(
                                listOf(
                                    it.copy(DefaultAlpha.LINE_BACKGROUND_SHADER_START),
                                    it.copy(DefaultAlpha.LINE_BACKGROUND_SHADER_END)
                                )
                            )
                        )
                    )
                }
            ),
            ChartStyle.Marker(),
            Color(DefaultColors.Light.elevationOverlayColor)
        )
    }) {
        val defaultLines = currentChartStyle.lineChart.lines

        Chart(
            modifier = Modifier
                .background(Color.Gray),
            chart = lineChart(
                remember(defaultLines) {
                    defaultLines.map { it.copy(lineBackgroundShader = null) }
                }
            ),
//            chartModelProducer = multiDataSetChartEntryModelProducer,
            chartModelProducer = run {
                chartEntryModelProducer1.plus(chartEntryModelProducer2)
            },
            startAxis = startAxis(
                maxLabelCount = 4,
                valueFormatter = start,
//                valueFormatter = AxisValueFormatter<AxisPosition.Vertical.Start> { value, _ ->
//                    values.filterIndexed {
//                        it < atomicInteger.get()
//                    }.map {
//                        atomicInteger.incrementAndGet()
//                    }.toString()
//                },
                label = axisLabelComponent(
                    color = Color.Black,
                ),
                horizontalLabelPosition = VerticalAxis.HorizontalLabelPosition.Outside
            ),
            bottomAxis = bottomAxis(
                valueFormatter = valueFormatter
            )
        )
    }
}

internal fun entriesOf(yValues: List<Number>): List<FloatEntry> =
    yValues.mapIndexed { index, number -> com.patrykandpatrick.vico.core.entry.entryOf(index, number) }
