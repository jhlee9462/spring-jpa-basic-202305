package com.study.jpa.chap04_relation.repository;

import com.study.jpa.chap04_relation.entity.Department;
import com.study.jpa.chap04_relation.entity.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(false)
class EmployeeRepositoryTest {

    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    DepartmentRepository departmentRepository;

    @BeforeEach
    void bulkInsert() {

        Department d1 = Department.builder()
                .name("영업부")
                .build();
        Department d2 = Department.builder()
                .name("개발부")
                .build();

        departmentRepository.save(d1);
        departmentRepository.save(d2);

        Employee e1 = Employee.builder()
                .name("라이옹")
                .department(d1)
                .build();
        Employee e2 = Employee.builder()
                .name("프로도")
                .department(d1)
                .build();
        Employee e3 = Employee.builder()
                .name("샘")
                .department(d2)
                .build();
        Employee e4 = Employee.builder()
                .name("빌보")
                .department(d2)
                .build();

        employeeRepository.save(e1);
        employeeRepository.save(e2);
        employeeRepository.save(e3);
        employeeRepository.save(e4);
    }

    @Test
    @DisplayName("특정 사원의 정보 조회")
    void testFindOne() {
        //given
        Long id = 2L;
        //when
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(
                        () -> new RuntimeException("사원이 없음")
                );
        //then
        System.out.println("employee = " + employee);

        assertEquals("프로도", employee.getName());
    }

    @Test
    @DisplayName("부서 정보 조회")
    void testFindDept() {
        //given
        Long id = 1L;
        //when
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당하는 부서 없음!"));
        //then
        System.out.println("department = " + department);
        assertEquals("영업부", department.getName());
    }

    @Test
    @DisplayName("Lazy로딩과 Eager로딩의 차이")
    void testLazyAndEager() {
        // 3번 사원을 조회하고 싶은데 굳이 부서정보는 필요없다.
        //given
        long id = 3L;
        //when
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당하는 사원 없음!!"));
        //then
        System.out.println("employee = " + employee);
    }

    @Test
    @DisplayName("양방향 연관관계에서 연관데이터의 수정")
    void testChangeDept() {
        // 3번사원의 부서를 2번부서에서 1번부서로 변경해야 한다.
        //given
        Employee employee = employeeRepository.findById(3L)
                .orElseThrow(() -> new RuntimeException("해당하는 사원 없음"));

        Department department = departmentRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("해당하는 부서 없음"));

        employee.setDepartment(department);

        employeeRepository.save(employee);
        //when

        Department foundDept = departmentRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("음슴"));

        // 1번 부서 정보를 조회해서 모든 사원을 보겠다.
        List<Employee> employees = foundDept.getEmployees();
        employees.forEach(e -> {
            System.out.println("e = " + e);
        });

        //then
    }
}