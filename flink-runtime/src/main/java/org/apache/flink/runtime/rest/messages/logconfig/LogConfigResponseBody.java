package org.apache.flink.runtime.rest.messages.logconfig;

import org.apache.flink.runtime.rest.messages.ResponseBody;

public class LogConfigResponseBody implements ResponseBody {
	public static final String STATUS_SUCCESS = "success";
	public static final String STATUS_ERROR = "error";

	private String status; // success, error
	private String message; //

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
}
