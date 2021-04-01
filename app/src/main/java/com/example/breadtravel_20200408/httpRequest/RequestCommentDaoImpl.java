package com.example.breadtravel_20200408.httpRequest;

import java.util.Map;

public class RequestCommentDaoImpl implements RequestCommentDao {
    protected static final String HOST = "http://10.70.48.215:8080/BreadTravel_20200408_war_exploded/";
    @Override
    public String queryComment(Map<String, String> params) {
        String host = HOST + "CommentServ?act=queryComment";
        String result = HttpGet.get(host, params);
        return result;
    }

    @Override
    public String addComment(Map<String, String> params) {
        String host = HOST + "CommentServ?act=addComment";
        String result = HttpGet.get(host, params);
        return result;
    }
}
