package com.example.aics.dto.page;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class KnowledgePageQueryDTO extends PageQueryDTO{
    private String keyword;
}
