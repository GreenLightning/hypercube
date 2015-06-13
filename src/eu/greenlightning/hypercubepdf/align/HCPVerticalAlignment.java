package eu.greenlightning.hypercubepdf.align;

import static eu.greenlightning.hypercubepdf.align.HCPLineAlignment.*;

import java.util.Objects;

import org.apache.pdfbox.pdmodel.common.PDRectangle;

import eu.greenlightning.hypercubepdf.HCPUnits;

/**
 * Alignment on the y-axis (which points <i>upwards</i>).
 * <p>
 * Unlike the Java Graphics API, the PDF standard defines a default user space y-axis which points upwards, as in
 * standard mathematical practice. Thus, both PDFBox and Hypercube assume that the y-axis points upwards.
 *
 * @author Green Lightning
 * @see HCPUnits Default User Space
 */
public enum HCPVerticalAlignment {

	/** The upper (larger) y-coordinates will be aligned. */
	TOP(END),

	/** The center y-coordinates will be aligned. */
	CENTER(MIDDLE),

	/** The lower (smaller) y-coordinates will be aligned. */
	BOTTOM(BEGINNING);

	private final HCPLineAlignment position;

	private HCPVerticalAlignment(HCPLineAlignment position) {
		this.position = Objects.requireNonNull(position);
	}

	/**
	 * Aligns a segment of the specified height inside the segment specified by the {@code lowerY} and {@code upperY}
	 * coordinates.
	 * <p>
	 * The {@code lowerY} argument should define the smaller and the {@code upperY} argument the larger y-coordinate of
	 * the parent segment. The {@code height} argument should define the length of the child segment (which should be
	 * positive).
	 * <p>
	 * Note that, if the child segment is larger than the parent segment, although correctly aligned, some part(s) of it
	 * will be outside of the parent segment. If this is undesired behavior, a size check should be performed before
	 * calling this method.
	 * <p>
	 * If the arguments do not satisfy the given constraints the return value of this method is unspecified. Otherwise,
	 * it returns the <i>lower</i> (the smaller) y-coordinate of the aligned segment based on the origin of
	 * {@code lowerY} and {@code upperY}.
	 * 
	 * @param height should be {@literal >= 0}
	 * @param lowerY should be {@literal <= upperY}
	 * @param upperY should be {@literal >= lowerY}
	 * @return the <i>lower</i> y-coordinate of the aligned segment
	 */
	public float align(float height, float lowerY, float upperY) {
		return position.align(height, lowerY, upperY);
	}

	/**
	 * Aligns a segment of the specified height inside the parent {@link PDRectangle}.
	 * <p>
	 * The height of the parent rectangle and the {@code height} argument should be positive.
	 * <p>
	 * Note that, if the child segment is higher than the parent rectangle, although correctly aligned, some part(s) of
	 * it will be outside of the parent rectangle. If this is undesired behavior, a size check should be performed
	 * before calling this method.
	 * <p>
	 * If the arguments do not satisfy the given constraints the return value of this method is unspecified. Otherwise,
	 * it returns the <i>lower</i> (the smaller) y-coordinate of the aligned segment based on the origin of the parent
	 * rectangle.
	 * 
	 * @param height should be {@literal >= 0}
	 * @param parent not {@code null} and {@linkplain PDRectangle#getWidth() getWidth()} should be {@literal >= 0}
	 * @return the <i>lower</i> y-coordinate of the aligned segment
	 * @throws NullPointerException if parent is {@code null}
	 */
	public float alignWithParent(float height, PDRectangle parent) {
		Objects.requireNonNull(parent, "Parent must not be null.");
		return align(height, parent.getLowerLeftY(), parent.getUpperRightY());
	}

	/**
	 * Aligns the child {@link PDRectangle} inside the parent {@link PDRectangle}.
	 * <p>
	 * The height of both rectangles should be positive.
	 * <p>
	 * Note that, if the child is higher than the parent, although correctly aligned, some part(s) of it will be outside
	 * of the parent. If this is undesired behavior, a size check should be performed before calling this method.
	 * <p>
	 * If the arguments do not satisfy the given constraints the y-coordinates of the child after this method returns
	 * are unspecified. Otherwise, the child will be vertically aligned with the parent and its x-coordinates will be
	 * unmodified.
	 * 
	 * @param child not {@code null} and {@linkplain PDRectangle#getHeight() getHeight()} should be {@literal >= 0}
	 * @param parent not {@code null} and {@linkplain PDRectangle#getHeight() getHeight()} should be {@literal >= 0}
	 * @throws NullPointerException if shape or parent is {@code null}
	 */
	public void alignChildWithParent(PDRectangle child, PDRectangle parent) {
		Objects.requireNonNull(child, "Child must not be null.");
		float height = child.getHeight();
		float y = alignWithParent(height, parent);
		child.setLowerLeftY(y);
		child.setUpperRightY(y + height);
	}

}
