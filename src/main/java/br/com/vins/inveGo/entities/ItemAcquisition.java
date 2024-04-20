package br.com.vins.inveGo.entities;

import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "acquisitionHistory")
public class ItemAcquisition {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String supplier;

	@Column(nullable = false)
	private String receiver;

	@Column(nullable = false)
	private String processNumber;

	@Column(nullable = false)
	private String invoiceNumber;

	@Column(nullable = false)
	private String category; 
	
	@Column(nullable = false)
	private String brand; 
	
	@Column(nullable = false)
	private String model; 
	
	@Column(nullable = false)
	private String quantity; 

	@Column(nullable = false)
	private String responsibleUser;
	
	@Column(nullable = false)
	private String description;

	@Column(nullable = false)
	private LocalDateTime receptionDate;
	
	@OneToOne
    private Item item;

	public ItemAcquisition() {
		super();
	}

	public ItemAcquisition(Long id, String supplier, String receiver, String processNumber, String invoiceNumber,
			String category, String brand, String model, String quantity, String responsibleUser, String description,
			LocalDateTime receptionDate) {
		super();
		this.id = id;
		this.supplier = supplier;
		this.receiver = receiver;
		this.processNumber = processNumber;
		this.invoiceNumber = invoiceNumber;
		this.category = category;
		this.brand = brand;
		this.model = model;
		this.quantity = quantity;
		this.responsibleUser = responsibleUser;
		this.description = description;
		this.receptionDate = receptionDate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSupplier() {
		return supplier;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getProcessNumber() {
		return processNumber;
	}

	public void setProcessNumber(String processNumber) {
		this.processNumber = processNumber;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getResponsibleUser() {
		return responsibleUser;
	}

	public void setResponsibleUser(String responsibleUser) {
		this.responsibleUser = responsibleUser;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDateTime getReceptionDate() {
		return receptionDate;
	}

	public void setReceptionDate(LocalDateTime receptionDate) {
		this.receptionDate = receptionDate;
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
		ItemAcquisition other = (ItemAcquisition) obj;
		return Objects.equals(id, other.id);
	}

}
