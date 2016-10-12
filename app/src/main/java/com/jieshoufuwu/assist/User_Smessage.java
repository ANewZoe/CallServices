package com.jieshoufuwu.assist;

import java.io.Serializable;

@SuppressWarnings("serial")
public class User_Smessage implements Serializable {
	private String id;
	private String nickname;
	private String face;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getFace() {
		return face;
	}

	public void setFace(String face) {
		this.face = face;
	}

}
