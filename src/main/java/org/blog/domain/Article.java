package org.blog.domain;


import com.google.common.base.Objects;
import org.jongo.marshall.jackson.oid.Id;
import org.jongo.marshall.jackson.oid.ObjectId;

public class Article {

    @Id
    @ObjectId
    private String key;
    private String title;
    private String content;

    public String getKey() {
        return key;
    }

    public Article setKey(final String key) {
        this.key = key;
        return this;
    }


    public String getTitle() {
        return title;
    }

    public Article setTitle(final String title) {
        this.title = title;
        return this;
    }

    public String getContent() {
        return content;
    }

    public Article setContent(String content) {
        this.content = content;
        return this;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("key", key)
                .add("title", title)
                .add("content", content)
                .toString();
    }
}
