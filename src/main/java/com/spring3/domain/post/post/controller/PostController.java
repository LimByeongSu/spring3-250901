package com.spring3.domain.post.post.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PostController {

    @GetMapping("/posts/write")
    @ResponseBody
    public String write() {


        return """
                <form action="https://localhost:8080/">
                  <input type="text" name="title">
                  <br>
                  <textarea name="content"></textarea>
                  <input type="submit" value="작성">
                </form>
                """;
    }
}
