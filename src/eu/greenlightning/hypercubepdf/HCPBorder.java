package eu.greenlightning.hypercubepdf;

import java.io.IOException;
import java.util.Objects;

import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;

public class HCPBorder implements HCPElement {

	public static HCPBorder getEmptyInstance(HCPElement element) {
		return new HCPBorder(element, 0, 0, 0, 0);
	}

	public static HCPBorder getTopInstance(HCPElement element, float top) {
		return new HCPBorder(element, top, 0, 0, 0);
	}

	public static HCPBorder getRightInstance(HCPElement element, float right) {
		return new HCPBorder(element, 0, right, 0, 0);
	}

	public static HCPBorder getBottomInstance(HCPElement element, float bottom) {
		return new HCPBorder(element, 0, 0, bottom, 0);
	}

	public static HCPBorder getLeftInstance(HCPElement element, float left) {
		return new HCPBorder(element, 0, 0, 0, left);
	}

	public static HCPBorder getAllSidesInstance(HCPElement element, float size) {
		return new HCPBorder(element, size, size, size, size);
	}
	
	public static HCPBorder getHorizontalInstance(HCPElement element, float horizontal) {
		return new HCPBorder(element, 0, horizontal, 0, horizontal);
	}

	public static HCPBorder getVerticalInstance(HCPElement element, float vertical) {
		return new HCPBorder(element, vertical, 0, vertical, 0);
	}

	public static HCPBorder getHorizontalVerticalInstance(HCPElement element, float horizontal, float vertical) {
		return new HCPBorder(element, vertical, horizontal, vertical, horizontal);
	}

	public static HCPBorder getTopRightBottomLeftInstance(HCPElement element, float top, float right,
			float bottom, float left) {
		return new HCPBorder(element, top, right, bottom, left);
	}

	private final HCPElement element;
	private final float top, right, bottom, left;

	private HCPBorder(HCPElement element, float top, float right, float bottom, float left) {
		this.element = Objects.requireNonNull(element, "Element must not be null.");
		this.top = checkSize(top, "Top");
		this.right = checkSize(right, "Right");
		this.bottom = checkSize(bottom, "Bottom");
		this.left = checkSize(left, "Left");
	}

	private float checkSize(float size, String name) {
		if (size < 0)
			throw new IllegalArgumentException(name + " must be equal to or greater than zero, but was "
					+ size + ".");
		return size;
	}

	@Override
	public float getWidth() throws IOException {
		return left + element.getWidth() + right;
	}

	@Override
	public float getHeight() throws IOException {
		return bottom + element.getHeight() + top;
	}

	@Override
	public void paint(PDPageContentStream content, PDRectangle shape) throws IOException {
		float width = shape.getWidth() - left - right;
		float height = shape.getHeight() - bottom - top;
		if (width > 0 && height > 0) {
			PDRectangle elementShape = new PDRectangle(width, height);
			elementShape.move(shape.getLowerLeftX() + left, shape.getLowerLeftY() + bottom);
			element.paint(content, elementShape);
		}
	}

}
