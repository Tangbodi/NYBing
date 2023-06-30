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
@Table(name = "images")
public class Image {
    @Id
    @Size(max = 36)
    @Column(name = "imageId", nullable = false, length = 36)
    private String id;

    @Size(max = 45)
    @NotNull
    @Column(name = "imageType", nullable = false, length = 45)
    private String imageType;

    @Size(max = 90)
    @NotNull
    @Column(name = "imagePath", nullable = false, length = 90)
    private String imagePath;

    @Size(max = 90)
    @NotNull
    @Column(name = "imageURL", nullable = false, length = 90)
    private String imageURL;

}