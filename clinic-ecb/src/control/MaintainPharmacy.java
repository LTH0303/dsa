package control;

import adt.HashMap;
import adt.Map;
import adt.ArrayList;
import adt.List;
import entity.DispenseItem;
import entity.DispenseRecord;
import entity.Medicine;

// Author: Team Member
// ADTs: Map for medicine stock, Map for dispense records, List for items
public class MaintainPharmacy {
	private Map<String, Medicine> codeToMedicine;
	private Map<String, DispenseRecord> idToDispense;

	public MaintainPharmacy() {
		codeToMedicine = new HashMap<>();
		idToDispense = new HashMap<>();
	}

	public boolean addMedicine(Medicine m) { return codeToMedicine.put(m.getCode(), m); }
	public Medicine findMedicine(String code) { return codeToMedicine.get(code); }

	public boolean dispense(String recordId, String consultationId, List<DispenseItem> items) {
		// Validate stock
		for (int i = 0; i < items.size(); i++) {
			DispenseItem item = items.get(i);
			Medicine med = codeToMedicine.get(item.getMedicineCode());
			if (med == null || med.getStock() < item.getQuantity()) return false;
		}
		// Deduct
		for (int i = 0; i < items.size(); i++) {
			DispenseItem item = items.get(i);
			Medicine med = codeToMedicine.get(item.getMedicineCode());
			med.setStock(med.getStock() - item.getQuantity());
		}
		// Record
		DispenseRecord rec = new DispenseRecord(recordId, consultationId);
		for (int i = 0; i < items.size(); i++) rec.addItem(items.get(i));
		idToDispense.put(recordId, rec);
		return true;
	}

	public DispenseRecord findDispense(String id) { return idToDispense.get(id); }

	// Reports
	public int totalMedicines() { return codeToMedicine.size(); }
	public int totalDispenses() { return idToDispense.size(); }
	public Object[] listMedicineCodes() { return codeToMedicine.keys(); }
}
