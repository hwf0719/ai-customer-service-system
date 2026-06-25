package com.example.aics.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.aics.dto.page.KnowledgePageQueryDTO;
import com.example.aics.entity.Knowledge;
import com.example.aics.mapper.KnowledgeMapper;
import com.example.aics.service.KnowledgeService;
import com.example.aics.utils.KnowledgeConverter;
import com.example.aics.vo.knowledge.KnowledgeVO;
import com.example.aics.vo.page.PageResultVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KnowledgeServiceImpl extends ServiceImpl<KnowledgeMapper, Knowledge> implements KnowledgeService {
    @Override
    public void addKnowledge(String question, String answer, String category) {
        Knowledge knowledge = Knowledge.builder()
                .question(question)
                .answer(answer)
                .category(category)
                .build();
        save(knowledge);
    }

    @Override
    public void deleteKnowledge(Long id) {
        removeById(id);
    }

    @Override
    public List<Knowledge> search(String query, int topK) {
        LambdaQueryWrapper<Knowledge> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(Knowledge::getQuestion, query)
                .last("LIMIT " + topK);
        return list(wrapper);
    }

    @Override
    public PageResultVO<KnowledgeVO> queryPage(KnowledgePageQueryDTO dto) {
        // 1. 创建分页对象
        Page<Knowledge> page = new Page<>(dto.getPage(), dto.getPageSize());

        // 2. 构建查询条件
        LambdaQueryWrapper<Knowledge> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(dto.getKeyword()),
                Knowledge::getQuestion, dto.getKeyword());
        wrapper.orderByDesc(Knowledge::getCreateTime);

        // 3. 执行分页查询
        Page<Knowledge> result = page(page, wrapper);

        // 4. 转换为 VO
        List<KnowledgeVO> voList = KnowledgeConverter.toVOList(result.getRecords());

        // 5. 封装返回
        PageResultVO<KnowledgeVO> pageResult = new PageResultVO<>();
        pageResult.setRecords(voList);
        pageResult.setTotal(result.getTotal());
        pageResult.setPage(dto.getPage());
        pageResult.setPageSize(dto.getPageSize());
        return pageResult;
    }
}
