package com.spring3.domain.post.post.controller;

import com.spring3.domain.post.post.entity.Post;
import com.spring3.domain.post.post.service.PostService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    private String getWriteFormHtml(String errorMessage, String title, String content, String errorFieldName){
        return """
                <div>%s</div>
                <form method="POST" action="/posts/doWrite">
                  <input type="text" name="title" value="%s" autoFocus>
                  <br>
                  <textarea name="content">%s</textarea>
                  <br>
                  <input type="submit" value="작성">
                </form>
                
                <script>
                    const errorFieldName = "%s";
                
                    if(errorFieldName.length > 0) {
                    const form = document.querySelector("form");
                    form[errorFieldName].focus();
                }
                </script>
                """.formatted(errorMessage, title, content, errorFieldName);

    }

    @GetMapping("/posts/write")
    @ResponseBody
    public String write() {

        //https://localhost:8080/posts/doWrite 로 적어도 되지만 시작과 끝이 localhost8080서버로 같다면 생략가능
        return getWriteFormHtml("", "", "", "");
    }

    @AllArgsConstructor
    @Getter
    public static class PostWriteForm{
        @NotBlank
        @Size(min = 2, max = 10)
        private String title;

        @NotBlank
        @Size(min = 2, max = 100)
        private String content;
    }

    @PostMapping("/posts/doWrite")
    @ResponseBody
    public String doWrite(
            @ModelAttribute("postWriteForm") @Valid PostWriteForm form //PostWriteForm객체 안에 있는 값들을 매개변수로 받으라는 의미다.
            //참고로 @ModelAttribute("이름")의 이름은 객체의 앞글자를 소문자로 바꾼 postWriteForm 이다.
            // 내가 지정안해도 스프링이 postWriteForm으로 지정한다.
    ) {

        /*if(title.isBlank()){
            return getWriteFormHtml("제목을 입력해 주세요", title, content, "title");
        }
        if(title.length() < 2){
            return getWriteFormHtml("제목은 2글자 이상 적어주세요.", title, content, "title");
        }
        if(title.length() > 10){
            return getWriteFormHtml("제목은 10글자 이상 넘을 수 없습니다.", title, content, "title");
        }
        if(content.isBlank()){
            return getWriteFormHtml("내용을 입력해 주세요", title, content, "content");
        }
        if(content.length() < 2){
            return getWriteFormHtml("내용은 2글자 이상 적어주세요.", title, content, "content");
        }
        if(content.length() > 100){
            return getWriteFormHtml("내용은 100글자 이상 넘을 수 없습니다.", title, content, "content");
        }*/

        Post post = postService.write(form.title, form.content);

        return "%d번 글이 작성되었습니다.".formatted(post.getId());
    }
}