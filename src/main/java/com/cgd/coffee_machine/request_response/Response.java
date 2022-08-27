package com.cgd.coffee_machine.request_response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data @AllArgsConstructor
public class Response implements Serializable {

    private boolean status;
    private String msg;
    private Object data;

    public Response(boolean status, String msg){
        this.status = status;
        this.msg = msg;
    }


}
