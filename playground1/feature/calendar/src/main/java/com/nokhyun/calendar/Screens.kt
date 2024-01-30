package com.nokhyun.calendar

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kizitonwose.calendar.compose.CalendarLayoutInfo
import com.kizitonwose.calendar.compose.CalendarState
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.kizitonwose.calendar.core.nextMonth
import com.kizitonwose.calendar.core.previousMonth
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.YearMonth

@Composable
fun CalendarScreen() {
    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth.minusMonths(100) } // Adjust as needed
    val endMonth = remember { currentMonth.plusMonths(100) } // Adjust as needed
    val firstDayOfWeek = remember { firstDayOfWeekFromLocale() } // Available from the library
    val coroutineScope = rememberCoroutineScope()
    var selections = remember { mutableStateListOf<CalendarDay>() }

    val state = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = firstDayOfWeek
    )

    val visibleMonth = rememberFirstMostVisibleMonth(state = state)
    Column(
        modifier = Modifier
            .background(Color.White)
    ) {
        CalendarTitle(
            onPrevious = {
                coroutineScope.launch {
                    state.animateScrollToMonth(state.firstVisibleMonth.yearMonth.previousMonth)
                }
            },
            onNext = {
                coroutineScope.launch {
                    state.animateScrollToMonth(state.firstVisibleMonth.yearMonth.nextMonth)
                }
            },
            year = visibleMonth.yearMonth.year.toString(),
            month = visibleMonth.yearMonth.monthValue.toString()
        )

        Spacer(modifier = Modifier.padding(top = 8.dp))

        HorizontalCalendar(
            modifier = Modifier.testTag("Calendar"),
            state = state,
            dayContent = { day ->
                Day(
                    day = day,
                    isSelected = selections.contains(day)
                ) {
                    if (selections.contains(it)) {
                        selections.remove(it)
                    } else {
                        selections.clear()
                        selections.add(day)
                    }
                }
            },
            monthHeader = { month ->
                val daysOfWeek = month.weekDays.first().map { it.date.dayOfWeek }
                MonthHeader(daysOfWeek = daysOfWeek)
            }
        )
    }
}


@Composable
fun CalendarTitle(
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    year: String,
    month: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(Color.White),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier
                .padding(start = 8.dp)
                .clickable { onPrevious() },
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = null
        )

        Text(
            modifier = Modifier,
            text = stringResource(
                id = R.string.unit_calendar_date,
                formatArgs = arrayOf(year, month)
            ),
            fontSize = 24.sp
        )

        Image(
            modifier = Modifier
                .padding(end = 8.dp)
                .clickable { onNext() },
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null
        )
    }
}

@Preview
@Composable
fun CalendarTitlePreview() {
    CalendarTitle(onPrevious = { }, onNext = {}, "2024", "1")
}

@Composable
fun MonthHeader(
    daysOfWeek: List<DayOfWeek>
) {
    Row {
        daysOfWeek.forEach {
            Text(
                modifier = Modifier.weight(1f),
                text = it.name.substring(0..2),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun Day(
    day: CalendarDay,
    isSelected: Boolean,
    onClick: (CalendarDay) -> Unit
) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(CircleShape)
            .background(if (isSelected) Color.Cyan else Color.Transparent)
            .clickable {
                onClick(day)
            }, // This is important for square sizing!
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = day.date.dayOfMonth.toString(),
            color = if (isSelected) Color.White else Color.Black
        )
    }
}

@Composable
fun rememberFirstMostVisibleMonth(
    state: CalendarState
): CalendarMonth {
    var visibleMonth by remember(state) { mutableStateOf(state.firstVisibleMonth) }

    LaunchedEffect(state) {
        snapshotFlow { state.layoutInfo.firstMostVisibleMonth() }
            .filterNotNull()
            .collect { month -> visibleMonth = month }
    }

    return visibleMonth
}

private fun CalendarLayoutInfo.firstMostVisibleMonth(): CalendarMonth? {
    return if (visibleMonthsInfo.isEmpty()) {
        null
    } else {
        val viewportSize = (viewportEndOffset + viewportStartOffset) * 90f / 100f
        visibleMonthsInfo.firstOrNull { item ->
            if (item.offset < 0) {
                item.offset + item.size >= viewportSize
            } else {
                item.size - item.offset >= viewportSize
            }
        }?.month
    }
}