package org.acme.models;

public class Branches {
    public Long branch_cd;
    public String branch_name;

    public Long getBranch_cd() {
        return branch_cd;
    }

    public void setBranch_cd(Long branch_cd) {
        this.branch_cd = branch_cd;
    }

    public String getBranch_name() {
        return branch_name;
    }

    public void setBranch_name(String branch_name) {
        this.branch_name = branch_name;
    }
}
