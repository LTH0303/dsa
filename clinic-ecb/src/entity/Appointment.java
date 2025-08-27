package entity;

// Author: Team Member
public class Appointment {
	private String id; // unique
	private String patientId;
	private String doctorId;
	private String date; // YYYY-MM-DD
	private String time; // HH:MM
	private String status; // BOOKED, COMPLETED, CANCELLED

	public Appointment(String id, String patientId, String doctorId, String date, String time) {
		this.id = id; this.patientId = patientId; this.doctorId = doctorId; this.date = date; this.time = time; this.status = "BOOKED";
	}

	public String getId() { return id; }
	public String getPatientId() { return patientId; }
	public String getDoctorId() { return doctorId; }
	public String getDate() { return date; }
	public String getTime() { return time; }
	public String getStatus() { return status; }

	public void setStatus(String status) { this.status = status; }

	@Override
	public String toString() {
		return id + ": P=" + patientId + ", D=" + doctorId + ", " + date + " " + time + " [" + status + "]";
	}
}
