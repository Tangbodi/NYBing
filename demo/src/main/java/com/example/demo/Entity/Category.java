package com.example.demo.Entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name = "category")
public class Category {
    @Id
    @Column(name = "categoryId", nullable = false)
    private Integer id;

    @Size(max = 30)
    @Column(name = "categoryName", length = 30)
    private String categoryName;

    @Size(max = 15)
    @Column(name = "categoryAlias", length = 15)
    private String categoryAlias;

    @Size(max = 255)
    @Column(name = "categoryDescription")
    private String categoryDescription;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryAlias() {
        return categoryAlias;
    }

    public void setCategoryAlias(String categoryAlias) {
        this.categoryAlias = categoryAlias;
    }

    public String getCategoryDescription() {
        return categoryDescription;
    }

    public void setCategoryDescription(String categoryDescription) {
        this.categoryDescription = categoryDescription;
    }

}