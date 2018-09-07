package com.truelight.cacihymn.Http;

import com.truelight.cacihymn.Models.Hymn;

import java.util.List;

public class Hymresponse {
    private String code;
    private String status;
    private String message;
    private List<Hymn> data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Hymn> getData() {
        return data;
    }

    public void setData(List<Hymn> data) {
        this.data = data;
    }
}
