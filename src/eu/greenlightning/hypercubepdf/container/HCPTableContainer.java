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

	public static enum HCPSpanDistributionPolicy {
		EQUAL {
			@Override
			protected void distribute(float[] sizes, float extra, int startIndex, int endIndex) {
				int count = endIndex - startIndex + 1;
				float amount = extra / count;
				for (int i = startIndex; i <= endIndex; i++) {
					sizes[i] += amount;
				}
			}
		},
		PROPORTIONAL {
			@Override
			protected void distribute(float[] sizes, float extra, int startIndex, int endIndex) {
				float total = totalSize(sizes, startIndex, endIndex);
				if (total == 0) {
					EQUAL.distribute(sizes, extra, startIndex, endIndex);
					return;
				}
				for (int i = startIndex; i <= endIndex; i++) {
					sizes[i] += extra * sizes[i] / total;
				}
			}
		};

		protected void adjustSizes(float[] sizes, float targetSize, int startIndex, int endIndex) {
			float extra = targetSize - totalSize(sizes, startIndex, endIndex);
			if (extra > 0) {
				distribute(sizes, extra, startIndex, endIndex);
			}
		}

		protected abstract void distribute(float[] sizes, float extra, int startIndex, int endIndex);

		protected final float totalSize(float[] sizes, int startIndex, int endIndex) {
			float size = 0;
			for (int i = startIndex; i <= endIndex; i++) {
				size += sizes[i];
			}
			return size;
		}

	}

	public static Builder create(HCPLayout layout) {
		return new Builder(layout);
	}

	public static Builder create(HCPLayout horizontalLayout, HCPLayout verticalLayout) {
		return new Builder(horizontalLayout, verticalLayout);
	}

	public static final class Builder {

		private HCPLayout horizontalLayout, verticalLayout;
		private HCPSpanDistributionPolicy horizontalPolicy, verticalPolicy;
		private List<HCPTablePosition> positions;

		private Builder(HCPLayout layout) {
			layout(layout);
		}

		private Builder(HCPLayout horizontalLayout, HCPLayout verticalLayout) {
			horizontalLayout(horizontalLayout);
			verticalLayout(verticalLayout);
		}

		{
			this.horizontalPolicy = HCPSpanDistributionPolicy.PROPORTIONAL;
			this.verticalPolicy = HCPSpanDistributionPolicy.PROPORTIONAL;
			this.positions = new ArrayList<>();
		}

		public Builder layout(HCPLayout layout) {
			Objects.requireNonNull(layout, "Layout must not be null.");
			horizontalLayout(layout);
			verticalLayout(layout);
			return this;
		}

		public Builder horizontalLayout(HCPLayout horizontalLayout) {
			this.horizontalLayout = Objects.requireNonNull(horizontalLayout, "Horizontal layout must not be null.");
			return this;
		}

		public Builder verticalLayout(HCPLayout verticalLayout) {
			this.verticalLayout = Objects.requireNonNull(verticalLayout, "Vertical layout must not be null.");
			return this;
		}

		public Builder distributionPolicy(HCPSpanDistributionPolicy policy) {
			Objects.requireNonNull(policy, "Policy must not be null.");
			horizontalDistributionPolicy(policy);
			verticalDistributionPolicy(policy);
			return this;
		}

		public Builder horizontalDistributionPolicy(HCPSpanDistributionPolicy horizontalPolicy) {
			this.horizontalPolicy = Objects.requireNonNull(horizontalPolicy, "Horizontal policy must not be null.");
			return this;
		}

		public Builder verticalDistributionPolicy(HCPSpanDistributionPolicy verticalPolicy) {
			this.verticalPolicy = Objects.requireNonNull(verticalPolicy, "Vertical policy must not be null.");
			return this;
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
			return new HCPTableContainer(horizontalLayout, verticalLayout, horizontalPolicy, verticalPolicy, positions);
		}

	}

	private static final HCPTablePosition[] EMPTY_POSITION_ARRAY = new HCPTablePosition[0];

	private final HCPLayout horizontalLayout, verticalLayout;
	private final HCPSpanDistributionPolicy horizontalPolicy, verticalPolicy;
	private final HCPTablePosition[] positions;
	private final int horizontalCount, verticalCount;

	private HCPTableContainer(HCPLayout horizontalLayout, HCPLayout verticalLayout,
		HCPSpanDistributionPolicy horizontalPolicy, HCPSpanDistributionPolicy verticalPolicy,
		List<HCPTablePosition> positions) {
		this.horizontalLayout = horizontalLayout;
		this.verticalLayout = verticalLayout;
		this.horizontalPolicy = horizontalPolicy;
		this.verticalPolicy = verticalPolicy;
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
		for (HCPTablePosition position : positions) {
			if (position.spansHorizontally()) {
				horizontalPolicy.adjustSizes(widths, position.getWidth(), position.getX(), position.getRightIndex());
			}
		}
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
		for (HCPTablePosition position : positions) {
			if (position.spansVertically()) {
				verticalPolicy.adjustSizes(heights, position.getHeight(), position.getY(), position.getLowerIndex());
			}
		}
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
