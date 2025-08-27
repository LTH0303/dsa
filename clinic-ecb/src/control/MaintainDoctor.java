package control;

import adt.HashMap;
import adt.Map;
import adt.ArrayList;
import adt.List;
import entity.Doctor;

// Author: Team Member
// ADTs used: Map for doctor storage, List for schedules by date strings
public class MaintainDoctor {
	private Map<String, Doctor> idToDoctor;
	private Map<String, List<String>> doctorIdToDates; // availability dates

	public MaintainDoctor() {
		idToDoctor = new HashMap<>();
		doctorIdToDates = new HashMap<>();
	}

	public boolean addDoctor(Doctor d) { return idToDoctor.put(d.getId(), d); }
	public Doctor findDoctor(String id) { return idToDoctor.get(id); }

	public void addAvailability(String doctorId, String date) {
		List<String> dates = doctorIdToDates.get(doctorId);
		if (dates == null) { dates = new ArrayList<>(); doctorIdToDates.put(doctorId, dates); }
		if (!dates.contains(date)) dates.add(date);
	}

	public boolean isAvailable(String doctorId, String date) {
		List<String> dates = doctorIdToDates.get(doctorId);
		return dates != null && dates.contains(date);
	}

	// Reports
	public int totalDoctors() { return idToDoctor.size(); }
	public Object[] listDoctorIds() { return idToDoctor.keys(); }
}
