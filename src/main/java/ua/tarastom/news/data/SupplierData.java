package ua.tarastom.news.data;

import ua.tarastom.news.model.Article;
import ua.tarastom.news.util.RSSFeedParser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SupplierData {

    public static List<Article> loadRSS() {
        long start = System.currentTimeMillis();
        ExecutorService executorService = Executors.newFixedThreadPool(8);
        List<String> urls = new ArrayList<>();
        urls.add("https://ua.interfax.com.ua/news/last.rss"); //interfax
//        urls.add("https://censor.net/includes/news_ru.xml"); //censor.net
        urls.add("https://www.liga.net/news/all/rss.xml"); //liga.net
        urls.add("https://www.radiosvoboda.org/api/zii$p_ejg$py"); //radiosvoboda
//        urls.add("https://nv.ua/ukr/rss/all.xml"); //НВ
        urls.add("https://www.5.ua/novyny/rss"); //5Channel
        urls.add("https://www.pravda.com.ua/rss"); //pravda.com.ua
//        urls.add("http://k.img.com.ua/rss/ua/all_news2.0.xml"); //korrespondent.net
        urls.add("https://rss.dw.com/xml/rss-ru-all"); //Deutsche Welle
//        urls.add("https://news.finance.ua/ua/rss"); //finance.ua - ua
//        urls.add("https://news.finance.ua/ru/rss"); //finance.ua - ru
        urls.add("https://www.rbc.ua/static/rss/all.ukr.rss.xml"); //rbc.ua - ua
//        urls.add("https://www.rbc.ua/static/rss/ukrnet.strong.rus.rss.xml"); //rbc.ua - ru
//        urls.add("https://ukranews.com/rss-gen/Sub=2&Sub=3&Sub=4&Sub=5&Sub=6&Sub=7&Sub=8&Sub=9&Sub=10&Sub=599&Sub=610&Sub=611&Sub=612&Sub=614&Sub=615&Sub=616&Sub=617&Sub=618&Sub=619&Sub=620&Sub=621&Sub=622&Sub=623&Sub=624&Sub=625&Sub=626&Sub=627&Sub=628&Sub=629&Sub=630&Sub=631&Sub=632&Sub=633&Sub=718&Sub=719&Sub=720&Sub=721&Sub=722&Sub=723&Sub=724&Sub=725&Sub=726"); //
        urls.add("https://www.unn.com.ua/rss/news_uk.xml"); //unn - ua
//        urls.add("https://www.unn.com.ua/rss/exclusive_uk.xml"); //unn exclusive - ua   --> https://www.unn.com.ua/uk/rss ще багато стрічок
        urls.add("https://24tv.ua/rss/all.xml"); //24tv - ua
//        urls.add("https://24tv.ua/rss/all.xml?lang=ru"); //24tv - ru
        urls.add("https://suspilne.media/rss/ukrnet.rss"); //suspilne - ua
        urls.add("https://hvylya.net/feed/rss2.xml"); //hvylya - ru
//        urls.add("https://gordonua.com/xml/rss_category/top.html"); //gordonua -ru   https://gordonua.com/rsslist.html ще багато стрічок
//        urls.add("https://gordonua.com/ukr/xml/rss_category/top.html"); //gordonua -ua   https://gordonua.com/rsslist.html ще багато стрічок
        urls.add("https://fakty.com.ua/ua/feed/"); //ictv -ua
//        urls.add("https://tsn.ua/rss/full.rss"); //tsn -ua     ще багато стрічок
//        urls.add("https://risu.ua/rss.xml"); //risu -ua
        urls.add("https://sundries.com.ua/feed/"); //sundries -ua
        urls.add("https://rss.unian.net/site/news_ukr.rss"); //unian -ua
//        urls.add("https://enovosty.com/rss/rss.xml"); //Экономические Новости - ua-ru
//        urls.add("https://telegraf.com.ua/yandex-feed/"); //telegraf - ru
        urls.add("https://telegraf.com.ua/ukr/yandex-feed/"); //telegraf - ua
//        urls.add("https://golos.ua/feed/"); //golos - ua
        urls.add("https://ua.krymr.com/api/zukopvepmipt"); //krymr - ua  https://ua.krymr.com/rssfeeds ще багато стрічок
//        urls.add("https://ru.krymr.com/api/zgtopme_iip_"); //krymr - ru  https://ua.krymr.com/rssfeeds ще багато стрічок
        urls.add("http://nashigroshi.org/feed/"); //nashigroshi -  ua
        urls.add("https://focus.ua/modules/rss.php"); //focus -  ua
//        urls.add("https://www.ostro.org/rss/"); //ostro -  ua
//
//        urls.add("http://treebuna.info/feed"); //treebuna - ru - Odesa
//        urls.add("http://1tv.od.ua/news.rss"); //Первый городской - ru - Odesa

        List<Article> articleList = new ArrayList<>();
        for (String url : urls) {
            try {
                articleList.addAll(executorService.submit(() -> new RSSFeedParser(url, "ua").readFeed()).get());
                Collections.sort(articleList);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                System.out.println("Exception: " + url);
            }
        }
        executorService.shutdown();
        long end = System.currentTimeMillis();
        System.out.println(end - start);
        return articleList;
    }
}
//https://news.google.com/
//https://bykvu.com/ua/
//https://itech.co.ua/
//https://odessamedia.net/
//https://cikavosti.com/
//https://newsyou.info/