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

	public static Builder create(HCPLayout layout) {
		return new Builder(layout);
	}

	public static Builder create(HCPLayout horizontalLayout, HCPLayout verticalLayout) {
		return new Builder(horizontalLayout, verticalLayout);
	}

	public static final class Builder {

		private HCPLayout horizontalLayout, verticalLayout;
		private List<HCPTablePosition> positions;

		private Builder(HCPLayout layout) {
			this(layout, layout);
		}

		private Builder(HCPLayout horizontalLayout, HCPLayout verticalLayout) {
			this.horizontalLayout = Objects.requireNonNull(horizontalLayout, "Horizontal layout must not be null.");
			this.verticalLayout = Objects.requireNonNull(verticalLayout, "Vertical layout must not be null.");
			this.positions = new ArrayList<>();
		}

		public Builder addPosition(HCPElement element, int x, int y) {
			return addPosition(new HCPTablePosition(element, x, y));
		}

		public Builder addPosition(HCPElement element, int x, int y, int horizontalSpan, int verticalSpan) {
			return addPosition(new HCPTablePosition(element, x, y, horizontalSpan, verticalSpan));
		}

		public Builder addPosition(HCPTablePosition position) {
			positions.add(Objects.requireNonNull(position, "Position must not be null."));
			return this;
		}

		public Builder addElements(HCPElement[][] elements, final int x, final int y) {
			if (elements != null) {
				for (int yIndex = 0; yIndex < elements.length; yIndex++) {
					HCPElement[] row = elements[yIndex];
					if (row != null) {
						for (int xIndex = 0; xIndex < row.length; xIndex++) {
							HCPElement element = row[xIndex];
							if (element != null)
								addPosition(element, x + xIndex, y + yIndex);
						}
					}
				}
			}
			return this;
		}

		public HCPTableContainer build() {
			return new HCPTableContainer(horizontalLayout, verticalLayout, positions);
		}

	}

	private static final HCPTablePosition[] EMPTY_POSITION_ARRAY = new HCPTablePosition[0];

	private final HCPLayout horizontalLayout;
	private final HCPLayout verticalLayout;
	private final HCPTablePosition[] positions;
	private final int horizontalCount;
	private final int verticalCount;

	private HCPTableContainer(HCPLayout horizontalLayout, HCPLayout verticalLayout, List<HCPTablePosition> positions) {
		this.horizontalLayout = horizontalLayout;
		this.verticalLayout = verticalLayout;
		this.positions = positions.toArray(EMPTY_POSITION_ARRAY);
		this.horizontalCount = calculateCount(HCPTablePosition::getRightIndex);
		this.verticalCount = calculateCount(HCPTablePosition::getLowerIndex);
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
		List<PositionPainter> painters = new ArrayList<>(positions.length);
		for (HCPTablePosition position : positions) {
			painters.add(new PositionPainter(position, horizontalCount, verticalCount));
		}

		HCPLayoutSpace horizontalSpace = new HCPLayoutSpace(shape.getLowerLeftX(), shape.getUpperRightX());
		HCPLayoutResults horizontalResults = horizontalLayout.apply(horizontalSpace, getWidths());
		setCoordinates(painters, horizontalResults, PositionPainter::setLeftX, PositionPainter::setRightX);

		HCPLayoutSpace verticalSpace = new HCPLayoutSpace(shape.getUpperRightY(), shape.getLowerLeftY());
		HCPLayoutResults verticalResults = verticalLayout.apply(verticalSpace, getHeights());
		setCoordinates(painters, verticalResults, PositionPainter::setLowerY, PositionPainter::setUpperY);

		for (PositionPainter painter : painters) {
			painter.paint(content);
		}
	}

	private void setCoordinates(List<PositionPainter> painters, HCPLayoutResults results, CoordinateSetter low,
		CoordinateSetter high) {
		while (results.hasNext()) {
			results.next();
			for (PositionPainter painter : painters) {
				low.setCoordinate(painter, results.getIndex(), results.getLow());
				high.setCoordinate(painter, results.getIndex(), results.getHigh());
			}
		}
	}

	private Stream<HCPTablePosition> positions() {
		return Arrays.stream(positions);
	}

	@FunctionalInterface
	private static interface CoordinateSetter {
			void setCoordinate(PositionPainter painter, int index, float coordinate);
	}

	private static class PositionPainter {

		private final HCPTablePosition position;
		private final int rightIndex, lowerIndex;
		private final PDRectangle shape;

		public PositionPainter(HCPTablePosition position, int horizontalCount, int verticalCount) {
			this.position = position;
			this.rightIndex = position.horizontallyRemaining() ? horizontalCount - 1 : position.getRightIndex();
			this.lowerIndex = position.verticallyRemaining() ? verticalCount - 1 : position.getLowerIndex();
			this.shape = new PDRectangle();
		}

		public void setLeftX(int index, float x) {
			if (position.getX() == index) {
				shape.setLowerLeftX(x);
			}
		}

		public void setRightX(int index, float x) {
			if (rightIndex == index) {
				shape.setUpperRightX(x);
			}
		}

		public void setUpperY(int index, float y) {
			if (position.getY() == index) {
				shape.setUpperRightY(y);
			}
		}

		public void setLowerY(int index, float y) {
			if (lowerIndex == index) {
				shape.setLowerLeftY(y);
			}
		}

		public void paint(PDPageContentStream content) throws IOException {
			position.getElement().paint(content, shape);
		}

	}

}
