package com.nit.responsehandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseHandler {

    private String message;
    private boolean status;
    private Object data;
    private Integer totalRecord;

 
    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getTotalRecord() {
        return totalRecord;
    }

    public void setTotalRecord(Integer totalRecord) {
        this.totalRecord = totalRecord;
    }

 
    public static ResponseEntity<Object> generateResponse(String message, HttpStatus status, Object responseObj) {
        ResponseHandler response = new ResponseHandler();
        response.setMessage(message);
        response.setStatus(true);
        response.setData(responseObj);
        response.setTotalRecord(null); 
        return new ResponseEntity<>(response, status);
    }


    public static ResponseEntity<Object> generateErrorResponse(String message, HttpStatus status) {
        ResponseHandler response = new ResponseHandler();
        response.setMessage(message);
        response.setStatus(false);
        response.setData(null);
        response.setTotalRecord(0);
        return new ResponseEntity<>(response, status);
    }
}
