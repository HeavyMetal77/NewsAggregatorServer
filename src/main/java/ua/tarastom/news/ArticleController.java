package ua.tarastom.news;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;
import ua.tarastom.news.model.Article;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class ArticleController {

    private ArticleRepository articleRepository;
    private ArticleModelAssembler articleModelAssembler;

    public ArticleController(ArticleRepository articleRepository, ArticleModelAssembler articleModelAssembler) {
        this.articleRepository = articleRepository;
        this.articleModelAssembler = articleModelAssembler;
    }

//    @PostMapping("/articles")
//    Article newArticle(@RequestBody Article newArticle) {
//        return articleRepository.save(newArticle);
//    }
//
//    @PutMapping("/articles/{id}")
//    Article replaceArticle(@RequestBody Article newArticle, Long id) {
//        return articleRepository.findById(id).map(article -> {
//            article.setLink(newArticle.getLink());
//            article.setTitle(newArticle.getTitle());
//            return articleRepository.save(article);
//        }).orElseGet(() ->
//        {
//            newArticle.setId(id);
//            return articleRepository.save(newArticle);
//        });
//    }

//    @DeleteMapping("/articles/{id}")
//    void deleteArticle(@PathVariable Long id) {
//        articleRepository.deleteById(id);
//    }

    @GetMapping("/articles/{id}")
    EntityModel<Article> one(@PathVariable Long id) {
        Article article = articleRepository.findById(id).orElseThrow(() -> new ArticleNotFoundException(id));
        return articleModelAssembler.toModel(article);
    }

    @GetMapping("/articles")
    CollectionModel<EntityModel<Article>> all() {
        List<EntityModel<Article>> articles = articleRepository.findAll().stream()
                .map(articleModelAssembler::toModel).collect(Collectors.toList());
        return CollectionModel.of(articles, linkTo(methodOn(ArticleController.class).all()).withSelfRel());
    }

}
