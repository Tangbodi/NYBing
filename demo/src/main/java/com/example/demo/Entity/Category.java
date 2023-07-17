package com.example.demo.Entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import org.springframework.data.redis.core.RedisHash;
@Getter
@Setter
@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "categoryId", nullable = false)
    private Integer id;

    @Size(max = 60)
    @Column(name = "categoryName", length = 60)
    private String categoryName;

}