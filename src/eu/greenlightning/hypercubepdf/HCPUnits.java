package eu.greenlightning.hypercubepdf;

/**
 * Utility class for converting between common units (inches, millimeters) and default user space units.
 * <p>
 * PDF defines a device-independent coordinate system for each page called user space. Most values used while
 * creating the PDF are specified in user space coordinates.
 * <p>
 * Since PDF version 1.6 it is possible to specify the length of the unit of the user space. However, if it is
 * not specified or the PDF does not support specifying it, the default value of 1 / 72 inch is used.
 * <p>
 * This class can only deal with values from the default user space. If you change the length of the user
 * space unit, you need to convert values manually.
 * <p>
 * PDFBox uses {@code float}s to represent user space coordinates and so does Hypercube.
 *
 * @author Green Lightning
 */
public final class HCPUnits {

	private static final float DEFAULT_USER_SPACE_UNITS_PER_INCH = 72;
	private static final float MILLIMETERS_PER_INCH = 25.4f;

	/**
	 * Converts from inches to default user space units.
	 * 
	 * @param inches a value in inches
	 * @return the value converted to default user space units
	 * @see HCPUnits Default User Space
	 */
	public static float defaultUserSpaceUnitsFromInches(float inches) {
		return inches * DEFAULT_USER_SPACE_UNITS_PER_INCH;
	}
	
	/**
	 * Converts from default user space units to inches.
	 * 
	 * @param units a value in default user space units
	 * @return the value converted to inches
	 * @see HCPUnits Default User Space
	 */
	public static float inchesFromDefaultUserSpaceUnits(float units) {
		return units / DEFAULT_USER_SPACE_UNITS_PER_INCH;
	}

	/**
	 * Converts from millimeters to default user space units.
	 * 
	 * @param millimeters a value in millimeters
	 * @return the value converted to default user space units
	 * @see HCPUnits Default User Space
	 */
	public static float defaultUserSpaceUnitsFromMillimeters(float millimeters) {
		return (millimeters / MILLIMETERS_PER_INCH) * DEFAULT_USER_SPACE_UNITS_PER_INCH;
	}

	/**
	 * Converts from default user space units to millimeters.
	 * 
	 * @param units a value in default user space units
	 * @return the value converted to millimeters
	 * @see HCPUnits Default User Space
	 */
	public static float millimetersFromDefaultUserSpaceUnits(float units) {
		return (units / DEFAULT_USER_SPACE_UNITS_PER_INCH) * MILLIMETERS_PER_INCH;
	}

	// prevent instantiation
	private HCPUnits() {
		throw new UnsupportedOperationException();
	}

}
