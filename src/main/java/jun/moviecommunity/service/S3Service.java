package jun.moviecommunity.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class S3Service {

    //Amazon S3
    private final AmazonS3Client amazonS3Client;
    //S3 Bucket 이름
    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;
    @Value("${cloud.aws.region.static}")
    private String region;

    @Transactional
    public Map<String, String> upload(InputStream inputStream, FileDto fileDto) {
        List<String> imagePathList = new ArrayList<>();

        String fileName = fileDto.getFileName();

        ObjectMetadata objectMetaData = new ObjectMetadata();
        objectMetaData.setContentType(fileDto.getFileType());
        objectMetaData.setContentLength(Long.parseLong(fileDto.getFileSize()));

        // S3에 업로드
        amazonS3Client.putObject(
                new PutObjectRequest(bucketName, fileName, inputStream, objectMetaData)
                        .withCannedAcl(CannedAccessControlList.PublicRead)
        );

        String filePath = amazonS3Client.getUrl(bucketName, fileName).toString(); // 접근가능한 URL 가져오기
        log.info("aws3에서 준 keyname" + filePath);
        imagePathList.add(filePath);

        ///////////////// 이미지 /////////////////
        String sFileInfo = "";

        // 정보 출력
        sFileInfo += "&bNewLine=true";
        // img 태그의 title 속성을 원본파일명으로 적용시켜주기 위함
        sFileInfo += "&sFileName="+ fileName;
        //여기서 이미지 저장 경로 설정(S3)
        sFileInfo += "&sFileURL="+filePath;

        Map<String, String> fileInfo = new HashMap<String, String>();
        fileInfo.put("sFileInfo", sFileInfo);
        fileInfo.put("filePath", filePath);

        return fileInfo;
    }

    public void delete(List<String> fileNames) {
        try {
            ArrayList<DeleteObjectsRequest.KeyVersion> keys = new ArrayList<DeleteObjectsRequest.KeyVersion>();

            for (String fileName : fileNames) {
                log.info("삭제할때갖고온이름 : " + fileName);
                keys.add(new DeleteObjectsRequest.KeyVersion(fileName));
            }

            DeleteObjectsRequest multiObjectDeleteRequest = new DeleteObjectsRequest(bucketName)
                    .withKeys(keys)
                    .withQuiet(false);
            DeleteObjectsResult delObjRes = amazonS3Client.deleteObjects(multiObjectDeleteRequest);
            int successfulDeletes = delObjRes.getDeletedObjects().size();
            log.info(successfulDeletes + " objects successfully deleted.");
        } catch (AmazonServiceException e) {
            // The call was transmitted successfully, but Amazon S3 couldn't process
            // it, so it returned an error response.
            e.printStackTrace();
        } catch (SdkClientException e) {
            // Amazon S3 couldn't be contacted for a response, or the client
            // couldn't parse the response from Amazon S3.
            e.printStackTrace();
        }
    }
}
