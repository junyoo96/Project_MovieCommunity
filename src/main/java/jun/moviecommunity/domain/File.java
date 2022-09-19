package jun.moviecommunity.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "files")
@Getter @Setter
public class File {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private Long id;
    private String urlPath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    /**
     * 연관관계 편의 메소드
    **/
    public void setPost(Post post) {
        //이전 post 객체는 제거하고
        if(this.post != null) {
            this.post.getFiles().remove(this);
        }
        //새로운 post로 갱신
        this.post = post;
        post.getFiles().add(this);
    }

    /**
     * 생성 메소드
    **/
    public static File createFile(Post post, String fileUrlPath) {
        File file = new File();
        //TODO - S3에 실제 파일 저장
        file.setUrlPath(fileUrlPath);
        file.setPost(post);
        return file;
    }
}
