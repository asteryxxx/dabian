package net.sppan.base.pachongJD;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainSpider {
    private static String HTTPS_PROTOCOL = "https:";
    public static void main(String[] args){
        Date startDate = new Date();
        // 使用现线程池提交任务
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        //引入countDownLatch进行线程同步，使主线程等待线程池的所有任务结束，便于计时
        final CountDownLatch countDownLatch = new CountDownLatch(100);
        for(int i = 1; i < 201; i += 2) {
            final Map<String, String> params = Maps.newHashMap();
            params.put("keyword", "零食");
            params.put("enc", "utf-8");
            params.put("wc", "零食");
            params.put("page", i + "");
            executorService.submit(new Runnable(){

                public void run() {
                    spiderData(SysConstant.BASE_URL, params);
                    countDownLatch.countDown();
                }
            });
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executorService.shutdown();
        Date endDate = new Date();

        FastDateFormat fdf = FastDateFormat.getInstance(SysConstant.DEFAULT_DATE_FORMAT);
        System.out.println("爬虫结束....");
        System.out.println("[开始时间:" + fdf.format(startDate) + ",结束时间:" + fdf.format(endDate) + ",耗时:"
                + (endDate.getTime() - startDate.getTime()) + "ms]");
    }
    public static void spiderData(String url, Map<String, String> params) {
        String html = HttpClientUtils.sendGet(url, null, params);
        if(!StringUtils.isBlank(html)) {
            List<GoodsInfo> goodsInfos =parseHtml(html);
           // goodsInfoDao.saveBatch(goodsInfos);
        }
    }
    /**
     * 解析html
     * @param html
     */
    public static List<GoodsInfo> parseHtml(String html) {
        //商品集合
        List<GoodsInfo> goods = Lists.newArrayList();
        /**
         * 获取dom并解析
         */
        Document document = Jsoup.parse(html);
        Elements elements = document.
                select("ul[class=gl-warp clearfix]").select("li[class=gl-item]");
        int index = 0;
        for(Element element : elements) {
            String goodsId = element.attr("data-sku");
            String goodsName = element.select("div[class=p-name p-name-type-2]").select("em").text();
            String goodsPrice = element.select("div[class=p-price]").select("strong").select("i").text();
            String imgUrl = HTTPS_PROTOCOL + element.select("div[class=p-img]").select("a").select("img").attr("src");
            GoodsInfo goodsInfo = new GoodsInfo(goodsId, goodsName, imgUrl, goodsPrice);
            goods.add(goodsInfo);
         //   String jsonStr = JSON.toJSONString(goodsInfo);
           System.out.println("成功爬取【" + goodsName + "】的基本信息 ");
        }
        return goods;
    }
}
