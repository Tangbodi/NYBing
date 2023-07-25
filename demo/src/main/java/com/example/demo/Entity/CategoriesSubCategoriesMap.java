package com.example.demo.Entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "categories_sub_categories_map")
public class CategoriesSubCategoriesMap {
    @EmbeddedId
    private CategoriesSubCategoriesMapId id;

    //TODO [JPA Buddy] generate columns from DB
}