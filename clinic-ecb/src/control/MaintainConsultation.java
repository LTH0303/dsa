package control;

import adt.HashMap;
import adt.Map;
import adt.ArrayList;
import adt.List;
import entity.Appointment;
import entity.Consultation;

// Author: Team Member
// ADTs: Map for appointment/consultation by id, List per patient
public class MaintainConsultation {
	private Map<String, Appointment> idToAppointment;
	private Map<String, Consultation> idToConsultation;
	private Map<String, List<String>> patientIdToAppointmentIds;

	public MaintainConsultation() {
		idToAppointment = new HashMap<>();
		idToConsultation = new HashMap<>();
		patientIdToAppointmentIds = new HashMap<>();
	}

	public boolean bookAppointment(Appointment appt) {
		boolean added = idToAppointment.put(appt.getId(), appt);
		if (!added) return false;
		List<String> list = patientIdToAppointmentIds.get(appt.getPatientId());
		if (list == null) { list = new ArrayList<>(); patientIdToAppointmentIds.put(appt.getPatientId(), list); }
		list.add(appt.getId());
		return true;
	}

	public Appointment findAppointment(String id) { return idToAppointment.get(id); }

	public boolean completeAppointment(String appointmentId, Consultation consultation) {
		Appointment appt = idToAppointment.get(appointmentId);
		if (appt == null) return false;
		appt.setStatus("COMPLETED");
		idToConsultation.put(consultation.getId(), consultation);
		return true;
	}

	public Consultation findConsultation(String id) { return idToConsultation.get(id); }

	// Reports
	public int totalAppointments() { return idToAppointment.size(); }
	public int totalConsultations() { return idToConsultation.size(); }
	public Object[] listAppointmentIds() { return idToAppointment.keys(); }
}
