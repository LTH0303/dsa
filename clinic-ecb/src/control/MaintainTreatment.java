package control;

import adt.HashMap;
import adt.Map;
import adt.ArrayList;
import adt.List;
import entity.Treatment;

// Author: Team Member
// ADTs: Map for treatments, List per consultation history
public class MaintainTreatment {
	private Map<String, Treatment> idToTreatment;
	private Map<String, List<String>> consultationIdToTreatmentIds;

	public MaintainTreatment() {
		idToTreatment = new HashMap<>();
		consultationIdToTreatmentIds = new HashMap<>();
	}

	public boolean addTreatment(Treatment t) {
		boolean added = idToTreatment.put(t.getId(), t);
		if (!added) return false;
		List<String> list = consultationIdToTreatmentIds.get(t.getConsultationId());
		if (list == null) { list = new ArrayList<>(); consultationIdToTreatmentIds.put(t.getConsultationId(), list); }
		list.add(t.getId());
		return true;
	}

	public Treatment findTreatment(String id) { return idToTreatment.get(id); }

	public List<String> getTreatmentsForConsultation(String consultationId) {
		List<String> list = consultationIdToTreatmentIds.get(consultationId);
		if (list == null) list = new ArrayList<>();
		return list;
	}

	// Reports
	public int totalTreatments() { return idToTreatment.size(); }
}
