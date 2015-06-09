package eu.greenlightning.hypercubepdf.align;

import java.io.IOException;
import java.util.Objects;

import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;

import eu.greenlightning.hypercubepdf.HCPElement;

/**
 * An {@link HCPElement} wrapper which aligns the element inside the shape in which it is painted.
 * <p>
 * Calls to {@linkplain #getWidth()} and {@linkplain #getHeight()} are delegated to the element.
 * <p>
 * The element will be painted using the width returned by it's {@linkplain HCPElement#getWidth() getWidth()} method and
 * it will be aligned in the target shape using the specified horizontal alignment. However, if the element is wider
 * than the target shape, then it will be painted using the width of the target shape making alignment not necessary.
 * The same logic is applied independently to the element's height calling it's {@linkplain HCPElement#getHeight()
 * getHeight()} method.
 * <p>
 * If no alignment is specified ({@linkplain #withHorizontalAlignment(HCPElement, HCPHorizontalAlignment)
 * withHorizontalAlignment()} does not specify a vertical alignment and
 * {@linkplain #withVerticalAlignment(HCPElement, HCPVerticalAlignment) withVerticalAlignment()} does not specify a
 * horizontal alignment), then the element is painted using the size of the shape.
 * <p>
 * Please refer to the documentation of the static factory methods of this class for specific descriptions of the
 * alignment process.
 * <p>
 * This class is immutable.
 *
 * @author Green Lightning
 */
public class HCPAligned implements HCPElement {

	/**
	 * Aligns the element horizontally.
	 * <p>
	 * The element will be painted using the width returned by it's {@linkplain HCPElement#getWidth() getWidth()} method
	 * and it will be aligned in the target shape using the specified horizontal alignment. However, if the element is
	 * wider than the target shape, then it will be painted using the width of the target shape making alignment not
	 * necessary. The element will always be painted using the height of the target shape.
	 * 
	 * @param element not {@code null}
	 * @param horizontal not {@code null}
	 * @return a wrapper for element with the specified horizontal alignment
	 * @throws NullPointerException if element or horizontal is {@code null}
	 */
	public static HCPAligned withHorizontalAlignment(HCPElement element, HCPHorizontalAlignment horizontal) {
		Objects.requireNonNull(horizontal, "Horizontal must not be null.");
		return new HCPAligned(element, horizontal, null);
	}

	/**
	 * Aligns the element vertically.
	 * <p>
	 * The element will be painted using the height returned by it's {@linkplain HCPElement#getHeight() getHeight()}
	 * method and it will be aligned in the target shape using the specified vertical alignment. However, if the element
	 * is higher than the target shape, then it will be painted using the height of the target shape making alignment
	 * not necessary. The element will always be painted using the width of the target shape.
	 * 
	 * @param element not {@code null}
	 * @param vertical not {@code null}
	 * @return a wrapper for element with the specified vertical alignment
	 * @throws NullPointerException if element or vertical is {@code null}
	 */
	public static HCPAligned withVerticalAlignment(HCPElement element, HCPVerticalAlignment vertical) {
		Objects.requireNonNull(vertical, "Vertical must not be null.");
		return new HCPAligned(element, null, vertical);
	}

	/**
	 * Aligns the element both horizontally and vertically.
	 * <p>
	 * The element will be painted using the width returned by it's {@linkplain HCPElement#getWidth() getWidth()} method
	 * and it will be aligned in the target shape using the specified horizontal alignment. However, if the element is
	 * wider than the target shape, then it will be painted using the width of the target shape making alignment not
	 * necessary. The same logic is applied independently to the element's height calling it's
	 * {@linkplain HCPElement#getHeight() getHeight()} method.
	 * <p>
	 * This operation is equivalent to calling:
	 * <p>
	 * {@code HCPAligned.withAlignment(element, alignment.getHorizontalAlignment(), alignment.getVerticalAlignment());}
	 * 
	 * @param element not {@code null}
	 * @param alignment not {@code null}
	 * @return a wrapper for element with the specified alignment
	 * @throws NullPointerException if element or alignment is {@code null}
	 * @see #withAlignment(HCPElement, HCPHorizontalAlignment, HCPVerticalAlignment)
	 * @see HCPAlignment#getHorizontalAlignment()
	 * @see HCPAlignment#getVerticalAlignment()
	 */
	public static HCPAligned withAlignment(HCPElement element, HCPAlignment alignment) {
		Objects.requireNonNull(alignment, "Alignment must not be null.");
		return new HCPAligned(element, alignment.getHorizontalAlignment(), alignment.getVerticalAlignment());
	}

	/**
	 * Aligns the element both horizontally and vertically.
	 * <p>
	 * The element will be painted using the width returned by it's {@linkplain HCPElement#getWidth() getWidth()} method
	 * and it will be aligned in the target shape using the specified horizontal alignment. However, if the element is
	 * wider than the target shape, then it will be painted using the width of the target shape making alignment not
	 * necessary. The same logic is applied independently to the element's height calling it's
	 * {@linkplain HCPElement#getHeight() getHeight()} method.
	 * <p>
	 * This operation is equivalent to calling:
	 * <p>
	 * {@code HCPAligned.withAlignment(element, HCPAlignment.valueOf(horizontal, vertical));}
	 * 
	 * @param element not {@code null}
	 * @param horizontal not {@code null}
	 * @param vertical not {@code null}
	 * @return a wrapper for element with the specified horizontal and vertical alignment
	 * @throws NullPointerException if element, horizontal or vertical is {@code null}
	 * @see #withAlignment(HCPElement, HCPAlignment)
	 * @see HCPAlignment#valueOf(HCPHorizontalAlignment, HCPVerticalAlignment)
	 */
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
		float width = Math.min(element.getWidth(), parentShape.getWidth());
		float height = Math.min(element.getHeight(), parentShape.getHeight());
		PDRectangle elementShape = new PDRectangle(width, height);
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
