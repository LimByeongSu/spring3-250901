package com.spring3.domain.post.comment.entity;

import com.spring3.domain.post.post.entity.Post;
import com.spring3.global.jpa.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor  //JPA는 매개변수없는 생성자를 통해 데이터를 넣어주기때문에 필요하다고 한다.
@Getter
public class Comment extends BaseEntity {

    private String content;

    @ManyToOne
    private Post post;

    public void update(String content) {
        this.content = content;
    }

}
