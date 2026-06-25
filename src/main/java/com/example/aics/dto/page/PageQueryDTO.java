package com.example.aics.dto.page;

import lombok.Data;

@Data
public class PageQueryDTO {
    private Integer page = 1;
    private Integer pageSize = 10;
}
