package br.com.vins.inveGo.dto;

public class ItemTransferDto {

	private String patrimony;
	private String newOwner;
	private String newUnitLocation;

	public ItemTransferDto() {
		super();
	}

	public ItemTransferDto(String patrimony, String newOwner, String newUnitLocation) {
		super();
		this.patrimony = patrimony;
		this.newOwner = newOwner;
		this.newUnitLocation = newUnitLocation;
	}

	public String getPatrimony() {
		return patrimony;
	}

	public void setPatrimony(String patrimony) {
		this.patrimony = patrimony;
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

}
