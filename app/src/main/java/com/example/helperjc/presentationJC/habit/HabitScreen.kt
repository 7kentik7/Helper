package com.example.helperjc.presentationJC.habit

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PaintingStyle.Companion.Stroke
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.helperjc.domain.habbits.HabitDay
import com.example.helperjc.presentationJC.theme.HelperJCTheme
import com.example.helperjc.utils.ThemePreviews
import com.example.helperjc.utils.localeAndMapToString
import com.example.helperjc.utils.muted
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import kotlin.math.min

@Composable
fun HabitScreen() {
    Scaffold(
    ) { innerPading ->


    }
}

@Composable
private fun Habit() {
    Card(
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.outline),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HabitProgress(
                progress = 0.1f,
                modifier = Modifier.size(48.dp)
            )

            Text(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp),
                maxLines = 1,
                text = "ЗарядЗарядЗарядЗарядЗарядЗарядЗарядЗарядЗарядЗаряд",
                overflow = TextOverflow.Ellipsis
            )

            StausIcon(5)
        }


        HabitWeek(modifier = Modifier)
    }
}


@Composable
private fun HabitWeek(
    modifier: Modifier
) {
    val today = LocalDate.now().dayOfWeek
    val daysOfWeek = DayOfWeek.entries
    Row(
        modifier = Modifier
            .padding(6.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        daysOfWeek.forEach { day ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = day.localeAndMapToString("ru")
                )
                HabitWeekDay(
                    modifier = modifier,
                    isToday = day == today,
                    habitDay = habitWeekTestList()[day.value - 1]
                )
            }

        }
    }
}


@Composable
private fun HabitWeekDay(
    modifier: Modifier,
    isToday: Boolean,
    habitDay: HabitDay
) {
    Box(
        modifier = Modifier
            .size(width = 32.dp, height = 18.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(color = MaterialTheme.colorScheme.primary)
            .then(
                if (isToday) {
                    Modifier.border(
                        color = MaterialTheme.colorScheme.outline,
                        width = 1.dp,
                        shape = RoundedCornerShape(6.dp)
                    )
                } else modifier
            )
            .padding(2.dp),
        contentAlignment = Alignment.Center
    ) {

        if (habitDay.countOfRepetitions > 1 && !habitDay.isCompleted) {
            LinearProgressBarHabit(
                progress = habitDay.progress
            )

        }
    }
}


@Composable
private fun StausIcon(
    streak: Int?
) {
    Box(
        modifier = Modifier
            .padding(4.dp)
            .background(
                color = MaterialTheme.colorScheme.background,
                shape = CircleShape
            )
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {


        Icon(
            imageVector = Icons.Default.Check,
            contentDescription = "Выполнено",
            modifier = Modifier.size(36.dp),
            tint = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun ProgressBarForItemHabitDay(progress: Int) {
    val progressFloat = progress / 100f
    val color = Color.Cyan
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(4.dp)
            .clip(RoundedCornerShape(50))
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(progressFloat)
                .fillMaxHeight()
                .clip(RoundedCornerShape(50))
                .background(color)
        )
    }
}

@Composable
fun LinearProgressBarHabit(
    progress: Int,
    modifier: Modifier = Modifier,
    height: Dp = 6.dp,
    color: Color = MaterialTheme.colorScheme.primary,
    trackColor: Color = color.muted(0.6f),
) {
    val target = (progress / 100f).coerceIn(0f, 1f)

    val animatedProgress by animateFloatAsState(
        targetValue = target,
        animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing),
        label = "linearProgress",
    )

    // Цвет плавно переходит в зелёный на 100%
    val animatedColor by animateColorAsState(
        targetValue = if (target >= 1f) Color(0xFF4CAF50) else color,
        animationSpec = tween(400),
        label = "linearColor",
    )

    // Пульсация непрозрачности, когда есть прогресс и он < 100%
    val pulse = rememberInfiniteTransition(label = "pulse")
    val alpha by pulse.animateFloat(
        initialValue = 1f,
        targetValue = if (target in 0.01f..0.99f) 0.75f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "alpha",
    )

    // Появление шкалы (fade + scale)
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }
    val appear by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(300),
        label = "appear",
    )


    val shape = RoundedCornerShape(percent = 50)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .alpha(appear)
            .clip(shape)
            .background(trackColor),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(animatedProgress)
                .fillMaxHeight()
                .clip(shape)
                .background(animatedColor.copy(alpha = alpha))
        )
    }
}

@Composable
fun HabitProgress(
    progress: Float,
    modifier: Modifier = Modifier,
) {
    val progressValue = progress.coerceIn(0f, 1f)

    Canvas(
        modifier = modifier.aspectRatio(1f)
    ) {
        val strokeWidth = 2.dp.toPx()
        // Радиус круга
        val radius = (size.minDimension - strokeWidth) / 2f
        // Сам заполненный круг
        drawCircle(
            color = Color.Cyan,
            radius = radius,
            center = center
        )
        // Фоновая обводка
        drawArc(
            color = Color.Cyan.muted(),
            startAngle = -90f,
            sweepAngle = 360f,
            useCenter = false,
            style = Stroke(
                width = strokeWidth
            )
        )
        // Прогресс
        drawArc(
            color = Color(0xFF10B981),
            startAngle = -90f,
            sweepAngle = 360f * progressValue,
            useCenter = false,
            style = Stroke(
                width = strokeWidth,
                cap = StrokeCap.Round
            )
        )
    }
}

private fun habitWeekTestList(): List<HabitDay> {
    val days = mutableListOf<HabitDay>()
    for (i in 1..7) {
        days.add(
            HabitDay(
                id = i,
                day = i,
                period = YearMonth.now(),
                isCompleted = i % 2 == 0,
                countOfRepetitions = i,
                countOfCompletedRepetitions = i,
                progress = i * 7
            )
        )
    }
    return days
}

@ThemePreviews
@Composable
private fun HabitPreview() {
    HelperJCTheme() {
        Habit()
    }
}



