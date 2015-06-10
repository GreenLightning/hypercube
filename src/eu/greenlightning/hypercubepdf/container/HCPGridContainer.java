package eu.greenlightning.hypercubepdf.container;

import java.io.IOException;
import java.util.Objects;

import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;

import eu.greenlightning.hypercubepdf.HCPElement;
import eu.greenlightning.hypercubepdf.layout.*;

public class HCPGridContainer implements HCPElement {

	private final HCPLayout horizontalLayout;
	private final HCPLayout verticalLayout;
	private final int horizontalCount;
	private final int verticalCount;
	private final HCPElement[][] elements;

	public HCPGridContainer(HCPLayout horizontalLayout, HCPLayout verticalLayout, HCPElement[][] elements) {
		this.horizontalLayout = Objects.requireNonNull(horizontalLayout,
				"Horizontal layout must not be null.");
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
