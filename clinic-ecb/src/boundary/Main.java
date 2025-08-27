package boundary;

import adt.ArrayList;
import adt.List;
import entity.*;
import control.*;

public class Main {
	public static void main(String[] args) {
		MaintainPatient patientCtl = new MaintainPatient();
		MaintainDoctor doctorCtl = new MaintainDoctor();
		MaintainConsultation consCtl = new MaintainConsultation();
		MaintainTreatment treatCtl = new MaintainTreatment();
		MaintainPharmacy pharmCtl = new MaintainPharmacy();

		// Seed data
		patientCtl.registerPatient(new Patient("P001", "Alice", "012-1111"));
		patientCtl.registerPatient(new Patient("P002", "Bob", "012-2222"));

		doctorCtl.addDoctor(new Doctor("D001", "Lim", "GP"));
		doctorCtl.addDoctor(new Doctor("D002", "Tan", "Dermatology"));
		doctorCtl.addAvailability("D001", "2025-09-01");
		doctorCtl.addAvailability("D001", "2025-09-02");

		pharmCtl.addMedicine(new Medicine("M001", "Paracetamol", 100, 0.50));
		pharmCtl.addMedicine(new Medicine("M002", "Cetirizine", 50, 0.80));

		// Walk-in queue
		patientCtl.enqueueWalkIn("P001");
		patientCtl.enqueueWalkIn("P002");

		// Book appointment
		Appointment a1 = new Appointment("A001", "P001", "D001", "2025-09-01", "09:00");
		consCtl.bookAppointment(a1);

		// Complete consultation
		Consultation c1 = new Consultation("C001", "A001", "Flu symptoms", 25.0);
		consCtl.completeAppointment("A001", c1);

		// Treatments
		treatCtl.addTreatment(new Treatment("T001", "C001", "Influenza"));

		// Dispense
		List<DispenseItem> items = new ArrayList<>();
		items.add(new DispenseItem("M001", 10));
		pharmCtl.dispense("R001", "C001", items);

		// Reports
		System.out.println("Patients: " + patientCtl.totalPatients());
		System.out.println("Doctors: " + doctorCtl.totalDoctors());
		System.out.println("Appointments: " + consCtl.totalAppointments());
		System.out.println("Consultations: " + consCtl.totalConsultations());
		System.out.println("Treatments: " + treatCtl.totalTreatments());
		System.out.println("Medicines: " + pharmCtl.totalMedicines());
		System.out.println("Dispenses: " + pharmCtl.totalDispenses());

		// Additional summaries
		System.out.println("Walk-in queue size: " + patientCtl.walkInQueueSize());
		System.out.println("Medicine codes: ");
		Object[] codes = pharmCtl.listMedicineCodes();
		for (int i = 0; i < codes.length; i++) System.out.println(" - " + codes[i]);
	}
}
