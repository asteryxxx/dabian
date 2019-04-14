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
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class wormCore {
    private static volatile Catch cc = new Catch();
    private static volatile Analysis analysis = new Analysis();
    //数据处理层对象
    public static volatile Access access = new Access();

    public void Wormcore(LinkedBlockingQueue<String> UrlQueue) throws IOException, InterruptedException {
        synchronized (this){
            if(!UrlQueue.isEmpty()){
                String url = UrlQueue.take();

                //通过Url队列中的Url抓取Document，进行Url和文本信息的抓取
                Document document = cc.CatchDocument(url);
                //数据解析模块返回的数据（含有文本信息以及URL）
                HashMap<String, ArrayList<String>> DataMap = analysis.AnalysisDocument(document, url);
                //数据处理模块分离出的、只含有URL的集合
                ArrayList<String> UrlList = access.DataAccess(DataMap);

                //定义迭代器，把抓取到的Url添加到Url队列中
                Iterator<String> iterator = UrlList.iterator();


                while (iterator.hasNext()) {
                    UrlQueue.put(iterator.next());
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
    }
}
class Catch {
    //根据网页的Url获取网页Document
    public Document CatchDocument(String Url) throws IOException {
        try {
            return Jsoup.connect(Url)
                    .userAgent("Mozilla/4.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0)").timeout(5000).get();
            //如果出现了超时问题就继续抓取
        }catch (SocketTimeoutException s){
            return Jsoup.connect(Url)
                    .userAgent("Mozilla/4.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0)").timeout(5000).get();
        }
    }
}
class Analysis {

    //根据Document解析网页
    public HashMap<String, ArrayList<String>> AnalysisDocument(Document document, String Url){
        //因为网页上的URL为相对地址，所以在这里进行URL的拼接，这是前半部分
        String Before_Url = Url.substring(0, Url.lastIndexOf("/") + 1);

        //储存文本信息的List
        ArrayList<String> Text = new ArrayList<>();
        //储存Url的List
        ArrayList<String> Urls = new ArrayList<>();

        HashMap<String,ArrayList<String>> Message = new HashMap<>();
        //最后一个页面的前三个文本不是我们想要的
        int Flag = 1;

        Elements elements = document.select("tr[class]").select("a[href]");
        //最后一个页面的处理
        if(elements.isEmpty()){
            elements = document.select("tr[class]").select("td");
            for (Element element : elements) {
                if (!IsNumber(element.text()) && Flag > 3) {
                    System.out.println(element.text());
                }
                Flag++;
            }
            //普通页面的处理
        }else {
            for (Element element : elements) {
                if (!IsNumber(element.text())) {
                    Text.add(element.text());
                    System.out.println(element.text());
                    Urls.add(Before_Url + element.attr("href"));
                }
            }
        }
        //把文本集合和URL集合装到Map中返回
        Message.put("text",Text);
        Message.put("Url",Urls);
        return Message;

    }

    public boolean IsNumber(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }

}

 class Access {
    //数据处理，把信息中的Url返回给核心，文本信息储存
    public ArrayList<String> DataAccess(HashMap<String, ArrayList<String>> Message){
        return Message.get("Url");
    }
}
