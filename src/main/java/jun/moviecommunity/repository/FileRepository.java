package jun.moviecommunity.repository;

import jun.moviecommunity.domain.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface FileRepository extends JpaRepository<File, Long> {

    @Query("select f from File f where f.filePath in :filePaths")
    List<File> findByFilePaths(@Param("filePaths") Collection<String> filePaths);

    @Query("select f from File f where f.post.id = :postId")
    List<File> findAllByPostId(@Param("postId") Long postId);

//    @Query("delete from File f where f.id in :ids")
//    void deleteAllByIdIn(@Param("ids") List<Long> ids);
}
