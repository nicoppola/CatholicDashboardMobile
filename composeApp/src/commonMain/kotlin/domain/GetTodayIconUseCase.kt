package domain

import com.coppola.catholic.Res
import com.coppola.catholic.baseline_calendar_today_24
import com.coppola.catholic.calendar_1
import com.coppola.catholic.calendar_10
import com.coppola.catholic.calendar_11
import com.coppola.catholic.calendar_12
import com.coppola.catholic.calendar_13
import com.coppola.catholic.calendar_14
import com.coppola.catholic.calendar_15
import com.coppola.catholic.calendar_16
import com.coppola.catholic.calendar_17
import com.coppola.catholic.calendar_18
import com.coppola.catholic.calendar_19
import com.coppola.catholic.calendar_2
import com.coppola.catholic.calendar_20
import com.coppola.catholic.calendar_21
import com.coppola.catholic.calendar_22
import com.coppola.catholic.calendar_23
import com.coppola.catholic.calendar_24
import com.coppola.catholic.calendar_25
import com.coppola.catholic.calendar_26
import com.coppola.catholic.calendar_27
import com.coppola.catholic.calendar_28
import com.coppola.catholic.calendar_29
import com.coppola.catholic.calendar_3
import com.coppola.catholic.calendar_30
import com.coppola.catholic.calendar_31
import com.coppola.catholic.calendar_4
import com.coppola.catholic.calendar_5
import com.coppola.catholic.calendar_6
import com.coppola.catholic.calendar_7
import com.coppola.catholic.calendar_8
import com.coppola.catholic.calendar_9
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.resources.DrawableResource

class GetTodayIconUseCase {
    operator fun invoke(today: LocalDate): DrawableResource {
        return when (today.dayOfMonth) {
            1 -> Res.drawable.calendar_1
            2 -> Res.drawable.calendar_2
            3 -> Res.drawable.calendar_3
            4 -> Res.drawable.calendar_4
            5 -> Res.drawable.calendar_5
            6 -> Res.drawable.calendar_6
            7 -> Res.drawable.calendar_7
            8 -> Res.drawable.calendar_8
            9 -> Res.drawable.calendar_9
            10 -> Res.drawable.calendar_10
            11 -> Res.drawable.calendar_11
            12 -> Res.drawable.calendar_12
            13 -> Res.drawable.calendar_13
            14 -> Res.drawable.calendar_14
            15 -> Res.drawable.calendar_15
            16 -> Res.drawable.calendar_16
            17 -> Res.drawable.calendar_17
            18 -> Res.drawable.calendar_18
            19 -> Res.drawable.calendar_19
            20 -> Res.drawable.calendar_20
            21 -> Res.drawable.calendar_21
            22 -> Res.drawable.calendar_22
            23 -> Res.drawable.calendar_23
            24 -> Res.drawable.calendar_24
            25 -> Res.drawable.calendar_25
            26 -> Res.drawable.calendar_26
            27 -> Res.drawable.calendar_27
            28 -> Res.drawable.calendar_28
            29 -> Res.drawable.calendar_29
            30 -> Res.drawable.calendar_30
            31 -> Res.drawable.calendar_31
            else -> Res.drawable.baseline_calendar_today_24
        }
    }
}