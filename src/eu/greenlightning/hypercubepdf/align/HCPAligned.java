package eu.greenlightning.hypercubepdf.align;

import java.io.IOException;
import java.util.Objects;

import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;

import eu.greenlightning.hypercubepdf.HCPElement;

public class HCPAligned implements HCPElement {

	private final HCPElement element;
	private final HCPAlignment alignment;

	public HCPAligned(HCPElement element, HCPHorizontalAlignment horizontal, HCPVerticalAlignment vertical) {
		this(element, HCPAlignment.valueOf(horizontal, vertical));
	}

	public HCPAligned(HCPElement element, HCPAlignment alignment) {
		this.element = Objects.requireNonNull(element, "Element must not be null.");
		this.alignment = Objects.requireNonNull(alignment, "Alignment must not be null.");
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
	public void paint(PDPageContentStream content, PDRectangle shape) throws IOException {
		PDRectangle elementShape = new PDRectangle(element.getWidth(), element.getHeight());
		alignment.alignShapeWithParent(elementShape, shape);
		element.paint(content, elementShape);
	}

}
