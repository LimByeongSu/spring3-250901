package com.spring3.domain.post.post.service;


import com.spring3.domain.post.post.entity.Post;
import com.spring3.domain.post.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public void write(String title, String content) {
        Post post = new Post(title, content);
        postRepository.save(post);
    }

    public long count() {
        return postRepository.count();
    }

}