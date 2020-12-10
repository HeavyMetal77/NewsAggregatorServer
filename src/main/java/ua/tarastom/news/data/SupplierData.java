package ua.tarastom.news.data;

import ua.tarastom.news.model.Article;
import ua.tarastom.news.util.RSSFeedParser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

public class SupplierData {

    public static List<Article> loadRSS() {
        long start = System.currentTimeMillis();
        ExecutorService executorService = Executors.newCachedThreadPool();
        List<String> urls = new ArrayList<>();
        urls.add("https://ua.interfax.com.ua/news/last.rss"); //censor.net
        urls.add("https://censor.net/includes/news_ru.xml"); //censor.net
        urls.add("https://www.liga.net/news/all/rss.xml"); //liga.net
        urls.add("https://www.radiosvoboda.org/api/zii$p_ejg$py"); //radiosvoboda
        urls.add("https://nv.ua/ukr/rss/all.xml"); //НВ
        urls.add("https://www.5.ua/novyny/rss"); //5Channel
        urls.add("https://www.pravda.com.ua/rss"); //pravda.com.ua
        urls.add("http://k.img.com.ua/rss/ua/all_news2.0.xml"); //korrespondent.net
        urls.add("https://rss.dw.com/xml/rss-ru-all"); //Deutsche Welle
        urls.add("https://news.finance.ua/ua/rss"); //finance.ua - ua
        urls.add("https://news.finance.ua/ru/rss"); //finance.ua - ru
        urls.add("https://www.rbc.ua/static/rss/all.ukr.rss.xml"); //rbc.ua - ua
        urls.add("https://www.rbc.ua/static/rss/ukrnet.strong.rus.rss.xml"); //rbc.ua - ru
        urls.add("https://ukranews.com/rss-gen/Sub=2&Sub=3&Sub=4&Sub=5&Sub=6&Sub=7&Sub=8&Sub=9&Sub=10&Sub=599&Sub=610&Sub=611&Sub=612&Sub=614&Sub=615&Sub=616&Sub=617&Sub=618&Sub=619&Sub=620&Sub=621&Sub=622&Sub=623&Sub=624&Sub=625&Sub=626&Sub=627&Sub=628&Sub=629&Sub=630&Sub=631&Sub=632&Sub=633&Sub=718&Sub=719&Sub=720&Sub=721&Sub=722&Sub=723&Sub=724&Sub=725&Sub=726"); //
        urls.add("https://www.unn.com.ua/rss/news_uk.xml"); //unn - ua
        urls.add("https://www.unn.com.ua/rss/exclusive_uk.xml"); //unn exclusive - ua   --> https://www.unn.com.ua/uk/rss ще багато стрічок

        List<Article> articleList = new ArrayList<>();
            for (String url : urls) {
                try {
                    articleList.addAll(executorService.submit(() -> new RSSFeedParser(url).readFeed()).get());
                    Collections.sort(articleList);

                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                    System.out.println("Exception: " + url);
                }
            }
        executorService.shutdown();
        long end = System.currentTimeMillis();
        System.out.println(end-start);
        return articleList;
    }
}
