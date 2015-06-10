package eu.greenlightning.hypercubepdf.border;

import java.io.IOException;
import java.util.Objects;

import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;

import eu.greenlightning.hypercubepdf.HCPElement;

/**
 * An {@link HCPElement} wrapper which adds an empty border of a fixed size around the element.
 * <p>
 * The {@link #getWidth()} and {@link #getHeight()} methods return the sum of the element's size and the size of the
 * border.
 * <p>
 * The element's {@link HCPElement#paint(PDPageContentStream, PDRectangle)} method will not be called if there is not
 * enough space for the border.
 * <p>
 * This class is immutable.
 *
 * @author Green Lightning
 */
public class HCPEmptyBorder implements HCPElement {

	/**
	 * Wraps the element with an invisible border.
	 * 
	 * @param element not {@code null}
	 * @return a wrapper for element
	 * @throws NullPointerException if element is {@code null}
	 */
	public static HCPEmptyBorder getEmptyInstance(HCPElement element) {
		return new HCPEmptyBorder(element, 0, 0, 0, 0);
	}

	/**
	 * Wraps the element with a border with the given size on the top.
	 * 
	 * @param element not {@code null}
	 * @param top {@literal >= 0}
	 * @return a wrapper for element with the given border size
	 * @throws NullPointerException if element is {@code null}
	 * @throws IllegalArgumentException if top is {@literal < 0}
	 */
	public static HCPEmptyBorder getTopInstance(HCPElement element, float top) {
		return new HCPEmptyBorder(element, top, 0, 0, 0);
	}

	/**
	 * Wraps the element with a border with the given size on the right.
	 * 
	 * @param element not {@code null}
	 * @param right {@literal >= 0}
	 * @return a wrapper for element with the given border size
	 * @throws NullPointerException if element is {@code null}
	 * @throws IllegalArgumentException if right is {@literal < 0}
	 */
	public static HCPEmptyBorder getRightInstance(HCPElement element, float right) {
		return new HCPEmptyBorder(element, 0, right, 0, 0);
	}

	/**
	 * Wraps the element with a border with the given size on the bottom.
	 * 
	 * @param element not {@code null}
	 * @param bottom {@literal >= 0}
	 * @return a wrapper for element with the given border size
	 * @throws NullPointerException if element is {@code null}
	 * @throws IllegalArgumentException if bottom is {@literal < 0}
	 */
	public static HCPEmptyBorder getBottomInstance(HCPElement element, float bottom) {
		return new HCPEmptyBorder(element, 0, 0, bottom, 0);
	}

	/**
	 * Wraps the element with a border with the given size on the left.
	 * 
	 * @param element not {@code null}
	 * @param left {@literal >= 0}
	 * @return a wrapper for element with the given border size
	 * @throws NullPointerException if element is {@code null}
	 * @throws IllegalArgumentException if left is {@literal < 0}
	 */
	public static HCPEmptyBorder getLeftInstance(HCPElement element, float left) {
		return new HCPEmptyBorder(element, 0, 0, 0, left);
	}

	/**
	 * Wraps the element with a border with the given size on all sides.
	 * 
	 * @param element not {@code null}
	 * @param size {@literal >= 0}
	 * @return a wrapper for element with the given border size
	 * @throws NullPointerException if element is {@code null}
	 * @throws IllegalArgumentException if size is {@literal < 0}
	 */
	public static HCPEmptyBorder getAllSidesInstance(HCPElement element, float size) {
		return new HCPEmptyBorder(element, size, size, size, size);
	}

	/**
	 * Wraps the element with a border with the {@code horizontal} size on the left and right.
	 * 
	 * @param element not {@code null}
	 * @param horizontal {@literal >= 0}
	 * @return a wrapper for element with the given border sizes
	 * @throws NullPointerException if element is {@code null}
	 * @throws IllegalArgumentException if horizontal is {@literal < 0}
	 */
	public static HCPEmptyBorder getHorizontalInstance(HCPElement element, float horizontal) {
		return new HCPEmptyBorder(element, 0, horizontal, 0, horizontal);
	}

	/**
	 * Wraps the element with a border with the {@code vertical} size on the top and bottom.
	 * 
	 * @param element not {@code null}
	 * @param vertical {@literal >= 0}
	 * @return a wrapper for element with the given border sizes
	 * @throws NullPointerException if element is {@code null}
	 * @throws IllegalArgumentException if vertical is {@literal < 0}
	 */
	public static HCPEmptyBorder getVerticalInstance(HCPElement element, float vertical) {
		return new HCPEmptyBorder(element, vertical, 0, vertical, 0);
	}

	/**
	 * Wraps the element with a border with the {@code horizontal} size on the left an right and the {@code vertical}
	 * size on the top and bottom.
	 * 
	 * @param element not {@code null}
	 * @param horizontal {@literal >= 0}
	 * @param vertical {@literal >= 0}
	 * @return a wrapper for element with the given border sizes
	 * @throws NullPointerException if element is {@code null}
	 * @throws IllegalArgumentException if any size is {@literal < 0}
	 */
	public static HCPEmptyBorder getHorizontalVerticalInstance(HCPElement element, float horizontal, float vertical) {
		return new HCPEmptyBorder(element, vertical, horizontal, vertical, horizontal);
	}

	/**
	 * Wraps the element with a border with a different size on each size, as specified.
	 * 
	 * @param element not {@code null}
	 * @param top {@literal >= 0}
	 * @param right {@literal >= 0}
	 * @param bottom {@literal >= 0}
	 * @param left {@literal >= 0}
	 * @return a wrapper for element with the given border sizes
	 * @throws NullPointerException if element is {@code null}
	 * @throws IllegalArgumentException if any size is {@literal < 0}
	 */
	public static HCPEmptyBorder getTopRightBottomLeftInstance(HCPElement element, float top, float right, float bottom,
		float left) {
		return new HCPEmptyBorder(element, top, right, bottom, left);
	}

	private final HCPElement element;
	private final float top, right, bottom, left;

	private HCPEmptyBorder(HCPElement element, float top, float right, float bottom, float left) {
		this.element = Objects.requireNonNull(element, "Element must not be null.");
		this.top = checkSize(top, "Top");
		this.right = checkSize(right, "Right");
		this.bottom = checkSize(bottom, "Bottom");
		this.left = checkSize(left, "Left");
	}

	private float checkSize(float size, String name) {
		if (size < 0)
			throw new IllegalArgumentException(name + " must be equal to or greater than zero, but was " + size + ".");
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
