package entity;

// Author: Team Member
public class DispenseItem {
	private String medicineCode;
	private int quantity;

	public DispenseItem(String medicineCode, int quantity) {
		this.medicineCode = medicineCode; this.quantity = quantity;
	}

	public String getMedicineCode() { return medicineCode; }
	public int getQuantity() { return quantity; }
}
