package com.study.jpa.chap04_relation.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tbl_dept")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"employees"})
@Builder
// jpa 연관관계 매핑에서는 연관관계 데이터는 ToString에서 제외해야 한다.
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dept_id")
    private Long id;

    @Column(name = "dept_name", nullable = false)
    private String name;

    // 양방향 매핑에서는 상대방 엔터티의 갱신에 관여할 수 없다
    // 단순히 읽기전용(조회)으로만 사용해야 함.
    // mappedBy에는 상대방 엔터티의 조인되는 필드명을 써줌
    // 반드시 초기화를 해야한다.
    @OneToMany(mappedBy = "department")
    @Builder.Default
    private List<Employee> employees = new ArrayList<>();

}
