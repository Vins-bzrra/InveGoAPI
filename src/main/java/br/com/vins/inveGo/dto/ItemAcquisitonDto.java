package br.com.vins.inveGo.dto;

import java.time.LocalDateTime;

public class ItemAcquisitonDto {

	private Long id;
	private String supplier;
	private String receiver;
	private String processNumber;
	private String invoiceNumber;
	private String category; 
	private String brand; 
	private String model; 
	private String quantity; 
	private String responsibleUser;
	private String description;
	private LocalDateTime receptionDate;
	
	public ItemAcquisitonDto(Long id, String supplier, String receiver, String processNumber, String invoiceNumber,
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
	
	
}
