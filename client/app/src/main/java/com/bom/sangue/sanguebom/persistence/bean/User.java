package com.bom.sangue.sanguebom.persistence.bean;

import com.bom.sangue.sanguebom.provider.DBProvider;

/**
 * Created by alan on 25/11/15.
 */
public class User implements Persistent{

    private Long id;
    private String login;
    private String password;
    private String email;
    private String token;

    public User(String login, String password, String email, String token) {
        this.login = login;
        this.password = password;
        this.token = token;
        this.email = email;
    }

    public User(String login, String password, String email) {
        this.login = login;
        this.password = password;
        this.email = email;
    }

    public User(String token) {
        this.token = token;
    }

    public User() {
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
