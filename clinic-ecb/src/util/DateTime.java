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
