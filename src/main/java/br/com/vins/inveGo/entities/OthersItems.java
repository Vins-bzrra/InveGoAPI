package br.com.vins.inveGo.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

@Entity
@Table(name = "tb_OthersItems")
@Inheritance(strategy = InheritanceType.JOINED)
public class OthersItems extends Item {

	@Column
	private String material;

	@Column
	private String dimensions;

	@Column
	private String color;

	@Column(unique = true)
	private String serialNumber;

	@Column
	private String description;

	public OthersItems() {
		super();
	}

	public OthersItems(Long id, String brand, String model, String patrimony, Date acquisitionDate, String supplier,
			String unitLocation, String status, double purchasePrice, String currentOwner, String category,
			String processNumber, String material, String dimensions, String color, String serialNumber, String description) {
		super(id, brand, model, patrimony, acquisitionDate, supplier, unitLocation, status, purchasePrice, currentOwner,
				category, processNumber);
		this.material = material;
		this.dimensions = dimensions;
		this.color = color;
		this.serialNumber = serialNumber;
		this.description = description;
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

}
