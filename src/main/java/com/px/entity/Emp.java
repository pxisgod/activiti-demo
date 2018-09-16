package com.px.entity;

public class Emp {
    private Integer id;

    private Integer smallboss;

    private Integer bigboss;

    private String name;

    private String password;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSmallboss() {
        return smallboss;
    }

    public void setSmallboss(Integer smallboss) {
        this.smallboss = smallboss;
    }

    public Integer getBigboss() {
        return bigboss;
    }

    public void setBigboss(Integer bigboss) {
        this.bigboss = bigboss;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }
}