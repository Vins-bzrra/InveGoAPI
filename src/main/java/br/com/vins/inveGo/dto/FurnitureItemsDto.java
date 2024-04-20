package br.com.vins.inveGo.dto;

import java.util.Date;

public class FurnitureItemsDto {
	private String brand;
    private String model;
    private String patrimony;
    private Date acquisitionDate;
    private String supplier;
    private String unitLocation;
    private String status;
    private double purchasePrice;
    private String currentOwner;
    private String category;
    private String description;
    private String processNumber;
    private String material;
	private String dimensions;
	private String color;
    private Long id;
	
    public FurnitureItemsDto() {
		super();
	}

	public FurnitureItemsDto(String brand, String model, String patrimony, Date acquisitionDate, String supplier,
			String unitLocation, String status, double purchasePrice, String currentOwner, String category,
			String description, String processNumber, String material, String dimensions, String color, Long id) {
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
		this.description = description;
		this.processNumber = processNumber;
		this.material = material;
		this.dimensions = dimensions;
		this.color = color;
		this.id = id;
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

	public String getMaterial() {
		return material;
	}

	public void setMaterial(String material) {
		this.material = material;
	}

	public String getDimensions() {
		return dimensions;
	}

	public void setDimensions(String dimensions) {
		this.dimensions = dimensions;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
  
}
