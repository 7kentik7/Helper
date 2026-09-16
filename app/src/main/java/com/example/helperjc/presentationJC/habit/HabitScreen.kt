package com.example.helperjc.presentationJC.habit

import android.widget.ProgressBar
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.helperjc.R
import com.example.helperjc.domain.habbits.HabitDay
import com.example.helperjc.presentationJC.theme.ProgressFifthStep
import com.example.helperjc.presentationJC.theme.ProgressFirstStep
import com.example.helperjc.presentationJC.theme.ProgressFourthStep
import com.example.helperjc.presentationJC.theme.ProgressSecondStep
import com.example.helperjc.presentationJC.theme.ProgressThirdStep
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun HabitScreen() {
    Scaffold(
    ) { innerPading ->


    }
}

@Preview
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
            modifier = Modifier,
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            StausIcon(2)
            Text(
                modifier = Modifier.padding(4.dp),
                maxLines = 1,
                text = "Зарядка",
                overflow = TextOverflow.Ellipsis
            )
        }

        HabitWeek()
    }
}


@Composable
private fun HabitWeek() {
    val month = YearMonth.now()
    val daysInMonth = month.lengthOfMonth()

    val weeksCount = (daysInMonth + 6) / 7

    val today = LocalDate.now()

    val initialPage = (today.dayOfMonth - 1) / 7

    val pagerState = rememberPagerState(
        initialPage = initialPage,
        pageCount = { weeksCount }
    )
    HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxWidth()
    ) { page ->

        val startDay = page * 7 + 1
        val endDay = minOf(
            startDay + 6,
            daysInMonth
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,

            ) {

            for (day in startDay..endDay) {

                val date = month.atDay(day)

                HabitDay()
            }
        }
    }
}


@Composable
private fun HabitDay(
) {
    Box(
        modifier = Modifier
            .size(width = 32.dp, height = 18.dp).clip(RoundedCornerShape(40.dp))
//            .border(
//                color = MaterialTheme.colorScheme.outline,
//                width = 1.dp,
//                shape = RoundedCornerShape(12.dp)
//            )

            .padding(1.dp)
            .background(color = MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        ProgressBarForItemHabitDay(23)
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

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {


        }


//            Icon(
//                imageVector = Icons.Default.Check,
//                contentDescription = "Выполнено",
//                modifier = Modifier.size(36.dp),
//                tint = MaterialTheme.colorScheme.primary
//            )


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





