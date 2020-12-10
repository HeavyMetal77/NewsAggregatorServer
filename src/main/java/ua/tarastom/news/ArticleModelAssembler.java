package ua.tarastom.news;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import ua.tarastom.news.model.Article;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ArticleModelAssembler implements RepresentationModelAssembler<Article, EntityModel<Article>> {
    @Override
    public EntityModel<Article> toModel(Article article) {
        return EntityModel.of(article,
                linkTo(methodOn(ArticleController.class).one(article.getId())).withSelfRel(),
                linkTo(methodOn(ArticleController.class).all()).withRel("articles"));
    }
}
