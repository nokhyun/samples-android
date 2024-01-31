package com.nokhyun.calendar

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kizitonwose.calendar.compose.CalendarLayoutInfo
import com.kizitonwose.calendar.compose.CalendarState
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.WeekCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.compose.weekcalendar.rememberWeekCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.atStartOfMonth
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.nextMonth
import com.kizitonwose.calendar.core.previousMonth
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth

val localDateSaver = listSaver<LocalDate, Any>(
    save = { listOf(it) },
    restore = {
        it[0] as LocalDate
    }
)

@Composable
fun CalendarScreen() {
    val currentDate = remember { LocalDate.now() }
    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth.minusMonths(500) } // Adjust as needed
    val endMonth = remember { currentMonth.plusMonths(500) } // Adjust as needed
    val daysOfWeek = remember { daysOfWeek() }
    val coroutineScope = rememberCoroutineScope()
    val selections = rememberSaveable { mutableStateOf(listOf<LocalDate>()) }
    var isWeekState by rememberSaveable { mutableStateOf(false) }

    val weekState = rememberWeekCalendarState(
        startDate = startMonth.atStartOfMonth(),
        endDate = endMonth.atEndOfMonth(),
        firstVisibleWeekDate = currentDate,
        firstDayOfWeek = daysOfWeek.first(),
    )

    val state = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = daysOfWeek.first()
    )

    val visibleMonth = rememberFirstMostVisibleMonth(state = state)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .pointerInput(state) {
                detectDragGestures { change, dragAmount ->

                    isWeekState = dragAmount.y < 0

//                    coroutineScope.launch {
//                        if (dragAmount.x < 0 && dragAmount.y > 20) {
//                            if (isWeekState) {
//                                weekState.animateScrollToWeek(weekState.startDate)
//                            } else {
//                                state.animateScrollToMonth(state.firstVisibleMonth.yearMonth.nextMonth)
//                            }
//                        }else{
//                            if (isWeekState) {
//                                weekState.animateScrollToWeek(weekState.startDate)
//                            }else{
//                                state.animateScrollToMonth(state.firstVisibleMonth.yearMonth.nextMonth)
//                            }
//                        }
//                    }
                }
            })
    {
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

        AnimatedVisibility(visible = !isWeekState) {
            HorizontalCalendar(
                modifier = Modifier
                    .testTag("Calendar"),
                userScrollEnabled = false,
                state = state,
                dayContent = { day ->
                    if (LocalDate
                            .now()
                            .isEqual(day.date)
                    ) {
                        Today(
                            day = day.date,
                            isSelected = selections.value.contains(day.date)
                        ) {
                            selections.value
                                .toMutableList()
                                .apply {
                                    if (contains(it)) {
                                        remove(it)
                                    } else {
                                        clear()
                                        add(it)
                                    }
                                }
                                .also {
                                    selections.value = it
                                }
                        }
                    } else {
                        Day(
                            day = day.date,
                            isSelected = selections.value.contains(day.date)
                        ) {
                            selections.value
                                .toMutableList()
                                .apply {
                                    if (contains(it)) {
                                        remove(it)
                                    } else {
                                        clear()
                                        add(it)
                                    }
                                }
                                .also {
                                    selections.value = it
                                }
                        }
                    }
                },
                monthHeader = { month ->
                    month.weekDays
                        .first()
                        .map { it.date.dayOfWeek }
                        .also { daysOfWeek ->
                            MonthHeader(daysOfWeek = daysOfWeek)
                        }
                }
            )
        }

        // Week Calendar
        AnimatedVisibility(visible = isWeekState) {
            WeekCalendar(
                modifier = Modifier,
                userScrollEnabled = false,
                state = weekState,
                dayContent = { day ->
                    Day(
                        day = day.date,
                        isSelected = selections.value.contains(day.date)
                    ) {
                        selections.value
                            .toMutableList()
                            .apply {
                                if (contains(it)) {
                                    remove(it)
                                } else {
                                    clear()
                                    add(it)
                                }
                            }
                            .also {
                                selections.value = it
                            }
                    }
                },
                weekHeader = {
                    it.days
                        .map { it.date.dayOfWeek }
                        .also { daysOfWeek ->
                            MonthHeader(daysOfWeek = daysOfWeek)
                        }
                }
            )
        }
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
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
            contentDescription = null
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
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null
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
    Row(
        modifier = Modifier
    ) {
        daysOfWeek.forEach {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
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
fun Day(
    day: LocalDate,
    isSelected: Boolean,
    onClick: (LocalDate) -> Unit
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
            text = day.dayOfMonth.toString(),
            color = if (isSelected) Color.White else Color.Black
        )
    }
}

@Composable
fun Today(
    day: LocalDate,
    isSelected: Boolean,
    onClick: (LocalDate) -> Unit
) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(CircleShape)
            .background(if (isSelected) Color.Cyan else Color.LightGray)
            .clickable {
                onClick(day)
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = day.dayOfMonth.toString(),
            color = Color.White
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

private fun log(msg: Any?) {
    Log.e("calendar Log: ", msg.toString())
}
