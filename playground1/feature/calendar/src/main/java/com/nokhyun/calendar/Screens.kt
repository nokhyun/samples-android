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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kizitonwose.calendar.compose.CalendarLayoutInfo
import com.kizitonwose.calendar.compose.CalendarState
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.WeekCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.compose.weekcalendar.WeekCalendarState
import com.kizitonwose.calendar.compose.weekcalendar.rememberWeekCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.Week
import com.kizitonwose.calendar.core.atStartOfMonth
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.nextMonth
import com.kizitonwose.calendar.core.previousMonth
import com.kizitonwose.calendar.core.yearMonth
import kotlinx.coroutines.flow.filter
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
    var isWeek by rememberSaveable { mutableStateOf(false) }

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
                    isWeek = dragAmount.y < 0
                }
            })
    {
        CalendarTitle(
            onPrevious = {
                coroutineScope.launch {
                    if (isWeek) {
                        val prevWeek = weekState.firstVisibleWeek.days.first().date.minusDays(1)
                        weekState.animateScrollToWeek(prevWeek)
                    } else {
                        state.animateScrollToMonth(state.firstVisibleMonth.yearMonth.previousMonth)
                    }
                }
            },
            onNext = {
                coroutineScope.launch {
                    if (isWeek) {
                        val nextWeek = weekState.firstVisibleWeek.days.last().date.plusDays(1)
                        weekState.animateScrollToWeek(nextWeek)
                    } else {
                        state.animateScrollToMonth(state.firstVisibleMonth.yearMonth.nextMonth)
                    }
                }
            },
            isWeek = isWeek,
            monthState = state,
            weekState = weekState
        )

        Spacer(modifier = Modifier.padding(top = 8.dp))

        AnimatedVisibility(visible = !isWeek) {
            // TODO month 현재 월의 LocalDate 필요.
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
        AnimatedVisibility(visible = isWeek) {
            // TODO 주로 변환 시 시점을 어디로 맞출지가 중요한듯. 선택된게 있으면 거기 위주 아니면 첫째 주?
            WeekCalendar(
                modifier = Modifier,
                userScrollEnabled = false,
                state = weekState,
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
    isWeek: Boolean,
    monthState: CalendarState,
    weekState: WeekCalendarState,
) {
    val visibleMonth = rememberFirstVisibleMonthAfterScroll(state = monthState)
    val visibleWeek = rememberFirstVisibleWeekAfterScroll(state = weekState)

    val currentMonth =
        if (isWeek) visibleWeek.days.first().date.yearMonth else visibleMonth.yearMonth

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
                formatArgs = arrayOf(currentMonth.year, currentMonth.month)
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

@Composable
fun MonthHeader(
    daysOfWeek: List<DayOfWeek>,
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
    onClick: (CalendarDay) -> Unit,
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
    onClick: (LocalDate) -> Unit,
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
    onClick: (LocalDate) -> Unit,
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
    state: CalendarState,
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

/**
 * Returns the first visible month in a paged calendar **after** scrolling stops.
 *
 * @see [rememberFirstCompletelyVisibleMonth]
 * @see [rememberFirstMostVisibleMonth]
 */
@Composable
fun rememberFirstVisibleMonthAfterScroll(state: CalendarState): CalendarMonth {
    val visibleMonth = remember(state) { mutableStateOf(state.firstVisibleMonth) }
    LaunchedEffect(state) {
        snapshotFlow { state.isScrollInProgress }
            .filter { scrolling -> !scrolling }
            .collect { visibleMonth.value = state.firstVisibleMonth }
    }
    return visibleMonth.value
}

/**
 * Find first visible week in a paged week calendar **after** scrolling stops.
 */
@Composable
fun rememberFirstVisibleWeekAfterScroll(state: WeekCalendarState): Week {
    val visibleWeek = remember(state) { mutableStateOf(state.firstVisibleWeek) }
    LaunchedEffect(state) {
        snapshotFlow { state.isScrollInProgress }
            .filter { scrolling -> !scrolling }
            .collect { visibleWeek.value = state.firstVisibleWeek }
    }
    return visibleWeek.value
}

private fun log(msg: Any?) {
    Log.e("calendar Log: ", msg.toString())
}
