package jun.moviecommunity.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "files")
@Getter @Setter
public class File {

    @Id @GeneratedValue
    @Column(name = "file_id")
    private Long id;
    private String urlPath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;
}
