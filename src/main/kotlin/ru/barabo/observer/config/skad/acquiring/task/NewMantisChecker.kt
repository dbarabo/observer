package ru.barabo.observer.config.skad.acquiring.task

import org.slf4j.LoggerFactory
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.cbr.sender.SenderInternalMail
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.template.periodic.SinglePerpetual
import ru.barabo.observer.mantis.FbQuery
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import java.sql.Time
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import kotlin.random.Random
import kotlin.random.nextInt

object NewMantisChecker : SinglePerpetual {

    override fun name(): String = "Mantis проверка задач"

    override fun config(): ConfigTask = SenderInternalMail // SenderMail

    override val accessibleData: AccessibleData = AccessibleData(
        workWeek = WeekAccess.WORK_ONLY,
        workTimeFrom = LocalTime.of(10, 45),
        workTimeTo = LocalTime.of(22, 0)
    )

    override val unit: ChronoUnit = ChronoUnit.MINUTES

    override val countTimes: Long = 15

    override fun execute(elem: Elem): State {

        initTodayWorkStaf()

        checkMorning()
        addMorning()

        checkEvening()
        addEvening()

        return super.execute(elem)
    }

    private fun checkEvening() {
        if( LocalTime.now().isAfter(END_MORNING) && morningOk.isNotEmpty()) {
            morningOk.clear()
        }

        if(eveningOk.size == staffCount() ||
            LocalTime.now().isBefore(EVENING_START) ){
                return
        }

        val eveningTimes = FbQuery.select(SELECT_EVENING_TIME)
        for(eveningRow in eveningTimes) {
            val id = eveningRow[0] as Number
            val staff = (eveningRow[1] as Number).toInt()

            if(!workTodayStaffs.contains(staff)) continue

            val time = (eveningRow[2]  as Time).toLocalTime()
            val timeStamp = eveningRow[3] as Timestamp
            val lastName = eveningRow[4] as String
            logger.error("lastName=$lastName time=$time id=$id staff=$staff timeStamp=$timeStamp")

            val eveningTime = checkMorningStart(staff)

            if(time.isBefore(eveningTime)) {
                updateRandomEvening(id, eveningTime)
            }

            if(!eveningOk.contains(staff)) {
                    eveningOk.add(staff)
            }
        }
    }

    private fun checkMorningStart(staff: Int): LocalTime {
        val morning = FbQuery.select(SELECT_MORNING_TIME_STAFF, arrayOf(staff))

        if(morning.isEmpty()) return EVENING

        val timeStart = (morning[0][2]  as Time).toLocalTime()

        return if(timeStart.isAfter(MORNING)) EVENING_DUTY else EVENING
    }

    private fun checkMorning() {

        if(LocalTime.now().isBefore(EVENING_START) && eveningOk.isNotEmpty()) {
            eveningOk.clear()
        }

        if(morningOk.size == staffCount() ||
                LocalTime.now().isAfter(END_MORNING)) {
            return
        }

        val morningTimes = FbQuery.select(SELECT_MORNING_TIME)

        for(morningRow in morningTimes) {
            val id = morningRow[0] as Number
            val staff = (morningRow[1] as Number).toInt()

            if(!workTodayStaffs.contains(staff)) continue

            val time = (morningRow[2]  as Time).toLocalTime()
            val timeStamp = morningRow[3] as Timestamp
            val lastName = morningRow[4] as String

            if(LocalTime.now().isBefore(CHECK_MORNING_TIME)) {
                logger.error("lastName=$lastName time=$time id=$id staff=$staff timeStamp=$timeStamp")
            }

            if(time.isAfter(MORNING)) {
                updateRandomMorning(id, time)
            }

            if(!morningOk.contains(staff)) {
                    morningOk.add(staff)
            }
        }
    }

    private fun isCheckDuty(idReg: Number, time: LocalTime): Boolean {

        val isDuty = ((morningOk.size >= staffCount() - 1) && (time.isAfter(DUTY_MINIMUM)))

        logger.error("isDuty=$isDuty morningOk.size=${morningOk.size} idReg=$idReg")

        if(isDuty) {
            if(time.isAfter(DUTY_START) )  updateRandomDutyMorning(idReg)
        }

        return isDuty
    }

    private fun updateRandomDutyMorning(idReg: Number) {

        val (newTime, newTimeStamp) = generateDutyMorningTime()

        FbQuery.execute(UPDATE_TIME_BY_ID, arrayOf(newTime, newTimeStamp, idReg))
    }

    private fun updateRandomMorning(idReg: Number, time: LocalTime) {

        if(isCheckDuty(idReg, time)) return

        val (newTime, newTimeStamp) = generateMorningTime()

        FbQuery.execute(UPDATE_TIME_BY_ID, arrayOf(newTime, newTimeStamp, idReg))
    }

    private fun addMorning() {
        if(morningOk.size == staffCount() ||
            LocalTime.now().isBefore(CHECK_MORNING_TIME) ||
            LocalTime.now().isAfter(END_MORNING_ADD)    ) {
            return
        }

        val absentList = workTodayStaffs.filter { !morningOk.contains(it) }

        for(absent in absentList) {
            val row = FbQuery.select(SELECT_MORNING_TIME_ANY_TYPE, arrayOf(absent))

            if(row.isEmpty() ) {
                addRandomMorning(absent)
            } else {
                val idReg = row[0][0] as Number
                updateFullRandomMorning(idReg)
            }
        }
    }

    private fun addEvening() {
        if(eveningOk.size == staffCount() ||
            LocalTime.now().isBefore(CHECK_EVENING_ENDTIME)) {
            return
        }

        val absentList = workTodayStaffs.filter { !eveningOk.contains(it) }

        for(absentStaff in absentList) {
            val row = FbQuery.select(SELECT_EVENING_TIME_ANY_TYPE, arrayOf(absentStaff))

            if(row.isEmpty() ) {
                addRandomEvening(absentStaff)
            } else {
                val idReg = row[0][0] as Number
                updateFullRandomEvening(idReg, absentStaff)
            }
        }
    }

    private fun addRandomMorning(idStaff: Int) {
        val (newTime, newTimeStamp) = generateMorningTime()

        val cardId = staffCardMap[idStaff]

        FbQuery.execute(INSERT_MORNING_TIME_BY_ID, arrayOf(newTime, cardId, idStaff, "", newTimeStamp))
    }

    private fun addRandomEvening(staff: Int) {
        val (newTime, newTimeStamp) =
            if(checkMorningStart(staff) == EVENING_DUTY) generateDutyEveningTime() else generateEveningTime()

        val cardId = staffCardMap[staff]

        FbQuery.execute(INSERT_EVENING_TIME_BY_ID, arrayOf(newTime, cardId, staff, "", newTimeStamp))
    }

    private fun updateRandomEvening(id: Number, timeEvening: LocalTime) {
        val (newTime, newTimeStamp) =
            if(timeEvening == EVENING_DUTY) generateDutyEveningTime() else generateEveningTime()

        FbQuery.execute(UPDATE_TIME_BY_ID, arrayOf(newTime, newTimeStamp, id))
    }

    private fun updateFullRandomMorning(idReg: Number) {
        val (newTime, newTimeStamp) = generateMorningTime()

        FbQuery.execute(UPDATE_FULL_MORNING_TIME_BY_ID, arrayOf(newTime, newTimeStamp, idReg))
    }

    private fun updateFullRandomEvening(idReg: Number, staff: Int) {
        val (newTime, newTimeStamp) =
            if(checkMorningStart(staff) == EVENING_DUTY) generateDutyEveningTime() else generateEveningTime()

        FbQuery.execute(UPDATE_FULL_EVENING_TIME_BY_ID, arrayOf(newTime, newTimeStamp, idReg))
    }

    private fun generateDutyMorningTime(): Pair<Time, Timestamp> = generateTimeByRange(10, 29..59)

    private fun generateEveningTime(): Pair<Time, Timestamp> = generateTimeByRange(17, 31..59)

    private fun generateDutyEveningTime(): Pair<Time, Timestamp> = generateTimeByRange(20, 0..19)

    private fun generateMorningTime(): Pair<Time, Timestamp> = generateTimeByRange(8, 1..29)

    private fun generateTimeByRange(hour: Int, rangeMinutes: IntRange): Pair<Time, Timestamp> {
        val minutes = Random.nextInt(rangeMinutes)

        val sec = LocalTime.now().second

        val timeLocal = LocalTime.of(hour, minutes, sec)

        val time = Time.valueOf(timeLocal)

        val localDateTime = LocalDateTime.now().withHour(timeLocal.hour).withMinute(timeLocal.minute).withSecond(0).withNano(0)

        return Pair(time, Timestamp.valueOf(localDateTime))
    }

    private fun initTodayWorkStaf() {

        if(workInit == LocalDate.now()) return

        workInit = LocalDate.now()

        workTodayStaffs.clear()

        val users = AfinaQuery.select(SELECT_VACATIONS)
        if(users.isEmpty()) {
            workTodayStaffs.addAll(staffCardMap.keys)
        } else {
            val usersOnly = users.map { it[0] as String }

            val works = staffUserMap.filter { !usersOnly.contains(it.key) }

            workTodayStaffs.addAll(works.values)
        }
    }

    private val CHECK_MORNING_TIME = LocalTime.of(12, 0)

    private val MORNING = LocalTime.of(8, 29, 59)

    private val DUTY_MINIMUM = LocalTime.of(9, 15)

    private val DUTY_START = LocalTime.of(11, 0)

    private val END_MORNING = LocalTime.of(17, 29, 59)

    private val END_MORNING_ADD = LocalTime.of(19, 29, 59)

    private val EVENING = LocalTime.of(17, 30, 59)

    private val EVENING_START =  LocalTime.of(20, 10)

    private val EVENING_DUTY = LocalTime.of(20, 1 )

    private val CHECK_EVENING_ENDTIME = LocalTime.of(21, 0)

    private val logger = LoggerFactory.getLogger(NewMantisChecker::class.java)
}

private fun staffCount(): Int = workTodayStaffs.size

private var workInit: LocalDate = LocalDate.of(2020, 1, 1)

private var workTodayStaffs: MutableList<Int> = ArrayList()

private var eveningOk: MutableList<Int> = ArrayList()

private var morningOk: MutableList<Int> = ArrayList()

private val staffCardMap = mapOf(
    38626 to 4242098,
    39638 to 3921548,
    33265 to 4231484,
    33266 to 15813571,
    45848 to 15813709
)

private val staffUserMap = mapOf(
     "SHERBO" to 38626,
     "NEGMA" to 39638,
     "BARDV" to 33265,
     "NAZVE" to 33266,
     "BRYAV" to 45848
)

private const val MY_LIST = "(38626, 39638, 33265, 33266, 45848)"
/// 29562 palla

private const val SELECT_MORNING_TIME = """
  select e.id_reg, e.staff_id, e.TIME_EV, e.last_timestamp, sf.last_name
  from reg_events e
 join staff sf on sf.id_staff = e.staff_id
where e.staff_id in $MY_LIST
  and e.date_ev = current_date
  and e.TIME_EV < CAST('12:00' AS TIME)
  and e.TIME_EV > CAST('5:00' AS TIME)
  and e.inner_number_ev = 17
  order by e.TIME_EV
"""

private const val SELECT_MORNING_TIME_STAFF = """
  select e.id_reg, e.staff_id, e.TIME_EV, e.last_timestamp, sf.last_name
  from reg_events e
 join staff sf on sf.id_staff = e.staff_id
where e.staff_id in $MY_LIST
  and e.date_ev = current_date
  and e.TIME_EV < CAST('12:00' AS TIME)
  and e.TIME_EV > CAST('5:00' AS TIME)
  and e.inner_number_ev = 17
  and e.staff_id = ?
  order by e.TIME_EV    
"""

private const val SELECT_EVENING_TIME = """
  select e.id_reg, e.staff_id, e.TIME_EV, e.last_timestamp, sf.last_name
  from reg_events e
 join staff sf on sf.id_staff = e.staff_id
where e.staff_id in $MY_LIST
  and e.date_ev = current_date
  and e.TIME_EV > CAST('14:00' AS TIME)
  and e.inner_number_ev = 17
  order by e.TIME_EV desc
"""

private const val SELECT_MORNING_TIME_ANY_TYPE = """
  select e.id_reg, e.staff_id, e.TIME_EV, e.last_timestamp, sf.last_name
  from reg_events e
 join staff sf on sf.id_staff = e.staff_id
where e.staff_id = ?
  and e.date_ev = current_date
  and e.TIME_EV < CAST('14:00' AS TIME)
  and e.TIME_EV > CAST('5:00' AS TIME)
"""

private const val SELECT_EVENING_TIME_ANY_TYPE = """
  select e.id_reg, e.staff_id, e.TIME_EV, e.last_timestamp, sf.last_name
  from reg_events e
 join staff sf on sf.id_staff = e.staff_id
where e.staff_id = ?
  and e.date_ev = current_date
  and e.TIME_EV > CAST('14:00' AS TIME)
"""

private const val UPDATE_TIME_BY_ID = "update reg_events set TIME_EV = ?, last_timestamp = ? where id_reg = ?"

private const val UPDATE_FULL_EVENING_TIME_BY_ID = """
    update reg_events set TIME_EV = ?, last_timestamp = ?, INNER_NUMBER_EV = 17, 
    configs_tree_id_controller = 32914, 
    configs_tree_id_resource = 33034,
    areas_id = 1 where id_reg = ?"""

private const val UPDATE_FULL_MORNING_TIME_BY_ID = """
    update reg_events set TIME_EV = ?, last_timestamp = ?, INNER_NUMBER_EV = 17, 
    configs_tree_id_controller = 33923, 
    configs_tree_id_resource = 34039,
    areas_id = 25743 where id_reg = ?"""

private const val INSERT_MORNING_TIME_BY_ID = """
    insert into reg_events 
    (INNER_NUMBER_EV, DATE_EV, TIME_EV, IDENTIFIER, CONFIGS_TREE_ID_CONTROLLER, CONFIGS_TREE_ID_RESOURCE, TYPE_PASS,
    CATEGORY_EV, SUBCATEGORY_EV, AREAS_ID, STAFF_ID, USER_ID, TYPE_IDENTIFIER, video_mark, LAST_TIMESTAMP, IDENTIFIER_OWNER_TYPE, SUBDIV_ID) values 
    (17, current_date, ?, ?, 33923, 34039, 1, 
    0, 0, 25743, ?, null, 0, ?, ?, 0, 41095)
"""

private const val INSERT_EVENING_TIME_BY_ID = """
    insert into reg_events 
    (INNER_NUMBER_EV, DATE_EV, TIME_EV, IDENTIFIER, CONFIGS_TREE_ID_CONTROLLER, CONFIGS_TREE_ID_RESOURCE, TYPE_PASS,
    CATEGORY_EV, SUBCATEGORY_EV, AREAS_ID, STAFF_ID, USER_ID, TYPE_IDENTIFIER, video_mark, LAST_TIMESTAMP, IDENTIFIER_OWNER_TYPE, SUBDIV_ID) values 
    (17, current_date, ?, ?, 32914, 33034, 1, 
    0, 0, 1, ?, null, 0, ?, ?, 0, 41095)
"""

private const val SELECT_VACATIONS = """
select wa.userid
from workplaceaccess wa
where wa.workplace = 1012279020
and wa.userid in ('BARDV', 'NEGMA', 'SHERBO', 'NAZVE', 'BRYAV')
and wa.validtodate >= trunc(sysdate)
and wa.validfromdate <= trunc(sysdate) 
"""
