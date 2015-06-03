package eu.greenlightning.hypercubepdf.align;

import java.util.Objects;

import org.apache.pdfbox.pdmodel.common.PDRectangle;

public enum HCPAlignment {

	TOP_LEFT(HCPHorizontalAlignment.LEFT, HCPVerticalAlignment.TOP), //
	TOP(HCPHorizontalAlignment.CENTER, HCPVerticalAlignment.TOP), //
	TOP_RIGHT(HCPHorizontalAlignment.RIGHT, HCPVerticalAlignment.TOP), //

	LEFT(HCPHorizontalAlignment.LEFT, HCPVerticalAlignment.CENTER), //
	CENTER(HCPHorizontalAlignment.CENTER, HCPVerticalAlignment.CENTER), //
	RIGHT(HCPHorizontalAlignment.RIGHT, HCPVerticalAlignment.CENTER), //

	BOTTOM_LEFT(HCPHorizontalAlignment.LEFT, HCPVerticalAlignment.BOTTOM), //
	BOTTOM(HCPHorizontalAlignment.CENTER, HCPVerticalAlignment.BOTTOM), //
	BOTTOM_RIGHT(HCPHorizontalAlignment.RIGHT, HCPVerticalAlignment.BOTTOM);

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

	public HCPHorizontalAlignment getHorizontalAlignment() {
		return horizontalAlignment;
	}

	public HCPVerticalAlignment getVerticalAlignment() {
		return verticalAlignment;
	}

	public void alignShapeWithParent(PDRectangle shape, PDRectangle parent) {
		getHorizontalAlignment().alignShapeWithParent(shape, parent);
		getVerticalAlignment().alignShapeWithParent(shape, parent);
	}

}
