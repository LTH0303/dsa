package control;

import adt.HashMap;
import adt.Map;
import adt.ArrayQueue;
import adt.Queue;
import entity.Patient;

// Author: Team Member
// ADTs used: Map for storage, Queue for walk-in queue
public class MaintainPatient {
	private Map<String, Patient> idToPatient;
	private Queue<String> walkInQueue; // queue of patientIds

	public MaintainPatient() {
		idToPatient = new HashMap<>();
		walkInQueue = new ArrayQueue<>();
	}

	// Registration and maintenance
	public boolean registerPatient(Patient p) { return idToPatient.put(p.getId(), p); }
	public Patient findPatient(String id) { return idToPatient.get(id); }
	public boolean removePatient(String id) { return idToPatient.remove(id) != null; }

	// Queuing management
	public void enqueueWalkIn(String patientId) { walkInQueue.enqueue(patientId); }
	public String peekNextWalkIn() { return walkInQueue.peek(); }
	public String dequeueWalkIn() { return walkInQueue.dequeue(); }
	public int walkInQueueSize() { return walkInQueue.size(); }

	// Reports
	public int totalPatients() { return idToPatient.size(); }
	public Object[] listPatientIds() { return idToPatient.keys(); }
}
