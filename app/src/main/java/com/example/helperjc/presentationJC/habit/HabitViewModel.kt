package com.example.helperjc.presentationJC.habit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.helperjc.domain.habbits.HabitWithDays
import com.example.helperjc.domain.habbits.usecases.DeleteHabitUseCase
import com.example.helperjc.domain.habbits.usecases.GetHabitWithDaysListUseCase
import com.example.helperjc.domain.tasks.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.TemporalAdjusters
import javax.inject.Inject

data class HabitState(
    val habitList: List<HabitWithDays> = emptyList(),
    val period: HabitPeriod = HabitPeriod.WEEK,
    val today: LocalDate = LocalDate.now()
)

@HiltViewModel
class HabitViewModel @Inject constructor(
    private val getHabitWithDaysListUseCase: GetHabitWithDaysListUseCase,
    private val deleteHabitUseCase: DeleteHabitUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(HabitState())
    val state = _state.asStateFlow()

    private val allHabits =
        MutableStateFlow<List<HabitWithDays>>(emptyList())

    init {
        viewModelScope.launch {
            getHabitWithDaysListUseCase().collect { habits ->

                allHabits.value = habits

                val currentState = _state.value

                _state.update {
                    it.copy(
                        habitList = filteredHabits(
                            habits = habits,
                            period = currentState.period,
                            today = currentState.today
                        )
                    )
                }
            }
        }
    }

    fun deleteHabit(habitWithDays: HabitWithDays) {
        viewModelScope.launch {
            deleteHabitUseCase(habitWithDays.habit.id)
        }
    }

    fun setPeriod(period: HabitPeriod) {
        _state.update {
            it.copy(
                habitList = filteredHabits(
                    habits = allHabits.value,
                    period = period,
                    today = it.today,
                ),
                period = period,
            )
        }
    }

    private fun filteredHabits(
        habits: List<HabitWithDays>,
        period: HabitPeriod,
        today: LocalDate
    ): List<HabitWithDays> {

        val range = getDateRange(
            period = period,
            today = today
        )

        return habits.map { habit ->
            habit.copy(
                days = habit.days.filter { day ->
                    day.date in range
                }
            )
        }
    }

    private fun getDateRange(
        period: HabitPeriod,
        today: LocalDate
    ): ClosedRange<LocalDate> {
        return when (period) {

            HabitPeriod.WEEK -> {
                val start = today.with(
                    TemporalAdjusters.previousOrSame(
                        DayOfWeek.MONDAY
                    )
                )
                val end = start.plusDays(6)
                start..end
            }

            HabitPeriod.MONTH -> {
                val month = YearMonth.from(today)

                month.atDay(1)..month.atEndOfMonth()
            }

            HabitPeriod.YEAR -> {
                val year = today.year

                LocalDate.of(year, 1, 1)..LocalDate.of(year, 12, 31)
            }
        }
    }
}

