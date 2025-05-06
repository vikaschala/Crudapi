package com.nit.entity;

public class UserListing {

	private int pageNo;

	private int pageSize;

	private String sortBy;

	private String sortOrder;


	private UserFilter userFilter;
	
	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public String getSortBy() {
		return sortBy;
	}

	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}

	public String getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}

	public UserFilter getUserFilter() {
		return userFilter;
	}

	public void setUserFilter(UserFilter userFilter) {
		this.userFilter = userFilter;
	}
}