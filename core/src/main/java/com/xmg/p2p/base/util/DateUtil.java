package com.xmg.p2p.base.util;

import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	private  DateUtil(){}
	
	public static Date getBeginDate(Date current){
		if(current==null){
			return null;
		}
		Calendar now = Calendar.getInstance();
		now.setTime(current);
		now.set(Calendar.HOUR_OF_DAY, 0);
		now.set(Calendar.MINUTE,0);
		now.set(Calendar.SECOND, 0);
		return now.getTime();
	}
	public static Date getEndDate(Date current){
		if(current==null){
			return null;
		}
		Calendar now = Calendar.getInstance();
		now.setTime(current);
		now.set(Calendar.HOUR_OF_DAY, 23);
		now.set(Calendar.MINUTE,59);
		now.set(Calendar.SECOND, 59);
		return now.getTime();
	}

    public static Long getDateBetween(Date sendTime, Date date) {
		return Math.abs( sendTime.getTime()-date.getTime());
    }
}
