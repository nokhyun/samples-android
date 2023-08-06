package com.nokhyun.third.composable

import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import java.time.LocalDate
import java.time.format.DateTimeFormatter

internal interface Provider<R> {
    fun create(): R
}

internal interface ValueFormatterFactory {
    abstract class VerticalProvider<R : AxisPosition> : Provider<AxisValueFormatter<R>>
    abstract class HorizontalProvider<R : AxisPosition> : Provider<AxisValueFormatter<R>>
}

internal class StartValueFormatter : ValueFormatterFactory.VerticalProvider<AxisPosition.Vertical.Start>() {
    override fun create(): AxisValueFormatter<AxisPosition.Vertical.Start> {
        return AxisValueFormatter { value, _ ->
            value.toInt().toString()
        }
    }
}

internal class TodayHorizontalValueFormatter : ValueFormatterFactory.HorizontalProvider<AxisPosition.Horizontal.Bottom>() {
    override fun create(): AxisValueFormatter<AxisPosition.Horizontal.Bottom> {
        return AxisValueFormatter { value, _ ->
            LocalDate.now().plusDays(value.toLong()).format(DateTimeFormatter.ofPattern("M월 d일"))
        }
    }
}

internal interface ThirdValueFormatterFactory {
    fun create(type: ThirdValueFormatter.Type): AxisValueFormatter<out AxisPosition>
}

internal class ThirdValueFormatter(
    private val todayHorizontalValueFormatter: TodayHorizontalValueFormatter = TodayHorizontalValueFormatter(),
    private val startValueFormatter: StartValueFormatter = StartValueFormatter()
) : ThirdValueFormatterFactory {

    enum class Type {
        BOTTOM_VALUE, START_VALUE
    }

    override fun create(type: Type): AxisValueFormatter<out AxisPosition> {
        return when (type) {
            Type.BOTTOM_VALUE -> todayHorizontalValueFormatter.create()
            Type.START_VALUE -> startValueFormatter.create()
        }
    }
}