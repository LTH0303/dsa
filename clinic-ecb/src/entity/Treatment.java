package entity;

// Author: Team Member
public class Treatment {
	private String id; // unique
	private String consultationId;
	private String diagnosis;

	public Treatment(String id, String consultationId, String diagnosis) {
		this.id = id; this.consultationId = consultationId; this.diagnosis = diagnosis;
	}

	public String getId() { return id; }
	public String getConsultationId() { return consultationId; }
	public String getDiagnosis() { return diagnosis; }

	public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }

	@Override
	public String toString() { return id + ": cons=" + consultationId + ", diag=" + diagnosis; }
}
