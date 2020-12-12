package ua.tarastom.news;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ua.tarastom.news.data.SupplierData;
import ua.tarastom.news.model.Article;

import java.util.Collection;
import java.util.List;

@Configuration
public class LoadDatabase {

    private static Logger logger = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(ArticleRepository articleRepository) {

        return args -> {
            while (true) {
                List<Article> articles = SupplierData.loadRSS();
                List<Article> all = articleRepository.findAll();
                Collection<Article> subtract = CollectionUtils.subtract(articles, all);
                articleRepository.saveAll(subtract);
                Thread.sleep(300000);
            }
        };
    }
}