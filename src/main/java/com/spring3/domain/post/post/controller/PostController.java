package com.spring3.domain.post.post.controller;

import com.spring3.domain.post.post.entity.Post;
import com.spring3.domain.post.post.service.PostService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }


    record PostWriteForm(
            @NotBlank(message = "01-title-제목을 입력해주세요.")
            @Size(min = 2, max = 10, message = "02-title-제목은 2글자 이상 10글자 이하로 입력해주세요.")
            String title,
            @NotBlank(message = "03-content-내용을 입력해주세요.")
            @Size(min = 2, max = 100, message = "04-content-내용은 2글자 이상 100글자 이하로 입력해주세요.")
            String content
    ) {
    }

    @GetMapping("/posts/write")
    public String write(@ModelAttribute("form") PostWriteForm form) {
        return "post/post/write";
    }

    @PostMapping("/posts/write")
    public String doWrite(
            //PostWriteForm객체 안에 있는 값들을 매개변수로 받으라는 의미다.
            //참고로 @ModelAttribute("이름")의 이름은 객체의 앞글자를 소문자로 바꾼 postWriteForm 이다.
            // 내가 지정안해도 스프링이 postWriteForm으로 지정한다.
            @ModelAttribute("form") @Valid PostWriteForm form,
            BindingResult bindingResult,
            Model model
    ) {

        if(bindingResult.hasErrors()) {
            return "post/post/write";

        }

        Post post = postService.write(form.title, form.content);

        model.addAttribute("id", post.getId());
        return "redirect:/posts/%d".formatted(post.getId()); // 주소창을 바꿔
    }



    record PostModifyForm (
        @NotBlank(message = "01-title-제목을 입력해주세요.")
        @Size(min = 2, max = 10, message = "02-title-제목은 2글자 이상 10글자 이하로 입력해주세요.")
        String title,

        @NotBlank(message = "03-content-내용을 입력해주세요.")
        @Size(min = 2, max = 100, message = "04-content-내용은 2글자 이상 100글자 이하로 입력해주세요.")
        String content
){}

    @GetMapping("/posts/{id}/modify")
    public String modify(
            @PathVariable Long id,
            //@ModelAttribute는 model.addAttribute랑 닮은걸 기억하자. ->
            //@ModelAttribute가 붙은 변수는 스프링이 자동으로 Model로 넘겨서 html에서 사용할 수 있음
            @ModelAttribute("form") PostModifyForm form,
            Model model
    ) {

        Post post = postService.findById(id).get();

        PostModifyForm form2 = new PostModifyForm(post.getTitle(), post.getContent());
        model.addAttribute("form", form2);

        model.addAttribute("post", post);
        return "post/post/modify";
    }

    @PutMapping("/posts/{id}")
    @Transactional
    public String doModify(
            @PathVariable Long id,
            @ModelAttribute("form") @Valid PostModifyForm form,
            BindingResult bindingResult
    ) {

        if(bindingResult.hasErrors()) {

            //model.addAttribute("form", form);
            //@ModelAttribute가 붙어있는건 따로 넘길필요없고 form은 이미 @ModelAttribute가 붙어있다.
            return "post/post/modify";
        }

        //findById와 modify를 묶어서 작업하기 위해 @Transactional을 사용했다.
        Post post = postService.findById(id).get();
        postService.modify(post, form.title, form.content);

        return "redirect:/posts/%d".formatted(post.getId());
    }

    @DeleteMapping("/posts/{id}")
    @Transactional
    public String doDelete(
            @PathVariable Long id
    ) {

        Post post = postService.findById(id).get();
        postService.delete(post);

        return "redirect:/posts";

    }


    @GetMapping("/posts/{id}")  //상세 보기
    @Transactional(readOnly = true)  //detail()을 조회의 기능으로만 사용하겠다고 명시하겠다는 의도로 사용
                                        //혹시나 여기에 update로직을 넣는 경우를 막기 위함
                                        //두번째 이유는 JPA에서 조회용으로 쓴다고 명시된 것은 좀더 빠르게 작동시켜줌
    public String detail(@PathVariable Long id, Model model) {

        Post post = postService.findById(id).get();
        model.addAttribute("post", post);

        return "post/post/detail";
    }

    @GetMapping("/posts")
    @Transactional(readOnly = true)
    public String list(Model model) {       //글 목록

        List<Post> posts = postService.findAll();
        model.addAttribute("posts", posts);
        return "post/post/list";
    }
}