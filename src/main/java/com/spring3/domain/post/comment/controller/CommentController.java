package com.spring3.domain.post.comment.controller;

import com.spring3.domain.post.post.entity.Post;
import com.spring3.domain.post.post.service.PostService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor

public class CommentController {

    private final PostService postService;

    @AllArgsConstructor
    @Getter
    public static class CommentWriteForm{
        @NotBlank(message = "댓글을 입력해주세요.")
        @Size(min = 2, max = 100, message = "댓글은 2글자 이상 100글자 이하로 입력해주세요.")
        private String content;
    }

    @PostMapping("posts/{postId}/comments/write")    //{id}번 post의 댓글 작성이란 의미의 url
    @Transactional
    public String write(
            @PathVariable Long  postId,
            @Valid CommentWriteForm form,    //유효성 체크를 위한 변수
            BindingResult bindingResult,
            Model model
    ){

        Post post = postService.findById(postId).get();

        if(bindingResult.hasErrors()) {
            model.addAttribute("post", post);
            return "post/detail";
        }


        postService.writeComment(post, form.getContent());
        //post.addComment(form.content);  //form에있는 content는 어떻게 html에서 받아오는거지?

        return "redirect:/posts/" + postId;
    }
}
