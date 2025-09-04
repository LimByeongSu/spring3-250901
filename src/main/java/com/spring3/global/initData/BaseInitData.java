package com.spring3.global.initData;

import com.spring3.domain.post.post.entity.Post;
import com.spring3.domain.post.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@RequiredArgsConstructor
public class BaseInitData {

    @Autowired
    @Lazy
    private BaseInitData self;
    private final PostService postService;

    @Bean
    ApplicationRunner initDataRunner() {
        return args -> {

            self.work1();   //프록시를 거쳐야하기 때문에 self를 씀

        };

    }

    @Transactional
    public void work1() {
        if(postService.count() > 0) {
            return;
        }

        Post post1 = postService.write("제목1", "내용1");
        Post post2 = postService.write("제목2", "내용2");
        Post post3 = postService.write("제목3", "내용3");

        post1.addComment("댓글 1-1");
        post1.addComment("댓글 1-2");
        post1.addComment("댓글 1-3");
        post2.addComment("댓글 2-1");
        post2.addComment("댓글 2-2");
    }
}