package ru.barabo.observer.config.fns.inn.task

import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.config.ConfigTask
import ru.barabo.observer.config.fns.inn.InnConfig
import ru.barabo.observer.config.task.AccessibleData
import ru.barabo.observer.config.task.WeekAccess
import ru.barabo.observer.config.task.template.periodic.SinglePerpetual
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.State
import java.time.Duration
import java.time.LocalTime
import java.time.temporal.ChronoUnit

object InnFiller : SinglePerpetual {
    override fun name(): String = "Заполнение ИНН"

    override fun config(): ConfigTask = InnConfig

    override val unit: ChronoUnit = ChronoUnit.SECONDS
    override val countTimes: Long = 20

    override val accessibleData: AccessibleData =  AccessibleData(
        WeekAccess.ALL_DAYS, false,
        LocalTime.MIN, LocalTime.MAX, Duration.ZERO)

    var personList: List<Person?>? = null

    var indexProcessed: Int = -1

    val nalogService = NalogService()

    override fun execute(elem: Elem): State {

        val person = getNextPerson() ?: return super.execute(elem)

        val (code, info) = nalogService.getInnByPerson(person)

        if(code == 200) {
            savePersonInn(person, info)
        } else {
            savePersonError(person, "$code:$info")
        }

        return super.execute(elem)
    }

    private fun getNextPerson(): Person? {

        if(personList == null) {
            personList = initPerson()
        }

        indexProcessed++

        if(personList.isNullOrEmpty() || (personList!!.size <= indexProcessed) ) return null

        return  personList!![indexProcessed]
    }

    private fun initPerson(): List<Person?> {

        return AfinaQuery.selectCursor(PERSON_OPEN_ACCOUNT).map { personFromRow(it) }
            .filter { it != null }
    }

    private fun savePersonInn(person: Person, inn: String) {

        val params = arrayOf<Any?>(inn, person.idClient, person.surname, person.firstName, person.secondName,
            person.birthday, person.docNumber.replace(" ", ""), person.docDate)

        AfinaQuery.execute(SAVE_INN_PERSON, params)
    }

    private fun savePersonError(person: Person, error: String) {

        val params = arrayOf<Any?>(error, person.idClient, person.surname, person.firstName, person.secondName,
            person.birthday, person.docNumber.replace(" ", ""), person.docDate)

        AfinaQuery.execute(SAVE_ERROR_PERSON, params)
    }
}

private const val PERSON_OPEN_ACCOUNT = "{ ? = call od.PTKB_440P.getPhysicPassportWithoutInn }"

private const val SAVE_INN_PERSON = "{ call od.PTKB_440P.saveInn(?,?,?,?,  ?,?,?,?) }"
/*
inn_ in varchar2,
client_ in number,
lastname_ in varchar2,
firstname_ in varchar2,
secondname_ in varchar2,
birthday_ in DATE,
doc_number_ in varchar2,
doc_out_ in DATE)*/

private const val SAVE_ERROR_PERSON = "{ call od.PTKB_440P.saveError(?,?,?,?,  ?,?,?,?) }"

/*
procedure saveErrorInn(
error_ in varchar2,
client_ in number,
lastname_ in varchar2,
firstname_ in varchar2,
secondname_ in varchar2,
birthday_ in DATE,
doc_number_ in varchar2,
doc_out_ in DATE)
*/