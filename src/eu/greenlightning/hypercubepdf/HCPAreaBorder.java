package eu.greenlightning.hypercubepdf;

import java.util.Objects;

public enum HCPAreaBorder {

	NO_BORDER(false, false, false, false),
	TOP_BORDER(true, false, false, false),
	RIGHT_BORDER(false, true, false, false),
	BOTTOM_BORDER(false, false, true, false),
	LEFT_BORDER(false, false, false, true),
	TOP_LEFT_BORDER(true, false, false, true),
	TOP_RIGHT_BORDER(true, true, false, false),
	BOTTOM_LEFT_BORDER(false, false, true, true),
	BOTTOM_RIGHT_BORDER(false, true, true, false),
	HORIZONTAL_BORDER(true, false, true, false),
	VERTICAL_BORDER(false, true, false, true),
	NO_TOP_BORDER(false, true, true, true),
	NO_RIGHT_BORDER(true, false, true, true),
	NO_BOTTOM_BORDER(true, true, false, true),
	NO_LEFT_BORDER(true, true, true, false),
	FULL_BORDER(true, true, true, true);

	private final boolean top, right, bottom, left;

	private HCPAreaBorder(boolean top, boolean right, boolean bottom, boolean left) {
		this.top = top;
		this.right = right;
		this.bottom = bottom;
		this.left = left;
	}

	public boolean paintTop() {
		return top;
	}

	public boolean paintRight() {
		return right;
	}

	public boolean paintBottom() {
		return bottom;
	}

	public boolean paintLeft() {
		return left;
	}

	public HCPAreaBorder combineWith(HCPAreaBorder other) {
		Objects.requireNonNull(other, "Other must not be null.");
		return valueOf(top || other.top, right || other.right, bottom || other.bottom, left || other.left);
	}

	public static HCPAreaBorder combine(HCPAreaBorder first, HCPAreaBorder second) {
		Objects.requireNonNull(first, "First must not be null.");
		Objects.requireNonNull(second, "Second must not be null.");
		return first.combineWith(second);
	}

	public static HCPAreaBorder valueOf(boolean top, boolean right, boolean bottom, boolean left) {
		for (HCPAreaBorder border : values())
			if (border.top == top && border.right == right
			 && border.bottom == bottom && border.left == left)
				return border;
		throw new RuntimeException("No border found. Should not get here.");
	}

}
