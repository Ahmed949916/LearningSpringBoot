package org.redmath.news;

import jakarta.persistence.Id;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
@Service
public class NewsService {

    private final NewsRepository repo;

    public NewsService(NewsRepository repo){
        this.repo=repo;
    }

    public List<News> findAll() {
        return repo.findAll();
    }

    public News create(News news) {
        return repo.save(news);
    }

    public News findNewsById(Long id) {
        return repo.findById(id).orElseThrow();
    }

    public Boolean DeleteNewsById(Long id) {
            if (repo.existsById(id)) {
                repo.deleteById(id);
                return true;
            } else {
                return false;
            }
        }

    public Page<News> find(String title, Pageable pageable) {
      return repo.findByTitleContainingIgnoreCase(title,pageable);
    }

    @Transactional
    public News updateNews(Long id, News news) {
        News fetchedNews = repo.findById(id)
                .orElseThrow();


        if (news.getAuthor() != null ) {
            fetchedNews.setAuthor(news.getAuthor());
        }
        if (news.getContent() != null ) {
            fetchedNews.setContent(news.getContent());
        }
        if (news.getTitle() != null  ) {
            fetchedNews.setTitle(news.getTitle());
        }

        return repo.save(fetchedNews);
    }

}

