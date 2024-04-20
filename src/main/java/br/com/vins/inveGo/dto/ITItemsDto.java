package br.com.vins.inveGo.dto;

import java.util.Date;

public class ITItemsDto {
	
	private String brand;
    private String model;
    private String patrimony;
    private Date acquisitionDate;
    private String supplier;
    private String unitLocation;
    private String status;
    private Double purchasePrice;
    private String currentOwner;
    private String category;
    private String serialNumber;
    private String description;
    private String processNumber;
    private Long id;

	public ITItemsDto(String brand, String model, String patrimony, Date acquisitionDate, String supplier,
			String unitLocation, String status, double purchasePrice, String currentOwner, String category,
			String serialNumber, String description, String processNumber) {
		super();
		this.brand = brand;
		this.model = model;
		this.patrimony = patrimony;
		this.acquisitionDate = acquisitionDate;
		this.supplier = supplier;
		this.unitLocation = unitLocation;
		this.status = status;
		this.purchasePrice = purchasePrice;
		this.currentOwner = currentOwner;
		this.category = category;
		this.serialNumber = serialNumber;
		this.description = description;
		this.processNumber = processNumber;
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

	public String getPatrimony() {
		return patrimony;
	}

	public void setPatrimony(String patrimony) {
		this.patrimony = patrimony;
	}

	public Date getAcquisitionDate() {
		return acquisitionDate;
	}

	public void setAcquisitionDate(Date acquisitionDate) {
		this.acquisitionDate = acquisitionDate;
	}

	public String getSupplier() {
		return supplier;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	public String getUnitLocation() {
		return unitLocation;
	}

	public void setUnitLocation(String unitLocation) {
		this.unitLocation = unitLocation;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public double getPurchasePrice() {
		return purchasePrice;
	}

	public void setPurchasePrice(double purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

	public String getCurrentOwner() {
		return currentOwner;
	}

	public void setCurrentOwner(String currentOwner) {
		this.currentOwner = currentOwner;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getProcessNumber() {
		return processNumber;
	}

	public void setProcessNumber(String processNumber) {
		this.processNumber = processNumber;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
    
}
