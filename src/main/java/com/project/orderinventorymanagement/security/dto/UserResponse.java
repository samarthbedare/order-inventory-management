package com.project.orderinventorymanagement.security.dto;

public class UserResponse {
    private Integer id;
    private String username;

    public UserResponse() {}

    public UserResponse(Integer id, String username) {
        this.id = id;
        this.username = username;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
