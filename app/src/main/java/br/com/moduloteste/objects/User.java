package br.com.moduloteste.objects;

import org.json.JSONArray;
import java.util.List;

/**
 * Created by claudio on 19/05/2015.
 */
public class User {

    private String id;
    private String name;
    private String login;
    private String email;
    private List<String> member_of;

    public List<String> getMember_of() {
        return member_of;
    }

    public void setMember_of(List<String> member_of) {
        this.member_of = member_of;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User degenerateJson (User user, String content){
        try{
            JSONArray ja = new JSONArray(content);

            for (int i = 0, tam = ja.length(); i < tam; i++){
                user.getMember_of().add(ja.getJSONObject(i).getString("Name"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return user;
    }
}
