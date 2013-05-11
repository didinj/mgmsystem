package com.djamware.model;

import java.io.Serializable;

import javax.persistence.Id;

/**
 * @author Didin
 * 
 */
public class Users implements Serializable {
	private static final long serialVersionUID = -8952392499312695985L;
	@Id
	Long id;
	String username;
	String password;
	String fullname;
	Integer roleId;

	public Users() {
	}

	public Users(String username, String password, String fullname,
			Integer roleId) {
		this();
		this.username = username;
		this.password = password;
		this.fullname = fullname;
		this.roleId = roleId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public Long getId() {
		return id;
	}

}
