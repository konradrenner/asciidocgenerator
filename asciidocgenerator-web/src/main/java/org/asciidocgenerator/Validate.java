package org.asciidocgenerator;

public final class Validate {

	private Validate() {

	}

	/**
	 * Returning a given value if not null, otherwise an IllegalArgumentException will
	 * occure
	 * 
	 * @param value
	 * @return given value if valid
	 */
	public static <T> T notNull(T value) {
		if (value == null) {
			throw new IllegalArgumentException("Null-Argument not vaild!");
		}
		return value;
	}

	/**
	 * Returning a given value if not null and contains characters without spaces, otherwise an IllegalArgumentException
	 * will occure
	 * 
	 * @param value
	 * @return given value if valid
	 */
	public static String notEmpty(String value) {
		String notNullValue = notNull(value);
		if (notNullValue.trim().isEmpty()) {
			throw new IllegalArgumentException("Leere Zeichenkette ist nicht valide!");
		}
		return notNullValue;
	}

	/**
	 * Returning a given value if not null and contains characters without spaces and doesn't start with a given prefix,
	 * otherwise an IllegalArgumentException
	 * will occure
	 * 
	 * @param value
	 * @param prefixes
	 * @return given value if valid
	 */
	public static String valueNotStartsWithPrefix(String value, String... prefixes) {
		String notEmpty = notEmpty(value);
		for (String prefix : prefixes) {
			if (notEmpty.startsWith(prefix)) {
				throw new IllegalArgumentException(String.format(	"Zeichenkette darf nicht mit Prefix %s beginnen!",
																	prefix));
			}
		}
		return notEmpty;
	}
}
