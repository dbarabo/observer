package ru.barabo.observer.utils;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

final public class DateUtils {

	final static transient private Logger logger = Logger.getLogger(DateUtils.class.getName());

	/**
	 * @return true - если дата dataFile больше даты файла otherFile или в
	 *         случае otherFile=null больше minDate
	 */
	static public boolean ftpMore(Calendar dataFile, File otherFile, Date minDate) {

		return (dataFile != null) && (dataFile.getTime().getTime() > nvl(otherFile, minDate));
	}

	static private long nvl(File fileDate, Date date2) {
		return fileDate != null ? fileDate.lastModified() : date2.getTime();
	}

	static public String formatTimeNow(Date date, String format) {
		if (date == null) {
			return null;
		}

		SimpleDateFormat formatter = new SimpleDateFormat(format);

		return formatter.format(date);
	}

	static public java.sql.Timestamp getTimestamp(Date date) {
		return date == null ? null : new java.sql.Timestamp(date.getTime());
	}

	static public Date parseDateNotNull(String date, String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);

		Date result = null;

		try {
			result = new java.util.Date(formatter.parse(date).getTime());
		} catch (ParseException e) {

			logger.error("ParseException parseDate", e);
			return null;
		}

		return result;
	}

	static public Date timeToNowDate(Date time) {

		Calendar calendarTime = Calendar.getInstance();

		calendarTime.setTime(time);

		Calendar calendarNow = Calendar.getInstance();

		calendarNow.set(Calendar.HOUR_OF_DAY, calendarTime.get(Calendar.HOUR_OF_DAY));

		calendarNow.set(Calendar.MINUTE, calendarTime.get(Calendar.MINUTE));

		calendarNow.set(Calendar.SECOND, calendarTime.get(Calendar.SECOND));

		return calendarNow.getTime();
	}

	static public Date plus(int field, int value) {
		return plus(new Date(), field, value);
	}

	static public Date plus(Date source, int field, int value) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(source);

		calendar.add(field, value);

		return calendar.getTime();
	}
}
