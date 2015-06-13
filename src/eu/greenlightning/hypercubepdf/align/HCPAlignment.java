package eu.greenlightning.hypercubepdf.align;

import java.util.Objects;

import org.apache.pdfbox.pdmodel.common.PDRectangle;

/**
 * Combination of a {@link HCPHorizontalAlignment} and {@link HCPVerticalAlignment}.
 * <p>
 * Describes an alignment in a 2D space where the x-axis points right and the y-axis points upwards (see also
 * {@linkplain HCPVerticalAlignment}).
 *
 * @author Green Lightning
 */
public enum HCPAlignment {

	/** The left (smaller) x-coordinates and the upper (larger) y-coordinates will be aligned. */
	TOP_LEFT(HCPHorizontalAlignment.LEFT, HCPVerticalAlignment.TOP),
	/** The center x-coordinates and the upper (larger) y-coordinates will be aligned. */
	TOP(HCPHorizontalAlignment.CENTER, HCPVerticalAlignment.TOP),
	/** The right (larger) x-coordinates and the upper (larger) y-coordinates will be aligned. */
	TOP_RIGHT(HCPHorizontalAlignment.RIGHT, HCPVerticalAlignment.TOP),

	/** The left (smaller) x-coordinates and the center y-coordinates will be aligned. */
	LEFT(HCPHorizontalAlignment.LEFT, HCPVerticalAlignment.CENTER),
	/** The center x-coordinates and the center y-coordinates will be aligned. */
	CENTER(HCPHorizontalAlignment.CENTER, HCPVerticalAlignment.CENTER),
	/** The right (larger) x-coordinates and the center y-coordinates will be aligned. */
	RIGHT(HCPHorizontalAlignment.RIGHT, HCPVerticalAlignment.CENTER),

	/** The left (smaller) x-coordinates and the lower (smaller) y-coordinates will be aligned. */
	BOTTOM_LEFT(HCPHorizontalAlignment.LEFT, HCPVerticalAlignment.BOTTOM),
	/** The center x-coordinates and the lower (smaller) y-coordinates will be aligned. */
	BOTTOM(HCPHorizontalAlignment.CENTER, HCPVerticalAlignment.BOTTOM),
	/** The right (larger) x-coordinates and the lower (smaller) y-coordinates will be aligned. */
	BOTTOM_RIGHT(HCPHorizontalAlignment.RIGHT, HCPVerticalAlignment.BOTTOM);

	/**
	 * Returns the {@link HCPAlignment} with the specified {@link HCPHorizontalAlignment} and
	 * {@link HCPVerticalAlignment}.
	 * 
	 * @param horizontal not {@code null}
	 * @param vertical not {@code null}
	 * @return the {@link HCPAlignment} with the specified horizontal and vertical alignment
	 * @throws NullPointerException if horizontal or vertical is {@code null}
	 */
	public static HCPAlignment valueOf(HCPHorizontalAlignment horizontal, HCPVerticalAlignment vertical) {
		Objects.requireNonNull(horizontal, "Horizontal must not be null.");
		Objects.requireNonNull(vertical, "Vertical must not be null.");
		// @formatter:off
		switch (vertical) {
		case TOP:
			switch (horizontal) {
			case LEFT:   return TOP_LEFT;
			case CENTER: return TOP;
			case RIGHT:  return TOP_RIGHT;
			default:     throw new IllegalArgumentException("Unknown horizontal alignment: " + horizontal + ".");
			}
		case CENTER:
			switch (horizontal) {
			case LEFT:   return LEFT;
			case CENTER: return CENTER;
			case RIGHT:  return RIGHT;
			default:     throw new IllegalArgumentException("Unknown horizontal alignment: " + horizontal + ".");
			}
		case BOTTOM:
			switch (horizontal) {
			case LEFT:   return BOTTOM_LEFT;
			case CENTER: return BOTTOM;
			case RIGHT:  return BOTTOM_RIGHT;
			default:     throw new IllegalArgumentException("Unknown horizontal alignment: " + horizontal + ".");
			}
		default:
			throw new IllegalArgumentException("Unknown vertical alignment: " + vertical + ".");
		}
		// @formatter:on
	}

	private final HCPHorizontalAlignment horizontalAlignment;
	private final HCPVerticalAlignment verticalAlignment;

	private HCPAlignment(HCPHorizontalAlignment horizontalAlignment, HCPVerticalAlignment verticalAlignment) {
		this.horizontalAlignment = Objects.requireNonNull(horizontalAlignment);
		this.verticalAlignment = Objects.requireNonNull(verticalAlignment);
	}

	/**
	 * Returns the horizontal alignment.
	 * 
	 * @return the horizontal alignment
	 */
	public HCPHorizontalAlignment getHorizontalAlignment() {
		return horizontalAlignment;
	}

	/**
	 * Returns the vertical alignment.
	 * 
	 * @return the vertical alignment
	 */
	public HCPVerticalAlignment getVerticalAlignment() {
		return verticalAlignment;
	}

	/**
	 * Aligns the child {@link PDRectangle} inside the parent {@link PDRectangle}.
	 * <p>
	 * The width and height of both rectangles should be positive.
	 * <p>
	 * Note that, if the child is wider or higher than the parent, although correctly aligned, some part(s) of it will
	 * be outside of the parent. If this is undesired behavior, a size check should be performed before calling this
	 * method.
	 * <p>
	 * If the arguments do not satisfy the given constraints the coordinates of the child after this method returns are
	 * unspecified. Otherwise, the child will be aligned with the parent.
	 * 
	 * @param child not {@code null} and {@linkplain PDRectangle#getWidth() getWidth()} and
	 *            {@linkplain PDRectangle#getHeight() getHeight()} should be {@literal >= 0}
	 * @param parent not {@code null} and {@linkplain PDRectangle#getWidth() getWidth()} and
	 *            {@linkplain PDRectangle#getHeight() getHeight()} should be {@literal >= 0}
	 * @throws NullPointerException if shape or parent is {@code null}
	 */
	public void alignChildWithParent(PDRectangle child, PDRectangle parent) {
		getHorizontalAlignment().alignChildWithParent(child, parent);
		getVerticalAlignment().alignChildWithParent(child, parent);
	}

}
