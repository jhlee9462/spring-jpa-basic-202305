package com.study.jpa.chap05_practice.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tbl_post")
@Getter
@Setter
@ToString(exclude = {"hashTags"})
@EqualsAndHashCode(of = {"id"})
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_no")
    private Long id;

    @Column(nullable = false)
    private String writer; // 작성자

    private String title; // 제목

    private String content; // 내용

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createDate; // 작성 시간

    @UpdateTimestamp
    @Column(updatable = false)
    private LocalDateTime updateDate; // 수정 시간

    @OneToMany(mappedBy = "post") // 상대편에 있는 나의 필드 이름
    @Builder.Default // 이것을 설정하지 않으면 빌더를 사용했을 때 기본값이 null 이 되어버린다.
                     // 때문에 빌더를 사용했을 때도 기본값을 new ArrayList<>() 로 만들게 하기 위해 사용
    private List<HashTag> hashTags = new ArrayList<>(); // 양방향의 1 에서 多 필드는 반드시 초기화해주어야한다.

    // 양방향 매핑에서 리스트쪽에 데이터를 추가하는 편의메서드 생성
    public void addHashTag(HashTag hashTag) {
        hashTags.add(hashTag);

        if (this != hashTag.getPost()) {
            hashTag.setPost(this);
        }
    }
}
