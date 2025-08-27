package entity;

// Author: Team Member
public class Medicine {
	private String code; // unique
	private String name;
	private int stock;
	private double unitPrice;

	public Medicine(String code, String name, int stock, double unitPrice) {
		this.code = code; this.name = name; this.stock = stock; this.unitPrice = unitPrice;
	}

	public String getCode() { return code; }
	public String getName() { return name; }
	public int getStock() { return stock; }
	public double getUnitPrice() { return unitPrice; }

	public void setName(String name) { this.name = name; }
	public void setStock(int stock) { this.stock = stock; }
	public void setUnitPrice(double unitPrice) { this.unitPrice = unitPrice; }

	@Override
	public String toString() { return code + ": " + name + ", stock=" + stock + ", RM" + unitPrice; }
}
