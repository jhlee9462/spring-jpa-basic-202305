package com.study.jpa.chap01_basic.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString @EqualsAndHashCode
@Builder
@Entity
@Table(name = "tbl_product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prod_id")
    private Long id;
    @Column(name = "prod_nm", nullable = false, length = 30)
    private String name;

    @Builder.Default
    private int price = 0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @UpdateTimestamp
    @Column(updatable = false)
    private LocalDateTime updateDate;

    public enum Category {
        FOOD, FASHION, ELECTRONIC
    }

}
