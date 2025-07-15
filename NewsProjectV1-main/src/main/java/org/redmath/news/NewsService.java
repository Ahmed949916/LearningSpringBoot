package org.redmath.news;

import jakarta.persistence.Id;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
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
        return repo.findById(id).orElse(null);
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
}

