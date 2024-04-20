package br.com.vins.inveGo.dto;

public class UserRegisterDto {
	private String name;
	private String lastName;
	private String registrationNumber;
	private String password;
	public UserRegisterDto(String name, String lastName, String registrationNumber, String password) {
		super();
		this.name = name;
		this.lastName = lastName;
		this.registrationNumber = registrationNumber;
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
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
