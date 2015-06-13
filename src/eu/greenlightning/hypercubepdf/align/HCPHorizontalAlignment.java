package eu.greenlightning.hypercubepdf.align;

import static eu.greenlightning.hypercubepdf.align.HCPLineAlignment.*;

import java.util.Objects;

import org.apache.pdfbox.pdmodel.common.PDRectangle;

/**
 * Alignment on the x-axis (which points to the right).
 *
 * @author Green Lightning
 */
public enum HCPHorizontalAlignment {

	/** The left (smaller) x-coordinates will be aligned. */
	LEFT(BEGINNING),

	/** The center x-coordinates will be aligned. */
	CENTER(MIDDLE),

	/** The right (larger) x-coordinates will be aligned. */
	RIGHT(END);

	private final HCPLineAlignment position;

	private HCPHorizontalAlignment(HCPLineAlignment position) {
		this.position = Objects.requireNonNull(position);
	}

	/**
	 * Aligns a segment of the specified width inside the segment specified by the {@code leftX} and {@code rightX}
	 * coordinates.
	 * <p>
	 * The {@code leftX} argument should define the smaller and the {@code rightX} argument the larger x-coordinate of
	 * the parent segment. The {@code width} argument should define the length of the child segment (which should be
	 * positive).
	 * <p>
	 * Note that, if the child segment is larger than the parent segment, although correctly aligned, some part(s) of it
	 * will be outside of the parent segment. If this is undesired behavior, a size check should be performed before
	 * calling this method.
	 * <p>
	 * If the arguments do not satisfy the given constraints the return value of this method is unspecified. Otherwise,
	 * it returns the <i>left</i> (the smaller) x-coordinate of the aligned segment based on the origin of {@code leftX}
	 * and {@code rightX}.
	 * 
	 * @param width should be {@literal >= 0}
	 * @param leftX should be {@literal <= rightX}
	 * @param rightX should be {@literal >= leftX}
	 * @return the <i>left</i> x-coordinate of the aligned segment
	 */
	public float align(float width, float leftX, float rightX) {
		return position.align(width, leftX, rightX);
	}

	/**
	 * Aligns a segment of the specified width inside the parent {@link PDRectangle}.
	 * <p>
	 * The width of the parent rectangle and the {@code width} argument should be positive.
	 * <p>
	 * Note that, if the child segment is wider than the parent rectangle, although correctly aligned, some part(s) of
	 * it will be outside of the parent rectangle. If this is undesired behavior, a size check should be performed
	 * before calling this method.
	 * <p>
	 * If the arguments do not satisfy the given constraints the return value of this method is unspecified. Otherwise,
	 * it returns the <i>left</i> (the smaller) x-coordinate of the aligned segment based on the origin of the parent
	 * rectangle.
	 * 
	 * @param width should be {@literal >= 0}
	 * @param parent not {@code null} and {@linkplain PDRectangle#getWidth() getWidth()} should be {@literal >= 0}
	 * @return the <i>left</i> x-coordinate of the aligned segment
	 * @throws NullPointerException if parent is {@code null}
	 */
	public float alignWithParent(float width, PDRectangle parent) {
		Objects.requireNonNull(parent, "Parent must not be null.");
		return align(width, parent.getLowerLeftX(), parent.getUpperRightX());
	}

	/**
	 * Aligns the child {@link PDRectangle} inside the parent {@link PDRectangle}.
	 * <p>
	 * The width of both rectangles should be positive.
	 * <p>
	 * Note that, if the child is wider than the parent, although correctly aligned, some part(s) of it will be outside
	 * of the parent. If this is undesired behavior, a size check should be performed before calling this method.
	 * <p>
	 * If the arguments do not satisfy the given constraints the x-coordinates of the child after this method returns
	 * are unspecified. Otherwise, the child will be horizontally aligned with the parent and its y-coordinates will be
	 * unmodified.
	 * 
	 * @param child not {@code null} and {@linkplain PDRectangle#getWidth() getWidth()} should be {@literal >= 0}
	 * @param parent not {@code null} and {@linkplain PDRectangle#getWidth() getWidth()} should be {@literal >= 0}
	 * @throws NullPointerException if shape or parent is {@code null}
	 */
	public void alignChildWithParent(PDRectangle child, PDRectangle parent) {
		Objects.requireNonNull(child, "Child must not be null.");
		float width = child.getWidth();
		float x = alignWithParent(width, parent);
		child.setLowerLeftX(x);
		child.setUpperRightX(x + width);
	}

}
