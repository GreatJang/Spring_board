package com.encore.board.post.dto;

import lombok.Data;

@Data
public class PostSaveReqDto {
    private String title;
    private String contents;
}
