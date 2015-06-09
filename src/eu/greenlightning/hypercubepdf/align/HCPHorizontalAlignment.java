package eu.greenlightning.hypercubepdf.align;

import static eu.greenlightning.hypercubepdf.align.HCPLineAlignment.*;

import java.util.Objects;

import org.apache.pdfbox.pdmodel.common.PDRectangle;

public enum HCPHorizontalAlignment {

	// Horizontal axis points right
	LEFT(BEGINNING), CENTER(MIDDLE), RIGHT(END);

	private final HCPLineAlignment position;

	private HCPHorizontalAlignment(HCPLineAlignment position) {
		this.position = Objects.requireNonNull(position);
	}

	public float align(float width, float leftX, float rightX) {
		return position.align(width, leftX, rightX);
	}

	public float alignWithParent(float width, PDRectangle parent) {
		Objects.requireNonNull(parent, "Parent must not be null.");
		return align(width, parent.getLowerLeftX(), parent.getUpperRightX());
	}

	public void alignShapeWithParent(PDRectangle shape, PDRectangle parent) {
		Objects.requireNonNull(shape, "Shape must not be null.");
		float width = shape.getWidth();
		float x = alignWithParent(width, parent);
		shape.setLowerLeftX(x);
		shape.setUpperRightX(x + width);
	}

}
