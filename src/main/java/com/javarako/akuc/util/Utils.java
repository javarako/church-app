package com.javarako.akuc.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class Utils {

	public static Date getSundayFromToday() {
		GregorianCalendar today = new GregorianCalendar();
		today.set(Calendar.HOUR_OF_DAY, 0);
		today.set(Calendar.MINUTE, 0);
		today.set(Calendar.SECOND, 0);
		today.set(Calendar.MILLISECOND, 0);

		int day = today.get(Calendar.DAY_OF_WEEK);
		if (day != 1) {
			today.add(Calendar.DAY_OF_MONTH, 8 - day);
		}
		return today.getTime();
	}

	public static Date getSundayFromToday(Date date) {
		GregorianCalendar today = new GregorianCalendar();

		if (date != null) {
			today.setGregorianChange(date);
		}

		today.set(Calendar.HOUR_OF_DAY, 0);
		today.set(Calendar.MINUTE, 0);
		today.set(Calendar.SECOND, 0);
		today.set(Calendar.MILLISECOND, 0);

		int day = today.get(Calendar.DAY_OF_WEEK);
		if (day != 1) {
			today.add(Calendar.DAY_OF_MONTH, 8 - day);
		}
		return today.getTime();
	}

	public static LocalDateTime toLocalDateTime(Calendar calendar) {
		if (calendar == null) {
			return null;
		}
		TimeZone tz = calendar.getTimeZone();
		ZoneId zid = tz == null ? ZoneId.systemDefault() : tz.toZoneId();
		return LocalDateTime.ofInstant(calendar.toInstant(), zid);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(getSundayFromToday(null));
	}

}
