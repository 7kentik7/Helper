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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.helperjc.domain.habbits.Habit
import com.example.helperjc.domain.habbits.HabitColorScheme
import com.example.helperjc.domain.habbits.HabitDay
import com.example.helperjc.domain.habbits.HabitWithDays
import com.example.helperjc.presentationJC.theme.DeleteColor
import com.example.helperjc.presentationJC.theme.HelperJCTheme
import com.example.helperjc.utils.ThemePreviews
import com.example.helperjc.utils.localeAndMapToString
import com.example.helperjc.utils.toVisibleProgressColor
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.TemporalAdjusters

@Composable
fun HabitScreen(
    modifier: Modifier = Modifier,

    ) {
//    val state by viewModel.state.collectAsStateWithLifecycle()
    Scaffold(topBar = {
        AppBar(
            selectedPeriod = HabitPeriod.MONTH,
            onPeriodSelected = {},
            onAddClick = {
            }
        )
    }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 6.dp)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(4) { habitWithDays ->
                val dismissState = rememberSwipeToDismissBoxState(
                    confirmValueChange = { value ->
                        if (value == SwipeToDismissBoxValue.EndToStart) {
//                            viewModel.deletePlan(planDetails)
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
                    HabitMonth()
                }
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
private fun HabitMonth(
    modifier: Modifier = Modifier,
    days: List<HabitDay>,
    habitColor: Color
) {
    val today = LocalDate.now()
    val currentMonth = YearMonth.from(today)

    val firstDayOfMonth = currentMonth.atDay(1)

    // Понедельник = 0, вторник = 1 ... воскресенье = 6
    val firstDayOffset =
        firstDayOfMonth.dayOfWeek.value - DayOfWeek.MONDAY.value

    val monthDates = remember(currentMonth) {
        buildList<LocalDate?> {

            // Пустые ячейки перед первым днём месяца
            repeat(firstDayOffset) {
                add(null)
            }

            // Дни текущего месяца
            for (day in 1..currentMonth.lengthOfMonth()) {
                add(currentMonth.atDay(day))
            }

            // Заполняем последнюю неделю до 7 ячеек
            while (size % 7 != 0) {
                add(null)
            }
        }
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        userScrollEnabled = false
    ) {
        items(
            count = monthDates.size,
            key = { index -> index }
        ) { index ->

            val date = monthDates[index]

            HabitMonthDay(
                date = date,
                habitDay = date?.let { currentDate ->
                    days.firstOrNull {
                        it.date == currentDate
                    }
                },
                habitColor = habitColor
            )
        }
    }
}
@Composable
private fun HabitMonthDay(
    date: LocalDate?,
    habitDay: HabitDay?,
    habitColor: Color
) {
    if (date == null) {
        Spacer(
            modifier = Modifier.size(32.dp)
        )
        return
    }

    val today = LocalDate.now()
    val colors = rememberHabitColorScheme(habitColor)

    val progress = habitDay?.progress ?: 0

    val backgroundColor = when {
        progress >= 100 -> {
            colors.completed
        }

        progress > 0 -> {
            colors.progress.copy(
                alpha = 0.25f + (progress / 100f) * 0.5f
            )
        }

        else -> {
            colors.background
        }
    }

    Box(
        modifier = Modifier
            .size(32.dp)
            .clip(
                RoundedCornerShape(7.dp)
            )
            .background(backgroundColor)
            .then(
                if (date == today) {
                    Modifier.border(
                        width = 2.dp,
                        color = colors.progress,
                        shape = RoundedCornerShape(7.dp)
                    )
                } else {
                    Modifier
                }
            )
    )
}
@Composable
private fun HabitMonthHeader(
    habitWithDays: HabitWithDays,
    month: String,
    streak: Int,
    colors: HabitColorScheme,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        // Иконка
        Box(
            modifier = Modifier
                .size(72.dp)
                .background(
                    color = colors.background,
                    shape = CircleShape
                )
                .border(
                    width = 5.dp,
                    color = colors.track,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "😍",
                fontSize = 32.sp
            )
        }

        Spacer(
            modifier = Modifier.width(16.dp)
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = habitWithDays.habit.title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text ="сент.2026",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        StreakIcon(
            streak = streak,
            color = colors.background
        )
    }
}
@Composable
private fun HabitWeekItem(
) {
    val habitWithDays = HabitWithDays(
        habit = Habit(title = "Зарядка", color = Color.Red),
        days = habitWeekTestList()
    )
    Card(
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.outline),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    ) {
        val colors = rememberHabitColorScheme(habitWithDays.habit.color)
        val today = LocalDate.now()

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

@ThemePreviews
@Composable
private fun HabitPreview() {
    HelperJCTheme() {
        HabitScreen()

    }
}



