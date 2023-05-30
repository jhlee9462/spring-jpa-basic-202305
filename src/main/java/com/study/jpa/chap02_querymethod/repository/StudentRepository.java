package com.study.jpa.chap02_querymethod.repository;

import com.study.jpa.chap02_querymethod.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, String> {

    List<Student> findByName(String name);

    List<Student> findByCityAndMajor(String city, String major);

    List<Student> findByMajorContaining(String major);

    // 네이티브 쿼리 사용
    @Query(value = "select * from tbl_student where stu_name = :nm", nativeQuery = true)
    Student findNameWithSQL(@Param("nm") String name);

    // JPQL
    // select 별칭 from 엔터티클래스명 as 별칭 where 별칭.필드명=?

    // native-sql: select * from tbl_student where stu_name = ?
    // jpql:       SELECT st FROM Student AS st WHERE st.name = ?

    // 도시 이름으로 학생 조회
    @Query("select s from Student as s where s.city = ?1")
    List<Student> getByCityWithJPQL(String city);

    @Query("select st from Student as st where st.name like %:name%")
    List<Student> searchByNamesWithJPQL(String name);

    // JPQL로 수정 삭제 쿼리 쓰기
    @Modifying // 조회가 아닐 경우 무조건 붙여야 함
    @Query("delete from Student as st where st.name = :name")
    void deleteByNameWithJPQL(String name);
}
