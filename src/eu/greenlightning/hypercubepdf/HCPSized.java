package eu.greenlightning.hypercubepdf;

import java.io.IOException;
import java.util.Objects;

import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;

/**
 * An {@link HCPElement} wrapper which changes the size of the element.
 *
 * @author Green Lightning
 */
public class HCPSized implements HCPElement {

	/**
	 * Can be used as either width or height and specifies that the element's {@link HCPElement#getWidth()} and
	 * {@link HCPElement#getHeight()} methods should be used respectively.
	 */
	public static final float USE_ELEMENT_SIZE = -1;

	/**
	 * Wraps the element giving it infinite width. Its height will be unchanged.
	 * 
	 * @param element not {@code null}
	 * @return a wrapper for element with infinite width
	 * @throws NullPointerException if element is {@code null}
	 */
	public static HCPSized withInfiniteWidth(HCPElement element) {
		return withWidth(element, Float.POSITIVE_INFINITY);
	}

	/**
	 * Wraps the element giving it infinite height. Its width will be unchanged.
	 * 
	 * @param element not {@code null}
	 * @return a wrapper for element with infinite height
	 * @throws NullPointerException if element is {@code null}
	 */
	public static HCPSized withInfiniteHeight(HCPElement element) {
		return withHeight(element, Float.POSITIVE_INFINITY);
	}

	/**
	 * Wraps the element giving it both infinite width and height.
	 * 
	 * @param element not {@code null}
	 * @return a wrapper for element with infinite size
	 * @throws NullPointerException if element is {@code null}
	 */
	public static HCPSized withInfiniteSize(HCPElement element) {
		return withSize(element, Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY);
	}

	/**
	 * Wraps the element giving it the specified width. Its height will be unchanged.
	 * <p>
	 * It is advised to use a width bigger than the width returned by the element's {@link HCPElement#getWidth()}
	 * method.
	 * 
	 * @param element not {@code null}
	 * @param width must be {@literal >= 0}
	 * @return a wrapper for element with the specified width
	 * @throws NullPointerException if element is {@code null}
	 * @throws IllegalArgumentException if width is {@literal < 0}
	 * @see #USE_ELEMENT_SIZE
	 */
	public static HCPSized withWidth(HCPElement element, float width) {
		return new HCPSized(element, width, USE_ELEMENT_SIZE);
	}

	/**
	 * Wraps the element giving it the specified height. Its width will be unchanged.
	 * <p>
	 * It is advised to use a height bigger than the height returned by the element's {@link HCPElement#getHeight()}
	 * method.
	 * 
	 * @param element not {@code null}
	 * @param height must be {@literal >= 0}
	 * @return a wrapper for element with the specified height
	 * @throws NullPointerException if element is {@code null}
	 * @throws IllegalArgumentException if height is {@literal < 0}
	 * @see #USE_ELEMENT_SIZE
	 */
	public static HCPSized withHeight(HCPElement element, float height) {
		return new HCPSized(element, USE_ELEMENT_SIZE, height);
	}

	/**
	 * Wraps the element giving it the specified width and height.
	 * <p>
	 * It is advised to use a width bigger than the width returned by the element's {@link HCPElement#getWidth()} method
	 * and a height bigger than the height returned by the element's {@link HCPElement#getHeight()} method.
	 * 
	 * @param element not {@code null}
	 * @param width must be {@literal >= 0}
	 * @param height must be {@literal >= 0}
	 * @return a wrapper for element with the specified size
	 * @throws NullPointerException if element is {@code null}
	 * @throws IllegalArgumentException if width or height is {@literal < 0}
	 * @see #USE_ELEMENT_SIZE
	 */
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
