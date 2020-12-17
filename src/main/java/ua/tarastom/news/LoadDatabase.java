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
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class LoadDatabase {

    private static Logger logger = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(ArticleRepository articleRepository) {

        return args -> {
            while (true) {
                List<Article> articles = SupplierData.loadRSS();
                List<Article> all = articleRepository.findAll();
                if (all.size() > 9800) {
                    all = all.stream().sorted().limit(8000).collect(Collectors.toList());
                    LocalDateTime pubDate = all.get(all.size() - 1).getPubDate();
                    articles = articles.stream().filter((element)-> element.getPubDate().isAfter(pubDate)).collect(Collectors.toList());
                }
                Collection<Article> subtract = CollectionUtils.subtract(articles, all);
                articleRepository.saveAll(subtract);
                Thread.sleep(20000);
            }
        };
    }
}