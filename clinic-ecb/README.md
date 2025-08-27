Clinic Management System (ECB, Custom ADTs)
- No Java Collections Framework used.
- Modules: Patient, Doctor, Consultation, Treatment, Pharmacy
- Architecture: Entity-Control-Boundary

Build:
  javac -d out $(find src -name "*.java")
Run demo:
  java -cp out boundary.Main
EOF

cat > /workspace/clinic-ecb/src/adt/List.java <<\"EOF\"
package adt;

public interface List<T> {
    int size();
    boolean isEmpty();
    void add(T element);
    void add(int index, T element);
    T get(int index);
    T set(int index, T element);
    T remove(int index);
    boolean remove(T element);
    int indexOf(T element);
    boolean contains(T element);
    void clear();
}
EOF

cat > /workspace/clinic-ecb/src/adt/ArrayList.java <<\"EOF\"
package adt;

@SuppressWarnings("unchecked")
public class ArrayList<T> implements List<T> {
    private T[] elements;
    private int count;

    public ArrayList() {
        this.elements = (T[]) new Object[10];
        this.count = 0;
    }

    public ArrayList(int initialCapacity) {
        if (initialCapacity <= 0) initialCapacity = 10;
        this.elements = (T[]) new Object[initialCapacity];
        this.count = 0;
    }

    @Override
    public int size() { return count; }

    @Override
    public boolean isEmpty() { return count == 0; }

    private void ensureCapacity(int minCapacity) {
        if (minCapacity <= elements.length) return;
        int newCap = Math.max(elements.length * 2, minCapacity);
        T[] newArr = (T[]) new Object[newCap];
        for (int i = 0; i < count; i++) newArr[i] = elements[i];
        elements = newArr;
    }

    @Override
    public void add(T element) {
        ensureCapacity(count + 1);
        elements[count++] = element;
    }

    @Override
    public void add(int index, T element) {
        if (index < 0 || index > count) throw new IndexOutOfBoundsException();
        ensureCapacity(count + 1);
        for (int i = count; i > index; i--) elements[i] = elements[i - 1];
        elements[index] = element;
        count++;
    }

    @Override
    public T get(int index) { if (index < 0 || index >= count) throw new IndexOutOfBoundsException(); return elements[index]; }

    @Override
    public T set(int index, T element) { if (index < 0 || index >= count) throw new IndexOutOfBoundsException(); T old = elements[index]; elements[index] = element; return old; }

    @Override
    public T remove(int index) {
        if (index < 0 || index >= count) throw new IndexOutOfBoundsException();
        T old = elements[index];
        for (int i = index; i < count - 1; i++) elements[i] = elements[i + 1];
        elements[--count] = null;
        return old;
    }

    @Override
    public boolean remove(T element) {
        int idx = indexOf(element);
        if (idx >= 0) { remove(idx); return true; }
        return false;
    }

    @Override
    public int indexOf(T element) {
        if (element == null) {
            for (int i = 0; i < count; i++) if (elements[i] == null) return i;
        } else {
            for (int i = 0; i < count; i++) if (element.equals(elements[i])) return i;
        }
        return -1;
    }

    @Override
    public boolean contains(T element) { return indexOf(element) >= 0; }

    @Override
    public void clear() {
        for (int i = 0; i < count; i++) elements[i] = null;
        count = 0;
    }
}
EOF

cat > /workspace/clinic-ecb/src/adt/Queue.java <<\"EOF\"
package adt;

public interface Queue<T> {
    int size();
    boolean isEmpty();
    void enqueue(T element);
    T dequeue();
    T peek();
    void clear();
}
EOF

cat > /workspace/clinic-ecb/src/adt/ArrayQueue.java <<\"EOF\"
package adt;

@SuppressWarnings("unchecked")
public class ArrayQueue<T> implements Queue<T> {
    private T[] elements;
    private int head;
    private int tail;
    private int count;

    public ArrayQueue() {
        this.elements = (T[]) new Object[16];
        this.head = 0;
        this.tail = 0;
        this.count = 0;
    }

    private void ensureCapacity() {
        if (count < elements.length) return;
        int newCap = elements.length * 2;
        T[] newArr = (T[]) new Object[newCap];
        for (int i = 0; i < count; i++) newArr[i] = elements[(head + i) % elements.length];
        elements = newArr;
        head = 0; tail = count;
    }

    @Override
    public int size() { return count; }

    @Override
    public boolean isEmpty() { return count == 0; }

    @Override
    public void enqueue(T element) {
        ensureCapacity();
        elements[tail] = element;
        tail = (tail + 1) % elements.length;
        count++;
    }

    @Override
    public T dequeue() {
        if (count == 0) return null;
        T val = elements[head];
        elements[head] = null;
        head = (head + 1) % elements.length;
        count--;
        return val;
    }

    @Override
    public T peek() { return count == 0 ? null : elements[head]; }

    @Override
    public void clear() {
        for (int i = 0; i < elements.length; i++) elements[i] = null;
        head = 0; tail = 0; count = 0;
    }
}
EOF

cat > /workspace/clinic-ecb/src/adt/Map.java <<\"EOF\"
package adt;

public interface Map<K, V> {
    int size();
    boolean isEmpty();
    boolean put(K key, V value);
    V get(K key);
    boolean containsKey(K key);
    V remove(K key);
    void clear();
    K[] keys();
}
EOF

cat > /workspace/clinic-ecb/src/adt/HashMap.java <<\"EOF\"
package adt;

@SuppressWarnings("unchecked")
public class HashMap<K, V> implements Map<K, V> {
    private static class Entry<K, V> {
        final K key; V value; Entry<K, V> next;
        Entry(K key, V value, Entry<K,V> next) { this.key = key; this.value = value; this.next = next; }
    }

    private Entry<K, V>[] table;
    private int count;

    public HashMap() { this.table = (Entry<K,V>[]) new Entry[16]; this.count = 0; }

    private int indexFor(Object key, int length) {
        int h = (key == null) ? 0 : key.hashCode();
        h ^= (h >>> 16);
        if (h < 0) h = -h;
        return h % length;
    }

    private void resizeIfNeeded() {
        if (count < table.length * 0.75) return;
        Entry<K,V>[] old = table;
        table = (Entry<K,V>[]) new Entry[old.length * 2];
        count = 0;
        for (Entry<K,V> eHead : old) {
            Entry<K,V> e = eHead;
            while (e != null) { put(e.key, e.value); e = e.next; }
        }
    }

    @Override
    public int size() { return count; }

    @Override
    public boolean isEmpty() { return count == 0; }

    @Override
    public boolean put(K key, V value) {
        resizeIfNeeded();
        int idx = indexFor(key, table.length);
        Entry<K,V> head = table[idx];
        for (Entry<K,V> e = head; e != null; e = e.next) {
            if ((key == null && e.key == null) || (key != null && key.equals(e.key))) {
                e.value = value; return false; // replaced
            }
        }
        table[idx] = new Entry<>(key, value, head);
        count++;
        return true;
    }

    @Override
    public V get(K key) {
        int idx = indexFor(key, table.length);
        for (Entry<K,V> e = table[idx]; e != null; e = e.next) {
            if ((key == null && e.key == null) || (key != null && key.equals(e.key))) return e.value;
        }
        return null;
    }

    @Override
    public boolean containsKey(K key) { return get(key) != null; }

    @Override
    public V remove(K key) {
        int idx = indexFor(key, table.length);
        Entry<K,V> prev = null;
        Entry<K,V> e = table[idx];
        while (e != null) {
            if ((key == null && e.key == null) || (key != null && key.equals(e.key))) {
                if (prev == null) table[idx] = e.next; else prev.next = e.next;
                count--;
                return e.value;
            }
            prev = e; e = e.next;
        }
        return null;
    }

    @Override
    public void clear() { for (int i = 0; i < table.length; i++) table[i] = null; count = 0; }

    @Override
    public K[] keys() {
        K[] arr = (K[]) new Object[count];
        int p = 0;
        for (Entry<K,V> eHead : table) {
            Entry<K,V> e = eHead;
            while (e != null) { arr[p++] = e.key; e = e.next; }
        }
        return arr;
    }
}
EOF

cat > /workspace/clinic-ecb/src/util/DateTime.java <<\"EOF\"
package util;

public class DateTime {
    public static String today() {
        java.time.LocalDate d = java.time.LocalDate.now();
        return d.toString();
    }
    public static String now() {
        java.time.LocalDateTime dt = java.time.LocalDateTime.now();
        return dt.toString();
    }
}
EOF

cat > /workspace/clinic-ecb/src/entity/Patient.java <<\"EOF\"
package entity;

// Author: Team Member
public class Patient {
    private String id;
    private String name;
    private String phone;

    public Patient(String id, String name, String phone) {
        this.id = id; this.name = name; this.phone = phone;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getPhone() { return phone; }

    public void setName(String name) { this.name = name; }
    public void setPhone(String phone) { this.phone = phone; }

    @Override
    public String toString() { return id + ": " + name + " (" + phone + ")"; }
}
EOF

cat > /workspace/clinic-ecb/src/entity/Doctor.java <<\"EOF\"
package entity;

// Author: Team Member
public class Doctor {
    private String id;
    private String name;
    private String specialty;

    public Doctor(String id, String name, String specialty) {
        this.id = id; this.name = name; this.specialty = specialty;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getSpecialty() { return specialty; }

    public void setName(String name) { this.name = name; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }

    @Override
    public String toString() { return id + ": Dr. " + name + " (" + specialty + ")"; }
}
EOF

cat > /workspace/clinic-ecb/src/entity/Appointment.java <<\"EOF\"
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
EOF

cat > /workspace/clinic-ecb/src/entity/Consultation.java <<\"EOF\"
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
EOF

cat > /workspace/clinic-ecb/src/entity/Treatment.java <<\"EOF\"
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
EOF

cat > /workspace/clinic-ecb/src/entity/Medicine.java <<\"EOF\"
package entity;

// Author: Team Member
public class Medicine {
    private String code; // unique
    private String name;
    private int stock;
    private double unitPrice;

    public Medicine(String code, String name, int stock, double unitPrice) {
        this.code = code; this.name = name; this.stock = stock; this.unitPrice = unitPrice;
    }

    public String getCode() { return code; }
    public String getName() { return name; }
    public int getStock() { return stock; }
    public double getUnitPrice() { return unitPrice; }

    public void setName(String name) { this.name = name; }
    public void setStock(int stock) { this.stock = stock; }
    public void setUnitPrice(double unitPrice) { this.unitPrice = unitPrice; }

    @Override
    public String toString() { return code + ": " + name + ", stock=" + stock + ", RM" + unitPrice; }
}
EOF

cat > /workspace/clinic-ecb/src/entity/DispenseItem.java <<\"EOF\"
package entity;

// Author: Team Member
public class DispenseItem {
    private String medicineCode;
    private int quantity;

    public DispenseItem(String medicineCode, int quantity) {
        this.medicineCode = medicineCode; this.quantity = quantity;
    }

    public String getMedicineCode() { return medicineCode; }
    public int getQuantity() { return quantity; }
}
EOF

cat > /workspace/clinic-ecb/src/entity/DispenseRecord.java <<\"EOF\"
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
EOF

cat > /workspace/clinic-ecb/src/control/MaintainPatient.java <<\"EOF\"
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
    public String[] listPatientIds() { return idToPatient.keys(); }
}
EOF

cat > /workspace/clinic-ecb/src/control/MaintainDoctor.java <<\"EOF\"
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
    public String[] listDoctorIds() { return idToDoctor.keys(); }
}
EOF

cat > /workspace/clinic-ecb/src/control/MaintainConsultation.java <<\"EOF\"
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
    public String[] listAppointmentIds() { return idToAppointment.keys(); }
}
EOF

cat > /workspace/clinic-ecb/src/control/MaintainTreatment.java <<\"EOF\"
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
EOF

cat > /workspace/clinic-ecb/src/control/MaintainPharmacy.java <<\"EOF\"
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
    public String[] listMedicineCodes() { return codeToMedicine.keys(); }
}
EOF

cat > /workspace/clinic-ecb/src/boundary/Main.java <<\"EOF\"
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
        String[] codes = pharmCtl.listMedicineCodes();
        for (int i = 0; i < codes.length; i++) System.out.println(" - " + codes[i]);
    }
}
EOF

javac -d /workspace/clinic-ecb/out $(find /workspace/clinic-ecb/src -name "*.java")
