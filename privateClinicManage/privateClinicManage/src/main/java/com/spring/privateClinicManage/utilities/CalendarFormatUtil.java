package com.spring.privateClinicManage.utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CalendarFormatUtil {

	private static final SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
	private static final SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static CalendarFormat parseStringToCalendarFormat(String s) {
		Date d;
		Calendar calendar = Calendar.getInstance();
		try {
			d = dateFormat1.parse(s);
			calendar.setTime(d);

		} catch (ParseException e) {
			try {
				d = dateFormat2.parse(s);
				calendar.setTime(d);

			} catch (ParseException e1) {
				System.err.println("ParseException: " + e.getMessage());
			}
			System.err.println("ParseException: " + e.getMessage());
		}

		Integer year = calendar.get(Calendar.YEAR);
		Integer month = calendar.get(Calendar.MONTH) + 1;
		Integer day = calendar.get(Calendar.DAY_OF_MONTH);

		return new CalendarFormat(year, month, day);
	}


}
