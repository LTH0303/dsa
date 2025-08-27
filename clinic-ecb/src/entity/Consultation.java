package entity;

// Author: Team Member
public class Consultation {
	private String id; // unique
	private String appointmentId;
	private String notes;
	private double fee;

	public Consultation(String id, String appointmentId, String notes, double fee) {
		this.id = id; this.appointmentId = appointmentId; this.notes = notes; this.fee = fee;
	}

	public String getId() { return id; }
	public String getAppointmentId() { return appointmentId; }
	public String getNotes() { return notes; }
	public double getFee() { return fee; }

	public void setNotes(String notes) { this.notes = notes; }
	public void setFee(double fee) { this.fee = fee; }

	@Override
	public String toString() { return id + ": appt=" + appointmentId + ", fee=" + fee; }
}
