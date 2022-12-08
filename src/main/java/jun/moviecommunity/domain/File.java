package jun.moviecommunity.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "files")
@Getter @Setter
public class File extends BaseTimeEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private Long id;

    @Column(length=1000)
    private String fileName;
    @Column(length=1000)
    private String filePath;

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
    public static File createFile(String fileName, String filePath) {
        File file = new File();
        file.setFileName(fileName);
        file.setFilePath(filePath);
        return file;
    }
}
