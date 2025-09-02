package com.spring3.domain.post.post.controller;

import com.spring3.domain.post.post.entity.Post;
import com.spring3.domain.post.post.service.PostService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.stream.Collectors;

@Controller
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/posts/write")
    public String write(@ModelAttribute("postWriteForm") PostWriteForm form) {

        return "post/write";
    }

    @AllArgsConstructor
    @Getter
    public static class PostWriteForm{
        @NotBlank(message = "1- 제목을 입력해주세요.")
        @Size(min = 2, max = 10, message = "2- 제목은 2글자 이상 10글자 이하로 입력해주세요.")
        private String title;

        @NotBlank(message = "3- 내용을 입력해주세요.")
        @Size(min = 2, max = 100, message = "4- 내용은 2글자 이상 100글자 이하로 입력해주세요.")
        private String content;
    }

    @PostMapping("/posts/doWrite")
    public String doWrite(
            @ModelAttribute("postWriteForm") @Valid PostWriteForm form //PostWriteForm객체 안에 있는 값들을 매개변수로 받으라는 의미다.
            //참고로 @ModelAttribute("이름")의 이름은 객체의 앞글자를 소문자로 바꾼 postWriteForm 이다.
            // 내가 지정안해도 스프링이 postWriteForm으로 지정한다.
            , BindingResult bindingResult
            , Model model
    ) {
        if(bindingResult.hasErrors()) {  // 결과에 에러가 있는가를 물어본다.

            String errorMessages = bindingResult.getFieldErrors()
                    .stream()
                    .map(field -> field.getField() + "-" + field.getDefaultMessage())
                    .map(message -> message.split("-"))
                    .map(bits -> """
                            <!-- %s --><li data-error-field-name="%s">%s</li>
                            """.formatted(bits[1], bits[0], bits[2]))
                    .sorted()
                    .collect(Collectors.joining("\n"));//"\n"은 자바에서 줄바꿈이고 에러메세지는 html에서 쓸거라 <br>을 해야함

            model.addAttribute("errorMessages", errorMessages);
            //model.addAttribute("form", form);
            //@ModelAttribute가 붙어있는건 따로 넘길필요없고 form은 이미 @ModelAttribute가 붙어있다.

            return "post/write";
        }

        Post post = postService.write(form.title, form.content);


        model.addAttribute("id", post.getId());
        return "post/writeDone";
    }
}