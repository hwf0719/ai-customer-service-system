package com.example.aics.vo.page;

import lombok.Data;

import java.util.List;

@Data
public class PageResultVO<T>{
    private Long total;
    private Integer page;
    private Integer pageSize;
    private List<T> records;

}