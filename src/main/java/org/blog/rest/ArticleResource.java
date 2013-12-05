package org.blog.rest;

import com.google.common.base.Optional;
import org.blog.domain.Article;
import org.blog.persistence.ArticleRepository;
import restx.Status;
import restx.annotations.*;
import restx.factory.Component;
import restx.security.PermitAll;

import static restx.common.MorePreconditions.checkEquals;

@Component @RestxResource
public class ArticleResource {

    private ArticleRepository articleRepository;

    public ArticleResource(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @PermitAll
    @GET("/articles")
    public Iterable<Article> findArticles(Optional<String> title) {
        return articleRepository.findArticles(title);
    }

    @POST("/articles")
    public Article createArticle(Article article) {
        return articleRepository.createArticle(article);
    }

    @GET("/articles/{oid}")
    public Optional<Article> findArticleById(String oid) {
        return articleRepository.findArticleById(oid);
    }

    @PUT("/articles/{oid}")
    public Article updateArticle(String oid, Article article) {
        checkEquals("oid", oid, "article.key", article.getKey());
        return articleRepository.updateArticle(article);
    }

    @DELETE("/articles/{oid}")
    public Status deleteArticle(String oid) {
        articleRepository.deleteArticle(oid);
        return Status.of("deleted");
    }
}
