package ua.tarastom.news;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.tarastom.news.model.Article;

public interface ArticleRepository extends JpaRepository<Article, Long> {
}
