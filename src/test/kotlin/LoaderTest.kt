import oracle.jdbc.OracleTypes
import org.jsoup.Jsoup
import org.junit.Before
import org.junit.Test
import org.slf4j.LoggerFactory
import ru.barabo.cmd.Cmd
import ru.barabo.exchange.VisaCalculator
import ru.barabo.observer.afina.AfinaQuery
import ru.barabo.observer.afina.clobToString
import ru.barabo.observer.afina.getTextFileBackup
import ru.barabo.observer.config.barabo.crypto.task.CreateAccount311p
import ru.barabo.observer.config.barabo.crypto.task.LoadBik
import ru.barabo.observer.config.barabo.crypto.task.LoadRateThb
import ru.barabo.observer.config.barabo.crypto.task.LoaderRutdfTicketReject
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
import ru.barabo.observer.config.cbr.sender.task.EmailTempSender
import ru.barabo.observer.config.cbr.ticket.task.GetProcess550pFiles
import ru.barabo.observer.config.cbr.ticket.task.XmlLoaderCbrTicket311p
import ru.barabo.observer.config.cbr.turncard.task.TurnOutTechOver
import ru.barabo.observer.config.correspond.task.DecryptEdFile
import ru.barabo.observer.config.correspond.task.loadDecodeFile
import ru.barabo.observer.config.fns.ens.task.CheckNotLoaded440pFiles
import ru.barabo.observer.config.jzdo.upay.task.LoadAcqAdvUPay
import ru.barabo.observer.config.jzdo.upay.task.LoadMtlUPay
import ru.barabo.observer.config.skad.acquiring.task.ExecuteWeechatFile
import ru.barabo.observer.config.skad.acquiring.task.MinComissionMonthPos
import ru.barabo.observer.config.skad.acquiring.task.RecalcTerminalsRate
import ru.barabo.observer.config.skad.anywork.task.*
import ru.barabo.observer.config.skad.crypto.p311.MessageCreator311p
import ru.barabo.observer.config.skad.crypto.p311.validateXml
import ru.barabo.observer.config.skad.crypto.task.PbSaverScadVer4
import ru.barabo.observer.config.skad.forms.ed711497.impl.DefaultPercentOutData
import ru.barabo.observer.config.skad.forms.form310.impl.DefaultForm310Data
import ru.barabo.observer.config.skad.plastic.task.CbrCurrencyLoader
import ru.barabo.observer.config.skad.plastic.task.LoadVisaRate
import ru.barabo.observer.config.skad.plastic.task.LoaderNbkiFileSent
import ru.barabo.observer.config.skad.plastic.task.SendXmlRiskClientCbrAuto
import ru.barabo.observer.config.task.Executor
import ru.barabo.observer.config.task.finder.isFind
import ru.barabo.observer.config.task.info.InfoHtmlData
import ru.barabo.observer.config.test.TestConfig
import ru.barabo.observer.report.ReportXlsLockCards
import ru.barabo.observer.store.Elem
import ru.barabo.observer.store.TaskMapper
import ru.barabo.observer.store.derby.StoreSimple
import ru.barabo.xls.*
import java.io.File
import java.nio.charset.Charset
import java.nio.file.Path
import java.nio.file.Paths
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
import kotlin.math.round
import kotlin.math.roundToLong


class LoaderTest {

    private val logger = LoggerFactory.getLogger(LoaderTest::class.java)

    @Before
    fun initTestBase() {
        TaskMapper.init("TEST", "AFINA" /*"TEST"*/)

        com.sun.javafx.application.PlatformImpl.startup {}
    }

    private fun separ() = ";"


    //@Test
    fun validateXmlTest() {
        val xmlFile = File("C:/report/SBC110507717_254020221102_002100002200005270_100.xml")
        val xsd = "/xsd/SBC1_512.xsd"

        validateXml(xmlFile, xsd) { File("C:/report/1")  }
    }

    //@Test
    fun testCbrKeyRateLoader() {
        val elem = Elem(idElem = 0L, task = CbrKeyRateLoader)

        CbrKeyRateLoader.execute(elem)
    }

    //@Test
    fun testParseKeyRate() {

        //val request = "https://www.cbr.ru/hd_base/KeyRate/"
        //val request = "https://www.cbr.ru/hd_base/KeyRate/?UniDbQuery.Posted=True&UniDbQuery.From=01.12.2023&UniDbQuery.To=18.12.2023"

        val request = "https://www.cbr.ru/press/keypr"

        val body = Jsoup.connect(request)
            .header("Content-Type","application/x-www-form-urlencoded")
            .cookie("TALanguage", "ALL")
            .data("mode", "filterReviews")
            .data("filterRating", "")
            .data("filterSegment", "")
            .data("filterSeasons", "")
            .data("filterLang", "ALL")
            .referrer(request)
            .header("X-Requested-With", "XMLHttpRequest")
            //.header("X-Puid",xpuid)
            //.data("returnTo",returnTo)
            .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
            //.method(Connection.Method.POST)
             .get().body()

        //logger.error("$body")

        logger.error("$body")

        val find = body.allElements?.first { (it.className()?.indexOf("data"/*"table-wrapper"*/) ?: -1) >= 0 }

        logger.error("$find")

       // val regex = Pattern.compile("(.*?)<td>(.*?)</td>(.*?)")

        val regex = Pattern.compile("(<td>(.*?)</td>)")

        val regexMatcher = regex.matcher("$find")

        while (regexMatcher.find()) {
            logger.error(regexMatcher.group(2))
        }
    }


    //@Test
    fun testZsoLoadVer4() {

        //val file = File("C:/Картстандарт/test/ZSO_400_ALLxsd_ONDATE.xml")

        val file = File("C:/Картстандарт/test/ZSO_400_ALLxsd_PERIOD.xml")

        ZsoLoaderVer4.processFile(file)
    }

    //@Test
    fun testCorrectPrim() {

        val ids = arrayOf(1236714696L, 1237175375L, 1237847807L, 1238080222L, 1238448441L, 1238814490L)

        //val elem = Elem(idElem = 1206623916L, task = CorrectPrim)
               // Elem(idElem = 1205659121L, task = CorrectPrim)
                //Elem(idElem = 1205641626L, task = CorrectPrim)

        for (id in ids) {
            val elem = Elem(idElem = id, task = CorrectPrim)
            CorrectPrim.execute(elem)
        }
    }

    //@Test
    fun testOutRegisterAquiring() {

        val terminals: Array<Any?> = arrayOf("J244999", "22.02.20 16:00:28", "24.02.20 10:41:06", "IP CHALINA T N",
                "DEREVENSKAYA 16  PAV 49", "chalinpoint@yandex.ru", null)

        OutRegisterAquiring.processTerminals(listOf(terminals), 1206971198L)
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

        val max = (File("X:/639-П/Out/2019/12/16").listFiles { f ->
            !f.isDirectory && search.matcher(f.name).find()}
            ?.map { Integer.parseInt(it.name.substring(it.name.indexOf(".") - 3, it.name.indexOf(".")))}?.maxOrNull() ?:0)+ 1

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

        file.listFiles()?.filter { !it.isDirectory }?.forEach {
            XmlLoaderCbrTicket311p.processFile(it)
        }
    }


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
    fun testBinLoader() {
        val elem = Elem(File("C:/Temp/bin_table_universal_20210111.csv"), BinLoader, Duration.ZERO)

        BinLoader.execute(elem)

        logger.error("END=${LocalDateTime.now()}")
    }

    // @Test
    fun testLoadAcqAdvUPay() {
        val elem = Elem(File("C:/КартСтандарт/ZKACQ_ADV_500764_200401.071"), LoadAcqAdvUPay, Duration.ZERO)

        LoadAcqAdvUPay.execute(elem)
    }


    //@Test
    fun test440pExec() {

        val elem = Elem(idElem = 1187605445L)// женя 1187580606L) // RPO=1186808886L

        Process440p.execute(elem)
    }

    //@Test
    fun loadZsn() {
        //val elem = Elem(File("D:/440-П/test/ZSN10507717_773120190311_500500.xml"), ZsnLoader, Duration.ZERO)

        val elem = Elem(File("D:/logs/QWE/PNO40507717_253620220511_057378.xml"), PnoLoader, Duration.ZERO)

        //val elem = Elem(File("C:/Картстандарт/test/ROO10507717_254320190226_000000.xml"), RooLoader, Duration.ZERO)

        // женя
        //val elem = Elem(File("C:/Картстандарт/test/RPO10507717_251020190226_000000.xml"), RpoLoader, Duration.ZERO)

        //val elem = Elem(File("C:/Картстандарт/test/рита/RPO10507717_251020190226_000000.xml"), RpoLoader, Duration.ZERO)

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
    fun save311p512() {
        MessageCreator311p.createMessage(1234430593) // juric close

        MessageCreator311p.createMessage(1234430597)  // physic open

        MessageCreator311p.createMessage(1234242680) // ip close

    }

    //@Test
    fun loadClearInt() {

        File("C:/Temp/1").listFiles()?.filter { !it.isDirectory }?.forEach {

            logger.error("START LOAD ${it.name}")

            val elem = Elem(it, ClearintLoader, Duration.ZERO)

            elem.task?.execute(elem)
        }
    }

    //@Test
    fun executeClearIntConverse() {
        val elem = Elem(idElem = 1234256304, name = "CLEARINT_20201223_055217_0226_3033.html", task = ExecuteClearIntConverse)

        elem.task?.execute(elem)
    }


    //@Test
    fun executeClearInt() {
        val elem = Elem(idElem = 1233726858, name = "CLEARINT_20201215_070032_0226_3027.html", task = ExecuteClearInt)

        elem.task?.execute(elem)
    }


    //@Test
    fun loadCtlMtl() {
        val elem = Elem(File("C:/КартСтандарт/MTL20200728_0226.0001"), LoadCtlMtl, Duration.ZERO)

        elem.task?.execute(elem)
    }

    //@Test
    fun loadMtlUPay() {
        val elem = Elem(File("C:/КартСтандарт/ZKMTL_20190909_0999.0001"), LoadMtlUPay, Duration.ZERO)

        elem.task?.execute(elem)
    }

    //@Test
    fun execWeechat() {
        val elem = Elem(idElem = 1218838711 /*1218838744*/, task = ExecuteWeechatFile)

        elem.task?.execute(elem)

        logger.error("Ok")
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
    fun validateXmlByXsd() {

        val xsd = "/xsd/LSOZ.xsd"

        //val xmlFile = File("C:/440-П/2021/03/10/test/RPO10507717_250220210308_706511.xml") // ИП

        val xmlFile = File("C:/temp2/LSOZ_0021_0000_F20230801_L20230831_C20230913_0001_WIN.xml")

        validateXml(xmlFile, xsd, ::errorFolder )
    }

    private fun errorFolder(): File = Cmd.createFolder("C:/temp2/err")


    //@Test
    fun execObi() {
        val elem = Elem(idElem = 1172320580, task = ExecuteObi)

        elem.task?.execute(elem)
    }

    //@Test
    fun turnOutTechOver() {
        val elem = Elem(idElem = 1172320580, task = TurnOutTechOver)

        elem.task?.execute(elem)
    }


    //@Test
    fun loadZsvVer4() {
        //val elem = Elem(File("C:/440-П/test/ZSV10507717_254220210310_195657.xml"), ZsvLoader, Duration.ZERO) // по всем - старый формат

        //val elem = Elem(File("C:/440-П/test/ZSV10507717_254220210317_195657.xml"), ZsvLoaderVer4, Duration.ZERO) // по всем новый формат

        val elem = Elem(File("C:/440-П/test/ZSV10507717_254320210316_100604.xml"), ZsvLoaderVer4, Duration.ZERO) // по указанным новый формат

        elem.task?.execute(elem)
    }

    //@Test
    fun exec440p() {

        val elem = Elem(idElem = 1238437141, task = Process440p)

        elem.task?.execute(elem)
    }

    //@Test
    fun outBnp() {

        val elem = Elem(idElem = 1238451941, task = BnpSaverVer4)

        elem.task?.execute(elem)
    }

    // @Test
    fun outBns() {

        //val elem = Elem(idElem = 1222895332, task = ExistsSaverVer4) // empty accounts

        val elem = Elem(idElem = 1201698918, task = ExistsSaverVer4) // with accounts

        elem.task?.execute(elem)
    }


    //@Test
    fun outPb() {

        // val elem = Elem(idElem = 1238034454, task = PbSaverScadVer4) // pb1

        val elem = Elem(idElem = 1238021154, task = PbSaverScadVer4) // pb2

        elem.task?.execute(elem)
    }

    //@Test
    fun outBvsExtract() {
        // many count operations
        //val elem = Elem(idElem = 1236717012, task = ExtractMainSaverVer4)


        // many count accounts
        //val elem = Elem(idElem = 1235843359, task = ExtractMainSaverVer4)

        // new many accounts format
        //val elem = Elem(idElem = 1238437144, task = ExtractMainSaverVer4)

        // bug infoNameAddFiles
        val elem = Elem(idElem = 1264214539, task = ExtractMainSaverVer4)


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
    fun cbrCurrencyLoader() {

        val currentRelativePath: Path = Paths.get("")
        val s: String = currentRelativePath.toAbsolutePath().toString()

        logger.error("CURRENT=$s")

        val elem = Elem(idElem = 0, task = CbrCurrencyLoader)

        CbrCurrencyLoader.execute(elem)

        //CbrCurrencyLoader.execByCheckToday(elem, false)
    }


    //@Test
    fun thbCurrencyLoader() {

        //logger.error("THB=${LoadRateThb.thbRate()}")

        logger.error("THB=${round( 100000 * LoadRateThb.thbRate() ).toLong()}")
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

        val value = "000000608900000".toLong() / 10000000.0

        logger.error("value=$value")
    }


    //@Test
    fun testLoadCurrencyCbr() {
        val site = "http://www.cbr.ru/scripts/XML_daily.asp?date_req=11/04/2021"

        //val site =  "https://www.cbr-xml-daily.ru/daily.xml"

        val body = Jsoup.connect(site).get()

        val findMain = body.allElements?.first { it.nodeName() == "ValCurs" }

        val date = findMain?.attr("Date")
        val name = findMain?.attr("name")

        logger.error("date = $date")
        logger.error("name = $name")

        findMain?.children()?.forEach {
            if(it.nodeName() == "Valute") {
                val nameX = it.children().firstOrNull { it.nodeName() == "Name" }
                logger.error("Name=${nameX?.text()}")

                val numCode = it.children().firstOrNull { it.nodeName() == "NumCode" }
                logger.error("numCode=${numCode?.text()}")

                val charCode = it.children().firstOrNull { it.nodeName() == "CharCode" }
                logger.error("charCode=${charCode?.text()}")

                val nominal = it.children().firstOrNull { it.nodeName() == "Nominal" }
                logger.error("nominal=${nominal?.text()}")

                val value = it.children().firstOrNull { it.nodeName() == "Value" }
                logger.error("value=${value?.text()}")
            }
        }
    }


    //@Test
    fun loadSite() {

        val site = "https://usa.visa.com/support/consumer/travel-support/exchange-rate-calculator.html?amount=100.48&fee=0.0&exchangedate=12%2F27%2F2019&fromCurr=USD&toCurr=RUB&submitButton=Calculate+exchange+rate"

        val body = Jsoup.connect(site).get().body()

        val find = body.allElements?.firstOrNull { (it.className()?.indexOf("currency-convertion-result") ?: -1) >= 0 }

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

    // @Test
    fun testEmailTempSender() {
        val elem = Elem(idElem = 1245512350L, name = "EmailTempSender", task = EmailTempSender)

        elem.task?.execute(elem)
    }

    // @Test
    fun testAssociateWith() {
        val headerMap = "columns;rows".split(";").associateWith { "left" }

        for (pair in headerMap) {
            logger.error("key=${pair.key}")
            logger.error("value=${pair.value}")
        }
    }


    //@Test
    fun testDecode() {

       // DecryptEdFile.loadFile(File("c:/temp/050771700000000000PacketEPD119009lf.ED"))
    }

    //@Test
    fun testReportTransListLoader() {

        val file = File("C:/Temp/ReportTransList/ReportTransList25082021_2.csv")

        ReportTransListLoader.processFile(file)
    }

    //@Test
    fun testRecalcTerminalsRate() {
        val elem = Elem(idElem = 30, name = "01/01/2020", task = RecalcTerminalsRate)

        elem.task?.execute(elem)
    }

    //@Test
    fun testMinComissionMonthPos() {
        val elem = Elem(idElem = 30, name = "01/01/2020", task = MinComissionMonthPos)

        elem.task?.execute(elem)
    }

    //@Test
    fun testCreateAccount311p() {
        val elem = Elem(idElem = 30, name = "01/01/2020", task = CreateAccount311p)

        elem.task?.execute(elem)
    }

    //@Test
    fun testSaveAccount311p() {

        //val EXEC_CREATE_JUR_ACCOUNT = "{ call od.PTKB_FNS_EXPORT_XML.execJurDataPriorDay }"

        //AfinaQuery.execute(EXEC_CREATE_JUR_ACCOUNT)

        MessageCreator311p.createMessage(1250875298) // juric

        MessageCreator311p.createMessage(1250875287) // juric
    }

    //@Test
    fun testOutInnByMerchant() {
        val elem = Elem(task = OutInnByMerchant)

        elem.task?.execute(elem)
    }

    //@Test
    fun testDecryptEdFile1() {

        loadDecodeFile(File("C:/311-П/050771700000000000PacketEID017003dm.ED.xml"))
    }

    //@Test
    fun testRutdfCreateReport() {
        val elem = Elem(idElem = 30, name = "01/01/2020", task = RutdfCreateReport)

        elem.task?.execute(elem)

        val file = File("")
        file.name
    }

    //@Test
    fun testLoaderRutdfTicketCheckTradeByPath() {
        LoaderRutdfTicketReject.checkTradeByPath( LocalDate.of(2023, 2, 1), LocalDate.of(2023, 6, 1) )
    }


    //@Test
    fun testRegExpFio() {
        val cliens = "{ ? = call OD.PTKB_CASH.getClientPhysicByUserDepart }"

        val clients = AfinaQuery.selectCursor(cliens).map { it[0]?.toString() ?: "" }

        val regExp = "[А-ЯЁ-]+\\s[А-ЯЁ-]+\\s[А-ЯЁ-]+(\\s[А-ЯЁ]+)?$" //"[А-ЯЁ-]+\\s[А-ЯЁ-]+(\\s[А-ЯЁ-]){1,2}"

        val pattern = Pattern.compile(regExp)

        logger.error("client count = ${clients.size}")


        var notFound = 0
        for (client in clients) {
            val isClient = pattern.matcher(client).matches()

            if(!isClient) {
                logger.error(client)
                notFound++
            }
        }

        logger.error("notFound = $notFound")
    }

    //@Test
    fun testFnsPercentOut() {

        val fnsPercentOut = DefaultPercentOutData(LocalDate.of(2023, 12, 31))
        fnsPercentOut.createFile()
    }

    //@Test
    fun testClientRiskLoader() {
        ClientRiskLoader.processFile(File("C:/311-П/KYC_20221114.xml"))
    }

    //@Test
    fun testDecryptEdFile() {
        val elem = Elem(idElem = 30, name = "01/01/2020", task = RutdfCreateReport)

        val state = DecryptEdFile.processFile(File("C:/311-П/2/05077170000000000PacketESID02200rfw.ED"), elem)
        //elem.task?.execute(elem)

        logger.error("state=$state")
    }

    //@Test
    fun testSendXmlRiskClientCbrAuto() {
        val elem = Elem(idElem = 30, name = "test", task = SendXmlRiskClientCbrAuto)

        elem.task?.execute(elem)
    }

    //@Test
    fun testForm310() {

        val form310 = DefaultForm310Data(LocalDate.of(2022, 11, 1))

        form310.createFile()
    }


    //@Test
    fun outBos() {
        //val elem = Elem(idElem = 1172496442, task = RestSaver)

        // val elem = Elem(idElem = 1223812573, task = RestSaverVer4) // response for zso with accounts rest

        val elem = Elem(idElem = 1276209727, task = RestSaverVer4) // response for rpo with accounts rest

        elem.task?.execute(elem)
    }

    //@Test
    fun testClearPrimFromArchiveDay() {

        val elem = Elem(idElem = 97329450L,
        //17 93584962L,
        //18 93584719L,
        //19 93592149L,
        // 93558795L,
            task = ClearPrimFromArchiveDay)

        ClearPrimFromArchiveDay.execute(elem)
    }

    //@Test
    fun testCheckNotLoaded440pFiles() {

        val elem = Elem(idElem = 93076872L, task = CheckNotLoaded440pFiles)

        CheckNotLoaded440pFiles.execute(elem)
    }

    //@Test
    fun testGetSmevArchives() {
        val it = GetSmevArchives.fileFinderData[0]
        val isFind = it.search!!.isFind("AFN_MIFNS00_0507717_20230412_00001.zip")
        logger.error("IS=$isFind")

        val elem = Elem(File("C:/440-П/smev/in/AFN_MIFNS00_0507717_20230412_00001.zip"), GetSmevArchives, Duration.ZERO)

        elem.task?.execute(elem)
    }

    fun testParent() {
        val file = File("C:/440-П/smev/in/AFN_MIFNS00_0507717_20230412_00001.zip")

        logger.error("parent=${file.parent}")
    }

    //@Test
    fun testValidate311P() {

        //val file =
            MessageCreator311p.createMessage(1289785658L) //1289785611L)
    }

    //@Test
    fun testToEmails() {

        val x = toEmailsList()
        for(i in x) {
            logger.error(i)
        }
    }

   //@Test
    fun testLoaderRutdfTicketReject() {
        LoaderRutdfTicketReject.loadTicket( File("X:/НБКИ/2024/07/31/UNCRYPTO/K301BB000001_20240731_100927_reject") )
    }

    //@Test
    fun testLoaderNbkiFileSent() {
        //LoaderNbkiFileSent.load(LocalDate.of(2024, 2,5))

        LoaderNbkiFileSent.loadByFile(File("K301BB000001_20240605_111930") )
    }

    //@Test
    fun testGetTextFileBackup() {
        val text = getTextFileBackup()

        logger.error(text)
    }

    //@Test
    fun testExtract407pByRfm() {
        val elem = Elem(File("C:/app/RFM_040507717_20240221_001.xml"), Extract407pByRfm, Duration.ZERO)

        Extract407pByRfm.execute(elem)
    }
}

fun errorFolder(): File = Cmd.createFolder("C:/311-П/test/ERROR")