package com.study.jpa.chap03_pagination.repository;

import com.study.jpa.chap02_querymethod.entity.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
// 만약에 서비스클래스를 사용한다면 해당 클래스에 붙일 것!
@Transactional // JPA는 I, U, D 시에 반드시 트랜잭션 처리가 필수
@Rollback(value = false)
class StudentPageRepositoryTest {

    @Autowired
    StudentPageRepository studentPageRepository;

    @BeforeEach
    void bulkInsert() {
        // 학생을 147명 저장
        for (int i = 0; i < 147; i++) {
            Student s = Student.builder()
                    .name("김파파" + i)
                    .city("도시시" + i)
                    .major("전공공" + i)
                    .build();

            studentPageRepository.save(s);
        }
    }

    @Test
    @DisplayName("기본 페이징 테스트")
    void testBasicPagination() {
        //given
        int pageNo = 1;
        int amount = 10;
        // 페이지 정보 생성
        // 페이지번호가 zero-based
        // 정렬을 위해서는 세 번째 파라미터로 Sort 를 넣어준다. by 안에는 정렬기준의 필드명을 넣어준다.
        PageRequest pageInfo = PageRequest.of(pageNo - 1,
                amount,
//                Sort.by("name").ascending()
                Sort.by(
                        Sort.Order.desc("name"),
                        Sort.Order.asc("city")
                )
        );
        //when
        Page<Student> students = studentPageRepository.findAll(pageInfo);
        // 페이징 완료된 데이터셋
        List<Student> studentList = students.getContent();
        // 총 페이지 수
        int totalPages = students.getTotalPages();
        // 총 학생 수
        long totalElements = students.getTotalElements();

        // 다양한 메서드들을 활용할 수 있다.
        Pageable prev = students.getPageable().previousOrFirst();
        boolean unpaged = prev.hasPrevious();
        boolean paged = students.getPageable().isPaged();
        Pageable next = students.getPageable().next();

        //then
        studentList.forEach(System.out::println);
        System.out.println("totalPages = " + totalPages);
        System.out.println("totalElements = " + totalElements);
        System.out.println("unpaged = " + unpaged);
        System.out.println("paged = " + paged);
    }

    @Test
    @DisplayName("이름검색 + 페이징")
    void testSearchAndPagination() {
        //given
        int pageNo = 1;
        int size = 10;
        PageRequest pageInfo = PageRequest.of(pageNo - 1, 10);
        //when
        Page<Student> students = studentPageRepository.findByNameContaining("3", pageInfo);
        List<Student> studentList = students.getContent();
        //then
        System.out.println("\n\n\n");
        studentList.forEach(System.out::println);
        System.out.println("\n\n\n");
    }
}