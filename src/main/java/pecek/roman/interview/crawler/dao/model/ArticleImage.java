package pecek.roman.interview.crawler.dao.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;


/**
 * Entity representing data in database table article_image
 */
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "article_image")
public class ArticleImage implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column(name = "article", nullable = false)
    private String article;

    @NonNull
    @Column(name = "image_path")
    private String imagePath;

}
