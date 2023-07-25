package com.example.demo.Entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@Entity
@Table(name = "sub_categories")
public class SubCategory {
    @Id
    @Column(name = "sub_categoryId", nullable = false)
    private Short id;

    @NotNull
    @Size(max = 63)
    @Column(name = "sub_categoryName", length = 63)
    private String subCategoryname;

}