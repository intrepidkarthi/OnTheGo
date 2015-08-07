package com.looksphere.goindia.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


public class DateUtils {
	static SimpleDateFormat formatter_utc = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US);
    static SimpleDateFormat formatter_local = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
    static SimpleDateFormat displayFormat = new SimpleDateFormat("MMM dd hh:mm a", Locale.US);

    public static enum TIME_UNIT {SECONDS, MINUTES, HOURS, DAYS, WEEKS, MONTHS, YEARS};
    static String str1 = "2014-04-12T01:41:23-07:00";
    static String str2 = "2014-04-12T01:41:23";
	
	
	public static Date getDateFromString(String dateString) {
		try {
            SimpleDateFormat formatter = getCorrectFormatter(dateString);
            Date date = formatter.parse(dateString);
    		return date;
		} catch (ParseException e) {
			return null;
		}

	}

    public static String getStringFromUTCDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US);
        String dateString = formatter.format(date);
        //Add the ":" into the string. Java simpleDateFormatter does not split the timeZOne hour
        // difference into hours:minutes
        int length = dateString.length();
        int index1 = dateString.lastIndexOf("-");
        int index2 = dateString.lastIndexOf("+");
        int startPos;
        if(index1 < index2) {
            startPos = index2+1;
        } else {
            startPos = index1+1;
        }

        //walk till the end of the string and add colons after every two characters
        String finalString = dateString.substring(0, startPos);

        for(int i= startPos; i<length; i=i+2) {

            finalString = finalString + dateString.substring(i, i+2);
            if((i+2) <length) {
                finalString = finalString + ":";
            }

        }

        return finalString;
    }

    private static SimpleDateFormat getCorrectFormatter(String dateString) {
        //return the correct format based on the string passed in....
        //Strings can be of the type "2014-04-12T01:41:23-07:00" or
        //"2014-04-12T01:41:23"
        boolean timeZonePresent = dateString.matches("(.*)T(\\d*):(\\d*):(\\d*)(-|\\+)(.*)");
        if(timeZonePresent) {
            formatter_utc.setTimeZone(TimeZone.getTimeZone("UTC"));
            return formatter_utc;
        } else {
            formatter_local.setTimeZone(TimeZone.getTimeZone("UTC"));
            return formatter_local;
        }
    }


    public static boolean isDateWithinPastWindow(Date eventDate, int duration, TIME_UNIT timeUnit) {
		Date currentTime = new Date();
		if(eventDate.after(currentTime)){
			return false;
		} else {
			long diffInMilli = currentTime.getTime() - eventDate.getTime();
			long secs = diffInMilli/1000;
			long mins = secs/60;
			long hours = mins/60;
			long days = hours/24;
			
			boolean retVal = false;
			switch(timeUnit){
				case SECONDS:
					 retVal = (secs <= duration);
				break;
				case MINUTES:
					retVal = (mins <= duration);
				break;
				case HOURS:
					retVal = (hours <= duration);
				break;
				case DAYS:
					retVal = (days <= duration);
				break;
				case WEEKS:

				break;
				case MONTHS:

				break;
				
				default:
				break;
			}
			
			return retVal;

		}
	
	}

    public static long convertMilliSecondsTo(long milliSeconds, TIME_UNIT timeUnit) {

        long retVal = 0;
        long secs = milliSeconds/1000;
        long mins = secs/60;
        long hours = mins/60;
        long days = hours/24;

        switch(timeUnit){
            case SECONDS:
                retVal = secs;
                break;
            case MINUTES:
                retVal = mins;
                break;
            case HOURS:
                retVal = hours;
                break;
            case DAYS:
                retVal = days;
                break;
            case WEEKS:
                break;
            case MONTHS:
                break;

            default:
                break;
        }

        return retVal;

    }

	public static long getDiffInMilli(Date date1, Date date2) {
		long diffInMilli;
		if(date1.after(date2)) {
			diffInMilli = date1.getTime() - date2.getTime();
		} else {
			diffInMilli = date2.getTime() - date1.getTime();
		}
		return diffInMilli;
	}

    public static long getDiffBetweenDates(Date currentDate, Date futureDate, TIME_UNIT timeUnit) {
        if(currentDate.after(futureDate)) {
            return 0;
        } else {
            long diffInMilli = getDiffInMilli(currentDate, futureDate);
            return convertMilliSecondsTo(diffInMilli, timeUnit);
        }
    }


}
