package com.example.MultiGreenMaster.controller;

import com.example.MultiGreenMaster.dto.CMPostFRM;
import com.example.MultiGreenMaster.entity.CMCommentENT;
import com.example.MultiGreenMaster.entity.CMPostENT;
import com.example.MultiGreenMaster.entity.UserENT;
import com.example.MultiGreenMaster.service.CMCommentSRV;
import com.example.MultiGreenMaster.service.CMPostSRV;
import com.example.MultiGreenMaster.service.CMRecommentSRV;
import com.example.MultiGreenMaster.service.UserSRV;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/posts") // "/posts" 경로와 매핑
public class CMPostCTL extends SessionCheckCTL {

    private static final Logger logger = LoggerFactory.getLogger(CMPostCTL.class); // 로그 설정

    @Autowired
    private CMPostSRV cmPostService;

    @Autowired
    private CMCommentSRV cmCommentService;


    @Autowired // UserService Bean 객체를 주입
    private UserSRV userService;

    @GetMapping("/new") // GET 요청을 "/new" 경로와 매핑
    public String newPostForm(Model model) {
        logger.info("Requesting new post form"); // 새 게시글 작성 폼 요청
        model.addAttribute("postForm", new CMPostFRM()); // 새로운 게시글 작성 폼을 모델에 추가
        return "cmPost/postForm"; // "postForm" 뷰를 반환
    }

    @PostMapping("/new") // POST 요청을 "/new" 경로와 매핑
    public String createPost(@ModelAttribute CMPostFRM form, HttpServletRequest request) {
        return null;

    }

    @GetMapping // GET 요청을 기본 경로와 매핑
    public String listPosts(Model model) {
        logger.info("Requesting post list"); // 게시글 목록 요청
        List<CMPostENT> posts = cmPostService.findAllPosts(); // 모든 게시글 조회
        List<CMPostFRM> postForms = posts.stream().map(post -> { // 각 게시글을 CMPostForm으로 변환
            List<String> pictureBase64List = post.getPictures() != null ? post.getPictures().stream()
                    .map(picture -> Base64.getEncoder().encodeToString(picture.getPictureData()))  // CMPicture의 byte[] 데이터를 Base64 문자열로 변환
                    .collect(Collectors.toList()) : null;
            return new CMPostFRM(
                    post.getId(),
                    post.getUser(),
                    post.getTitle(),
                    post.getContent(),
                    pictureBase64List,
                    post.getLikeCount(),
                    post.getRegdate(),
                    post.getCount()
            );
        }).collect(Collectors.toList());
        model.addAttribute("posts", postForms); // 게시글 리스트를 모델에 추가
        logger.info("Post list retrieved successfully"); // 게시글 목록 조회 완료
        return "cmPost/postList"; // "postList" 뷰를 반환
    }

    @GetMapping("/{id}") // GET 요청을 "/{id}" 경로와 매핑
    public String viewPostDetail(@PathVariable Long id, Model model) {
        logger.info("Requesting post detail: Post ID {}", id); // 게시글 상세 조회 요청
        cmPostService.incrementCount(id); // 조회수 증가
        CMPostENT post = cmPostService.findPostById(id); // ID로 게시글 조회
        if (post != null) {
            List<String> pictureBase64List = post.getPictures() != null ? post.getPictures().stream()
                    .map(picture -> Base64.getEncoder().encodeToString(picture.getPictureData()))  // CMPicture의 byte[] 데이터를 Base64 문자열로 변환
                    .collect(Collectors.toList()) : null;
            CMPostFRM postForm = new CMPostFRM(
                    post.getId(),
                    post.getUser(),
                    post.getTitle(),
                    post.getContent(),
                    pictureBase64List,
                    post.getLikeCount(),
                    post.getRegdate(),
                    post.getCount()
            );
            model.addAttribute("post", postForm); // 게시글을 모델에 추가

            List<CMCommentENT> comments = cmCommentService.findCommentsByPostId(id); // 게시글의 모든 댓글 조회
            model.addAttribute("comments", comments); // 댓글 리스트를 모델에 추가
            logger.info("Post detail retrieved successfully: Post ID {}", id); // 게시글 상세 조회 완료
        }
        return "cmPost/postDetail"; // "postDetail" 뷰를 반환
    }

    @PostMapping("/{id}/like")
    @ResponseBody
    public int likePost(@PathVariable Long id) {
        logger.info("Request to increase like count: Post ID {}", id); // 게시글 좋아요 증가 요청
        cmPostService.incrementLikeCount(id);
        CMPostENT post = cmPostService.findPostById(id);
        return post != null ? post.getLikeCount() : 0;
    }
}
