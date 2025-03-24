package org.acme.models;

public class Users {
    public Long user_id;
    public Long branch_cd;

    public Long getBranch_cd() {
        return branch_cd;
    }

    public void setBranch_cd(Long branch_cd) {
        this.branch_cd = branch_cd;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public Long getRole_id() {
        return role_id;
    }

    public void setRole_id(Long role_id) {
        this.role_id = role_id;
    }

    public Long role_id;
}
