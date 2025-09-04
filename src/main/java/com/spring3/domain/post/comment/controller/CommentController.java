package com.spring3.domain.post.comment.controller;

import com.spring3.domain.post.comment.entity.Comment;
import com.spring3.domain.post.post.entity.Post;
import com.spring3.domain.post.post.service.PostService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class CommentController {

    private final PostService postService;

    //DTO(데이터를 전달하는 용도로만 사용하는 객체)로 사용하는 문법
    record CommentModifyForm (
        @NotBlank(message = "댓글 내용을 입력해주세요.")
        @Size(min = 2, max = 100, message = "댓글 내용은 2글자 이상 100글자 이하로 입력해주세요.")
        String content
    ){}


    @GetMapping("/posts/{postId}/comments/{commentId}/modify")
    public String modify(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            CommentModifyForm form,
            Model model
    ) {

        Post post = postService.findById(postId).get();
        Comment comment = post.findCommentById(commentId).get();

        model.addAttribute("comment", comment);
        model.addAttribute("post", post);

        return "post/comment/modify";
    }

    @PostMapping("/posts/{postId}/comments/{commentId}/modify")
    @Transactional
    public String doModify(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @Valid CommentModifyForm form
    ) {

        Post post = postService.findById(postId).get();
        postService.modifyComment(post, commentId, form.content);

        return "redirect:/posts/" + postId;
    }

    record CommentWriteForm(
            @NotBlank(message = "댓글 내용을 입력해주세요.")
            @Size(min = 2, max = 100, message = "댓글 내용은 2글자 이상 100글자 이하로 입력해주세요.")
            String content
    ) {}

    @PostMapping("/posts/{postId}/comments/write")  //{id}번 post의 댓글 작성이란 의미의 url
    @Transactional
    public String write(
            @PathVariable Long postId,
            @Valid CommentWriteForm form    //유효성 체크를 위한 변수
    ) {
        Post post = postService.findById(postId).get();
        //post.addComment(form.content);  //form에있는 content는 어떻게 html에서 받아오는거지?
        postService.writeComment(post, form.content);
        return "redirect:/posts/" + postId;
    }


    @GetMapping("/posts/{postId}/comments/{commentId}/delete")
    @Transactional
    public String delete(
            @PathVariable Long postId,
            @PathVariable Long commentId
    ) {

        Post post = postService.findById(postId).get();
        postService.deleteComment(post, commentId);

        return "redirect:/posts/" + postId;
    }
}