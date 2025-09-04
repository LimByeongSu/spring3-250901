package com.spring3.domain.post.post.entity;

import com.spring3.domain.post.comment.entity.Comment;
import com.spring3.global.jpa.entity.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
public class Post extends BaseEntity {
    private String title;
    private String content;

    @OneToMany(mappedBy = "post", cascade = {CascadeType.PERSIST, CascadeType.REMOVE},orphanRemoval = true, fetch = FetchType.LAZY)
    //PERSIST는 연쇄저장, REMOVE는 연쇄삭제를 의미한다.
    //참고로 cascade는 @Transactional안에서만 작동한다.

    //Post가 Comment를 가지고있지만 상황에따라 Post만 쓸 떄도 있다.
    //그때 Comment를 가져오면 낭비니까 Comment를 사용할 때만 가져오게 하는 것이
    //fetch = FetchType.LAZY이다.
    private List<Comment> comments = new ArrayList<>();

    public Post(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public Comment addComment(String content) {
        //부모정보를 꼭 알려줘야한다. -> 이 부분 이해가 좀 안됨
        Comment comment = new Comment(content, this);
        this.comments.add(comment);

        return comment;
    }

    public void deleteComment(Long commentId) {
        comments.stream()
                .filter(c -> c.getId().equals(commentId))
                .findFirst()
                .ifPresent(comments::remove);
    }
}