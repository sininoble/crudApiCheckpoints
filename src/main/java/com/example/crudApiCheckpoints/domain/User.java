package com.example.crudApiCheckpoints.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;


@Entity
@NamedQuery(name="User.getByEmailId",
    query="select u from User u where u.email=?1")
@NamedQuery(name="User.updateEmailOnly",
        query="Update User u set u.email=?2 where u.id=?1")
@NamedQuery(name="User.updateUser"
        ,query="Update User u set u.email=?2 , u.password=?3 where u.id=?1")

public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;

    private String email;


    private String password;

   public User(){

    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @JsonProperty
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString(){
        return "USer [id =" + id + ", email=" + email + "]";
    }

}
