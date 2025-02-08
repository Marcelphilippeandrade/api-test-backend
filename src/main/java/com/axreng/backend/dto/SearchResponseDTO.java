package com.axreng.backend.dto;

import java.util.ArrayList;
import java.util.List;

public class SearchResponseDTO {
	private String id = "";
	private String status = "";
	private List<String> urls = new ArrayList<String>();
	private int statusCode = 0;

	public SearchResponseDTO(String id, String status, List<String> urls, int statusCode) {
		this.id = id;
		this.status = status;
		this.urls = urls;
		this.statusCode = statusCode;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public String getId() {
		return id;
	}

	public String getStatus() {
		return status;
	}

	public List<String> getUrls() {
		return urls;
	}
}
