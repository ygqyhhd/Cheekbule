package com.meeblue.celsius.entity;

import java.io.Serializable;


public class User implements Serializable {


    private  int id;
    private  String name;
    private String sex;
    private  int age;

    public User(){

    }

    public User(String name,String sex,int age){
        this.name=name;
        this.age=age;
        this.sex=sex;
    }
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSex() {
        return sex;
    }

    public int getAge() {
        return age;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
