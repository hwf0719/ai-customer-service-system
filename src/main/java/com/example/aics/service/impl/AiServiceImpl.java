package com.example.aics.service.impl;

import com.example.aics.service.AiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Slf4j
@Service
public class AiServiceImpl implements AiService {
    @Autowired
    private RestTemplate restTemplate;
    @Value("${ai-service.base-url}")
    private String aiServiceUrl;
    @Override
    public String classify(String title, String description) {
        Map<String,String> request=new HashMap<>();
        request.put("title",title);
        request.put("description",description);
        try{
            Map response=restTemplate.postForObject(
                    aiServiceUrl+"/classify",
                    request,
                    Map.class
            );
            return (String) response.get("category");

        } catch (Exception e) {
            log.error("ai服务调用失败", e);
            return "OTHER";
        }

    }

    @Override
    public String sentiment(String content) {
        Map<String, String> request = new HashMap<>();
        request.put("content", content);
        try {
            Map response = restTemplate.postForObject(
                    aiServiceUrl + "/sentiment",
                    request,
                    Map.class
            );
            return (String) response.get("sentiment");
        } catch (Exception e) {
            log.error("ai服务调用失败", e);
            return "NEUTRAL";
        }
    }

    @Override
    public String summary(List<Map<String, String>> messages) {
        Map<String, Object> request = new HashMap<>();
        request.put("messages", messages);
        try{
            Map response = restTemplate.postForObject(
                    aiServiceUrl + "/summary",
                    request,
                    Map.class
            );
            return (String) response.get("summary");
        }catch (Exception e){
            log.error("ai服务调用失败",e);
            return "无";
        }
    }
}
