package net.sppan.base.pachong;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;


import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class App {
    public static void main(String[] args) {

        // 确定目标地址 URL 统一资源定位符
        String url="https://book.douban.com/";

        // 2 解析 html ：  https：//jsoup.org
        try {
            //
            long startTime=System.currentTimeMillis();   //获取开始时间
            Document doc = Jsoup.connect(url).get();
            long endTime=System.currentTimeMillis(); //获取结束时间
            System.out.println("程序运行时间： "+(endTime-startTime)+"ms");

            //从 Doc 的树形结构中查找 img 标签
            //.class 选择器
            Elements els = doc.select(".cover img");
            System.out.println(els.size());


            // 创建一个线程池
            //.class 选择器
            ExecutorService pool =  Executors.newFixedThreadPool(9);
            for(Element e : els) {
                String src = e.attr("src");
                System.out.println(src);
                // 下载每张图片
                pool.execute(new DownloadTask(src));
            }
            //释放资源
            pool.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
