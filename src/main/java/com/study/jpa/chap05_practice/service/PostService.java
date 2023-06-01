package com.study.jpa.chap05_practice.service;

import com.study.jpa.chap05_practice.dto.*;
import com.study.jpa.chap05_practice.entity.HashTag;
import com.study.jpa.chap05_practice.entity.Post;
import com.study.jpa.chap05_practice.repository.HashTagRepository;
import com.study.jpa.chap05_practice.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional // JPA 레파지토리를 사용하는 빈은 반드시 트랙잭션 어노테이션 필수
public class PostService {

    private final PostRepository postRepository;
    private final HashTagRepository hashTagRepository;

    public PostListResponseDTO getPosts(PageDTO pageDTO) {

        // 데이터베이스에서 게시물 목록 조회
        Page<Post> posts = postRepository.findAll(PageRequest.of(pageDTO.getPage() - 1,
                pageDTO.getSize(),
                Sort.by("createDate").descending())
        );

        // DB 에서 조회한 정보를 JSON 형태에 맞는 DTO로 변환
        return PostListResponseDTO.builder()
                .count(posts.getSize()) // 조회된 게시물 수
                .pageInfo(new PageResponseDTO<>(posts))
                .posts(posts.getContent().stream()
                        .map(PostDetailResponseDTO::new)
                        .collect(Collectors.toList()))
                .build();
    }

    public PostDetailResponseDTO getDetail(Long id) {
        return new PostDetailResponseDTO(postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        id + "번 게시물이 존재하지 않습니다!"
                )));
    }

    public PostDetailResponseDTO insert(final PostCreateDTO dto)
            throws RuntimeException {

        // 게시물 저장
        Post post = postRepository.save(dto.toEntity());

        // 해시태그 저장
        List<String> hashTags = dto.getHashTags();
        if (hashTags != null && hashTags.size() > 0) {
            hashTags.forEach(h -> {
                HashTag hashTag = HashTag.builder()
                        .tagName(h)
                        .post(post)
                        .build();
                hashTagRepository.save(hashTag);

                post.addHashTag(hashTag);
            });
        }

        return new PostDetailResponseDTO(post);
    }
}
