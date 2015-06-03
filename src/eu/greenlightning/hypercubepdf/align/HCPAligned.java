package eu.greenlightning.hypercubepdf.align;

import java.io.IOException;
import java.util.Objects;

import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;

import eu.greenlightning.hypercubepdf.HCPElement;

public class HCPAligned implements HCPElement {

	public static HCPAligned withHorizontalAlignment(HCPElement element, HCPHorizontalAlignment horizontal) {
		Objects.requireNonNull(horizontal, "Horizontal must not be null.");
		return new HCPAligned(element, horizontal, null);
	}

	public static HCPAligned withVerticalAlignment(HCPElement element, HCPVerticalAlignment vertical) {
		Objects.requireNonNull(vertical, "Vertical must not be null.");
		return new HCPAligned(element, null, vertical);
	}

	public static HCPAligned withAlignment(HCPElement element, HCPAlignment alignment) {
		Objects.requireNonNull(alignment, "Alignment must not be null.");
		return new HCPAligned(element, alignment.getHorizontalAlignment(), alignment.getVerticalAlignment());
	}

	public static HCPAligned withAlignment(HCPElement element, HCPHorizontalAlignment horizontal,
		HCPVerticalAlignment vertical) {
		Objects.requireNonNull(horizontal, "Horizontal must not be null.");
		Objects.requireNonNull(vertical, "Vertical must not be null.");
		return new HCPAligned(element, horizontal, vertical);
	}

	private final HCPElement element;
	private final HCPHorizontalAlignment horizontal;
	private final HCPVerticalAlignment vertical;

	private HCPAligned(HCPElement element, HCPHorizontalAlignment horizontal, HCPVerticalAlignment vertical) {
		this.element = Objects.requireNonNull(element, "Element must not be null.");
		this.horizontal = horizontal;
		this.vertical = vertical;
	}

	@Override
	public float getWidth() throws IOException {
		return element.getWidth();
	}

	@Override
	public float getHeight() throws IOException {
		return element.getHeight();
	}

	@Override
	public void paint(PDPageContentStream content, PDRectangle parentShape) throws IOException {
		PDRectangle elementShape = new PDRectangle(element.getWidth(), element.getHeight());
		alignHorizontally(elementShape, parentShape);
		alignVertically(elementShape, parentShape);
		element.paint(content, elementShape);
	}

	private void alignHorizontally(PDRectangle elementShape, PDRectangle parentShape) {
		if (horizontal == null) {
			elementShape.setLowerLeftX(parentShape.getLowerLeftX());
			elementShape.setUpperRightX(parentShape.getUpperRightX());
		} else {
			horizontal.alignShapeWithParent(elementShape, parentShape);
		}
	}

	private void alignVertically(PDRectangle elementShape, PDRectangle parentShape) {
		if (vertical == null) {
			elementShape.setLowerLeftY(parentShape.getLowerLeftY());
			elementShape.setUpperRightY(parentShape.getUpperRightY());
		} else {
			vertical.alignShapeWithParent(elementShape, parentShape);
		}
	}

}
