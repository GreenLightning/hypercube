package eu.greenlightning.hypercubepdf.container;

import java.io.IOException;
import java.util.*;
import java.util.function.ToIntFunction;
import java.util.stream.Stream;

import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;

import eu.greenlightning.hypercubepdf.HCPElement;
import eu.greenlightning.hypercubepdf.layout.*;

public class HCPTableContainer implements HCPElement {

	private final HCPLayout horizontalLayout;
	private final HCPLayout verticalLayout;
	private final HCPTablePosition[] positions;
	private final int horizontalCount;
	private final int verticalCount;

	public HCPTableContainer(HCPLayout layout, HCPTablePosition[] positions) {
		this(layout, layout, positions);
	}

	public HCPTableContainer(HCPLayout horizontalLayout, HCPLayout verticalLayout, HCPTablePosition[] positions) {
		this.horizontalLayout = Objects.requireNonNull(horizontalLayout, "Horizontal layout must not be null.");
		this.verticalLayout = Objects.requireNonNull(verticalLayout, "Vertical layout must not be null.");
		this.positions = Objects.requireNonNull(positions, "Positions must not be null.").clone();
		checkPositions();
		this.horizontalCount = calculateCount(HCPTablePosition::getX);
		this.verticalCount = calculateCount(HCPTablePosition::getY);
	}

	private void checkPositions() {
		for (int i = 0; i < positions.length; i++)
			Objects.requireNonNull(positions[i], String.format("Positions must not contain null (index = %d).", i));
	}

	private int calculateCount(ToIntFunction<? super HCPTablePosition> mapper) {
		return positions().mapToInt(mapper).max().orElse(-1) + 1;
	}

	@Override
	public float getWidth() throws IOException {
		return horizontalLayout.getSize(getWidths());
	}

	private float[] getWidths() throws IOException {
		float[] widths = new float[horizontalCount];
		for (HCPTablePosition position : positions) {
			if (!position.spansHorizontally()) {
				int index = position.getX();
				float width = position.getWidth();
				if (width > widths[index])
					widths[index] = width;
			}
		}
		// TODO: adjust for spans
		return widths;
	}

	@Override
	public float getHeight() throws IOException {
		return verticalLayout.getSize(getHeights());
	}

	private float[] getHeights() throws IOException {
		float[] heights = new float[verticalCount];
		for (HCPTablePosition position : positions) {
			if (!position.spansVertically()) {
				int index = position.getY();
				float height = position.getHeight();
				if (height > heights[index])
					heights[index] = height;
			}
		}
		// TODO: adjust for spans
		return heights;
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
			horizontalResults.reset();
			while (horizontalResults.hasNext()) {
				horizontalResults.next();
				elementShape.setLowerLeftX(horizontalResults.getLow());
				elementShape.setUpperRightX(horizontalResults.getHigh());
				int x = horizontalResults.getIndex();
				int y = verticalResults.getIndex();
				Optional<HCPElement> element = findSimpleElementAt(x, y);
				if (element.isPresent()) {
					element.get().paint(content, elementShape);
				}
			}
		}

		List<Span> spans = new ArrayList<>();
		for (HCPTablePosition position : positions) {
			if (position.spans()) {
				spans.add(new Span(position));
			}
		}

		horizontalResults.reset();
		while (horizontalResults.hasNext()) {
			horizontalResults.next();
			for (Span span : spans) {
				span.setLeftX(horizontalResults.getIndex(), horizontalResults.getLow());
				span.setRightX(horizontalResults.getIndex(), horizontalResults.getHigh());
			}
		}

		verticalResults.reset();
		while (verticalResults.hasNext()) {
			verticalResults.next();
			for (Span span : spans) {
				span.setLowerY(verticalResults.getIndex(), verticalResults.getLow());
				span.setUpperY(verticalResults.getIndex(), verticalResults.getHigh());
			}
		}

		for (Span span : spans) {
			span.paint(content);
		}
	}

	private Optional<HCPElement> findSimpleElementAt(int x, int y) {
		return positions().filter(p -> p.isAt(x, y) && !p.spans()).findAny().map(HCPTablePosition::getElement);
	}

	private Stream<HCPTablePosition> positions() {
		return Arrays.stream(positions);
	}

	private static class Span {

		private final HCPTablePosition position;
		private final PDRectangle shape;

		public Span(HCPTablePosition position) {
			this.position = position;
			this.shape = new PDRectangle();
		}

		public void setLeftX(int index, float x) {
			if (position.getX() == index) {
				shape.setLowerLeftX(x);
			}
		}

		public void setRightX(int index, float x) {
			if (position.getX() + position.getHorizontalSpan() - 1 == index) {
				shape.setUpperRightX(x);
			}
		}

		public void setUpperY(int index, float y) {
			if (position.getY() == index) {
				shape.setUpperRightY(y);
			}
		}

		public void setLowerY(int index, float y) {
			if (position.getY() + position.getVerticalSpan() - 1 == index) {
				shape.setLowerLeftY(y);
			}
		}

		public void paint(PDPageContentStream content) throws IOException {
			position.getElement().paint(content, shape);
		}

	}

}
