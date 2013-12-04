package org.blog.persistence;

import com.google.common.base.Optional;
import org.blog.domain.Article;
import org.bson.types.ObjectId;
import restx.factory.Component;
import restx.jongo.JongoCollection;

import javax.inject.Named;

@Component
public class ArticleRepository {

    private final JongoCollection articles;

    public ArticleRepository(@Named("articles") JongoCollection articles) {
        this.articles = articles;
    }

    public Iterable<Article> findArticles(Optional<String> name) {
        if (name.isPresent()) {
            return articles.get().find("{name: #}", name.get()).as(Article.class);
        } else {
            return articles.get().find().as(Article.class);
        }
    }

    public Article createArticle(Article article) {
        articles.get().save(article);
        return article;
    }

    public Optional<Article> findArticleById(String oid) {
        return Optional.fromNullable(articles.get().findOne(new ObjectId(oid)).as(Article.class));
    }

    public Article updateArticle(Article article) {
        articles.get().save(article);
        return article;
    }

    public void deleteArticle(String oid) {
        articles.get().remove(new ObjectId(oid));
    }

}
