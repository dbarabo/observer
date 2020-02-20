import oracle.jdbc.OracleTypes
import org.jsoup.Jsoup
import org.junit.Before
import org.junit.Test
import org.slf4j.LoggerFactory
import ru.barabo.cmd.Cmd
import ru.barabo.exchange.VisaCalculator
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.afina.clobToString
import ru.barabo.observer.config.barabo.crypto.task.LoadBik
import ru.barabo.observer.config.barabo.crypto.task.LoadRateThb
import ru.barabo.observer.config.barabo.p440.task.*
import ru.barabo.observer.config.barabo.plastic.release.PlasticReleaseConfig
import ru.barabo.observer.config.barabo.plastic.release.add.OutApplicationData
import ru.barabo.observer.config.barabo.plastic.release.task.*
import ru.barabo.observer.config.barabo.plastic.release.task.OutRegisterAquiringMonth.processTerminals
import ru.barabo.observer.config.barabo.plastic.release.task.autoupdate.isRemoteNetDisk
import ru.barabo.observer.config.barabo.plastic.release.task.autoupdate.remoteFilePath
import ru.barabo.observer.config.barabo.plastic.turn.checker.CtlChecker
import ru.barabo.observer.config.barabo.plastic.turn.checker.MtlChecker
import ru.barabo.observer.config.barabo.plastic.turn.task.*
import ru.barabo.observer.config.cbr.ibank.task.CheckAccountFaktura
import ru.barabo.observer.config.cbr.ibank.task.LoanInfoSaver
import ru.barabo.observer.config.cbr.ibank.task.UploadExtract
import ru.barabo.observer.config.cbr.other.task.*
import ru.barabo.observer.config.cbr.other.task.form101.BalanceChecker101f
import ru.barabo.observer.config.cbr.other.task.form101.CheckerAbsentBalance
import ru.barabo.observer.config.cbr.other.task.form101.CheckerRedSaldo
import ru.barabo.observer.config.cbr.other.task.nbki.clob2string
import ru.barabo.observer.config.cbr.ptkpsd.task.CheckerAllBalance
import ru.barabo.observer.config.cbr.ptkpsd.task.ClearPrimFromArchiveDay
import ru.barabo.observer.config.cbr.ptkpsd.task.Load101FormXml
import ru.barabo.observer.config.cbr.ptkpsd.task.p550.EsProcess
import ru.barabo.observer.config.cbr.ticket.task.GetProcess550pFiles
import ru.barabo.observer.config.cbr.ticket.task.XmlLoaderCbrTicket311p
import ru.barabo.observer.config.jzdo.upay.task.LoadMtlUPay
import ru.barabo.observer.config.skad.plastic.task.LoadVisaRate
import ru.barabo.observer.config.task.Executor
import ru.barabo.observer.config.task.info.InfoHtmlData
import ru.barabo.observer.config.task.p311.ticket.TicketCbr
import ru.barabo.observer.config.test.TestConfig
import ru.barabo.observer.report.ReportXlsLockCards
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.TaskMapper
import ru.barabo.observer.store.derby.StoreSimple
import ru.barabo.xls.*
import java.io.File
import java.nio.charset.Charset
import java.sql.Clob
import java.sql.Timestamp
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.regex.Pattern
import kotlin.collections.ArrayList
import kotlin.math.roundToLong


class LoaderTest {

    private val logger = LoggerFactory.getLogger(LoaderTest::class.java)

    @Before
    fun initTestBase() {
        TaskMapper.init("TEST", "TEST")

        com.sun.javafx.application.PlatformImpl.startup {}
    }

    private fun separ() = ";"

    //@Test
    fun testCorrectPrim() {

        val elem = Elem(idElem = 1206623916L, task = CorrectPrim)
               // Elem(idElem = 1205659121L, task = CorrectPrim)
                //Elem(idElem = 1205641626L, task = CorrectPrim)

        CorrectPrim.execute(elem)
    }


    // @Test
    fun excelSqlTestAcq() {

        val TEMPLATE_REGISTER_RANGE = File("${Cmd.LIB_FOLDER}/acquir_test.xls")

        val testFile = File("${Cmd.LIB_FOLDER}/test.xls")

        val vars = ArrayList<Var>()

        vars += Var("TERMINALID", VarResult(VarType.VARCHAR, "J244997") )//back-"J244997" //one-"J295331" //some-"J267667" //0-"J193574"

        vars += Var("MTL", VarResult(VarType.INT, 1206344191) ) // 1206495410 //back-1206344191

        val excelSql = ExcelSql(testFile, TEMPLATE_REGISTER_RANGE)

        excelSql.initRowData(vars)

        excelSql.processData()
    }

   // @Test
    fun pareserTest() {

       /* val text ="""
FIND = '%БАРАБОШКИН%';            
CLOSED = od.PTKB_PLASTIC_AUTO.getCardsClosed([FIND]);        
CARD = [CLOSED.CARDNUM];  
PRODUCT = [CLOSED.PRODUCT_NAME]"""
*/

        val text = """
info = '';
id = 1206343529;
calc = select round(4.5, 0)*333 RES, 1 INTTEST, 2.2 DEC_TEST from dual;
od.PTKB_PLASTIC_TURN.getInfoProcessedCtl([out info], [id]);
res = [calc.res];
res2 = [calc.INTTEST];
res3 = [calc.DEC_TEST];
[info];
"""
        val vars = ArrayList<Var>()

        val parser = Parser(AfinaQuery)

        val expression = parser.parseExpression(text, vars)

        expression.forEach { logger.error("$it") }
        vars.forEach { logger.error("$it") }

        val result = parser.execExpression(expression)
        logger.error("result=$result")
        vars.forEach { logger.error("$it") }
    }

    //@Test
    fun selectWithMetaData() {
        val meta = //AfinaQuery.selectWithMetaData("select count(*) from dual where 1=0")
                AfinaQuery.selectCursorWithMetaData("{ ? = call OD.PTKB_PLASTIC_AUTO.getCardsByMode(?, ?, ?) }",
                        arrayOf(1, "", "") )

        meta.columns.forEach { logger.error(it) }

        meta.types.forEach { logger.error("type=$it") }
    }

    //@Test
    fun fileMax() {


        val search = Pattern.compile("ARH550P_0021_0000_${GetProcess550pFiles.todayBlind()}_...\\.arj",
                Pattern.CASE_INSENSITIVE or Pattern.UNICODE_CASE)

        val max = (File("X:/550-П/Out/2019/12/16").listFiles { f ->
            !f.isDirectory && search.matcher(f.name).find()}
                ?.map { Integer.parseInt(it.name.substring(it.name.indexOf(".") - 3, it.name.indexOf(".")))}?.max()?:0)+ 1

        logger.error("max=$max")
    }

    //@Test
    fun testOutSalaryResponseFile() {

        val data = AfinaQuery.selectCursor(OutSalaryResponseFile.select)[0]

        val elem = Elem(data[0] as Number?,
                if(data.size < 2)null else data[1] as String?,
                OutSalaryResponseFile, OutSalaryResponseFile.accessibleData.executeWait)

        OutSalaryResponseFile.execute(elem)
    }

    //@Test
    fun nbki60Test() {
        //NbkiAllReportsSend.fillData()

        // val fileName = NbkiAllReportsSend.createGuarantorTextFile() ?: throw Exception("!!!!")
        //logger.error("fileName=$fileName")

        //val xlsFile = NbkiAllReportsSend.createNbkiXlsFile(fileName)
    }

   // @Test
    fun extractBtrt30() {

        val file = File("C:/IIA_20190821_090000_0226")

        val query = "{ call od.PTKB_PLASTIC_AUTO.createBTRT30File(?, ?, ?)} "

        val firstParamId = 1192328637L

        val uniqueSession = AfinaQuery.uniqueSession()

        val clob = AfinaQuery.execute(query, arrayOf(firstParamId, file.name),
                uniqueSession, intArrayOf(OracleTypes.CLOB))!![0] as Clob

        file.writeText(clob.clobToString(), charset = Charset.forName("cp1251"))
    }

    //@Test
    fun testClearPrimFromArchiveDay() {

        val elem = Elem(idElem = 75363011L, task = ClearPrimFromArchiveDay)

        ClearPrimFromArchiveDay.execute(elem)
    }

    //@Test
    fun testCheckAccountFaktura() {

        val elem = Elem(idElem = 0L, task = CheckAccountFaktura)

        CheckAccountFaktura.execute(elem)
    }

    //@Test
    fun testUploadExtract() {

        val elem = Elem(idElem = 0L, task = UploadExtract)

        UploadExtract.execute(elem)
    }

   // @Test
    fun testMails() {
        val SELECT_EMAIL_LAID_OFF = "{ ? = call od.PTKB_PLASTIC_REPORT.getMailLaidOffEmployees }"

        val mails = AfinaQuery.selectCursor(SELECT_EMAIL_LAID_OFF).joinToString {  "'${it[0]}'" }

        logger.error(mails)
    }


    //@Test
    fun testXmlLoaderCbrTicket311p() {

        val file = File("C:/Картстандарт/test/UV")

        file.listFiles().filter { !it.isDirectory }.forEach {
            XmlLoaderCbrTicket311p.processFile(it)
        }
    }

    private fun TicketCbr.asString(): String = "$archiveName : $resultArchive ${this.nameRecords?.fileRecords?.joinToString(";") { it.fileName  }}"



    //@Test
    fun testReportXlsLockCards() {

        ReportXlsLockCards.createReport()
    }


    //@Test
    fun testLoadAcq() {
        val elem = Elem(File("C:/КартСтандарт/test/AFP_ACQ20190315_0226.0023"), LoadAcq, Duration.ZERO)

        LoadAcq.execute(elem)
    }

    //@Test
    fun test440pExec() {

        val elem = Elem(idElem = 1187605445L)// женя 1187580606L) // RPO=1186808886L

        Process440p.execute(elem)
    }

    //@Test
    fun loadZsn() {
        //val elem = Elem(File("D:/440-П/test/ZSN10507717_773120190311_500500.xml"), ZsnLoader, Duration.ZERO)

        //val elem = Elem(File("D:/440-П/test/PNO10507717_253720180131_000068.xml"), PnoLoader, Duration.ZERO)

        //val elem = Elem(File("C:/Картстандарт/test/ROO10507717_254320190226_000000.xml"), RooLoader, Duration.ZERO)

        // женя
        //val elem = Elem(File("C:/Картстандарт/test/RPO10507717_251020190226_000000.xml"), RpoLoader, Duration.ZERO)

        val elem = Elem(File("C:/Картстандарт/test/рита/RPO10507717_251020190226_000000.xml"), RpoLoader, Duration.ZERO)

        elem.task?.execute(elem)
    }

        //@Test
    fun processOci() {

        val file = File("H:/in/2019/03/13/OCI_20190313_123101_0226")

        GetOciData.processFile(file)
    }

    //@Test
    fun checkBalance() {

        BalanceChecker101f.allCheckerForm()

        /*arrayOf(1181259487,1181355012,1181379408,1181399004,1181427774,1181468982,1181236600,1181142910,1181178506).forEach {
            BalanceChecker101f.check101form(it, Timestamp(Date().time))
        }*/
    }

    //@Test
    fun testSetLimitBlock() {
       // val EXEC_BLOCK = "{ call od.PTKB_440P.runSetBlockByAccountList }"

       // AfinaQuery.execute(EXEC_BLOCK)
    }

    //@Test
    fun loadRpo() {
        //val elem = Elem(File("D:/440-П/test/PNO10507717_253720180131_000068.xml"), PnoLoader, Duration.ZERO)

        val elem = Elem(File("C:/Картстандарт/test/ROO10507717_254320190226_000000.xml"), RooLoader, Duration.ZERO)

        //val elem = Elem(File("C:/440-П/test/ZSV10507717_250820180131_000024.xml"), ZsvLoader, Duration.ZERO)

        //val elem = Elem(File("C:/Картстандарт/test/RPO10507717_251020190226_000000.xml"), RpoLoader, Duration.ZERO)

        elem.task?.execute(elem)
    }




    //@Test
    fun outRegisterAquiringMonth() {
        //val elem = Elem(task = OutRegisterAquiringMonth)

        //elem.task?.execute(elem)

        val termArr: Array<Any?> = arrayOf("J224501", "01/01/2019", "31/01/2019", "Leman", "")

        val list = listOf(termArr)

        list.processTerminals()

        /*

        this["TerminalId"] = terminal[0]?.let { it as String}?:""

        this["dtStart"] = terminal[1]?.let { it as String}?:""

        this["dtEnd"] = terminal[2]?.let { it as String}?:""

        this["CompanyName"] = terminal[3]?.let { it as String}?:""

        this["adressCompany"] = terminal[4]?.let { it as String}?:""

        OutRegisterAquiringMonth.createRegisters()*/
    }

    //@Test
    fun loadCtlMtl() {
        val elem = Elem(File("C:/КартСтандарт/MTL20200109_0226.0001"), LoadCtlMtl, Duration.ZERO)

        elem.task?.execute(elem)
    }

    //@Test
    fun loadMtlUPay() {
        val elem = Elem(File("C:/КартСтандарт/ZKMTL_20190909_0999.0001"), LoadMtlUPay, Duration.ZERO)

        elem.task?.execute(elem)
    }


    //@Test
    fun execCtl() {
        val elem = Elem(idElem = 1204883376, task = ExecuteCtlMtl) //1186566414

        elem.task?.execute(elem)

        logger.error("Ok")
    }

    //@Test
    fun testExecAfpSchema() {

        val elem = Elem(idElem = 1204552531, task = ExecuteAfp)

        elem.task?.execute(elem)
    }



    //@Test
    fun testLoadAfp() {
        val elem = Elem(File("C:/КартСтандарт/test/AFP20190218_0226.0024"), LoadAfp, Duration.ZERO)

        LoadAfp.execute(elem)
    }

    //@Test
    fun testMtlChecker() {
        MtlChecker.check()
    }

    //@Test
    fun testCtlChecker() {
        CtlChecker.check()
    }

    //@Test
    fun testCtlNewExec() {
        val EXEC_CTL_MTL = "{ call od.PTKB_PLASTIC_TURN.testProcCtl(?) }"

        AfinaQuery.execute(EXEC_CTL_MTL, arrayOf(1186187440L))
    }

    //@Test
    fun testZsoLoad() {

        val file = File("C:/Картстандарт/test/ZSO10507717_250820190212_516920.xml")

        ZsoLoader.processFile(file)
    }

    //@Test
    fun testZsvLoad() {

        val file = File("C:/Картстандарт/test/ZSV10507717_631020190211_000025.xml")

        ZsvLoader.processFile(file)
    }

    //@Test
    fun testIsRemoteNetDisk() {
        val remote = "I:/Modules/java/new/plastic.jar"

        logger.error(remote.isRemoteNetDisk().toString())
    }

    //@Test
    fun checkSaldoAbsent() {
        CheckerRedSaldo.isCheckSaldo(Timestamp(Date().time))

        CheckerAbsentBalance.checkAbsent()
    }

    //@Test
    fun checkerAllBalance() {
        CheckerAllBalance.execute(Elem())
    }

    //@Test
    fun quoteCsvTestRegex() {
        val text =
                "TR;58;116030547764;116031459732;2937320473;;CMTP502;TPTP205;22620810600020720005;4755450117030235;20190110003154;20190110033154;D;810;810;810;2015600;2015600;2015600;200000000005528;\"RU        SU25CQTG;3   \";;643;;Pet;18503;20001999;320473;20190111;20190111;9985;4511;74377739010010004230355;901000423035;E;000150V05000;;Petrozavodsk;;0000020156000000000081020190110033154000002015600810"

        val regex = "${separ()}(?=([^\"]*\"[^\"]*\")*[^\"]*$)"

        val splitted = text.split(regex.toRegex())

        splitted.forEach {
            logger.error(it)
        }
        logger.error(null)

        //val patternCsvQuote = Pattern.compile(regex)

       // val matcher = patternCsvQuote!!.matcher(text)

//        val fields = ArrayList<String>()
//
//        while (matcher.find()) {
//            var group = matcher.group(1)?:null
//
//            if (group?.isNotEmpty() == true && '"' == group[0]) {
//                group = matcher.group(2)
//            }
//
//            group = group?.replace("\n".toRegex(), "\"")
//
//            logger.error(group?:"")
//        }
    }


    //@Test
    fun loanInfoSaver() {
        LoanInfoSaver.findAll()

        StoreSimple.getItems {it?.config() == TestConfig }.forEach {

            logger.error("elem=$it")

            (it.task as? Executor)?.executeElem(it)
        }
    }


    //@Test
    fun uriBaseTest() {

        AutoUpdatePlasticJarCritical.findAbstract()

        StoreSimple.getItems {it?.config() == PlasticReleaseConfig}
                .forEach {
                    val remoteFilePath = it.remoteFilePath()
                    logger.error("remoteFilePath=${remoteFilePath.absoluteFile}")

                    logger.error("isExists=${remoteFilePath.exists()}")
                }
    }

    //@Test
    fun testHmacIbi() {

        val execHmac = "{ call od.PTKB_PLASTIC_TURNOUT.testHmac(?, ?) }"

        val data = AfinaQuery.execute(query = execHmac,
                outParamTypes = intArrayOf(OracleTypes.CLOB, OracleTypes.VARCHAR))

        OutIbi.saveFile(data!![1] as String, (data[0] as Clob).clob2string())
    }

    //@Test
    fun loadRestAccount() {

        val elem = Elem(File("C:/КартСтандарт/test/ACC_20180119_062624_0226"), LoadRestAccount, Duration.ZERO)

        LoadRestAccount.execute(elem)
    }

   // @Test
    fun loadObi() {

        val elem = Elem(File("C:/КартСтандарт/ZKOBI_20200116_080024_0133_FEE"), LoadObi, Duration.ZERO)

        LoadObi.execute(elem)
    }

    //@Test
    fun loadObr() {
        val elem = Elem(File("C:/КартСтандарт/test/OBR_20171205_161447_0226"), LoadObr, Duration.ZERO)

        elem.task?.execute(elem)
    }

    //@Test
    fun loadLoadRateTT057() {

        val elem3 = Elem(File("C:/КартСтандарт/TT057T0.20200114124641.001"), LoadRateTT057, Duration.ZERO)
        elem3.task?.execute(elem3)

        val elem2 = Elem(File("C:/КартСтандарт/TT057T0.20200113123804.001"), LoadRateTT057, Duration.ZERO)
        elem2.task?.execute(elem2)

        val elem = Elem(File("C:/КартСтандарт/TT057T0.20200113123802.001"), LoadRateTT057, Duration.ZERO)
        elem.task?.execute(elem)
    }


    //@Test
    fun loadAfp() {
        val elem = Elem(File("C:/КартСтандарт/test/AFP20180924_0226.0013"), LoadAfp, Duration.ZERO)

        elem.task?.execute(elem)
    }

    //@Test
    fun outIbi() {
        val elem = Elem(task = OutIbi)

        elem.task?.execute(elem)
    }


    //@Test
    fun execObi() {
        val elem = Elem(idElem = 1172320580, task = ExecuteObi)

        elem.task?.execute(elem)
    }

    //@Test
    fun loadZsv() {
        //val elem = Elem(File("C:/440-П/test/ZSV10507717_132720180130_000152.xml"), ZsvLoader, Duration.ZERO)

        val elem = Elem(File("C:/440-П/test/ZSV10507717_132720180130_000164.xml"), ZsvLoader, Duration.ZERO)

        elem.task?.execute(elem)
    }



    //@Test
    fun exec440p() {

        //val elem = Elem(idElem = 1172496436, task = Process440p)

        //val elem = Elem(idElem = 1172496444, task = Process440p)

        val elem = Elem(idElem = 1172496446, task = Process440p)

        elem.task?.execute(elem)
    }

    //@Test
    fun outBos() {
        val elem = Elem(idElem = 1172496442, task = RestSaver)

        elem.task?.execute(elem)
    }

    //@Test
    fun outBvsExtract() {
        //val elem = Elem(idElem = 1172496449, task = ExtractMainSaver)

        val elem = Elem(idElem = 1172496453, task = ExtractMainSaver)

        elem.task?.execute(elem)
    }


    //@Test
    fun oiaProcess() {
        //val elem = Elem(idElem = 1172496453, task = GetOiaConfirm)

        val elem = Elem(File("H:/КартСтандарт/in/OIA_20180202_054637_0226"), GetOiaConfirm, Duration.ZERO)

        elem.task?.execute(elem)
    }

    //@Test
    fun ociProcess() {
        //val elem = Elem(idElem = 1172496453, task = GetOiaConfirm)

        val elem = Elem(File("H:/КартСтандарт/in/OIA_20180202_054637_0226"), GetOiaConfirm, Duration.ZERO)

        elem.task?.execute(elem)
    }

    //@Test
    fun checkOpenArchiveDay() {

        val elem = Elem(idElem = 57558470, name = "16.02.2018", task = CheckOpenArchiveDay)

        elem.task?.execute(elem)
    }


    //@Test
    fun execOpenArchiveDay() {

        val elem = Elem(idElem = 0, task = ExecOpenArchiveDay)

        elem.task?.execute(elem)
    }

    //@Test
    fun execLoadBik() {

        val elem = Elem(idElem = 0, task = LoadBik)

        elem.task?.execute(elem)
    }

    //@Test
    fun saveOutOutApplicationData() {
        OutApplicationData.execute(idPacket = 1182748836)
    }

    //@Test
    fun saveOutSmsData() {

        val elem = Elem(idElem = 1182782271, task = OutSmsData)

        elem.task?.execute(elem)
    }

    //@Test
    fun loadForm101() {

        //val f101Xml = XmlLoaderF101<F101Xml>().load(File("C:/440-П/test/Ф101_40507717.xml"))

        //logger.error("f101Xml=$f101Xml")

        val elem = Elem(File("C:/440-П/test/Ф101_40507717.xml"), Load101FormXml, Duration.ZERO)

        elem.task?.execute(elem)
    }

    //@Test
    fun sentHtmlReport() {
        InfoHtmlData.sendInfo(LocalDate.now(), StoreSimple.getRootElem())
    }

    //@Test
    fun executeOverdraftJuric() {
        val elem = Elem(task = ExecuteOverdraftJuric)

        elem.task?.execute(elem)
    }

    //@Test
    fun executeGroupRateLoan() {
        val elem = Elem(task = ExecuteGroupRateLoan)

        elem.task?.execute(elem)
    }

   // @Test
    fun load550P() {
        EsProcess.process(File("C:/440-П/test/CB_ES550P_20180423_002.XML"))
    }


    //@Test
    fun execCheckActializationEntryDouble() {
        val elem = Elem(idElem = 57425917, task = CheckActializationEntryDouble)

        elem.task?.execute(elem)
    }


    //@Test
    fun execLoadRateThb() {
        val elem = Elem(task = LoadRateThb)

        elem.task?.execute(elem)
    }

    //@Test
    fun masterTest() {

        /*
        val consumerKey = "your consumer key" // You should copy this from "My Keys" on your project page e.g. UTfbhDCSeNYvJpLL5l028sWL9it739PYh6LU5lZja15xcRpY!fd209e6c579dc9d7be52da93d35ae6b6c167c174690b72fa

        val keyAlias = "keyalias" // For production: change this to the key alias you chose when you created your production key

        val keyPassword = "keystorepassword" // For production: change this to the key alias you chose when you created your production key

        val s = FileInputStream("path to your .p12 private key file") // e.g. /Users/yourname/project/sandbox.p12 | C:\Users\yourname\project\sandbox.p12

        ApiConfig.setAuthentication(OAuthAuthentication(consumerKey, s, keyAlias, keyPassword)) // You only need to set this once

        ApiConfig.setDebug(true) // Enable http wire logging

        // This is needed to change the environment to run the sample code. For production: use ApiConfig.setSandbox(false);
        // This is needed to change the environment to run the sample code. For production: use ApiConfig.setSandbox(false);
        ApiConfig.setEnvironment(Environment.parse("sandbox_mtf"))


        val map = RequestMap()
        map["fxDate"] = "2019-09-30"
        map["transCurr"] = "ALL"
        map["crdhldBillCurr"] = "DZD"
        map["bankFee"] = "5"
        map["transAmt"] = "23"

        val response = ConversionRate.query(map)

        logger.error("${response["data.conversionRate"]}")
*/
    }


    //@Test
    fun loadRateThb() {
        logger.error("thbRate=${LoadRateThb.thbRate()}")
    }

    //@Test
    fun outRegisterAquiring() {

        val elem = Elem(idElem = 1181803901, task = OutRegisterAquiring)

        elem.task?.execute(elem)
    }


    //@Test
    fun testVisaLoaderRate() {
        val elem = Elem(idElem = 30, name = "01/01/2020", task = LoadVisaRate)

        elem.task?.execute(elem)
    }



    //@Test
    fun outRestCheck() {

        val elem = Elem(task = OutRestCheck)

        elem.task?.execute(elem)
    }

    //@Test
    fun testToIntOrNull() {

        val x = "ACCOUNT[2017810058]".toIntOrNull()

        logger.error("x=$x")
    }

    //@Test
    fun cecReportProcess() {

        val elem = Elem(task = CecReportProcess, path = "X:\\ЦИК\\2018.10.05\\Запрос", name = "F1027700466640_021018_Z_0039.xml")

        elem.task?.execute(elem)
    }

    //@Test
    fun parseIntLong() {
       //val value = (File("SFF010507717_254020181106_002100001800002869_700.xml").nameWithoutExtension
        //        .substringAfterLast("0000").substringBefore('_').toLong() % 1000000).toInt()

        val value =   "000000608900000".toLong() / 10000000.0

        logger.error("value=$value")
    }


    //@Test
//    fun testSubstring() {
//
//        val responseDateTime = LocalDateTime.now()
//
//        val fileRequest = File("X:\\ЦИК\\2018.10.05\\Запрос/F1027700466640_021018_Z_0039.xml")
//
//        val requestDate = fileRequest.nameWithoutExtension.substringAfter('_').substringBefore('_')
//        logger.error("requestDate=$requestDate")
//
//        val requestNumber = fileRequest.nameWithoutExtension.substringAfter("_Z_")
//        logger.error("requestNumber=$requestNumber")
//
//        val responseFile = File("${CecReportProcess.OUR_CODE}_${responseDateTime.formatDateTime()}_K_${requestNumber}_1000_${CecReportProcess.CEC_CODE}.xml")
//        logger.error("responseFile=$responseFile")
//
//        val textResponse = CecReportProcess.emptyTicketTemplate(responseDateTime.formatDateTime(), requestNumber, requestDate, responseDateTime.formatDateDDMMYYYY())
//        logger.error("textResponse=$textResponse")
//    }

    //@Test
    fun loadSite() {

        val site = "https://usa.visa.com/support/consumer/travel-support/exchange-rate-calculator.html?amount=100.48&fee=0.0&exchangedate=12%2F27%2F2019&fromCurr=USD&toCurr=RUB&submitButton=Calculate+exchange+rate"

        val body = Jsoup.connect(site).get().body()

        val find = body.allElements?.firstOrNull { it.className()?.indexOf("currency-convertion-result")?:-1 >= 0 }

        find?.let {

            val start = find.text().indexOf("Russian Ruble =")

            val end = find.text().indexOf("United States Dollar")

            if(start in 0 until end) {
                val txt = find.text().substring(start + "Russian Ruble =".length, end).trim()

                logger.error(txt.trim())

                val amount: Long = (txt.toDouble() * 100).roundToLong()

                logger.error("$amount")
            }
        }


      //  logger.error(body.text())
    }

    //@Test
    fun testVisaCalculator() {
        logger.error("${VisaCalculator.convertRurToUsd(10000, LocalDateTime.of(2019, 1, 1, 0, 0))}")
    }

    //@Test
    fun decFormat() {
        val otherSymbols = DecimalFormatSymbols()
        otherSymbols.decimalSeparator = '.'
        val format = DecimalFormat("#.##", otherSymbols)

        val value: Long= 100048

        logger.error(format.format(value/100.0 ))
    }

    //@Test
    fun dateFormat() {
        val value = DateTimeFormatter.ofPattern("MM%2'F'dd%2'F'yyyy").format(LocalDateTime.now())

        logger.error(value)
    }




}