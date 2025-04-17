package com.spring.privateClinicManage.custom.response.HFResponse;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MessageDto {
    private String role;
    private List<ContentDto> content;
}
