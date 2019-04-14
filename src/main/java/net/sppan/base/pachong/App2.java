package net.sppan.base.pachong;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.sppan.base.pachong.App2.IsNumber;
import static net.sppan.base.pachong.App2.UrlQueue;

public class App2 {
    public static final LinkedBlockingQueue<String> UrlQueue=new LinkedBlockingQueue<>();
    public static void main(String[] args) {
        // 确定目标地址 URL 统一资源定位符
        String url="http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2017/44/03/440304.html";
        Runnable runnable=new MyRunnable();
        ExecutorService fixed = Executors.newFixedThreadPool(10);
        UrlQueue.offer(url);
         long startTime = System.currentTimeMillis();   //获取开始时间
        System.out.println("begin "+startTime);
        for (int i = 0; i < 10; i++) {
            fixed.submit(runnable);
        }
        fixed.shutdown();

    }

    public static boolean IsNumber(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }
}
class MyRunnable implements Runnable{

    public void run() {
        while (true){
            try {
                Thread.sleep(200);
                synchronized (this) {
                    if (!UrlQueue.isEmpty()) {
                        String url = UrlQueue.take();
                        System.out.println("取出Url---"+url);
                        Document doc = Jsoup.connect(url)
                                .timeout(500000).get();
                        //     HashMap<String, ArrayList<String>> DataMap= AnalysisDocument(url, doc);
                        //数据处理模块分离出的、只含有URL的集合
            /*    System.out.println("map的url----"+DataMap.get("Url").size());

                ArrayList<String> UrlList=DataAccess(DataMap);
                //定义迭代器，把抓取到的Url添加到Url队列中
                Iterator<String> iterator = UrlList.iterator();
*/
                        ArrayList<String> UrlList= AnalysisDocument(url, doc);
                        for (String urltemp:UrlList
                                ) {
                            System.out.println("/// "+urltemp);
                            UrlQueue.put(urltemp);

                        }

                        //打印URL队列中的URL条数以及队列是否为空
                        System.out.println(UrlQueue.size());
                        System.out.println(UrlQueue.isEmpty());
                        //为空说明爬取完毕，由于个人技术问题，在抓取完毕之后只能强制退出
                        if (UrlQueue.isEmpty()) {
                            System.out.println("抓取完毕！");
                            System.exit(1);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void maindo(LinkedBlockingQueue<String> UrlQueue)throws Exception{

    }



    private ArrayList<String> DataAccess(HashMap<String, ArrayList<String>> Message) {
        //数据处理，把信息中的Url返回给核心，文本信息储存
            return Message.get("Url");
    }

    private  ArrayList<String> AnalysisDocument(String url, Document doc) {
        //从 Doc 的树形结构中查找 img 标签
        //.class 选择器

        Elements document = doc.select(".towntr a");
        //因为网页上的URL为相对地址，所以在这里进行URL的拼接，这是前半部分
        String Before_Url = url.substring(0, url.lastIndexOf("/") + 1);


        //储存Url的List
        ArrayList<String> Urls = new ArrayList<>();

        HashMap<String, ArrayList<String>> Message = new HashMap<>();
        //最后一个页面的前三个文本不是我们想要的
        int Flag = 1;
        HashMap<String,ArrayList<String>> mess = new HashMap<>();

        //最后一个页面的处理
        if (document.isEmpty()) {
            document = doc.select(".villagetr td");
            System.out.println(url+" 到尾巴了 ");
            for (Element element : document) {
                if (!IsNumber(element.text()) && Flag >= 3) {
                    System.out.println(element.text());
                }
                Flag++;
            }
            //普通页面的处理
        } else {
            for (Element element : document) {
                if (!IsNumber(element.text())) {
                    String zhi = element.text();
                  //  Text.add(zhi);
                    System.out.println(" read "+zhi);
                    String valueurl = Before_Url + element.attr("href");
                    Urls.add(valueurl);
                }
            }
        }
       // System.out.println("Url "+Urls.size());

        //把文本集合和URL集合装到Map中返回
       // Message.put("text", Text);
        //Message.put("Url", Urls);
        return Urls;
    }
}