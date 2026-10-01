package com.example.helperjc.presentationJC.habit

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.helperjc.domain.habbits.Habit
import com.example.helperjc.domain.habbits.HabitColorScheme
import com.example.helperjc.domain.habbits.HabitDay
import com.example.helperjc.domain.habbits.HabitWithDays
import com.example.helperjc.presentationJC.theme.DeleteColor
import com.example.helperjc.presentationJC.theme.HelperJCTheme
import com.example.helperjc.utils.ThemePreviews
import com.example.helperjc.utils.localeAndMapToString
import com.example.helperjc.utils.parseToString
import com.example.helperjc.utils.toVisibleProgressColor
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Year
import java.time.YearMonth
import java.time.temporal.TemporalAdjusters

@Composable
fun HabitScreen(
    modifier: Modifier = Modifier,
    viewModel: HabitViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    Scaffold(topBar = {
        AppBar(
            selectedPeriod = HabitPeriod.MONTH,
            onPeriodSelected = {},
            onAddClick = {
            }
        )
    }
    ) { innerPadding ->
        when (state.period) {
            HabitPeriod.WEEK -> {
                HabitWeakLazyColumn(
                    state = state,
                    viewModel = viewModel,
                    innerPadding = innerPadding
                )
            }

            HabitPeriod.MONTH -> {
                HabitMonthGrid(
                    state = state,
                    viewModel = viewModel,
                    innerPadding = innerPadding
                )
            }

            HabitPeriod.YEAR -> {
                HabitYearLazyColumn(
                    state = state,
                    viewModel = viewModel,
                    innerPadding = innerPadding
                )
            }
        }

    }
}

@Composable
private fun HabitWeakLazyColumn(
    state: HabitState,
    viewModel: HabitViewModel,
    innerPadding: PaddingValues
) {
    LazyColumn(
        modifier = Modifier
            .padding(innerPadding)
            .padding(horizontal = 6.dp)
            .fillMaxHeight(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items = state.habitList, key = { it.habit.id }) { habitWithDays ->
            val dismissState = rememberSwipeToDismissBoxState(
                confirmValueChange = { value ->
                    if (value == SwipeToDismissBoxValue.EndToStart) {
                        viewModel.deleteHabit(habitWithDays)
                        true
                    } else false
                },
                positionalThreshold = { totalDistance ->
                    totalDistance * 0.7f
                }
            )

            SwipeToDismissBox(
                modifier = Modifier.animateItem(),
                state = dismissState,
                backgroundContent = {
                    DeleteBackground()
                }
            ) {
                HabitWeekItem(
                    habitWithDays = habitWithDays,
                    today = state.today
                )
            }
        }
    }
}

@Composable
private fun HabitMonthGrid(
    state: HabitState,
    viewModel: HabitViewModel,
    innerPadding: PaddingValues
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .padding(innerPadding)
            .padding(horizontal = 6.dp)
            .fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = state.habitList,
            key = { it.habit.id }
        ) { habitWithDays ->

            val dismissState = rememberSwipeToDismissBoxState(
                confirmValueChange = { value ->
                    if (value == SwipeToDismissBoxValue.EndToStart) {
                        viewModel.deleteHabit(habitWithDays)
                        true
                    } else {
                        false
                    }
                },
                positionalThreshold = { totalDistance ->
                    totalDistance * 0.7f
                }
            )

            SwipeToDismissBox(
                modifier = Modifier.animateItem(),
                state = dismissState,
                backgroundContent = {
                    DeleteBackground()
                }
            ) {
                HabitMonthItem(
                    habitWithDays = habitWithDays,
                    today = state.today
                )
            }
        }
    }
}

@Composable
private fun HabitYearLazyColumn(
    state: HabitState,
    viewModel: HabitViewModel,
    innerPadding: PaddingValues
) {
    LazyColumn(
        modifier = Modifier
            .padding(innerPadding)
            .padding(horizontal = 6.dp)
            .fillMaxHeight(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = state.habitList,
            key = { it.habit.id }
        ) { habitWithDays ->
            val dismissState = rememberSwipeToDismissBoxState(
                confirmValueChange = { value ->
                    if (value == SwipeToDismissBoxValue.EndToStart) {
                        viewModel.deleteHabit(habitWithDays)
                        true
                    } else {
                        false
                    }
                },
                positionalThreshold = { totalDistance ->
                    totalDistance * 0.7f
                }
            )

            SwipeToDismissBox(
                modifier = Modifier.animateItem(),
                state = dismissState,
                backgroundContent = {
                    DeleteBackground()
                }
            ) {
                HabitYearItem(
                    habitWithDays = habitWithDays,
                    today = state.today
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppBar(
    selectedPeriod: HabitPeriod,
    onPeriodSelected: (HabitPeriod) -> Unit,
    onAddClick: () -> Unit
) {
    var expanded by rememberSaveable {
        mutableStateOf(false)
    }

    val title = when (selectedPeriod) {
        HabitPeriod.WEEK -> "Неделя"
        HabitPeriod.MONTH -> "Месяц"
        HabitPeriod.YEAR -> "Год"
    }

    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        ),

        navigationIcon = {
            IconButton(
                onClick = {
                    // TODO: открыть меню приложения
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Home"
                )
            }
        },

        title = {
            Box {
                Row(
                    modifier = Modifier
                        .clickable {
                            expanded = true
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = title,
                        fontSize = 22.sp
                    )

                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Выбрать период",
                        modifier = Modifier.size(24.dp)
                    )
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = {
                        expanded = false
                    }
                ) {
                    HabitPeriod.entries.forEach { period ->

                        val periodTitle = when (period) {
                            HabitPeriod.WEEK -> "Неделя"
                            HabitPeriod.MONTH -> "Месяц"
                            HabitPeriod.YEAR -> "Год"
                        }

                        DropdownMenuItem(
                            text = {
                                Text(text = periodTitle)
                            },
                            trailingIcon = {
                                if (period == selectedPeriod) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null
                                    )
                                }
                            },
                            onClick = {
                                onPeriodSelected(period)
                                expanded = false
                            }
                        )
                    }
                }
            }
        },
        actions = {
            IconButton(
                onClick = onAddClick
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Добавить привычку"
                )
            }
        }
    )
}

@Composable
private fun DeleteBackground() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(8.dp))
            .background(DeleteColor)
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterEnd,
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onBackground
        )
    }
}


@Composable
private fun HabitWeekItem(
    habitWithDays: HabitWithDays,
    today: LocalDate
) {

    Card(
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.outline),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    ) {
        val colors = rememberHabitColorScheme(habitWithDays.habit.color)
        val todayHabitDay = habitWithDays.days
            .firstOrNull { it.date == today }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 6.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HabitCircleProgress(
                progressPercent = todayHabitDay?.progress ?: 0,
                modifier = Modifier
                    .size(36.dp)
                    .padding(2.dp),
                color = colors.progress,
                trackColor = colors.track
            )

            Text(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 4.dp),
                maxLines = 1,
                text = habitWithDays.habit.title,
                overflow = TextOverflow.Ellipsis
            )
            StreakIcon(
                streak = 3,
                color = colors.background
            )
        }
        HabitWeek(
            modifier = Modifier,
            days = habitWeekTestList(),
            habitColor = habitWithDays.habit.color
        )
    }
}

@Composable
private fun HabitMonthItem(
    habitWithDays: HabitWithDays,
    today: LocalDate,
    modifier: Modifier = Modifier,
) {
    val habit = habitWithDays.habit
    val colors = rememberHabitColorScheme(habit.color)

    val month = YearMonth.from(today)

    val firstDayOffset =
        month.atDay(1).dayOfWeek.value - DayOfWeek.MONDAY.value

    val daysInMonth = month.lengthOfMonth()

    val calendarDays = buildList {
        repeat(firstDayOffset) {
            add(null)
        }

        for (day in 1..daysInMonth) {
            add(month.atDay(day))
        }

        while (size % 7 != 0) {
            add(null)
        }
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outline
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {


            HabitMonthHeader(
                modifier = modifier,
                habitWithDays = habitWithDays,
                today = today,
                colors = colors
            )
            Spacer(
                modifier = Modifier.height(8.dp)
            )

            // Календарь 7 x 5
            calendarDays
                .chunked(7)
                .forEach { week ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        week.forEach { date ->

                            if (date == null) {

                                Spacer(
                                    modifier = Modifier
                                        .weight(1f)
                                        .aspectRatio(1.15f)
                                )

                            } else {

                                val habitDay =
                                    habitWithDays.days.firstOrNull {
                                        it.date == date
                                    }

                                HabitMonthDay(
                                    modifier = Modifier.weight(1f),
                                    date = date,
                                    habitDay = habitDay,
                                    today = today,
                                    habitColor = habit.color,
                                )
                            }
                        }
                    }

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )
                }
        }
    }
}
@Composable
private fun HabitYearItem(
    habitWithDays: HabitWithDays,
    today: LocalDate,
    modifier: Modifier = Modifier,
) {
    val habit = habitWithDays.habit
    val colors = rememberHabitColorScheme(habit.color)

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outline
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            HabitYearHeader(
                habitWithDays = habitWithDays,
                today = today,
                colors = colors
            )

            Spacer(modifier = Modifier.height(12.dp))

            HabitYearGrid(
                habitWithDays = habitWithDays,
                today = today
            )
        }
    }
}
@Composable
private fun HabitYearHeader(
    habitWithDays: HabitWithDays,
    today: LocalDate,
    colors: HabitColorScheme
) {
    val todayHabitDay = habitWithDays.days
        .firstOrNull { it.date == today }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HabitCircleProgress(
            progressPercent = todayHabitDay?.progress ?: 0,
            color = colors.background,
            trackColor = colors.background,
            modifier = Modifier.size(44.dp)
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 4.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = habitWithDays.habit.title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = 14.sp
            )

            Text(
                text = today.parseToString(),
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        StreakIcon(
            streak = 3,
            color = colors.background
        )
    }
}
@Composable
private fun HabitYearGrid(
    habitWithDays: HabitWithDays,
    today: LocalDate,
) {
    val year = today.year
    val firstDay = LocalDate.of(year, 1, 1)
    val firstDayOffset = firstDay.dayOfWeek.value - 1
    val daysInYear = if (Year.isLeap(year.toLong())) 366 else 365

    val totalCells = firstDayOffset + daysInYear
    val totalWeeks = (totalCells + 6) / 7

    val dates = remember(year) {
        List(totalWeeks * 7) { index ->
            firstDay
                .plusDays((index - firstDayOffset).toLong())
                .takeIf { it.year == year }
        }
    }

    val daysByDate = remember(habitWithDays.days) {
        habitWithDays.days.associateBy { it.date }
    }

    LazyHorizontalGrid(
        rows = GridCells.Fixed(7),
        modifier = Modifier
            .fillMaxWidth()
            .height(7 * 8.dp + 6 * 3.dp),
        horizontalArrangement = Arrangement.spacedBy(3.dp),
        verticalArrangement = Arrangement.spacedBy(3.dp),
        userScrollEnabled = false
    ) {
        items(
            count = dates.size,
            key = { it }
        ) { index ->
            val date = dates[index]

            if (date == null) {
                Spacer(Modifier.size(8.dp))
            } else {
                HabitYearDay(
                    modifier = Modifier.size(8.dp),
                    date = date,
                    habitDay = daysByDate[date],
                    today = today,
                    habitColor = habitWithDays.habit.color
                )
            }
        }
    }
}

@Composable
private fun HabitYearDay(
    date: LocalDate,
    habitDay: HabitDay?,
    today: LocalDate,
    habitColor: Color,
    modifier: Modifier = Modifier
) {
    val colors = rememberHabitColorScheme(habitColor)

    val isToday = date == today
    val isFuture = date.isAfter(today)
    val progress = habitDay?.progress ?: 0

    val backgroundColor = when {
        isFuture ->
            MaterialTheme.colorScheme.surfaceVariant

        habitDay?.isCompleted == true ->
            colors.completed

        progress > 0 ->
            colors.progress.copy(
                alpha = 0.25f + progress / 100f * 0.45f
            )

        else ->
            colors.background
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(3.dp))
            .background(backgroundColor)
            .then(
                if (isToday) {
                    Modifier.border(
                        width = 1.dp,
                        color = colors.progress.copy(alpha = 0.7f),
                        shape = RoundedCornerShape(3.dp)
                    )
                } else {
                    Modifier
                }
            )
    )
}

@Composable
private fun HabitMonthDay(
    date: LocalDate,
    habitDay: HabitDay?,
    today: LocalDate,
    habitColor: Color,
    modifier: Modifier = Modifier
) {
    val colors = rememberHabitColorScheme(habitColor)

    val isToday = date == today
    val isFuture = date.isAfter(today)

    val progress = habitDay?.progress ?: 0

    val backgroundColor = when {
        isFuture ->
            MaterialTheme.colorScheme.surfaceVariant

        habitDay?.isCompleted == true ->
            colors.completed

        progress > 0 ->
            colors.progress.copy(
                alpha = 0.25f + progress / 100f * 0.45f
            )

        else ->
            colors.background
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1.15f)
            .clip(RoundedCornerShape(6.dp))
            .background(backgroundColor)
            .then(
                if (isToday) {
                    Modifier.border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.onBackground,
                        shape = RoundedCornerShape(6.dp)
                    )
                } else {
                    Modifier
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        // Число дня
        Text(
            text = date.dayOfMonth.toString(),
            style = MaterialTheme.typography.labelSmall,
            color = if (isFuture) {
                MaterialTheme.colorScheme.onSurfaceVariant
            } else {
                MaterialTheme.colorScheme.onBackground
            }
        )
    }
}

@Composable
private fun HabitWeekDay(
    habitDay: HabitDay?,
    date: LocalDate,
    habitColor: Color,
    modifier: Modifier = Modifier
) {
    val today = LocalDate.now()

    val isToday = date == today
    val isFuture = date.isAfter(today)
    val isCompleted = habitDay?.isCompleted == true

    val colors = rememberHabitColorScheme(habitColor)

    val backgroundColor = when {
        isFuture -> MaterialTheme.colorScheme.surfaceVariant
        isCompleted -> colors.completed
        else -> colors.background
    }

    Column(
        modifier = modifier.padding(6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = date.dayOfWeek.localeAndMapToString("ru"),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(4.dp))

        Box(
            modifier = Modifier
                .size(width = 42.dp, height = 18.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(backgroundColor)
                .then(
                    if (isToday) {
                        Modifier.border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.onBackground,
                            shape = RoundedCornerShape(6.dp)
                        )
                    } else {
                        Modifier
                    }
                )
                .padding(2.dp),
            contentAlignment = Alignment.Center
        ) {
            if (
                !isFuture &&
                habitDay != null &&
                !isCompleted &&
                habitDay.countOfRepetitions > 1
            ) {
                HabitDayLinearProgress(
                    progressPercent = habitDay.progress,
                    color = colors.progress,
                    trackColor = colors.track,
                    modifier = Modifier.padding(4.dp),
                    height = 4.dp
                )
            }
        }
    }
}

@Composable
private fun HabitMonthHeader(
    modifier: Modifier = Modifier,
    habitWithDays: HabitWithDays,
    today: LocalDate,
    colors: HabitColorScheme
) {
    val todayHabitDay = habitWithDays.days
        .firstOrNull { it.date == today }



    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HabitCircleProgress(
                progressPercent = todayHabitDay?.progress ?: 0,
                color = colors.background,
                trackColor = colors.background,
                modifier = Modifier
                    .size(44.dp)

            )
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    modifier = Modifier
                        .padding(horizontal = 4.dp),
                    text = habitWithDays.habit.title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 14.sp
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        modifier = Modifier
                            .padding(horizontal = 4.dp),
                        text = today.parseToString(),
                        fontSize = 12.sp
                    )
                    StreakIcon(
                        streak = 3,
                        color = colors.background
                    )
                }
            }

        }
        Spacer(
            modifier = Modifier.height(4.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            DayOfWeek.entries.forEach { dayOfWeek ->
                Text(
                    modifier = Modifier.weight(1f),
                    text = dayOfWeek.localeAndMapToString("ru"),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 12.sp,
                    lineHeight = 14.sp
                )
            }
        }
    }
}

@Composable
private fun HabitWeek(
    modifier: Modifier = Modifier,
    days: List<HabitDay>,
    habitColor: Color
) {
    val today = LocalDate.now()

    val startOfWeek = today.with(
        TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)
    )

    val weekDates = (0..6).map { offset ->
        startOfWeek.plusDays(offset.toLong())
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        weekDates.forEach { date ->

            HabitWeekDay(
                date = date,
                habitDay = days.firstOrNull { it.date == date },
                habitColor = habitColor
            )
        }
    }
}

@Composable
private fun StreakIcon(
    streak: Int?,
    color: Color
) {
    Box(
        modifier = Modifier
            .padding(2.dp)
            .height(24.dp)
            .defaultMinSize(minWidth = 42.dp)
            .background(
                color = color,
                shape = RoundedCornerShape(percent = 50)
            )
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(percent = 50)
            )
            .padding(horizontal = 7.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(3.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "🔥",
                fontSize = 11.sp
            )

            Text(
                text = (streak ?: 0).toString(),
                fontSize = 11.sp
            )
        }
    }
}

@Composable
private fun HabitDayLinearProgress(
    progressPercent: Int,
    color: Color,
    trackColor: Color,
    modifier: Modifier = Modifier,
    height: Dp = 6.dp,
) {
    val progress = (progressPercent / 100f)
        .coerceIn(0f, 1f)

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(
            durationMillis = 500,
            easing = FastOutSlowInEasing
        ),
        label = "progress"
    )

    val shape = RoundedCornerShape(percent = 50)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(shape)
            .background(trackColor)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(animatedProgress)
                .fillMaxHeight()
                .clip(shape)
                .background(color)
        )
    }
}

@Composable
private fun HabitCircleProgress(
    progressPercent: Int,
    color: Color,
    trackColor: Color,
    modifier: Modifier = Modifier,
) {
    val progress = (progressPercent / 100f)
        .coerceIn(0f, 1f)

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(
            durationMillis = 500,
            easing = FastOutSlowInEasing
        ),
        label = "habitProgress"
    )

    Canvas(
        modifier = modifier.aspectRatio(1f)
    ) {
        val strokeWidth = 3.dp.toPx()
        val radius = (size.minDimension - strokeWidth) / 2f

        // Фоновое кольцо
        drawCircle(
            color = trackColor,
            radius = radius,
            center = center,
            style = Stroke(
                width = strokeWidth
            )
        )

        // Прогресс
        if (animatedProgress > 0f) {
            drawArc(
                color = color,
                startAngle = -90f,
                sweepAngle = 360f * animatedProgress,
                useCenter = false,
                style = Stroke(
                    width = strokeWidth,
                    cap = StrokeCap.Round
                )
            )
        }
    }
}


@Composable
private fun rememberHabitColorScheme(
    habitColor: Color
): HabitColorScheme {

    val surface = MaterialTheme.colorScheme.surface

    val progressColor = habitColor.toVisibleProgressColor()

    val background = habitColor
        .copy(alpha = 0.18f)
        .compositeOver(surface)

    val track = progressColor
        .copy(alpha = 0.35f)
        .compositeOver(background)

    return HabitColorScheme(
        background = background,
        track = track,
        progress = progressColor,
        completed = progressColor,
    )
}

private fun habitWeekTestList(): List<HabitDay> {
    val today = LocalDate.now()

    val startOfWeek = today.with(
        TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)
    )

    return listOf(
        // Понедельник — полностью выполнено
        HabitDay(
            id = 1,
            habitId = 1,
            date = startOfWeek,
            isCompleted = true,
            countOfRepetitions = 3,
            countOfCompletedRepetitions = 3,
            progress = 10
        ),

        // Вторник — выполнено частично
        HabitDay(
            id = 2,
            habitId = 1,
            date = startOfWeek.plusDays(1),
            isCompleted = false,
            countOfRepetitions = 3,
            countOfCompletedRepetitions = 1,
            progress = 80
        ),

        // Среда — полностью выполнено
        HabitDay(
            id = 3,
            habitId = 1,
            date = startOfWeek.plusDays(2),
            isCompleted = true,
            countOfRepetitions = 1,
            countOfCompletedRepetitions = 1,
            progress = 100
        ),

        // Четверг — ничего не выполнено
        HabitDay(
            id = 4,
            habitId = 1,
            date = startOfWeek.plusDays(3),
            isCompleted = false,
            countOfRepetitions = 3,
            countOfCompletedRepetitions = 0,
            progress = 0
        ),

        // Пятница — частично
        HabitDay(
            id = 5,
            habitId = 1,
            date = startOfWeek.plusDays(4),
            isCompleted = false,
            countOfRepetitions = 4,
            countOfCompletedRepetitions = 2,
            progress = 50
        ),

        // Суббота
        HabitDay(
            id = 6,
            habitId = 1,
            date = startOfWeek.plusDays(5),
            isCompleted = false,
            countOfRepetitions = 2,
            countOfCompletedRepetitions = 1,
            progress = 50
        ),

        // Воскресенье
        HabitDay(
            id = 7,
            habitId = 1,
            date = startOfWeek.plusDays(6),
            isCompleted = false,
            countOfRepetitions = 1,
            countOfCompletedRepetitions = 0,
            progress = 0
        )
    )
}

//@ThemePreviews
//@Composable
//private fun HabitPreview() {
//    HelperJCTheme() {
//        HabitScreen()
//    }
//}
@ThemePreviews
@Composable
private fun HabitPeriodsPreview() {
    HelperJCTheme {
        val today = LocalDate.of(2026, 10, 6)

        val habits = listOf(
            HabitWithDays(
                habit = Habit(
                    id = 1,
                    title = "Привет Привет...",
                    color = Color(0xFF00BFA5)
                ),
                days = previewHabitDays(1)
            ),
            HabitWithDays(
                habit = Habit(
                    id = 2,
                    title = "Зарядка",
                    color = Color.Red
                ),
                days = previewHabitDays(2)
            )
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(6.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Месяц
            item {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(380.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    userScrollEnabled = false
                ) {
                    items(
                        items = habits,
                        key = { "month-${it.habit.id}" }
                    ) { habit ->
                        HabitMonthItem(
                            habitWithDays = habit,
                            today = today
                        )
                    }
                }
            }

            // Неделя
            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    habits.forEach { habit ->
                        HabitWeekItem(
                            habitWithDays = habit,
                            today = today
                        )
                    }
                }
            }

            // Год
            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    habits.forEach { habit ->
                        HabitYearItem(
                            habitWithDays = habit,
                            today = today
                        )
                    }
                }
            }
        }
    }
}

private fun previewHabitDays(habitId: Int): List<HabitDay> {
    val start = LocalDate.of(2026, 1, 1)

    return (0 until 280)
        .map { start.plusDays(it.toLong()) }
        .filter { it.dayOfWeek.value % 2 == 0 }
        .mapIndexed { index, date ->
            HabitDay(
                id = habitId * 1000 + index,
                habitId = habitId,
                date = date,
                isCompleted = index % 3 != 0,
                countOfRepetitions = 1,
                countOfCompletedRepetitions = if (index % 3 != 0) 1 else 0,
                progress = if (index % 3 != 0) 100 else 40
            )
        }
}