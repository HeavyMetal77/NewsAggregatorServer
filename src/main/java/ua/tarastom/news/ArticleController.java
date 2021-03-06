package ua.tarastom.news;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ua.tarastom.news.model.Article;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class ArticleController {

    private final ArticleRepository articleRepository;
    private final ArticleModelAssembler articleModelAssembler;
    private final ArticleService articleService;

    public ArticleController(ArticleRepository articleRepository, ArticleModelAssembler articleModelAssembler, ArticleService articleService) {
        this.articleRepository = articleRepository;
        this.articleModelAssembler = articleModelAssembler;
        this.articleService = articleService;
    }

    @GetMapping("/articles/article/{id}")
    EntityModel<Article> one(@PathVariable Long id) {
        Article article = articleRepository.findById(id).orElseThrow(() -> new ArticleNotFoundException(id));
        System.out.println(article);
        return articleModelAssembler.toModel(article);
    }

    @GetMapping("/articles/all")
    @Transactional
    CollectionModel<EntityModel<Article>> all() {
        return  all("", "", "", "", "", 0, 30);
//
//        Pageable pageable = PageRequest.of(0, 30);
//        List<EntityModel<Article>> articles = articleRepository.findAll(pageable).stream()
//                .map(articleModelAssembler::toModel)
//                .collect(Collectors.toList());
//        return CollectionModel.of(articles, linkTo(methodOn(ArticleController.class).all()).withSelfRel());
    }

    @GetMapping("/articles/find")
    @Transactional
    public CollectionModel<EntityModel<Article>> all(
            @RequestParam(required = false) String key,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String language,
            @RequestParam(required = false) String titleChannel,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size
    ) {
        Article articleFilter = new Article();
        if (!country.isEmpty()) {
            articleFilter.setCountry(country);
        }
        if (!category.isEmpty()) {
            List<String> categories = new ArrayList<>();
            categories.add(category);
            articleFilter.setCategory(categories);
        }
        if (!language.isEmpty()) {
            articleFilter.setLanguage(language);
        }
        if (!titleChannel.isEmpty()) {
            articleFilter.setTitleChannel(titleChannel);
        }

        List<Article> matchingArticles = articleService.getMatchingArticles(articleFilter);
        matchingArticles.sort(Comparator.naturalOrder());
//        matchingArticles.sort((o1, o2) -> o2.getPubDate().compareTo(o1.getPubDate()));
        List<EntityModel<Article>> entityModels = matchingArticles.stream().filter(article -> {
            if (!category.isEmpty()) {
                if (!article.getCategory().contains(category.trim())) {
                    return false;
                }
            }
            if (key != null && !key.isEmpty()) {
                String searchLine = article.getTitle() + " "
                        + article.getDescription() + " "
                        + article.getFull_text() + " "
                        + article.getTitleChannel();
                return StringUtils.containsIgnoreCase(searchLine, key.trim());
            }
            return true;
        }).map(articleModelAssembler::toModel).collect(Collectors.toList());

        MutableSortDefinition mutableSortDefinition = new MutableSortDefinition();
        PagedListHolder<EntityModel<Article>> entityModelPagedListHolder = new PagedListHolder<>(entityModels, mutableSortDefinition);
        entityModelPagedListHolder.setPageSize(size);
        entityModelPagedListHolder.setPage(page);
        entityModelPagedListHolder.getPageCount();
        List<EntityModel<Article>> pageList = entityModelPagedListHolder.getPageList();
        return CollectionModel.of(pageList, linkTo(methodOn(ArticleController.class).all()).withSelfRel());
    }
}
