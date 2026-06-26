package com.example.aics.service;

import java.util.List;
import java.util.Map;

public interface AiService {
    String classify(String title,String description);
    String sentiment(String content);
    String summary(List<Map<String,String>> messages);
}
