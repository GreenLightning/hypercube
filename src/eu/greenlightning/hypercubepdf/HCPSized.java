package eu.greenlightning.hypercubepdf;

import java.io.IOException;
import java.util.Objects;

import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;

public class HCPSized implements HCPElement {

	public static final float USE_ELEMENT_SIZE = -1;

	public static HCPSized withInfiniteWidth(HCPElement element) {
		return withWidth(element, Float.POSITIVE_INFINITY);
	}

	public static HCPSized withInfiniteHeight(HCPElement element) {
		return withHeight(element, Float.POSITIVE_INFINITY);
	}

	public static HCPSized withInfiniteSize(HCPElement element) {
		return withSize(element, Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY);
	}

	public static HCPSized withWidth(HCPElement element, float width) {
		return new HCPSized(element, width, USE_ELEMENT_SIZE);
	}

	public static HCPSized withHeight(HCPElement element, float height) {
		return new HCPSized(element, USE_ELEMENT_SIZE, height);
	}

	public static HCPSized withSize(HCPElement element, float width, float height) {
		return new HCPSized(element, width, height);
	}

	private final HCPElement element;
	private final float width;
	private final float height;

	private HCPSized(HCPElement element, float width, float height) {
		this.element = Objects.requireNonNull(element, "Element must not be null.");
		this.width = checkSize(width, "Width");
		this.height = checkSize(height, "Height");
	}

	private float checkSize(float size, String name) {
		if (size != USE_ELEMENT_SIZE && size < 0)
			throw new IllegalArgumentException(name + " must be USE_ELEMENT_SIZE or "
					+ "equal to or greater than zero, but was " + size + ".");
		return size;
	}

	@Override
	public float getWidth() throws IOException {
		return width == USE_ELEMENT_SIZE ? element.getWidth() : width;
	}

	@Override
	public float getHeight() throws IOException {
		return height == USE_ELEMENT_SIZE ? element.getHeight() : height;
	}

	@Override
	public void paint(PDPageContentStream content, PDRectangle shape) throws IOException {
		element.paint(content, shape);
	}

}
