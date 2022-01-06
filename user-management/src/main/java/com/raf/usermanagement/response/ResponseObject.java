package com.raf.usermanagement.response;

import java.util.HashMap;

import lombok.Data;

@Data
public class ResponseObject {
    
    private String status;
    private HashMap<String, Object> data;

}
