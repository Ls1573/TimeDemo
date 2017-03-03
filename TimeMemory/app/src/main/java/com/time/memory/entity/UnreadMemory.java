package com.time.memory.entity;

import java.util.List;

/**
 * Created by Administrator on 2016/10/31.
 */
public class UnreadMemory extends BaseEntity {

    public UnreadMemory(){

    }
    private String code;
    private String clientUserId;
    private List<Reminds> reminds;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getClientUserId() {
        return clientUserId;
    }

    public void setClientUserId(String clientUserId) {
        this.clientUserId = clientUserId;
    }

    public List<Reminds> getReminds() {
        return reminds;
    }

    public void setReminds(List<Reminds> reminds) {
        this.reminds = reminds;
    }
}
