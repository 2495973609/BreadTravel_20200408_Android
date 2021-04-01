package com.example.breadtravel_20200408.httpRequest;

import java.util.Map;

public interface RequestCommentDao {
    String queryComment(Map<String, String> params);
    String addComment(Map<String,String> params);
}
