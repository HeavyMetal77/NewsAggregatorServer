package ua.tarastom.news;

import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import ua.tarastom.news.model.Article;

import java.util.List;

@Service
public class ArticleService {
    private ArticleRepository repository;

    private ArticleService(ArticleRepository repository) {
        this.repository = repository;
    }

    public List<Article> getMatchingArticles(@RequestBody Article articleFilter) {
        return repository.findAll(Example.of(articleFilter));
    }
}
