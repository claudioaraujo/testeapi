package br.com.moduloteste.objects;

import org.json.JSONObject;

/**
 * Created by claudio.araujo on 15/05/2015.
 */
public class Asset {
    private String name;
    private String type;
    private String relevance;
    private String perimeter;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRelevance() {
        return relevance;
    }

    public void setRelevance(String relevance) {
        this.relevance = relevance;
    }

    public String getPerimeter() {
        return perimeter;
    }

    public void setPerimeter(String perimeter) {
        this.perimeter = perimeter;
    }

    public String generateJSON(Asset asset){
        JSONObject jo = new JSONObject();

        try{
            jo.put("Name", asset.getName());
            jo.put("Path", asset.getPerimeter());
            jo.put("AssetType", asset.getType());
            jo.put("Relevance", asset.getRelevance());
        }catch (Exception e){
            e.printStackTrace();
        }

        return jo.toString();
    }
}
