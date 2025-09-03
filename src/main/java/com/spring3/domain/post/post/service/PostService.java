package com.spring3.domain.post.post.service;


import com.spring3.domain.post.post.entity.Post;
import com.spring3.domain.post.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public Post write(String title, String content) {
        Post post = new Post(title, content);
        return postRepository.save(post);
    }

    public long count() {
        return postRepository.count();
    }

    public Optional<Post> findById(Long id) {
        return postRepository.findById(id);
    }
}