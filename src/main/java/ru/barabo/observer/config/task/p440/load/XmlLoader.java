package ru.barabo.observer.config.task.p440.load;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import org.apache.log4j.Logger;
import ru.barabo.observer.config.task.p440.load.ver4.TypeFileLoadVer4;
import ru.barabo.observer.config.task.p440.load.ver4.request.*;
import ru.barabo.observer.config.task.p440.load.xml.AbstractFromFns;
import ru.barabo.observer.config.task.p440.load.xml.AbstractFromFnsInfo;
import ru.barabo.observer.config.task.p440.load.xml.apx.*;
import ru.barabo.observer.config.task.p440.load.xml.decision.AbstractDecision;
import ru.barabo.observer.config.task.p440.load.xml.decision.DecisionCancel;
import ru.barabo.observer.config.task.p440.load.xml.decision.DecisionSuspend;
import ru.barabo.observer.config.task.p440.load.xml.impl.*;
import ru.barabo.observer.config.task.p440.load.xml.pno.OrderTax;
import ru.barabo.observer.config.task.p440.load.xml.pno.OrderTaxVal;
import ru.barabo.observer.config.task.p440.load.xml.request.*;
import ru.barabo.observer.config.task.p440.load.xml.ticket.AbstractTicket;
import ru.barabo.observer.config.task.p440.load.xml.ticket.impl.IzvTicketInfo;
import ru.barabo.observer.config.task.p440.load.xml.ticket.impl.KwtResultCode;
import ru.barabo.observer.config.task.p440.load.xml.ticket.impl.KwtTicketInfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

public class XmlLoader<E> {

	final static transient private Logger logger = Logger.getLogger(XmlLoader.class.getName());

	private static final Map<String, Class> MY_HASH_ANNOTATION_FILE_OLD_VERSION;

	private static final Map<String, Class> MY_HASH_ANNOTATION_FILE_NEW_VER4;

	static {
		Hashtable<String, Class> tmp = new Hashtable<>();

		for (TypeFileLoad type : TypeFileLoad.values()) {
			tmp.put(type.getPrefixFile(), type.getClazz());
		}

		MY_HASH_ANNOTATION_FILE_OLD_VERSION = Collections.unmodifiableMap(tmp);


		Hashtable<String, Class> tmpNew = new Hashtable<>();
		for (TypeFileLoadVer4 type : TypeFileLoadVer4.values()) {
			tmpNew.put(type.getPrefixFile(), type.getClazz());
		}

		MY_HASH_ANNOTATION_FILE_NEW_VER4 = tmpNew;
	}

	static private Class getClassByPrefixFile(String prefixFile, boolean isNewFormats) {

		return isNewFormats ? MY_HASH_ANNOTATION_FILE_NEW_VER4.get(prefixFile) : MY_HASH_ANNOTATION_FILE_OLD_VERSION.get(prefixFile);
	}

	static private LocalDate getDateFile(File file) {

		if(file.getName().substring(0, 3).equalsIgnoreCase("IZV") ||
				file.getName().substring(0, 3).equalsIgnoreCase("KWT")
		) {
			return LocalDate.of(2021, 3,16);

		} else {
			return XmlLoader.parseShorDate(file.getName().substring(16, 16+8)).toLocalDate();
		}
	}

	static public boolean isNewFormats(File file) {
		return !getDateFile(file).isBefore(LocalDate.of(2021, 3,16 ));
	}

	static private XStream getXStream(File file) {

		String head3 = file.getName().substring(0, 3).toUpperCase();


		XStream xstream = new XStream(new DomDriver("CP1251"));

		xstream.processAnnotations(getClassByPrefixFile(head3, isNewFormats(file) ));

		xstream.processAnnotations(AbstractFromFns.class);
		xstream.processAnnotations(AbstractFromFnsInfo.class);

		xstream.processAnnotations(Account.class);
		xstream.processAnnotations(Address.class);
		xstream.processAnnotations(Fio.class);
		xstream.processAnnotations(FnsXml.class);
		xstream.processAnnotations(Kesp.class);
		xstream.processAnnotations(PayerXml.class);
		xstream.processAnnotations(PayerIp.class);
		xstream.processAnnotations(PayerJur.class);
		xstream.processAnnotations(PayerPhysic.class);
		xstream.processAnnotations(BankXml.class);
		xstream.processAnnotations(Period.class);

		xstream.processAnnotations(AbstractDecision.class);
		xstream.processAnnotations(DecisionCancel.class);
		xstream.processAnnotations(DecisionSuspend.class);

		xstream.processAnnotations(AbstractRequest.class);
		xstream.processAnnotations(ExistsRequest.class);
		xstream.processAnnotations(ExtractRequest.class);
		xstream.processAnnotations(RestRequest.class);

		xstream.processAnnotations(OrderTax.class);
		xstream.processAnnotations(OrderTaxVal.class);

		xstream.processAnnotations(IzvTicketInfo.class);
		xstream.processAnnotations(KwtTicketInfo.class);
		xstream.processAnnotations(KwtResultCode.class);
		xstream.processAnnotations(AbstractTicket.class);

		xstream.processAnnotations(ApnInfo.class);
		xstream.processAnnotations(ApzInfo.class);
		xstream.processAnnotations(AbstractApx.class);
		xstream.processAnnotations(PnoCancel.class);
		xstream.processAnnotations(ApoInfo.class);

		xstream.processAnnotations(DateWorkState.class);
		xstream.processAnnotations(DatePeriod.class);



		///-----------!!!
		xstream.processAnnotations(TypeDetailAccounts.class);
		xstream.processAnnotations(TypeAllAccounts.class);
		xstream.processAnnotations(RestRequestVer4.class);
		xstream.processAnnotations(RestOwnerAccount.class);
		xstream.processAnnotations(RestOnDatePeriod.class);
		xstream.processAnnotations(RestOnDate.class);
		xstream.processAnnotations(ExtractRequestVer4.class);


		xstream.useAttributeFor(String.class);
		xstream.useAttributeFor(Integer.class);

		return xstream;
	}

	private static String XML_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

	private static String XML_DATE_FORMAT = "yyyy-MM-dd";

	private static String SHORT_DATE_FORMAT = "yyyyMMdd";

	private static String XML_TIME_FORMAT = "HH:mm:ss";

	public static String formatShortDate(Date date) {
		if (date == null) {
			return "";
		}

		SimpleDateFormat formatter = new SimpleDateFormat(SHORT_DATE_FORMAT);

		return formatter.format(date);
	}

	public static String formatDateTime(Date date) {
		if (date == null) {
			return null;
		}

		SimpleDateFormat formatter = new SimpleDateFormat(XML_DATE_TIME_FORMAT);

		return formatter.format(date);
	}

	public static String formatDate(Date date) {
		if (date == null) {
			return null;
		}

		SimpleDateFormat formatter = new SimpleDateFormat(XML_DATE_FORMAT);

		return formatter.format(date);
	}

	public static String formatTime(Date dateTime) {
		if (dateTime == null) {
			return null;
		}

		SimpleDateFormat formatter = new SimpleDateFormat(XML_TIME_FORMAT);

		return formatter.format(dateTime);
	}

	public static java.sql.Date parseShorDate(String date) {
		if (date == null || "".equals(date.trim())) {
			return null;
		}

		SimpleDateFormat formatter = new SimpleDateFormat(SHORT_DATE_FORMAT);

		java.sql.Date result;

		try {
			result = new java.sql.Date(formatter.parse(date).getTime());
		} catch (ParseException e) {
			logger.error("ParseException parseDate", e);
			return null;
		}

		return result;
	}


	public static java.sql.Date parseDate(String date) {
		if (date == null || "".equals(date.trim())) {
			return null;
		}

		SimpleDateFormat formatter = new SimpleDateFormat(XML_DATE_FORMAT);

		java.sql.Date result;

		try {
			result = new java.sql.Date(formatter.parse(date).getTime());
		} catch (ParseException e) {
			logger.error("ParseException parseDate", e);
			return null;
		}

		return result;
	}

	public static String formatSum(Number sum, String defnull) {

		String format = formatSum(sum);
		if (format == null) {
			format = defnull;
		}

		return format;
	}

	public static String formatInteger(Number integer) {
		if (integer == null) {
			return null;
		}

		return new DecimalFormat("0").format(integer);
	}

	public static String formatSum(Number sum) {
		if (sum == null) {
			return null;
		}

		DecimalFormat decimalFormat = new DecimalFormat("0.00");
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator('.');
		decimalFormat.setDecimalFormatSymbols(symbols);

		return decimalFormat.format(sum);
	}

	public static String formatDecimal3(Number sum) {
		if (sum == null) {
			return null;
		}

		DecimalFormat decimalFormat = new DecimalFormat("0.000");
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator('.');
		decimalFormat.setDecimalFormatSymbols(symbols);

		return decimalFormat.format(sum);
	}

	public static Number parseSum(String sum) {
		if (sum == null || "".equals(sum.trim())) {
			return null;
		}

		try {
			return Double.parseDouble(sum);
		} catch (NumberFormatException e) {
			logger.error("NumberFormatException parseSum", e);
			return null;
		}
	}

	public E load(File file) {

		FileInputStream fl;
		E objectXml;

		try {
			fl = new FileInputStream(file);


			//logger.error("file.getName=" + file.getName());

			XStream xstream = getXStream(file);

			objectXml = (E) xstream.fromXML(fl);

			fl.close();
		} catch (FileNotFoundException e) {
			logger.error("FileNotFoundException load", e);
			return null;

		} catch (IOException e) {
			logger.error("IOException load", e);
			return null;
		}

		return objectXml;
	}
}
