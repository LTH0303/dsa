package entity;

// Author: Team Member
public class Doctor {
	private String id;
	private String name;
	private String specialty;

	public Doctor(String id, String name, String specialty) {
		this.id = id; this.name = name; this.specialty = specialty;
	}

	public String getId() { return id; }
	public String getName() { return name; }
	public String getSpecialty() { return specialty; }

	public void setName(String name) { this.name = name; }
	public void setSpecialty(String specialty) { this.specialty = specialty; }

	@Override
	public String toString() { return id + ": Dr. " + name + " (" + specialty + ")"; }
}
