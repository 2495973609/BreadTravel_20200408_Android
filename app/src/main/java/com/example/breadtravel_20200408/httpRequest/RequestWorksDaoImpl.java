package com.example.breadtravel_20200408.httpRequest;

import java.util.Map;

public class RequestWorksDaoImpl implements RequestWorksDao {

    protected static final String HOST = "http://10.70.48.215:8080/BreadTravel_20200408_war_exploded/";

    @Override
    public String recommendPreview(Map<String, String> params) {
        String host=HOST+"WorksServ?act=recommendPreview";
        String result=HttpGet.get(host,params);
        return result;
    }

    @Override
    public String attentionPreview(Map<String, String> params) {
        String host=HOST+"WorksServ?act=attentionPreview";
        String result=HttpGet.get(host,params);
        return result;
    }

    @Override
    public String praisePreview(Map<String, String> params) {
        String host=HOST+"WorksServ?act=praisePreview";
        String result=HttpGet.get(host,params);
        return result;
    }

    @Override
    public String myWorksPreview(Map<String, String> params) {
        String host=HOST+"WorksServ?act=myWorksPreview";
        String result=HttpGet.get(host,params);
        return result;
    }

    @Override
    public String worksContent(Map<String, String> params) {
        String host=HOST+"WorksServ?act=worksContent";
        String result=HttpGet.get(host,params);
        return result;
    }

    @Override
    public String initRelation(Map<String, String> params) {
        String host=HOST+"WorksServ?act=initRelation";
        String result=HttpGet.get(host,params);
        return result;
    }

    @Override
    public String upLoadWorksPreview(Map<String, String> params) {
        String host=HOST+"WorksServ?act=upLoadWorksPreview";
        String result=HttpGet.get(host,params);
        return result;
    }

    @Override
    public String upLoadWorksContent(Map<String, String> params) {
        String host=HOST+"WorksServ?act=upLoadWorksContent";
        String result=HttpGet.get(host,params);
        return result;
    }

    @Override
    public String deleteWorks(Map<String, String> params) {
        String host=HOST+"WorksServ?act=deleteWorks";
        String result=HttpGet.get(host,params);
        return result;
    }

    @Override
    public void praise(Map<String, String> params) {
        String host=HOST+"WorksServ?act=praise";
        HttpGet.get(host,params);
    }

    @Override
    public void attention(Map<String, String> params) {
        String host=HOST+"WorksServ?act=attention";
        HttpGet.get(host,params);
    }

    @Override
    public String cityPreview(Map<String, String> params) {
        String host=HOST+"WorksServ?act=cityPreview";
        String result=HttpGet.get(host,params);
        return result;
    }

}
