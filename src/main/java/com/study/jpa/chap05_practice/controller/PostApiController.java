package com.study.jpa.chap05_practice.controller;

import com.study.jpa.chap05_practice.dto.*;
import com.study.jpa.chap05_practice.service.PostService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostApiController {

    // 리소스 : 게시물 (Post)
/*
     게시물 목록 조회:  /posts       - GET
     게시물 개별 조회:  /posts/{id}  - GET
     게시물 등록:      /posts       - POST
     게시물 수정:      /posts/{id}  - PATCH
     게시물 삭제:      /posts/{id}  - DELETE
 */

    private final PostService postService;

    // 게시물 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        log.info("/api/v1/posts/{} DELETE", id);

        try {
            postService.remove(id);
            return ResponseEntity.ok("DELETE SUCCESS");
        } catch (Exception e) {
            return ResponseEntity
                    .internalServerError()
                    .body(e.getMessage());
        }

    }

    // 게시물 수정
    @RequestMapping(method = {RequestMethod.PUT, RequestMethod.PATCH})
    public ResponseEntity<?> update(@RequestBody @Validated PostModifyDTO dto
            , BindingResult result
            , HttpServletRequest request) {

        log.info("/api/v1/posts {} - dto : {}", request.getMethod(), dto);

        ResponseEntity<List<FieldError>> fieldErrors = getValidateResult(result);
        if (fieldErrors != null) return fieldErrors;

        PostDetailResponseDTO responseDTO = null;
        try {
            responseDTO = postService.modify(dto);
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            return ResponseEntity
                    .internalServerError()
                    .body(e.getMessage());
        }

    }

    // 게시물 등록
    @Parameters({
            @Parameter(name = "작성자", description = "게시물의 작성자 이름을 쓰세요", example = "김뚜루", required = true)
            , @Parameter(name = "title", description = "게시물의 제목을 쓰세요", example = "뚯뚜루", required = true)
            , @Parameter(name = "content", description = "게시물의 내용을 쓰세요", example = "냉무")
            , @Parameter(name = "hastTags", description = "게시물의 해시태그를 쓰세요", example = "해시태그")
    })
    @PostMapping
    public ResponseEntity<?> create(
            @Validated @RequestBody PostCreateDTO dto
            , BindingResult result // 검증 에러 정보를 가진 객체
    ) {
        log.info("/api/v1/post POST - payload: {}", dto);

        if (dto == null) {
            return ResponseEntity
                    .badRequest()
                    .body("등록 게시물 정보를 전달해주세요!");
        }

        ResponseEntity<List<FieldError>> fieldErrors = getValidateResult(result);
        if (fieldErrors != null) return fieldErrors;

        PostDetailResponseDTO responseDTO = null;
        try {
            responseDTO = postService.insert(dto);
            return ResponseEntity.ok().body(responseDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError()
                    .body("원인 불명의 에러 발생!! 메세지 : " + e.getMessage());
        }

    }

    // 입력값 검증 확인 메서드
    private static ResponseEntity<List<FieldError>> getValidateResult(BindingResult result) {
        if (result.hasErrors()) { // 입력값 검증에 걸림
            List<FieldError> fieldErrors = result.getFieldErrors();

            fieldErrors.forEach(e -> {
                log.warn("invalid client data - {}", e.toString());
            });

            return ResponseEntity
                    .badRequest()
                    .body(fieldErrors);
        }
        return null;
    }

    // 하나 조회
    @GetMapping("/{id}")
    public ResponseEntity<?> detail(@PathVariable Long id) {
        log.info("/api/v1/posts/{} GET", id);

        PostDetailResponseDTO dto = null;
        try {
            dto = postService.getDetail(id);
            return ResponseEntity.ok().body(dto);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 전체 조회
    @GetMapping
    public ResponseEntity<?> list(PageDTO pageDTO) {
        log.info("/api/v1/posts?page={}&size={} GET", pageDTO.getPage(), pageDTO.getSize());

        PostListResponseDTO dto = postService.getPosts(pageDTO);

        return ResponseEntity
                .ok()
                .body(dto);
    }
}
