package com.example.demo.Entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "parent_comments_map")
public class ParentCommentsMap {
    @EmbeddedId
    private ParentCommentsMapId id;

    //TODO [JPA Buddy] generate columns from DB
}