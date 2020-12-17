package ua.tarastom.news;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ua.tarastom.news.data.SupplierData;
import ua.tarastom.news.model.Article;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class LoadDatabase {

    private static final Logger logger = LoggerFactory.getLogger(LoadDatabase.class);
    private final int maxElementDB = 9500;
    private final int normElementDB = 8000;

    @Bean
    CommandLineRunner initDatabase(ArticleRepository articleRepository) {

        return args -> {
            articleRepository.deleteAll();
            while (true) {
                List<Article> articles = SupplierData.loadRSS();
                articles.sort((o1, o2) -> o2.getPubDate().compareTo(o1.getPubDate()));

                List<Article> allFromDB = articleRepository.findAll();
                List<Article> newAllFromDB = new ArrayList<>();

                if (allFromDB.size() > maxElementDB) {

                    allFromDB.sort((o1, o2) -> o2.getPubDate().compareTo(o1.getPubDate()));

                    newAllFromDB = allFromDB.stream().limit(normElementDB).collect(Collectors.toList());
                    List<Article> articleListForDelete = allFromDB.stream().skip(normElementDB).collect(Collectors.toList());
                    articleRepository.deleteAll(articleListForDelete);

                    LocalDateTime pubDate = newAllFromDB.get(newAllFromDB.size() - 1).getPubDate();
                    articles = articles.stream().filter((element) -> element.getPubDate().isAfter(pubDate)).collect(Collectors.toList());
                }
                Collection<Article> subtract = CollectionUtils.subtract(articles, newAllFromDB);
                articleRepository.saveAll(subtract);
                Thread.sleep(3000);
            }
        };
    }
}