package com.example.demo.Entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "category_sub_map")
public class CategorySubMap {
    @EmbeddedId
    private CategorySubMapId id;
    //TODO [JPA Buddy] generate columns from DB
}