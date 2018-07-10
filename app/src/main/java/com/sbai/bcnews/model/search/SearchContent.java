package com.sbai.bcnews.model.search;

import com.sbai.bcnews.model.NewsFlash;
import com.sbai.bcnews.model.author.Author;
import com.sbai.bcnews.model.author.AuthorArticle;

import java.util.List;

/**
 * Modified by $nishuideyu$ on 2018/7/10
 * <p>
 * Description:
 * </p>
 * APIS:{@link com.sbai.bcnews.http.Apic#requestSearchContent}
 */
public class SearchContent {

    private List<Author> author;
    private List<NewsFlash> information;
    private List<AuthorArticle> bitcoin;

    public List<Author> getAuthor() {
        return author;
    }

    public void setAuthor(List<Author> author) {
        this.author = author;
    }

    public List<NewsFlash> getInformation() {
        return information;
    }

    public void setInformation(List<NewsFlash> information) {
        this.information = information;
    }

    public List<AuthorArticle> getBitcoin() {
        return bitcoin;
    }

    public void setBitcoin(List<AuthorArticle> bitcoin) {
        this.bitcoin = bitcoin;
    }
}
