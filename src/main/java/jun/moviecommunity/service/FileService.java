package jun.moviecommunity.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import jun.moviecommunity.domain.File;
import jun.moviecommunity.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;

    public List<File> findFilesByFilePaths(List<String> filePaths) {
        return fileRepository.findByFilePaths(filePaths);
    }

    public List<File> findFilesByPostId(Long postId) {
        return fileRepository.findAllByPostId(postId);
    }

    /**
     * 파일 등록
     **/
    @Transactional
    public Long save(String fileName, String filePath) {
        File file = File.createFile(fileName, filePath);
        fileRepository.save(file);
        return file.getId();
    }

    /**
     * html에서 img 태그의 이미지파일 경로만 추출
     **/
    public List<String> getFilePathsFromContent(String content) {
        Pattern pattern = Pattern.compile("<img[^>]*src=[\\\"']?([^>\\\"']+)[\\\"']?[^>]*>");
        Matcher matcher = pattern.matcher(content);

        List<String> filePaths = new ArrayList<>();
        while (matcher.find()) {
            filePaths.add(matcher.group(1));
        }

        return filePaths;
    }

    @Transactional
    public void deletePostByIds(List<Long> removeFileIds) {
        fileRepository.deleteAllByIdInBatch(removeFileIds);
    }
}
