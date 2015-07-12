package eu.greenlightning.hypercubepdf.container;

import java.io.IOException;
import java.util.*;
import java.util.function.ToIntFunction;
import java.util.stream.Stream;

import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;

import eu.greenlightning.hypercubepdf.HCPElement;
import eu.greenlightning.hypercubepdf.layout.*;

/**
 * Paints a table of {@link HCPElement}s. Allows for empty cells and elements spanning over multiple cells. Elements are
 * added to the container wrapped in {@link HCPTablePosition}s which describe the cell(s) that the elements occupy.
 * <p>
 * Rows and columns are laid out independently and different {@link HCPLayout}s can be used for rows and columns. The
 * maximum width of all elements which occupy only a given column is used as the base width of that column. The maximum
 * height of all elements which occupy only a given row is used as the height of that row. If an element spans over
 * multiple columns or rows and the sum of all base sizes of these columns or rows is smaller than the element, the
 * remaining size is split up and added to the base sizes of these columns or rows. How the remaining size is split up
 * is determined by an {@link HCPSpanDistributionPolicy}. Different policies can be configured for columns and rows.
 * <p>
 * The elements are painted in the order in that they were added to the container.
 * <p>
 * This class is immutable.
 * 
 * @author Green Lightning
 */
public class HCPTableContainer implements HCPElement {

	/**
	 * Specifies how the widths or heights of a series of columns or rows should be adjusted if an element spanning over
	 * that series needs more space than available.
	 *
	 * @author Green Lightning
	 */
	public static enum HCPSpanDistributionPolicy {
		/** The additional space is distributed equally to all columns or rows. */
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
		/**
		 * The additional space is distributed proportionally to all columns or rows, i.&nbsp;e. the largest column or
		 * row receives the largest amount of extra space.
		 */
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

		/**
		 * Ensures that the sum of the sizes in the index range [startIndex, endIndex] (both inclusive) is at least
		 * targetSize. If not, the additionally required space is distributed using the
		 * {@link #distribute(float[], float, int, int)} method (this modifies the sizes array).
		 * 
		 * @param sizes the sizes of the individual columns or rows; not {@code null}
		 * @param targetSize the required size
		 * @param startIndex inclusive
		 * @param endIndex inclusive
		 */
		protected void adjustSizes(float[] sizes, float targetSize, int startIndex, int endIndex) {
			float extra = targetSize - totalSize(sizes, startIndex, endIndex);
			if (extra > 0) {
				distribute(sizes, extra, startIndex, endIndex);
			}
		}

		/**
		 * Distributes the extra size over the size values in the index range [startIndex, endIndex] (both inclusive).
		 * 
		 * @param sizes the sizes of the individual columns or rows; not {@code null}
		 * @param extra the additional size
		 * @param startIndex inclusive
		 * @param endIndex inclusive
		 */
		protected abstract void distribute(float[] sizes, float extra, int startIndex, int endIndex);

		private static float totalSize(float[] sizes, int startIndex, int endIndex) {
			float size = 0;
			for (int i = startIndex; i <= endIndex; i++) {
				size += sizes[i];
			}
			return size;
		}

	}

	/**
	 * Creates a new builder using the specified layout for the horizontal and the vertical direction.
	 * 
	 * @param layout not {@code null}
	 * @return a new builder for an {@link HCPTableContainer}
	 * @throws NullPointerException if layout is {@code null}
	 */
	public static Builder create(HCPLayout layout) {
		return new Builder(layout, layout);
	}

	/**
	 * Creates a new builder using the specified horizontal and vertical layouts.
	 * 
	 * @param horizontalLayout not {@code null}
	 * @param verticalLayout not {@code null}
	 * @return a new builder for an {@link HCPTableContainer}
	 * @throws NullPointerException if horizontalLayout or verticalLayout is {@code null}
	 */
	public static Builder create(HCPLayout horizontalLayout, HCPLayout verticalLayout) {
		return new Builder(horizontalLayout, verticalLayout);
	}

	/**
	 * Mutable class used to construct {@link HCPTableContainer} instances.
	 *
	 * @author Green Lightning
	 */
	public static final class Builder {

		/**
		 * By default {@link HCPSpanDistributionPolicy#PROPORTIONAL} is used if a policy has not been explicitly set
		 * using one of the methods of the builder.
		 */
		public static final HCPSpanDistributionPolicy DEFAULT_POLICY = HCPSpanDistributionPolicy.PROPORTIONAL;

		private HCPLayout horizontalLayout, verticalLayout;
		private HCPSpanDistributionPolicy horizontalPolicy = DEFAULT_POLICY, verticalPolicy = DEFAULT_POLICY;
		private List<HCPTablePosition> positions = new ArrayList<>();

		private Builder(HCPLayout horizontalLayout, HCPLayout verticalLayout) {
			horizontalLayout(horizontalLayout);
			verticalLayout(verticalLayout);
		}

		/**
		 * Uses the specified layout for both the horizontal and vertical direction. Previously set layouts are
		 * overwritten.
		 * 
		 * @param layout not {@code null}
		 * @return this builder for chaining
		 * @throws NullPointerException if layout is {@code null}
		 */
		public Builder layout(HCPLayout layout) {
			Objects.requireNonNull(layout, "Layout must not be null.");
			horizontalLayout(layout);
			verticalLayout(layout);
			return this;
		}

		/**
		 * Uses the specified layout for the horizontal direction. Any previously set horizontal layouts are
		 * overwritten.
		 * 
		 * @param horizontalLayout not {@code null}
		 * @return this builder for chaining
		 * @throws NullPointerException if horizontalLayout is {@code null}
		 */
		public Builder horizontalLayout(HCPLayout horizontalLayout) {
			this.horizontalLayout = Objects.requireNonNull(horizontalLayout, "Horizontal layout must not be null.");
			return this;
		}

		/**
		 * Uses the specified layout for the vertical direction. Any previously set vertical layouts are overwritten.
		 * 
		 * @param verticalLayout not {@code null}
		 * @return this builder for chaining
		 * @throws NullPointerException if verticalLayout is {@code null}
		 */
		public Builder verticalLayout(HCPLayout verticalLayout) {
			this.verticalLayout = Objects.requireNonNull(verticalLayout, "Vertical layout must not be null.");
			return this;
		}

		/**
		 * Uses the specified {@link HCPSpanDistributionPolicy} for both the horizontal and vertical direction. Any
		 * previously set layouts are overwritten.
		 * 
		 * @param policy not {@code null}
		 * @return this builder for chaining
		 * @throws NullPointerException if policy is {@code null}
		 * @see #DEFAULT_POLICY
		 */
		public Builder distributionPolicy(HCPSpanDistributionPolicy policy) {
			Objects.requireNonNull(policy, "Policy must not be null.");
			horizontalDistributionPolicy(policy);
			verticalDistributionPolicy(policy);
			return this;
		}

		/**
		 * Uses the specified span distribution policy for the horizontal direction. Any previously set horizontal span
		 * distribution policies are overwritten.
		 * 
		 * @param horizontalPolicy not {@code null}
		 * @return this builder for chaining
		 * @throws NullPointerException if horizontalPolicy is {@code null}
		 * @see #DEFAULT_POLICY
		 */
		public Builder horizontalDistributionPolicy(HCPSpanDistributionPolicy horizontalPolicy) {
			this.horizontalPolicy = Objects.requireNonNull(horizontalPolicy, "Horizontal policy must not be null.");
			return this;
		}

		/**
		 * Uses the specified span distribution policy for the vertical direction. Any previously set vertical span
		 * distribution policies are overwritten.
		 * 
		 * @param verticalPolicy not {@code null}
		 * @return this builder for chaining
		 * @throws NullPointerException if verticalPolicy is {@code null}
		 * @see #DEFAULT_POLICY
		 */
		public Builder verticalDistributionPolicy(HCPSpanDistributionPolicy verticalPolicy) {
			this.verticalPolicy = Objects.requireNonNull(verticalPolicy, "Vertical policy must not be null.");
			return this;
		}

		/**
		 * Adds an element at the specified position to the table container. Elements added more than once will also be
		 * painted multiple times, even if added to the same position.
		 * 
		 * @param element not {@code null}
		 * @param x must be {@literal >= 0}
		 * @param y must be {@literal >= 0}
		 * @return this builder for chaining
		 * @throws NullPointerException if element is {@code null}
		 * @throws IllegalArgumentException if x or y is {@literal < 0}
		 */
		public Builder addPosition(HCPElement element, int x, int y) {
			return addPosition(new HCPTablePosition(element, x, y));
		}

		/**
		 * Adds an element at the specified position and with the specified spans to the table container. Elements added
		 * more than once will also be painted multiple times, even if added to the same position.
		 * 
		 * @param element not {@code null}
		 * @param x must be {@literal >= 0}
		 * @param y must be {@literal >= 0}
		 * @param horizontalSpan must be {@literal >= 1}
		 * @param verticalSpan must be {@literal >= 1}
		 * @return this builder for chaining
		 * @throws NullPointerException if element is {@code null}
		 * @throws IllegalArgumentException if x or y is {@literal < 0}
		 * @throws IllegalArgumentException if horizontalSpan or verticalSpan is {@literal < 1}
		 */
		public Builder addPosition(HCPElement element, int x, int y, int horizontalSpan, int verticalSpan) {
			return addPosition(new HCPTablePosition(element, x, y, horizontalSpan, verticalSpan));
		}

		/**
		 * Adds the specified position to the table container.
		 * 
		 * @param position not {@code null}
		 * @return this builder for chaining
		 * @throws NullPointerException if position is {@code null}
		 */
		public Builder addPosition(HCPTablePosition position) {
			positions.add(Objects.requireNonNull(position, "Position must not be null."));
			return this;
		}

		/**
		 * Adds an array of elements to the table container. The array is treated as a grid, where the first index
		 * specifies the y-coordinate and the second index the x-coordinate, i.&nbsp;e. the array is an array of row
		 * arrays. This grid is added at the specified position, i.&nbsp;e. the element at [0][0] is added at position
		 * (x, y) and the element at [2][5] is added at position (x + 5, y + 2).
		 * <p>
		 * <b>Note:</b> This method permits {@code null}s. If the whole array is {@code null}, this method does nothing.
		 * If a row array is {@code null}, it acts like an empty row and if an element is {@code null} nothing is added
		 * at this position.
		 * <p>
		 * Elements added more than once will also be painted multiple times.
		 * 
		 * @param elements the elements to add; may be {@code null} and may contain {@code null}
		 * @param x must be {@literal >= 0}
		 * @param y must be {@literal >= 0}
		 * @return this builder for chaining
		 * @throws IllegalArgumentException if x or y is {@literal < 0}
		 */
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

		/**
		 * Creates the {@link HCPTableContainer}.
		 * 
		 * @return a new {@link HCPTableContainer} containing the elements added using this builder
		 */
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
		this.horizontalCount = calculateCount(HCPTablePosition::getRightX);
		this.verticalCount = calculateCount(HCPTablePosition::getLowerY);
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
				horizontalPolicy.adjustSizes(widths, position.getWidth(), position.getX(), position.getRightX());
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
				verticalPolicy.adjustSizes(heights, position.getHeight(), position.getY(), position.getLowerY());
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
			this.rightIndex = position.horizontallyRemaining() ? horizontalCount - 1 : position.getRightX();
			this.lowerIndex = position.verticallyRemaining() ? verticalCount - 1 : position.getLowerY();
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
