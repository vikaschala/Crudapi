package com.nit.responsehandler;

public class ResponseHandler{
String message;
boolean status;
private Object data;
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

Integer totalRecord;

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



}
