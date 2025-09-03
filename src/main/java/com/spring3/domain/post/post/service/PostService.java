package com.spring3.domain.post.post.service;


import com.spring3.domain.post.post.entity.Post;
import com.spring3.domain.post.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public List<Post> findAll() {
        return postRepository.findAll();
    }

    public void modify(Post post, String title, String content) {
        post.update(title, content); //-> 이걸 쓰려면 @Transactional을 붙여서 더티체킹을 하게해야한다.
        //하지만 @Transactional은 작업단위를 묶은 용도로 더 많이 사용되기 때문에 이 메서드에서 쓰는것 보단
        //Controller에서 doModify에 쓰는게 의미상 맞다.

        //postRepository.save(post);  //물론 repository에 @Transactional이 있어서 repository를 쓰면 되긴하다.
    }
}