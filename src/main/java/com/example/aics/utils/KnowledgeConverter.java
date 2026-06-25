package com.example.aics.utils;

import com.example.aics.entity.Knowledge;
import com.example.aics.vo.knowledge.KnowledgeVO;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Knowledge 实体与 VO 转换工具类
 */
public class KnowledgeConverter {

    private KnowledgeConverter() {}

    /**
     * Knowledge -> KnowledgeVO
     */
    public static KnowledgeVO toVO(Knowledge knowledge) {
        if (knowledge == null) {
            return null;
        }
        KnowledgeVO vo = new KnowledgeVO();
        vo.setId(knowledge.getId());
        vo.setQuestion(knowledge.getQuestion());
        vo.setAnswer(knowledge.getAnswer());
        vo.setCategory(knowledge.getCategory());
        return vo;
    }

    /**
     * List<Knowledge> -> List<KnowledgeVO>
     */
    public static List<KnowledgeVO> toVOList(List<Knowledge> list) {
        if (list == null) {
            return Collections.emptyList();
        }
        return list.stream()
                .map(KnowledgeConverter::toVO)
                .collect(Collectors.toList());
    }
}
