package com.passwordentropy.mapreduce.dto;


public class PasswordStrengthDto {
	private String password;
	private long strength;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public long getStrength() {
		return strength;
	}

	public void setStrength(long strength) {
		this.strength = strength;
	}

	public PasswordStrengthDto(String password, long strength) {
		super();
		this.password = password;
		this.strength = strength;
	}

}
