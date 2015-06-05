package eu.greenlightning.hypercubepdf;

import java.util.Objects;

/**
 * Defines which of the four border parts of an {@link HCPArea} should be painted.
 *
 * @author Green Lightning
 */
public enum HCPAreaBorder {

	/** Do not paint any border parts. */
	NO_BORDER(false, false, false, false),
	
	/** Paint only the top border part. */
	TOP_BORDER(true, false, false, false),
	
	/** Paint only the right border part. */
	RIGHT_BORDER(false, true, false, false),
	
	/** Paint only the bottom border part. */
	BOTTOM_BORDER(false, false, true, false),
	
	/** Paint only the left border part. */
	LEFT_BORDER(false, false, false, true),
	
	/** Paint the top and the left border parts. */
	TOP_LEFT_BORDER(true, false, false, true),
	
	/** Paint the top and the right border parts. */
	TOP_RIGHT_BORDER(true, true, false, false),
	
	/** Paint the bottom and the left border parts. */
	BOTTOM_LEFT_BORDER(false, false, true, true),
	
	/** Paint the bottom and the right border parts. */
	BOTTOM_RIGHT_BORDER(false, true, true, false),
	
	/** Paint the top and the bottom border parts. */
	HORIZONTAL_BORDER(true, false, true, false),
	
	/** Paint the left and the right border parts. */
	VERTICAL_BORDER(false, true, false, true),
	
	/** Paint the left, the bottom and the right border parts. */
	NO_TOP_BORDER(false, true, true, true),
	
	/** Paint the top, the left and the bottom border parts. */
	NO_RIGHT_BORDER(true, false, true, true),
	
	/** Paint the left, the top and the right border parts. */
	NO_BOTTOM_BORDER(true, true, false, true),
	
	/** Paint the top, the right and the bottom border parts. */
	NO_LEFT_BORDER(true, true, true, false),
	
	/** Paint all four border parts. */
	FULL_BORDER(true, true, true, true);

	private final boolean top, right, bottom, left;

	private HCPAreaBorder(boolean top, boolean right, boolean bottom, boolean left) {
		this.top = top;
		this.right = right;
		this.bottom = bottom;
		this.left = left;
	}

	/**
	 * Returns {@code true} if the top border part should be painted for this border.
	 * 
	 * @return whether the top border part should be painted
	 */
	public boolean paintTop() {
		return top;
	}

	/**
	 * Returns {@code true} if the right border part should be painted for this border.
	 * 
	 * @return whether the right border part should be painted
	 */
	public boolean paintRight() {
		return right;
	}

	/**
	 * Returns {@code true} if the bottom border part should be painted for this border.
	 * 
	 * @return whether the bottom border part should be painted
	 */
	public boolean paintBottom() {
		return bottom;
	}

	/**
	 * Returns {@code true} if the left border part should be painted for this border.
	 * 
	 * @return whether the left border part should be painted
	 */
	public boolean paintLeft() {
		return left;
	}

	/**
	 * Returns the {@link HCPAreaBorder} which is a combination between this border and the other border. That is the
	 * resulting border will paint all border parts painted by this border and all border parts painted by the other
	 * border. In other words, the resulting border will paint a given border part if this border or the other border
	 * (or both) paint it.
	 * 
	 * @param other not {@code null}
	 * @return the combination of this border and the other border
	 * @throws NullPointerException if other is {@code null}
	 */
	public HCPAreaBorder combineWith(HCPAreaBorder other) {
		Objects.requireNonNull(other, "Other must not be null.");
		return valueOf(top || other.top, right || other.right, bottom || other.bottom, left || other.left);
	}

	/**
	 * Returns the {@link HCPAreaBorder} which is a combination between the two specified borders. That is the resulting
	 * border will paint all border parts painted by the first border and all border parts painted by the second border.
	 * In other words, the resulting border will paint a given border part if the first border or the second border (or
	 * both) paint it.
	 * 
	 * @param first not {@code null}
	 * @param second not {@code null}
	 * @return the combination of the first border and the second border
	 * @throws NullPointerException if first or second is {@code null}
	 */
	public static HCPAreaBorder combine(HCPAreaBorder first, HCPAreaBorder second) {
		Objects.requireNonNull(first, "First must not be null.");
		Objects.requireNonNull(second, "Second must not be null.");
		return first.combineWith(second);
	}

	/**
	 * Returns the {@link HCPAreaBorder} which paints the specified border parts.
	 * 
	 * @param top whether the top border part should be painted
	 * @param right whether the right border part should be painted
	 * @param bottom whether the bottom border part should be painted
	 * @param left whether the left  border part should be painted
	 * @return the {@link HCPAreaBorder} which paints the specified border parts
	 */
	public static HCPAreaBorder valueOf(boolean top, boolean right, boolean bottom, boolean left) {
		for (HCPAreaBorder border : values())
			if (border.top == top && border.right == right
			 && border.bottom == bottom && border.left == left)
				return border;
		throw new RuntimeException("No border found. Should not get here.");
	}

}
