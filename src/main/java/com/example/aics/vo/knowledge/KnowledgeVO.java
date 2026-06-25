package com.example.aics.vo.knowledge;

import lombok.Data;

@Data
public class KnowledgeVO {
    private Long id;
    private String question;
    private String answer;
    private String category;
}
