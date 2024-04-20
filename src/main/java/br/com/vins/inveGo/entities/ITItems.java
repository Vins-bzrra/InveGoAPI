package br.com.vins.inveGo.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

@Entity
@Table(name = "tb_itItems")
@Inheritance(strategy = InheritanceType.JOINED)
public class ITItems extends Item {

	@Column(nullable = false, unique = true)
	private String serialNumber;

	@Column
	private String description;

	public ITItems() {
		super();
	}
	
	public ITItems(Long id, String brand, String model, String patrimony, Date acquisitionDate, String supplier,
			String unitLocation, String status, double purchasePrice, String currentOwner, String category,
			String processNumber, String serialNumber, String description) {
		super(id, brand, model, patrimony, acquisitionDate, supplier, unitLocation, status, purchasePrice, currentOwner,
				category, processNumber);
		this.serialNumber = serialNumber;
		this.description = description;
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
