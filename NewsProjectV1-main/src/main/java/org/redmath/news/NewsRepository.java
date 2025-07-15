package org.redmath.news;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.List;


@Repository
public interface NewsRepository extends JpaRepository<News,Long> {
    Page<News> findByTitleContainingIgnoreCase(String title, Pageable pageable);

}
