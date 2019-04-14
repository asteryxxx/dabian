package net.sppan.base.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SimpleDateutils {
    private static ThreadLocal<DateFormat> threadLocal=new ThreadLocal<DateFormat>(){
        protected DateFormat initialValue(){

            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };
    public  static Date parse (String dateStr)throws ParseException{
        return threadLocal.get().parse(dateStr);
    }

    public static String format(Date date){
        return threadLocal.get().format(date);
    }

}
