package org.redmath.news;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "news")
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "news_id")
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    private String title;

    @Column(name = "details", length = 1000)
    private String content;

    @Column(name = "reported_by")
    private String author;

    @Column(name = "reported_at")
    private LocalDateTime reportedAt;
}
