package entity;

import adt.ArrayList;
import adt.List;

// Author: Team Member
public class DispenseRecord {
	private String id; // unique
	private String consultationId;
	private List<DispenseItem> items;

	public DispenseRecord(String id, String consultationId) {
		this.id = id; this.consultationId = consultationId; this.items = new ArrayList<>();
	}

	public String getId() { return id; }
	public String getConsultationId() { return consultationId; }
	public List<DispenseItem> getItems() { return items; }

	public void addItem(DispenseItem item) { items.add(item); }
}
