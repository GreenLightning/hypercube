package eu.greenlightning.hypercubepdf.align;

import static eu.greenlightning.hypercubepdf.align.HCPAlignedPosition.*;

import java.util.Objects;

import org.apache.pdfbox.pdmodel.common.PDRectangle;

public enum HCPVerticalAlignment {

	// Vertical axis points up
	TOP(END), CENTER(MIDDLE), BOTTOM(BEGINNING);

	private final HCPAlignedPosition position;

	private HCPVerticalAlignment(HCPAlignedPosition position) {
		this.position = Objects.requireNonNull(position);
	}

	public float align(float height, float lowerY, float upperY) {
		return position.align(height, lowerY, upperY);
	}

	public float alignWithParent(float height, PDRectangle parent) {
		Objects.requireNonNull(parent, "Parent must not be null.");
		return align(height, parent.getLowerLeftY(), parent.getUpperRightY());
	}

	public void alignShapeWithParent(PDRectangle shape, PDRectangle parent) {
		Objects.requireNonNull(shape, "Shape must not be null.");
		float height = shape.getHeight();
		float y = alignWithParent(height, parent);
		shape.setLowerLeftY(y);
		shape.setUpperRightY(y + height);
	}

}
