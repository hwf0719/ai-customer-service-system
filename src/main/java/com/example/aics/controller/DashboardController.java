package com.example.aics.controller;

import com.example.aics.common.Result;
import com.example.aics.service.DashboardService;
import com.example.aics.vo.dashboard.StatsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {
    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/stats")
    public Result<StatsVO> getStats(){
        return Result.success(dashboardService.getStats());
    }
}
