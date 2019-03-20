import org.slf4j.LoggerFactory
import ru.barabo.cmd.Cmd
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.xls.ExcelSimple
import java.io.File

private const val SEL_40817 = """
     select p.doc
      ,doctreelabel(p.doc)      N1
      ,to_char(p.begindate)              N2
      ,to_char(trunc(p.planenddate))    N3
      ,to_char(trunc(p.enddate))         N4
      ,accrestin(a1.doc, to_date('01/03/2017', 'dd/mm/yyyy'), 1, 0)
R5
      ,accrestin(a1.doc, to_date('01/03/2019', 'dd/mm/yyyy'), 1, 0)
R6
      ,currencyiso(a1.currency) N7
      ,clientdesc(a1.client)     N8
      ,to_char(trunc(bb.operdate, 'DD')) N9
      ,substr(to_char(bb.operdate, 'dd.mm.yyyy hh24:mi:ss'), 12) N10
      ,accountcode(bb.debaccount)  N11
      ,accountcode(bb.credaccount) N12
      ,bb.amountdeb                R13
      ,bb.amountrurdeb             R14
      ,'D'                         N15
      ,dt.description              N16
      ,productlabel(p.product)     N17
      ,dp.label                    N18
  from doctree    dt
      ,bbook      bb
      ,account    a1
      ,doctree    dt2
      ,pact       p
      ,department dp
 where bb.operdate >= to_date('01/03/2017', 'dd/mm/yyyy')
   and bb.operdate < to_date('01/03/2019', 'dd/mm/yyyy')
   and bb.parent = dt.classified
   and dt.docstate = 1000000035
   and bb.debaccount = a1.doc
   and dt2.operobj = dp.classified
   and (
        SUBSTR(a1.code,1,5) IN ('40817','40820')
       )
   and a1.doc     = dt2.classified
   and dt2.parent = p.doc(+)

union all

select p.doc
      ,doctreelabel(p.doc)      N1
      ,to_char(p.begindate)              N2
      ,to_char(trunc(p.planenddate))     N3
      ,to_char(trunc(p.enddate))         N4
      ,accrestin(a1.doc, to_date('01/03/2017', 'dd/mm/yyyy'), 1, 0)
R5
      ,accrestin(a1.doc, to_date('01/03/2019', 'dd/mm/yyyy'), 1, 0)
R6
      ,currencyiso(a1.currency) N7
      ,clientdesc(p.client)     N8
      ,to_char(trunc(bb.operdate, 'DD')) N9
      ,substr(to_char(bb.operdate, 'dd.mm.yyyy hh24:mi:ss'), 12) N10
      ,accountcode(bb.debaccount)  N11
      ,accountcode(bb.credaccount) N12
      ,bb.amountcred               N13
      ,bb.amountrurcred            N14
      ,'K'                         N15
      ,dt.description              N16
      ,productlabel(p.product)     N17
      ,dp.label                    N18
  from doctree    dt
      ,bbook      bb
      ,account    a1
      ,doctree    dt2
      ,pact       p
      ,department dp
 where bb.operdate >= to_date('01/03/2017', 'dd/mm/yyyy')
   and bb.operdate < to_date('01/03/2019', 'dd/mm/yyyy')
   and bb.parent = dt.classified
   and dt.docstate = 1000000035
   and bb.credaccount = a1.doc
   and dt2.operobj = dp.classified
   and (SUBSTR(a1.code,1,5) IN ('40817','40820')
       )
   and a1.doc = dt2.classified
   and dt2.parent = p.doc(+)

 order by 16
         ,10
         ,15
         ,6
"""

private val logger = LoggerFactory.getLogger(ExtractXls::class.java)

object ExtractXls {


    private val TEMPLATE_EXTARCT = File("${Cmd.LIB_FOLDER}/extract40817.xls")

    fun extract() {
        val file = File("c:/temp/extract_40817.xls")

        val excelProcess = ExcelSimple(file, TEMPLATE_EXTARCT)

        AfinaQuery.selectCursor("{? = call OD.PTKB_PRECEPT.getAllExt }").createTransactData(excelProcess)

        excelProcess.save()
    }
}

internal fun List<Array<Any?>>.createTransactData(excelSimple: ExcelSimple) {

    val transactVar = createTransactVar()

    for (transact in this) {

        logger.error("${transact[0]}")

        transactVar.putBodyRow(transact)

        excelSimple.createBodyRow(transactVar)
    }
}

private fun createTransactVar(): MutableMap<String, Any> = mutableMapOf(
        "ДОГОВОР_N" to "",
        "ДАТА_ДОГОВОРА" to "",
        "ДАТА_ЗАКРЫТИЯ" to "",
        "ДАТА_ФАКТ_ЗАКРЫТИЯ" to "",
        "ОСТАТОК_НА_01_03_2017" to 0.0,
        "ОСТАТОК_НА_01_03_2019" to 0.0,
        "ВАЛЮТА" to "",
        "ФИО" to "",
        "ДАТА_ОПЕРАЦИИ" to "",
        "ВРЕМЯ_ОПЕРАЦИИ" to "",
        "ДЕБЕТ" to "",
        "КРЕДИТ" to "",
        "СУММА_ОПЕРАЦИИ" to 0.0,
        "СУММА_ОПЕРАЦИИ_В_НАЦ_ЭКВИВ" to 0.0,
        "Д_К" to "",
        "НАЗНАЧЕНИЕ_ПЛАТЕЖА" to "",
        "ПРОДУКТ" to "",
        "ПОДРАЗДЕЛЕНИЕ_БАНКА" to ""
        )


private fun MutableMap<String, Any>.putBodyRow(transact: Array<Any?>) {

    this["ДОГОВОР_N"] = transact[1]?.let { it as String}?:""

    this["ДАТА_ДОГОВОРА"] = transact[2]?.let { it as String}?:""

    this["ДАТА_ЗАКРЫТИЯ"] = transact[3]?.let { it as String}?:""

    this["ДАТА_ФАКТ_ЗАКРЫТИЯ"] = transact[4]?.let { it as String}?:""

    this["ОСТАТОК_НА_01_03_2017"] = transact[5]?.let { (it as Number).toDouble()} ?: 0.0

    this["ОСТАТОК_НА_01_03_2019"] = transact[6]?.let { (it as Number).toDouble()} ?: 0.0

    this["ВАЛЮТА"] = transact[7]?.let { it as String}?:""

    this["ФИО"] = transact[8]?.let { it as String}?:""

    this["ДАТА_ОПЕРАЦИИ"] = transact[9]?.let { it as String}?:""

    this["ВРЕМЯ_ОПЕРАЦИИ"] = transact[10]?.let { it as String}?:""

    this["ДЕБЕТ"] = transact[11]?.let { it as String}?:""

    this["КРЕДИТ"] = transact[12]?.let { it as String}?:""

    this["СУММА_ОПЕРАЦИИ"] = transact[13]?.let { (it as Number).toDouble()} ?: 0.0

    this["СУММА_ОПЕРАЦИИ_В_НАЦ_ЭКВИВ"] = transact[14]?.let { (it as Number).toDouble()} ?: 0.0

    this["Д_К"] = transact[15]?.let { it as String}?:""

    this["НАЗНАЧЕНИЕ_ПЛАТЕЖА"] = transact[16]?.let { it as String}?:""

    this["ПРОДУКТ"] = transact[17]?.let { it as String}?:""

    this["ПОДРАЗДЕЛЕНИЕ_БАНКА"] = transact[18]?.let { it as String}?:""
}