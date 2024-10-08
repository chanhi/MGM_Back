package com.example.MultiGreenMaster.dto;

import com.example.MultiGreenMaster.entity.AnnounceBoardENT;
import lombok.*;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class AnnounceBoardFRM {
    private Long id;
    private String title; // 제목을 받을 필드
    private String content; // 내용을 받을 필드

    public AnnounceBoardENT toEntity() {
        return AnnounceBoardENT.builder()
                .id(this.id)
                .title(this.title)
                .content(this.content)
                .disable(false)
                .build();
    }



}
