package eu.greenlightning.hypercubepdf.border;

import java.awt.Color;
import java.io.IOException;
import java.util.Objects;

import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;

import eu.greenlightning.hypercubepdf.HCPElement;

public class HCPLineBorder implements HCPElement {

	private final HCPElement element;
	private final Color color;
	private final float size;

	public HCPLineBorder(HCPElement element) {
		this(element, Color.BLACK);
	}

	public HCPLineBorder(HCPElement element, Color color) {
		this(element, color, 1);
	}

	public HCPLineBorder(HCPElement element, Color color, float size) {
		this.element = Objects.requireNonNull(element, "Element must not be null.");
		this.color = Objects.requireNonNull(color, "Color must not be null.");
		this.size = checkSize(size);
	}

	private float checkSize(float size) {
		if (size <= 0)
			throw new IllegalArgumentException("Size must be greater than zero, but was " + size + ".");
		return size;
	}

	@Override
	public float getWidth() throws IOException {
		return 2 * size + element.getWidth();
	}

	@Override
	public float getHeight() throws IOException {
		return 2 * size + element.getHeight();
	}

	@Override
	public void paint(PDPageContentStream content, PDRectangle shape) throws IOException {
		float elementWidth = shape.getWidth() - 2 * size;
		float elementHeight = shape.getHeight() - 2 * size;
		if (elementWidth > 0 && elementHeight > 0) {
			paintBorder(content, shape);
			paintElement(content, shape, elementWidth, elementHeight);
		} else {
			fill(content, shape);
		}
	}

	private void paintBorder(PDPageContentStream content, PDRectangle shape) throws IOException {
		content.setStrokingColor(color);
		content.setLineWidth(size);
		float x = shape.getLowerLeftX() + size / 2;
		float y = shape.getLowerLeftY() + size / 2;
		float width = shape.getWidth() - size;
		float height = shape.getHeight() - size;
		content.addRect(x, y, width, height);
		content.stroke();
	}

	private void paintElement(PDPageContentStream content, PDRectangle shape, float elementWidth, float elementHeight)
		throws IOException {
		PDRectangle elementShape = new PDRectangle(elementWidth, elementHeight);
		elementShape.move(shape.getLowerLeftX() + size, shape.getLowerLeftY() + size);
		element.paint(content, elementShape);
	}

	private void fill(PDPageContentStream content, PDRectangle shape) throws IOException {
		content.setNonStrokingColor(color);
		content.fillRect(shape.getLowerLeftX(), shape.getLowerLeftY(), shape.getWidth(), shape.getHeight());
	}

}
