package jun.moviecommunity.controller;

import jun.moviecommunity.domain.Category;
import jun.moviecommunity.domain.File;
import jun.moviecommunity.domain.Post;
import jun.moviecommunity.service.*;
import jun.moviecommunity.validator.PostCreateValidator;
import jun.moviecommunity.validator.PostUpdateValidator;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PostController {

    private final PostService postService;
    private final FileService fileService;
    private final S3Service s3Service;
    private final PostCreateValidator postCreateValidator;
    private final PostUpdateValidator postUpdateValidator;

    private final int pagingSize = 2;

    /**
     * 게시물 등록 페이지
    **/
    @GetMapping("/posts/new")
    public String createPostForm(Model model) {
        PostForm form = new PostForm(Stream.of(Category.values()).map(Enum::name).collect(Collectors.toList()));
        model.addAttribute("postForm", form);
        return "posts/createPostForm";
    }

    /**
     * 게시물 등록
     **/
    @PostMapping("/posts/new")
    public String create(@Valid PostForm form, BindingResult result, HttpSession httpSession) {
        log.info("게시물 작성 요청");
        postCreateValidator.validate(form, result);

        if(result.hasErrors()) {
            log.info("게시물 작성 요청 에러");
            form.setCategories(Stream.of(Category.values()).map(Enum::name).collect(Collectors.toList()));
            return "posts/createPostForm";
        }

        UserDto userDto = (UserDto) httpSession.getAttribute(SessionConst.LOGIN_USER);

        String content = form.getContent();

        List<String> filePaths = fileService.getFilePathsFromContent(content);
        List<File> files = fileService.findFilesByFilePaths(filePaths);

        Long postId = postService.savePost(new CreatePostRequest(
                userDto.getId(),
                form.getCategory(),
                form.getTitle(),
                content,
                files
        ));

        //TODO-등록중에 지워진 사진은 주기적으로 File 테이블에서 post_id가 null인것 제거하는식으로 처리

        return "redirect:/posts";
    }

    /**
     * 게시물 사진 등록
    **/
    @PostMapping("/posts/image/new")
    public void imageUpload(HttpServletRequest request, HttpServletResponse response) {

        try {
            InputStream inputStream = request.getInputStream();
            String fileName = UUID.randomUUID() + "_" + request.getHeader("file-name");
            String fileType = request.getHeader("file-Type");
            String fileSize = request.getHeader("file-size");

            FileDto fileDto = new FileDto(fileName, fileType, fileSize);
            log.info("파일원본이름" + fileName);

            //AWS에 이미지 저장
            Map<String, String> fileInfo = s3Service.upload(inputStream, fileDto);
            String sFileInfo = fileInfo.get("sFileInfo");
            String filePath = fileInfo.get("filePath");
            fileService.save(fileName, filePath);

            //서버로 파일 전송 후 이미지 정보 확인
            PrintWriter printWriter = response.getWriter();
            printWriter.print(sFileInfo);
            printWriter.flush();
            printWriter.close();

        } catch (Exception e) {

        }

//        System.out.println("들어왔습니다");
//        try {
//            //파일정보
//            String sFileInfo = "";
//            //파일명을 받는다 - 일반 원본파일명
//            String sFilename = request.getHeader("file-name");
//            System.out.println("파일이름" + sFilename);
//            //파일 확장자
//            String sFilenameExt = sFilename.substring(sFilename.lastIndexOf(".")+1);
//            //확장자를소문자로 변경
//            sFilenameExt = sFilenameExt.toLowerCase();
//
//            //이미지 검증 배열변수
//            String[] allowFileArr = {"jpg","png","bmp","gif"};
//
//            //확장자 체크
//            int nCnt = 0;
//            for(int i=0; i<allowFileArr.length; i++) {
//                if(sFilenameExt.equals(allowFileArr[i])){
//                    nCnt++;
//                }
//            }
//
//            //이미지가 아니라면
//            if(nCnt == 0) {
//                PrintWriter print = response.getWriter();
//                print.print("NOTALLOW_"+sFilename);
//                print.flush();
//                print.close();
//            } else {
//                //디렉토리 설정 및 업로드
//
//                //파일경로
//                String filePath = "src/uploadimage/";
//                File file = new File(filePath);
//
//                if(!file.exists()) {
//                    file.mkdirs();
//                }
//
//                String sRealFileNm = "";
//                SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
//                String today= formatter.format(new java.util.Date());
//                sRealFileNm = today+ UUID.randomUUID().toString() + sFilename.substring(sFilename.lastIndexOf("."));
//                String rlFileNm = filePath + sRealFileNm;
//                System.out.println("rlFileNm = " + rlFileNm);
//
////                ///////////////// 서버에 파일쓰기 /////////////////
//                InputStream inputStream = request.getInputStream();
////                OutputStream outputStream=new FileOutputStream(rlFileNm);
////                int numRead;
////                byte bytes[] = new byte[Integer.parseInt(request.getHeader("file-size"))];
////                while((numRead = inputStream.read(bytes,0,bytes.length)) != -1){
////                    outputStream.write(bytes,0,numRead);
////                }
////                if(inputStream != null) {
////                    inputStream.close();
////                }
////                outputStream.flush();
////                outputStream.close();
//
//                // AWS에 파일 저장
////                for(MultipartFile multipartFile: multipartFileList) {
//
//                System.out.println("AWS 시작");
//                List<MultipartFile> imgFiles = new ArrayList<>();
//                Map<String, MultipartFile> fileMap = new HashMap<>();
//                System.out.println("request.getClass() = " + request.getClass());
////                if(request instanceof MultipartHttpServletRequest) {
////                    System.out.println("ASW 진행중");
////                    MultipartHttpServletRequest req = (MultipartHttpServletRequest) request;
////                    fileMap = req.getFileMap();
////                    fileMap.forEach((key, value) -> {
////                        imgFiles.add(value);
////                    });
////                } else {
//////                    model.addAttribute("result", HttpStatus.BAD_REQUEST);
////                }
////
////                List<String> imagePathList = new ArrayList<>();
////                if(imgFiles.size() > 0) {
////                    MultipartFile multipartFile = imgFiles.get(0);
////
////                    String originalName = multipartFile.getOriginalFilename(); // 파일 이름
////                    long size = multipartFile.getSize(); // 파일 크기
////
////                    ObjectMetadata objectMetaData = new ObjectMetadata();
////                    objectMetaData.setContentType(multipartFile.getContentType());
////                    objectMetaData.setContentLength(size);


//        } catch (Exception e) {
//            e.printStackTrace();
//        }

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
            list = postService.findPosts(pageable);
        }
        else {
            list = postService.findPostsByTitleOrContent(searchKeyword, pageable);
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
                post.getContent(),
                false
        );

        model.addAttribute("postForm", form);
        return "posts/updatePostForm";
    }

    /**
     * 게시물 수정
    **/
    @PostMapping("/posts/{postId}/edit")
    public String update(@Valid PostForm form, BindingResult result) {
        log.info("게시물 수정");

        //게시물 유효성 검사
        log.info("null임 {} {} {}", postUpdateValidator, form, result);
        postUpdateValidator.validate(form, result);

        if(result.hasErrors()) {
            log.info("게시물 수정 에러");
            form.setCategories(Stream.of(Category.values()).map(Enum::name).collect(Collectors.toList()));
            return "posts/updatePostForm";
        }

        //사진 관련 처리
        Long postId = form.getId();
        String content = form.getContent();
        List<File> existedFiles = fileService.findFilesByPostId(postId);
        List<String> updatedFilePaths = fileService.getFilePathsFromContent(content);

        List<File> existedFilesRemoved = new ArrayList<>();
        List<String> updatedFilePathsRemoved = new ArrayList<>();

        for (File file : existedFiles) {
            String filePath = file.getFilePath();
            //이미 존재했던 파일이 현재에는 없다면
            if(!updatedFilePaths.contains(filePath)) {
                //삭제
                existedFilesRemoved.add(file);
            } else {
                updatedFilePathsRemoved.add(filePath);
            }
        }

        existedFiles.removeAll(existedFilesRemoved);
        updatedFilePaths.removeAll(updatedFilePathsRemoved);

        List<File> updatedFile = fileService.findFilesByFilePaths(updatedFilePaths);

        for (File file : updatedFile) {
            existedFiles.add(file);
        }

        //게시물 수정
        postService.updatePost(new UpdatePostRequest(postId, form.getTitle(), content, form.getCategory(), existedFiles));

        List<Long> removeFileIds = new ArrayList<>();
        List<String> removeFileNames = new ArrayList<>();
        for (File file :existedFilesRemoved) {
            removeFileIds.add(file.getId());
            removeFileNames.add(file.getFileName());
        }

        //S3에서 사용안하는 이미지 제거
        s3Service.delete(removeFileNames);
        //파일 테이블에서 사용안하는 이미지 제거
        fileService.deletePostByIds(removeFileIds);
        return "redirect:/posts/" + postId;
    }

    /**
     * 게시물 삭제
    **/
    @GetMapping("/posts/{postId}/delete")
    public String delete(@PathVariable("postId") Long postId) {
        Post post = postService.findOne(postId);
        List<File> files = fileService.findFilesByPostId(postId);
        List<String> fileNames = new ArrayList<>();

        for(File file : files) {
            fileNames.add(file.getFileName());
        }

        //S3에 저장된 파일 삭제
        s3Service.delete(fileNames);
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
