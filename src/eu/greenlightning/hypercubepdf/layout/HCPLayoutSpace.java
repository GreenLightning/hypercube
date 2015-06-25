package eu.greenlightning.hypercubepdf.layout;

/**
 * Defines the space in which a layout algorithm should place the elements. A layout space is defined by its absolute
 * position and by its length. Furthermore it has a direction, which indicates at which end the first element and at
 * which the last element should be placed.
 * <p>
 * This class is immutable.
 * 
 * @author Green Lightning
 */
public class HCPLayoutSpace {

	private final float start, end;

	/**
	 * Creates an {@link HCPLayoutSpace} instance from a start point and an end point. The layout process will begin at
	 * the start coordinate and work towards the end coordinate. However, this does <b>not</b> mean that the start point
	 * must be less than the end point.
	 * 
	 * @param start the start point of the layout space
	 * @param end the end point of the layout space
	 */
	public HCPLayoutSpace(float start, float end) {
		this.start = start;
		this.end = end;
	}

	/**
	 * Returns the start point of this layout space.
	 * 
	 * @return the start point
	 */
	public float getStart() {
		return start;
	}

	/**
	 * Returns the end point of this layout space.
	 * 
	 * @return the end point
	 */
	public float getEnd() {
		return end;
	}

	/**
	 * Returns the (normalized) direction of this layout space. If {@literal start != end}, direction and length are
	 * defined as:
	 * 
	 * <pre>
	 *     start + direction * length = end
	 *     
	 *     |direction| = 1
	 *     {@code length > 0}
	 * </pre>
	 * 
	 * In the abnormal case that the start and end point are equal, both direction and length are 0.
	 * 
	 * @return -1, 0 or 1 indicating the direction of this layout space
	 */
	public float getDirection() {
		return Math.signum(end - start);
	}

	/**
	 * Returns the length of this layout space. If {@literal start != end}, direction and length are defined as:
	 * 
	 * <pre>
	 *     start + direction * length = end
	 *     
	 *     |direction| = 1
	 *     {@code length > 0}
	 * </pre>
	 * 
	 * In the abnormal case that the start and end point are equal, both direction and length are 0.
	 * 
	 * @return the length of this layout space; always {@literal >= 0}
	 */
	public float getLength() {
		return Math.abs(end - start);
	}

}
