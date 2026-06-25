package com.example.aics.controller;

import com.example.aics.common.Result;
import com.example.aics.dto.knowledge.KnowledgeDTO;
import com.example.aics.dto.page.KnowledgePageQueryDTO;
import com.example.aics.entity.Knowledge;
import com.example.aics.service.KnowledgeService;
import com.example.aics.utils.KnowledgeConverter;
import com.example.aics.vo.knowledge.KnowledgeVO;
import com.example.aics.vo.page.PageResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/knowledge")
public class KnowledgeController {

    @Autowired
    private KnowledgeService knowledgeService;

    @PostMapping("")
    public Result<Void> addKnowledge(@RequestBody @Valid KnowledgeDTO dto) {
        knowledgeService.addKnowledge(dto.getQuestion(), dto.getAnswer(), dto.getCategory());
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteKnowledge(@PathVariable Long id) {
        knowledgeService.deleteKnowledge(id);
        return Result.success();
    }

    @GetMapping("/search")
    public Result<List<KnowledgeVO>> search(@RequestParam String query, @RequestParam(defaultValue = "3") int topK) {
        List<Knowledge> list = knowledgeService.search(query, topK);
        return Result.success(KnowledgeConverter.toVOList(list));
    }

    @GetMapping("/list")
    public Result<PageResultVO<KnowledgeVO>> listKnowledge(KnowledgePageQueryDTO dto) {
        return Result.success(knowledgeService.queryPage(dto));
    }
}
