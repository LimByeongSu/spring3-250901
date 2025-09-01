package com.spring3.domain.post.post.controller;

import com.spring3.domain.post.post.entity.Post;
import com.spring3.domain.post.post.service.PostService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    private String getWriteFormHtml(String errorMessage){
        return """
                <div>%s</div>
                <form method="POST" action="/posts/doWrite">
                  <input type="text" name="title">
                  <br>
                  <textarea name="content"></textarea>
                  <br>
                  <input type="submit" value="작성">
                </form>
                """.formatted(errorMessage);
    }

    @GetMapping("/posts/write")
    @ResponseBody
    public String write() {

        //https://localhost:8080/posts/doWrite 로 적어도 되지만 시작과 끝이 localhost8080서버로 같다면 생략가능
        return getWriteFormHtml("");
    }

    @PostMapping("/posts/doWrite")
    @ResponseBody
    public String doWrite(
            String title,
            String content
    ) {

        if(title.isBlank()){
            return getWriteFormHtml("제목을 입력해 주세요");
        }
        if(content.isBlank()){
            return getWriteFormHtml("내용을 입력해 주세요");
        }

        Post post = postService.write(title, content);

        return "%d번 글이 작성되었습니다.".formatted(post.getId());
    }
}