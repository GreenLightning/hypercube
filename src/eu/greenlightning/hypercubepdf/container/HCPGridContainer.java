package eu.greenlightning.hypercubepdf.container;

import java.io.IOException;
import java.util.Objects;

import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;

import eu.greenlightning.hypercubepdf.HCPElement;
import eu.greenlightning.hypercubepdf.HCPEmpty;
import eu.greenlightning.hypercubepdf.layout.*;

/**
 * Paints a 2-dimensional grid of {@link HCPElement}s. Rows and columns are laid out independently and different
 * {@link HCPLayout}s can be used for rows and columns.The maximum width of all elements in a column is used as the
 * width of the column. The maximum height of all elements in a row is used as the height of the row.
 * <p>
 * The elements are painted in rows from left to right and rows are painted from top to bottom.
 * <p>
 * This class is immutable.
 * 
 * @author Green Lightning
 */
public class HCPGridContainer implements HCPElement {

	private final HCPLayout horizontalLayout;
	private final HCPLayout verticalLayout;
	private final int horizontalCount;
	private final int verticalCount;
	private final HCPElement[][] elements;

	/**
	 * Creates an {@link HCPGridContainer} with a single {@link HCPLayout} for rows and columns.
	 * <p>
	 * Elements is an array of row arrays. The first array corresponds to the top-most row and the last array to the
	 * bottom-most row. Each row array contains the elements of the row from left to right.
	 * <p>
	 * An empty elements array as well as an elements array containing only empty row arrays are allowed, however the
	 * resulting container behaves like an empty element (see {@link HCPEmpty}).
	 * 
	 * @param layout not {@code null}
	 * @param elements not {@code null}; all row arrays must not be {@code null}, must have the same length and must not
	 *            contain {@code null} elements themselves
	 * @throws NullPointerException if layout or elements is {@code null}
	 * @throws NullPointerException if elements contains a {@code null} row array or a row array contains a {@code null}
	 *             element
	 * @throws IllegalArgumentException if a row array has the wrong length
	 */
	public HCPGridContainer(HCPLayout layout, HCPElement[][] elements) {
		this(layout, layout, elements);
	}

	/**
	 * Creates an {@link HCPGridContainer} with separate horizontal and vertical layouts.
	 * <p>
	 * Elements is an array of row arrays. The first array corresponds to the top-most row and the last array to the
	 * bottom-most row. Each row array contains the elements of the row from left to right.
	 * <p>
	 * An empty elements array as well as an elements array containing only empty row arrays are allowed, however the
	 * resulting container behaves like an empty element (see {@link HCPEmpty}).
	 * 
	 * @param horizontalLayout used to lay out columns; not {@code null}
	 * @param verticalLayout used to lay out rows; not {@code null}
	 * @param elements not {@code null}; all row arrays must not be {@code null}, must have the same length and must not
	 *            contain {@code null} elements themselves
	 * @throws NullPointerException if horizontalLayout, verticalLayout or elements is {@code null}
	 * @throws NullPointerException if elements contains a {@code null} row array or a row array contains a {@code null}
	 *             element
	 * @throws IllegalArgumentException if a row array has the wrong length
	 */
	public HCPGridContainer(HCPLayout horizontalLayout, HCPLayout verticalLayout, HCPElement[][] elements) {
		this.horizontalLayout = Objects.requireNonNull(horizontalLayout, "Horizontal layout must not be null.");
		this.verticalLayout = Objects.requireNonNull(verticalLayout, "Vertical layout must not be null.");

		this.elements = Objects.requireNonNull(elements, "Elements must not be null.").clone();
		this.verticalCount = this.elements.length;
		copyRows();
		this.horizontalCount = (verticalCount == 0) ? 0 : this.elements[0].length;
		checkRows();
	}

	private void copyRows() {
		for (int v = 0; v < verticalCount; v++) {
			String message = "Elements must not contain null row (index = " + v + ").";
			elements[v] = Objects.requireNonNull(elements[v], message).clone();
		}
	}

	private void checkRows() {
		for (int v = 0; v < verticalCount; v++)
			checkRow(v);
	}

	private void checkRow(int v) {
		checkRowLength(v);
		checkRowForNull(v);
	}

	private void checkRowLength(int v) {
		if (elements[v].length != horizontalCount) {
			String information = "(expected = " + horizontalCount + "; actual = " + elements[v].length + ").";
			throw new IllegalArgumentException("Row at index " + v + " has wrong length " + information);
		}
	}

	private void checkRowForNull(int v) {
		for (int h = 0; h < horizontalCount; h++) {
			String message = "Elements must not contain null element (index = " + v + ";" + h + ").";
			Objects.requireNonNull(elements[v][h], message);
		}
	}

	@Override
	public float getWidth() throws IOException {
		return horizontalLayout.getSize(getWidths());
	}

	private float[] getWidths() throws IOException {
		float[] widths = new float[horizontalCount];
		for (int h = 0; h < horizontalCount; h++)
			widths[h] = getWidth(h);
		return widths;
	}

	private float getWidth(int h) throws IOException {
		float width = 0;
		for (int v = 0; v < verticalCount; v++)
			width = Math.max(width, elements[v][h].getWidth());
		return width;
	}

	@Override
	public float getHeight() throws IOException {
		return verticalLayout.getSize(getHeights());
	}

	private float[] getHeights() throws IOException {
		float[] heights = new float[verticalCount];
		for (int v = 0; v < verticalCount; v++)
			heights[v] = getHeight(v);
		return heights;
	}

	private float getHeight(int v) throws IOException {
		float height = 0;
		for (int h = 0; h < horizontalCount; h++)
			height = Math.max(height, elements[v][h].getHeight());
		return height;
	}

	@Override
	public void paint(PDPageContentStream content, PDRectangle shape) throws IOException {
		PDRectangle elementShape = new PDRectangle();

		HCPLayoutSpace verticalSpace = new HCPLayoutSpace(shape.getUpperRightY(), shape.getLowerLeftY());
		HCPLayoutResults verticalResults = verticalLayout.apply(verticalSpace, getHeights());

		HCPLayoutSpace horizontalSpace = new HCPLayoutSpace(shape.getLowerLeftX(), shape.getUpperRightX());
		HCPLayoutResults horizontalResults = horizontalLayout.apply(horizontalSpace, getWidths());

		while (verticalResults.hasNext()) {
			verticalResults.next();
			elementShape.setLowerLeftY(verticalResults.getLow());
			elementShape.setUpperRightY(verticalResults.getHigh());
			HCPElement[] cells = elements[verticalResults.getIndex()];

			horizontalResults.reset();
			while (horizontalResults.hasNext()) {
				horizontalResults.next();
				elementShape.setLowerLeftX(horizontalResults.getLow());
				elementShape.setUpperRightX(horizontalResults.getHigh());
				cells[horizontalResults.getIndex()].paint(content, elementShape);
			}
		}
	}

}
