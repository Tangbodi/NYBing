package com.example.demo.Entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Getter
@Setter
@Entity
@Table(name = "sub_category_post_map")
public class SubCategoryPostMap {
    @EmbeddedId
    private SubCategoryPostMapId id;

    @Size(max = 36)
    @Column(name = "commentId", length = 36)
    private String commentId;

}