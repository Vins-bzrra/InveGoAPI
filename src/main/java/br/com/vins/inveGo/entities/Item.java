package br.com.vins.inveGo.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Item {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String brand;

	@Column(nullable = false)
	private String model;

	@Column(nullable = false, unique = true)
	private String patrimony;

	@Column(nullable = false)
	private Date acquisitionDate;

	@Column(nullable = false)
	private String supplier;

	@Column(nullable = false)
	private String unitLocation;

	@Column(nullable = false)
	private String status;

	@Column(nullable = false)
	private double purchasePrice;

	@Column(nullable = false)
	private String currentOwner;

	@Column(nullable = false)
	private String category;
	
	@Column(nullable = false)
	private String processNumber;

	@OneToMany(mappedBy = "item", cascade = { CascadeType.ALL, CascadeType.REMOVE })
	@JsonIgnore
	private List<ItemMovementHistory> movementHistory = new ArrayList<>();
	
	@OneToOne(mappedBy = "item")
	@JoinColumn(name = "acquisition", referencedColumnName = "processNumber")
	@JsonIgnore
    private ItemAcquisition acquisition;

	public Item() {
		super();
	}

	public Item(Long id, String brand, String model, String patrimony, Date acquisitionDate, String supplier,
			String unitLocation, String status, double purchasePrice, String currentOwner, String category,
			String processNumber) {
		super();
		this.id = id;
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
		this.processNumber = processNumber;
	}

	public void addMovementHistory(ItemMovementHistory movementHistory) {
        this.movementHistory.add(movementHistory);
    }
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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

	public String getProcessNumber() {
		return processNumber;
	}

	public void setProcessNumber(String processNumber) {
		this.processNumber = processNumber;
	}

	public List<ItemMovementHistory> getMovementHistory() {
		return movementHistory;
	}

	public void setMovementHistory(List<ItemMovementHistory> movementHistory) {
		this.movementHistory = movementHistory;
	}
	
	/*public ItemAcquisition getAcquisition() {
		return acquisition;
	}

	public void setAcquisition(ItemAcquisition acquisition) {
		this.acquisition = acquisition;
	}*/

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
		Item other = (Item) obj;
		return Objects.equals(id, other.id);
	}

}
