package com.example.demo.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentResDTO<T> {
    private int code;
    private String message;
    private T data;

    public CommentResDTO success(){
        CommentResDTO commentResDTO = new CommentResDTO();
        commentResDTO.setCode(200);
        commentResDTO.setMessage("SUCCESSFUL");
        return commentResDTO;
    }
    public <T> CommentResDTO success(T data){
        CommentResDTO commentResDTO = new CommentResDTO();
        commentResDTO.setCode(200);
        commentResDTO.setMessage("SUCCESSFUL");
        commentResDTO.setData(data);
        return commentResDTO;
    }
}
