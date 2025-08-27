package entity;

// Author: Team Member
public class Patient {
	private String id;
	private String name;
	private String phone;

	public Patient(String id, String name, String phone) {
		this.id = id; this.name = name; this.phone = phone;
	}

	public String getId() { return id; }
	public String getName() { return name; }
	public String getPhone() { return phone; }

	public void setName(String name) { this.name = name; }
	public void setPhone(String phone) { this.phone = phone; }

	@Override
	public String toString() { return id + ": " + name + " (" + phone + ")"; }
}
