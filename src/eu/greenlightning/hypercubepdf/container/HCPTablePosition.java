package eu.greenlightning.hypercubepdf.container;

import java.io.IOException;
import java.util.Objects;

import eu.greenlightning.hypercubepdf.HCPElement;

/**
 * Describes the position of an {@link HCPElement} inside an {@link HCPTableContainer}. An element may extend over
 * multiple continuous columns and / or rows. Therefore the position is defined by the x- and y-coordinates of the
 * element as well as the number of columns and rows spanned.
 * <p>
 * This class is immutable.
 * 
 * @author Green Lightning
 */
public class HCPTablePosition {

	/**
	 * Can be used as a span value to indicate that the element should always extend to the end of the container
	 * spanning over all remaining columns or rows.
	 */
	public static final int REMAINING = -1;

	private final HCPElement element;
	private final int x, y;
	private final int horizontalSpan, verticalSpan;

	/**
	 * Creates a new {@link HCPTablePosition} using the specified element and position.
	 * 
	 * @param element not {@code null}
	 * @param x must be {@literal >= 0}
	 * @param y must be {@literal >= 0}
	 * @throws NullPointerException if element is {@code null}
	 * @throws IllegalArgumentException if x or y contains an illegal value
	 */
	public HCPTablePosition(HCPElement element, int x, int y) {
		this(element, x, y, 1, 1);
	}

	/**
	 * Creates a new {@link HCPTablePosition} using the specified element, position and spans.
	 * 
	 * @param element not {@code null}
	 * @param x must be {@literal >= 0}
	 * @param y must be {@literal >= 0}
	 * @param horizontalSpan must be {@literal >= 1} or {@link #REMAINING}
	 * @param verticalSpan must be {@literal >= 1} or {@link #REMAINING}
	 * @throws NullPointerException if element is {@code null}
	 * @throws IllegalArgumentException if x, y, horizontalSpan or verticalSpan contains an illegal value
	 */
	public HCPTablePosition(HCPElement element, int x, int y, int horizontalSpan, int verticalSpan) {
		this.element = Objects.requireNonNull(element, "Element must not be null.");
		this.x = checkPosition(x, "X");
		this.y = checkPosition(y, "Y");
		this.horizontalSpan = checkSpan(horizontalSpan, "HorizontalSpan");
		this.verticalSpan = checkSpan(verticalSpan, "VerticalSpan");
	}

	private int checkPosition(int position, String name) {
		if (position < 0)
			throw new IllegalArgumentException(name + " must be equal to or greater than zero.");
		return position;
	}

	private int checkSpan(int span, String name) {
		if (span != REMAINING && span < 1)
			throw new IllegalArgumentException(name + " must be REMAINING or equal to or greater than one.");
		return span;
	}

	/**
	 * Returns the element at this position.
	 * 
	 * @return the element at this position
	 */
	public HCPElement getElement() {
		return element;
	}

	/**
	 * Returns the width of the element at this position.
	 * 
	 * @return the width of the element
	 * @throws IOException if an error occurs
	 */
	public float getWidth() throws IOException {
		return element.getWidth();
	}

	/**
	 * Returns the height of the element at this position.
	 * 
	 * @return the height of the element
	 * @throws IOException if an error occurs
	 */
	public float getHeight() throws IOException {
		return element.getHeight();
	}

	/**
	 * Returns whether this position has the specified coordinates.
	 * 
	 * @param x the x-coordinate to test for
	 * @param y the y-coordinate to test for
	 * @return {@code true} if the coordinates of this position match the specified coordinates; {@code false} otherwise
	 */
	public boolean isAt(int x, int y) {
		return this.x == x && this.y == y;
	}

	/**
	 * Returns the x-coordinate of this position.
	 * 
	 * @return the x-coordinate
	 */
	public int getX() {
		return x;
	}

	/**
	 * Returns the y-coordinate of this position.
	 * 
	 * @return the y-coordinate
	 */
	public int getY() {
		return y;
	}

	/**
	 * Returns the right-most x-coordinate spanned by this position. If this position does not span multiple columns,
	 * the value of {@link #getX()} is returned. If this position spans all remaining columns, the value of
	 * {@link #getX()} is returned by default as well.
	 * 
	 * @return the right-most x-coordinate
	 */
	public int getRightX() {
		return horizontallyRemaining() ? getX() : getX() + getHorizontalSpan() - 1;
	}

	/**
	 * Returns the lowest y-coordinate spanned by this position. If this position does not span multiple rows, the value
	 * of {@link #getY()} is returned. If this position spans all remaining rows, the value of {@link #getY()} is
	 * returned by default as well.
	 * 
	 * @return the lowest y-coordinate
	 */
	public int getLowerY() {
		return verticallyRemaining() ? getY() : getY() + getVerticalSpan() - 1;
	}

	/**
	 * Returns whether this position spans multiple columns and / or rows.
	 * 
	 * @return whether this position spans multiple columns and / or rows
	 */
	public boolean spans() {
		return spansHorizontally() || spansVertically();
	}

	/**
	 * Returns whether this position spans multiple columns.
	 * 
	 * @return whether this position spans multiple columns
	 */
	public boolean spansHorizontally() {
		return horizontallyRemaining() || horizontalSpan > 1;
	}

	/**
	 * Returns whether this position spans multiple rows.
	 * 
	 * @return whether this position spans multiple rows
	 */
	public boolean spansVertically() {
		return verticallyRemaining() || verticalSpan > 1;
	}

	/**
	 * Returns whether this position spans over all remaining columns.
	 * 
	 * @return whether this position spans all remaining columns
	 */
	public boolean horizontallyRemaining() {
		return horizontalSpan == REMAINING;
	}

	/**
	 * Returns whether this position spans over all remaining rows.
	 * 
	 * @return whether this position spans all remaining rows
	 */
	public boolean verticallyRemaining() {
		return verticalSpan == REMAINING;
	}

	/**
	 * Returns the number of columns spanned by this position. A value of 1 indicates that this position spans only one
	 * column and is a regular position. This method may also return the special value {@link #REMAINING} which
	 * indicates that the position spans all remaining columns in the {@link HCPTableContainer}.
	 * 
	 * @return the number of columns spanned by this position or {@link #REMAINING}
	 */
	public int getHorizontalSpan() {
		return horizontalSpan;
	}

	/**
	 * Returns the number of rows spanned by this position. A value of 1 indicates that this position spans only one row
	 * and is a regular position. This method may also return the special value {@link #REMAINING} which indicates that
	 * the position spans all remaining rows in the {@link HCPTableContainer}.
	 * 
	 * @return the number of rows spanned by this position or {@link #REMAINING}
	 */
	public int getVerticalSpan() {
		return verticalSpan;
	}

}
