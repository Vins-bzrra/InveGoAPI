package br.com.vins.inveGo.entities;

import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "movementHistory")
public class ItemMovementHistory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String previousOwner;

	@Column(nullable = false)
	private String previousUnitLocation;

	@Column(nullable = false)
	private String newOwner;

	@Column(nullable = false)
	private String newUnitLocation;

	@Column(nullable = false)
	private String movementType; 

	@Column(nullable = false)
	private String nameUser;
	
	@Column(nullable = false)
	private String registrationUser;

	@Column(nullable = false)
	private LocalDateTime movementDateTime;

	@ManyToOne
	@JoinColumn(name = "item_id", referencedColumnName = "id")
	@JsonIgnore
	private Item item;

	public ItemMovementHistory() {
		super();
	}

	

	public ItemMovementHistory(Long id, String previousOwner, String previousUnitLocation, String newOwner,
			String newUnitLocation, String movementType, String nameUser, String registrationUser,
			LocalDateTime movementDateTime, Item item) {
		super();
		this.id = id;
		this.previousOwner = previousOwner;
		this.previousUnitLocation = previousUnitLocation;
		this.newOwner = newOwner;
		this.newUnitLocation = newUnitLocation;
		this.movementType = movementType;
		this.nameUser = nameUser;
		this.registrationUser = registrationUser;
		this.movementDateTime = movementDateTime;
		this.item = item;
	}



	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPreviousOwner() {
		return previousOwner;
	}

	public void setPreviousOwner(String previousOwner) {
		this.previousOwner = previousOwner;
	}

	public String getPreviousUnitLocation() {
		return previousUnitLocation;
	}

	public void setPreviousUnitLocation(String previousUnitLocation) {
		this.previousUnitLocation = previousUnitLocation;
	}

	public String getNewOwner() {
		return newOwner;
	}

	public void setNewOwner(String newOwner) {
		this.newOwner = newOwner;
	}

	public String getNewUnitLocation() {
		return newUnitLocation;
	}

	public void setNewUnitLocation(String newUnitLocation) {
		this.newUnitLocation = newUnitLocation;
	}

	public String getMovementType() {
		return movementType;
	}

	public void setMovementType(String movementType) {
		this.movementType = movementType;
	}

	public String getNameUser() {
		return nameUser;
	}

	public void setNameUser(String nameUser) {
		this.nameUser = nameUser;
	}

	public String getRegistrationUser() {
		return registrationUser;
	}

	public void setRegistrationUser(String registrationUser) {
		this.registrationUser = registrationUser;
	}

	public LocalDateTime getMovementDateTime() {
		return movementDateTime;
	}

	public void setMovementDateTime(LocalDateTime movementDateTime) {
		this.movementDateTime = movementDateTime;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ItemMovementHistory other = (ItemMovementHistory) obj;
		return Objects.equals(id, other.id);
	}
	
}
