package com.example.breadtravel_20200408.httpRequest;

import java.util.Map;

public interface RequestWorksDao {
    String recommendPreview(Map<String, String> params);
    String attentionPreview(Map<String, String> params);
    String praisePreview(Map<String,String> params);
    String myWorksPreview(Map<String,String> params);
    String worksContent(Map<String,String> params);
    String initRelation(Map<String,String> params);
    String upLoadWorksPreview(Map<String,String> params);
    String upLoadWorksContent(Map<String,String> params);
    String deleteWorks(Map<String,String> params);
    void praise(Map<String,String> params);
    void attention(Map<String,String> params);
    String cityPreview(Map<String,String> params);
}
