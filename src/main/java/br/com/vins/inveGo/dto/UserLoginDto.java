package br.com.vins.inveGo.dto;

public class UserLoginDto {
	private String registrationNumber;
	private String password;
	
	public UserLoginDto(String registrationNumber, String password) {
		super();
		this.registrationNumber = registrationNumber;
		this.password = password;
	}
	public String getRegistrationNumber() {
		return registrationNumber;
	}
	public void setRegistrationNumber(String registrationNumber) {
		this.registrationNumber = registrationNumber;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
