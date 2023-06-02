package com.study.jpa.chap05_practice.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "tbl_hash")
@Getter
@Setter
@ToString(exclude = {"post"})
@EqualsAndHashCode(of = {"id"})
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HashTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_no")
    private Long id;

    private String tagName; // 해시태그 이름

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "post_no") // 상대방 pk 이름과 똑같이 안지어도 됨
    private Post post;
}
