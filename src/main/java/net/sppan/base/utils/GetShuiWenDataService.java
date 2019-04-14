package net.sppan.base.utils;

import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import net.sppan.base.entity.pachong.Weather;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
public class GetShuiWenDataService {
    //陕西水文信息网址
    private static String url = "http://www.tianqihoubao.com/yubao/";
    //获取数据的地址
    private static String hedaoAction = ".html";



    public static List<Weather> getRiverInfoList (String cityname)throws Exception {
        String dataUrl=url+cityname+hedaoAction;
        Document doc = null;
        String VIEWSTATE="/wEPDwUKLTYzOTI4OTc1NA9kFgICAxBkZBYGZg8WAh4LXyFJdGVtQ291bnQCAxYGAgEPZBYCZg8VCwoyMDE5LTAzLTIyCnNob3dlci5naWYG6Zi16ZuoBumYtembqAUyNeKEgxbml6DmjIHnu63po47lkJEgMS0y57qnDHNob3dlcl8xLmdpZgbpmLXpm6gG6Zi16ZuoBTE34oSDFuaXoOaMgee7remjjuWQkSAxLTLnuqdkAgIPZBYCZg8VCwoyMDE5LTAzLTIzCnNob3dlci5naWYG6Zi16ZuoBumYtembqAUyMeKEgxDkuJzljJfpo44gMy0057qnDHNob3dlcl8xLmdpZgbpmLXpm6gG6Zi16ZuoBTE44oSDEOS4nOWMl+mjjiAzLTTnuqdkAgMPZBYCZg8VCwoyMDE5LTAzLTI0CnNob3dlci5naWYG6Zi16ZuoBumYtembqAUyMeKEgxbml6DmjIHnu63po47lkJEgMS0y57qnDHNob3dlcl8xLmdpZgbpmLXpm6gG6Zi16ZuoBTE44oSDFuaXoOaMgee7remjjuWQkSAxLTLnuqdkAgEPFgIfAAICFgRmD2QWAmYPFQQGemh1aGFpBuW5v+S4nAbnj6DmtbcG54+g5rW3ZAIBD2QWAmYPFQQGZG91bWVuBuW5v+S4nAbmlpfpl6gG5paX6ZeoZAICDxYCHwACBBYIZg9kFgJmDxUEBDExOTgy54+g5rW35pil6IqC5aSp5rCUXzIwMTflubTnj6DmtbfmmKXoioLlpKnmsJTpooTmiqUs54+g5rW35pil6IqC5aSp5rCUXzIwMTflubTnj6DmtbfmmKXoioLlpKnmsJQIMjAxNjEyMjNkAgEPZBYCZg8VBAM4NzMy54+g5rW35YWD5pem5aSp5rCUXzIwMTflubTnj6DmtbflhYPml6blpKnmsJTpooTmiqUs54+g5rW35YWD5pem5aSp5rCUXzIwMTflubTnj6DmtbflhYPml6blpKnmsJQIMjAxNjEyMjJkAgIPZBYCZg8VBAM1MzU15bm/5Lic54+g5rW36auY6ICD5aSp5rCUXzIwMTbnj6Dmtbfpq5jogIPlpKnmsJTpooTmiqUs5bm/5Lic54+g5rW36auY6ICD5aSp5rCUXzIwMTbnj6Dmtbfpq5jogIPlpKkIMjAxNjA1MjRkAgMPZBYCZg8VBAMxOTcy54+g5rW356uv5Y2I6IqC5aSp5rCUX+ePoOa1tzIwMTblubTnq6/ljYjoioLlpKnmsJQs54+g5rW356uv5Y2I6IqC5aSp5rCUX+ePoOa1tzIwMTblubTnq6/ljYjoioIIMjAxNjA1MDFkZL/h72Z9l+X77tsi+epCjXZfave4";
             Connection con = Jsoup.connect(dataUrl).userAgent("Netscape/5.0").timeout(3000);
                con.data("__VIEWSTATE",VIEWSTATE);
        long start = System.currentTimeMillis();

        try {
            con.timeout(50000);
            doc=con.post();
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            System.out.println("Time is:"+(System.currentTimeMillis()-start) + "ms");
        }


        VIEWSTATE=doc.getElementById("__VIEWSTATE").val();
            Elements elements = doc.getElementsByTag("tr");
            List<Weather> weatherList=new ArrayList<>();
            for (int j=1; j<elements.size();j++) {
                    if(j==7||j==8){
                         continue;
                    }else{
                        String oneday = elements.get(j).toString();
                        Weather onedaywea = getWeath(oneday);
                        weatherList.add(onedaywea);
                        continue;
                    }
            }
            return weatherList;
        }
    public static String setzhi(int i,String[] str1){
        String ss = str1[i].trim();
        int start = ss.indexOf("<td");
        int end = ss.indexOf(">");
        String s2 = ss.replace(ss.substring(start, end + 1), "").replace(" ", "").replace("<b>", "").replace("</b>", "");
        return s2;
    }
    public static String setname(int i,String[] str1){
        String ss=str1[i].trim();
        int start = ss.indexOf("<td");
        int end = ss.indexOf(">");
        String s2= ss.replace(ss.substring(start, end+1),"");
        int startimg = s2.indexOf("<img");
        int startend = s2.indexOf(">");
        String s3 = s2.replace(s2.substring(startimg, startend + 1), "");
        s3=s3.replace("<b>","").replace("</b>","").replace("&nbsp;","").replace(" ","");
        return s3;
    }

    public static Weather getWeath(String str){
        String[] str1 = str.replace("<tr>", "").replace("</tr>", "").replace("<td>", "").replace("<td bgcolor=\"#D8EDFF\">", "").split("</td>");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        Date date = new Date();
        String year = sdf.format(date);//取的当前年份，
        boolean hasyear=false;
        Weather newwea=new Weather();
        if(str1[0].indexOf(year)!=-1){
            //说明包含是奇数行，前面有日期，
            String ss=str1[0].trim();
            int start = ss.indexOf("<td");
            int end = ss.indexOf(">");
            String riqi= ss.replace(ss.substring(start, end+1),"").replace(" ", "");
            hasyear=true;
            //得到白天/晚上
        }

        if(hasyear==false) {
            Weather  weather= new Weather();
            for (int i = 0; i < str1.length-1; i++) {//遍历一行白天或者晚上的天气
                //说明前面没有日期，第二个遍历不一样，其他都一样
                if (i != 1) {
                    String zhi = setzhi(i, str1);
                    System.out.println("值： " + zhi);
                    if (i == 0) {
                        weather.setMornornight(zhi);
                        continue;
                    } else if (i == 2) {
                        weather.setWendu(zhi);
                        continue;
                    } else if(i==3){
                        weather.setFengli(zhi);
                        continue;
                    }
                }else {
                    //还有个概况不一样的。
                    String zhi = setname(i, str1);
                    System.out.println("值： " + zhi);
                    weather.setName(zhi);
                    continue;
                }
            }
            newwea=weather;
        }else{
            Weather  weather= new Weather();
            for (int i = 1; i < str1.length-1; i++) {//遍历一行白天或者晚上的天气.
                //说明前面有日期
                if (i != 2) {
                    String zhi = setzhi(i, str1);
                    System.out.println("值： " + zhi);

                    if (i == 1) {
                        weather.setMornornight(zhi);
                        continue;

                    } else if (i == 3) {
                        weather.setWendu(zhi);
                        continue;

                    } else {
                        weather.setFengli(zhi);
                        continue;
                    }
                }else {
                    //还有个概况不一样的。
                    String zhi = setname(i, str1);
                    System.out.println("值： " + zhi);
                    weather.setName(zhi);
                    continue;
                }
            }
            newwea=weather;
        }
        return newwea;
    }
}
