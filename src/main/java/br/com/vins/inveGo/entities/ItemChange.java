package br.com.vins.inveGo.entities;

import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "itemChange")
public class ItemChange {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String patrimonyItem; 
    
    @Column(nullable = false)
    private String attributeName; 
    
    @Column(nullable = false)
    private String oldValue; 
    
    @Column(nullable = false)
    private String newValue; 
    
    @Column(nullable = false)
    private String userRegistration;
    
    @Column(nullable = false)
    private String UserName;
    
    @Column(nullable = false)
    private LocalDateTime changeDateTime;

	public ItemChange() {
		super();
	}

	public ItemChange(Long id, String patrimonyItem, String attributeName, String oldValue, String newValue,
			String userRegistration, String userName, LocalDateTime changeDateTime) {
		super();
		this.id = id;
		this.patrimonyItem = patrimonyItem;
		this.attributeName = attributeName;
		this.oldValue = oldValue;
		this.newValue = newValue;
		this.userRegistration = userRegistration;
		UserName = userName;
		this.changeDateTime = changeDateTime;
	}
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPatrimonyItem() {
		return patrimonyItem;
	}

	public void setPatrimonyItem(String patrimonyItem) {
		this.patrimonyItem = patrimonyItem;
	}

	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public String getOldValue() {
		return oldValue;
	}

	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}

	public String getNewValue() {
		return newValue;
	}

	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}

	public String getUserRegistration() {
		return userRegistration;
	}

	public void setUserRegistration(String userRegistration) {
		this.userRegistration = userRegistration;
	}

	public String getUserName() {
		return UserName;
	}

	public void setUserName(String userName) {
		UserName = userName;
	}

	public LocalDateTime getChangeDateTime() {
		return changeDateTime;
	}

	public void setChangeDateTime(LocalDateTime changeDateTime) {
		this.changeDateTime = changeDateTime;
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
		ItemChange other = (ItemChange) obj;
		return Objects.equals(id, other.id);
	}
    
    
}
