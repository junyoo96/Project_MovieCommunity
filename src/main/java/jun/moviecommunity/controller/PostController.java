package jun.moviecommunity.controller;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import jun.moviecommunity.domain.Category;
import jun.moviecommunity.domain.Post;
import jun.moviecommunity.service.CreatePostRequest;
import jun.moviecommunity.service.PostDto;
import jun.moviecommunity.service.PostService;
import jun.moviecommunity.service.UpdatePostRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PostController {

    private final PostService postService;
    private final int pagingSize = 2;

    //Amazon S3관련
    private final AmazonS3Client amazonS3Client;
    private String S3Bucket = "";

    /**
     * 게시물 등록 페이지
    **/
    @GetMapping("/posts/new")
    public String createPost(Model model) {
        PostForm form = new PostForm(1L, Stream.of(Category.values()).map(Enum::name).collect(Collectors.toList()));
        model.addAttribute("postForm", form);
        return "posts/createPostForm";
    }

    /**
     * 게시물 등록
    **/
    @PostMapping("/posts/new")
    public String create(PostForm form, BindingResult result) {
//        if(result.hasErrors()) {
//            return "posts/createPostForm";
//        }

        postService.savePost(new CreatePostRequest(
                form.getAuthorId(),
                form.getCategory(),
                form.getTitle(),
                form.getContent()
        ));

        return "redirect:/posts";
    }

    /**
     * 게시물 사진 등록
    **/
    @PostMapping("/posts/image/new")
    public void singleImageUpload(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("들어왔습니다");
        try {
            //파일정보
            String sFileInfo = "";
            //파일명을 받는다 - 일반 원본파일명
            String sFilename = request.getHeader("file-name");
            System.out.println("파일이름" + sFilename);
            //파일 확장자
            String sFilenameExt = sFilename.substring(sFilename.lastIndexOf(".")+1);
            //확장자를소문자로 변경
            sFilenameExt = sFilenameExt.toLowerCase();

            //이미지 검증 배열변수
            String[] allowFileArr = {"jpg","png","bmp","gif"};

            //확장자 체크
            int nCnt = 0;
            for(int i=0; i<allowFileArr.length; i++) {
                if(sFilenameExt.equals(allowFileArr[i])){
                    nCnt++;
                }
            }

            //이미지가 아니라면
            if(nCnt == 0) {
                PrintWriter print = response.getWriter();
                print.print("NOTALLOW_"+sFilename);
                print.flush();
                print.close();
            } else {
                //디렉토리 설정 및 업로드

                //파일경로
                String filePath = "src/uploadimage/";
                File file = new File(filePath);

                if(!file.exists()) {
                    file.mkdirs();
                }

                String sRealFileNm = "";
                SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
                String today= formatter.format(new java.util.Date());
                sRealFileNm = today+ UUID.randomUUID().toString() + sFilename.substring(sFilename.lastIndexOf("."));
                String rlFileNm = filePath + sRealFileNm;
                System.out.println("rlFileNm = " + rlFileNm);

//                ///////////////// 서버에 파일쓰기 /////////////////
                InputStream inputStream = request.getInputStream();
//                OutputStream outputStream=new FileOutputStream(rlFileNm);
//                int numRead;
//                byte bytes[] = new byte[Integer.parseInt(request.getHeader("file-size"))];
//                while((numRead = inputStream.read(bytes,0,bytes.length)) != -1){
//                    outputStream.write(bytes,0,numRead);
//                }
//                if(inputStream != null) {
//                    inputStream.close();
//                }
//                outputStream.flush();
//                outputStream.close();

                // AWS에 파일 저장
                //TODO - FileService에 로직 옮기기
//                for(MultipartFile multipartFile: multipartFileList) {

                System.out.println("AWS 시작");
                List<MultipartFile> imgFiles = new ArrayList<>();
                Map<String, MultipartFile> fileMap = new HashMap<>();
                System.out.println("request.getClass() = " + request.getClass());
//                if(request instanceof MultipartHttpServletRequest) {
//                    System.out.println("ASW 진행중");
//                    MultipartHttpServletRequest req = (MultipartHttpServletRequest) request;
//                    fileMap = req.getFileMap();
//                    fileMap.forEach((key, value) -> {
//                        imgFiles.add(value);
//                    });
//                } else {
////                    model.addAttribute("result", HttpStatus.BAD_REQUEST);
//                }
//
//                List<String> imagePathList = new ArrayList<>();
//                if(imgFiles.size() > 0) {
//                    MultipartFile multipartFile = imgFiles.get(0);
//
//                    String originalName = multipartFile.getOriginalFilename(); // 파일 이름
//                    long size = multipartFile.getSize(); // 파일 크기
//
//                    ObjectMetadata objectMetaData = new ObjectMetadata();
//                    objectMetaData.setContentType(multipartFile.getContentType());
//                    objectMetaData.setContentLength(size);


                    List<String> imagePathList = new ArrayList<>();
                    String originalName = request.getHeader("file-name");
                    ObjectMetadata objectMetaData = new ObjectMetadata();
                    objectMetaData.setContentType(request.getHeader("file-Type"));
                    objectMetaData.setContentLength(Long.parseLong(request.getHeader("file-size")));


                    // S3에 업로드
                    amazonS3Client.putObject(
                            new PutObjectRequest(S3Bucket, originalName, inputStream, objectMetaData)
                                    .withCannedAcl(CannedAccessControlList.PublicRead)
                    );
//                    amazonS3Client.putObject(
//                            new PutObjectRequest(S3Bucket, originalName, multipartFile.getInputStream(), objectMetaData)
//                                    .withCannedAcl(CannedAccessControlList.PublicRead)
//                    );

                    String imagePath = amazonS3Client.getUrl(S3Bucket, originalName).toString(); // 접근가능한 URL 가져오기
                    imagePathList.add(imagePath);

                    ///////////////// 이미지 /////////////////
                    // 정보 출력
                    sFileInfo += "&bNewLine=true";
                    // img 태그의 title 속성을 원본파일명으로 적용시켜주기 위함
                    sFileInfo += "&sFileName="+ sFilename;
                    //여기서 이미지 저장 경로 설정(S3)
//                sFileInfo += "&sFileURL="+filePath+sRealFileNm;
                    sFileInfo += "&sFileURL="+imagePath;
                    System.out.println("sFileInfo = " + sFileInfo);
                    PrintWriter printWriter = response.getWriter();
                    printWriter.print(sFileInfo);
                    printWriter.flush();
                    printWriter.close();
//                }
                }






        } catch (Exception e) {
            e.printStackTrace();
        }

//        List<MultipartFile> imgFiles = new ArrayList<>();

//        Map<String, MultipartFile> fileMap = new HashMap<>();
//        if(request instanceof MultipartHttpServletRequest) {
//            MultipartHttpServletRequest req = (MultipartHttpServletRequest) request;
//            fileMap = req.getFileMap();
//            System.out.println("안녕" + fileMap);
//            fileMap.forEach((key, value) -> {
//                System.out.println("무슨값");
//                System.out.println(value);
//                imgFiles.add(value);
//            });
//        } else {
//            model.addAttribute("result", HttpStatus.BAD_REQUEST);
//        }
//
//        if(imgFiles.size() > 0) {
//            System.out.println("이미지파일 있음");
//            MultipartFile imgFile = imgFiles.get(0);
//            try {
//                SmartEditor result = postService.singleImageUpload(imgFile);
//
//                model.addAttribute("sFileURL", result.getSFileURL());
//                model.addAttribute("sFileName", result.getSFileName());
//                model.addAttribute("result", HttpStatus.OK);
//            } catch(Exception e) {
//                System.out.println("e.getMessage() = " + e.getMessage());
//                model.addAttribute("result", HttpStatus.BAD_REQUEST);
//            }
//        } else {
//            model.addAttribute("result", HttpStatus.BAD_REQUEST);
//        }
//
//        return new MappingJackson2JsonView();
    }

    /**
     * 게시물 전체 조회 - 페이징 적용
    **/
    @GetMapping("/posts")
    public String list(Model model, @PageableDefault(size = pagingSize, sort = "createDate", direction = Sort.Direction.DESC) Pageable pageable, String searchKeyword) {

        Page<PostDto> list = null;

        if (searchKeyword == null) {
            list = postService.findAll(pageable);
        }
        else {
            list = postService.findAllByTitleOrContent(searchKeyword, pageable);
        }

        model.addAttribute("paging", list);
        return "posts/postList";
    }

    /**
     * 게시물 상세 조회
    **/
    @GetMapping("/posts/{postId}")
    public String findPost(@PathVariable("postId") Long postId, Model model) {
        Post post = postService.updateVisit(postId);
        model.addAttribute("post", new PostDto(post));
        return "posts/post";
    }

    /**
     * 게시물 수정 페이지
    **/
    @GetMapping("/posts/{postId}/edit")
    public String updatePostForm(@PathVariable("postId") Long postId, Model model) {
        Post post = postService.findOne(postId);

        PostForm form = new PostForm(
                post.getId(),
                post.getAuthor().getId(),
                post.getCategory(),
                Stream.of(Category.values()).map(Enum::name).collect(Collectors.toList()),
                post.getTitle(),
                post.getContent()
        );

        model.addAttribute("postForm", form);
        return "posts/updatePostForm";
    }

    /**
     * 게시물 수정
    **/
    @PostMapping("/posts/{postId}/edit")
    public String update(@ModelAttribute("postForm") PostForm form) {
        postService.updatePost(new UpdatePostRequest(form.getId(), form.getTitle(), form.getContent(), form.getCategory()));
        return "redirect:/posts";
    }

    /**
     * 게시물 삭제
    **/
    @GetMapping("/posts/{postId}/delete")
    public String delete(@PathVariable("postId") Long postId) {
        postService.deletePost(postId);
        return "redirect:/posts";
    }

    /**
     * 게시물 좋아요
    **/
    //TODO 나중에 User id 받아서 좋아요한 user가 누군지 저장하는거 하기
    @PutMapping("/posts/{postId}/like")
    private ResponseEntity like(@PathVariable("postId") Long postId) {
        postService.likePost(postId);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
