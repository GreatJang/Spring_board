package com.encore.board.author.domain;

import lombok.Data;

@Data
public class AuthorSaveReqDto {
    private String name;
    private String email;
    private String password;
}
