package br.com.vins.inveGo.entities;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "userHistory")
public class UserHistory {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nameUserRemoved; 
    
    @Column(nullable = false)
    private String lastNameUserRemoved; 
    
    @Column(nullable = false)
    private String registrationNumberUserRemoved; 
    
    @Column(nullable = false)
    private String registrationNumberResponsibleUser; 
    
    @Column(nullable = false)
    private LocalDateTime changeDateTime;

	public UserHistory() {
		super();
	}

	public UserHistory(Long id, String nameUserRemoved, String lastNameUserRemoved,
			String registrationNumberUserRemoved, String registrationNumberResponsibleUser,
			LocalDateTime changeDateTime) {
		super();
		this.id = id;
		this.nameUserRemoved = nameUserRemoved;
		this.lastNameUserRemoved = lastNameUserRemoved;
		this.registrationNumberUserRemoved = registrationNumberUserRemoved;
		this.registrationNumberResponsibleUser = registrationNumberResponsibleUser;
		this.changeDateTime = changeDateTime;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNameUserRemoved() {
		return nameUserRemoved;
	}

	public void setNameUserRemoved(String nameUserRemoved) {
		this.nameUserRemoved = nameUserRemoved;
	}

	public String getLastNameUserRemoved() {
		return lastNameUserRemoved;
	}

	public void setLastNameUserRemoved(String lastNameUserRemoved) {
		this.lastNameUserRemoved = lastNameUserRemoved;
	}

	public String getRegistrationNumberUserRemoved() {
		return registrationNumberUserRemoved;
	}

	public void setRegistrationNumberUserRemoved(String registrationNumberUserRemoved) {
		this.registrationNumberUserRemoved = registrationNumberUserRemoved;
	}

	public String getRegistrationNumberResponsibleUser() {
		return registrationNumberResponsibleUser;
	}

	public void setRegistrationNumberResponsibleUser(String registrationNumberResponsibleUser) {
		this.registrationNumberResponsibleUser = registrationNumberResponsibleUser;
	}

	public LocalDateTime getChangeDateTime() {
		return changeDateTime;
	}

	public void setChangeDateTime(LocalDateTime changeDateTime) {
		this.changeDateTime = changeDateTime;
	}
    
    
}
