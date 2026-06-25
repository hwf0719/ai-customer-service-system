package com.example.aics.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.aics.dto.page.KnowledgePageQueryDTO;
import com.example.aics.entity.Knowledge;
import com.example.aics.vo.knowledge.KnowledgeVO;
import com.example.aics.vo.page.PageResultVO;

import java.util.List;

public interface KnowledgeService extends IService<Knowledge> {

    /**
     * 添加知识库条目（同步到向量数据库）
     */
    void addKnowledge(String question, String answer, String category);

    /**
     * 删除知识库条目
     */
    void deleteKnowledge(Long id);

    /**
     * 搜索相关知识（RAG检索）
     */
    List<Knowledge> search(String query, int topK);

    /**
     * 分页查询知识库
     */
    PageResultVO<KnowledgeVO> queryPage(KnowledgePageQueryDTO dto);
}