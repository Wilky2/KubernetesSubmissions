package com.wilky.logoutputapp;

public class CurrentStatus {

	private String randomString;

	private String timestamp;

	private long pingpong;

	public CurrentStatus() {
		this.randomString = "";
		this.timestamp = "";
		this.pingpong = 0;
	}

	public String getRandomString() {
		return this.randomString;
	}

	public void setRandomString(String randomString) {
		this.randomString = randomString;
	}

	public String getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public long getPingpong() {
		return pingpong;
	}

	public void setPingpong(long pingpong) {
		this.pingpong = pingpong;
	}

}
