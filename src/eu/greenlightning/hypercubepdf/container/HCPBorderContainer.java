package eu.greenlightning.hypercubepdf.container;

import java.io.IOException;
import java.util.Optional;

import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;

import eu.greenlightning.hypercubepdf.HCPElement;

/**
 * Paints up to five {@link HCPElement}s, one for the center and four for each side. Each element is optional and must
 * not be provided.
 * <p>
 * When all elements are painted, the left, center and right element form a row in the middle, while the top element is
 * painted above and the bottom element is painted below all three of them. If the opposite arrangement is wanted (the
 * top, center and bottom element form a column in the center, while the left element is painted to the left and the
 * right element is painted to the right of all three of them), then two {@link HCPBorderContainer}s must be nested. The
 * inner container should contain the top, center and bottom elements and the outer container contains the left and
 * right elements and the inner container in the center position.
 * <p>
 * If provided, the top and bottom elements will be asked for their preferred height and the left and right elements for
 * their preferred width. The center element will take all the remaining space in the center. The elements will then be
 * painted in the following order as long as there is still space available to paint them: top, bottom, left, right,
 * center.
 * <p>
 * An individual spacing value can be set for each of the four side elements, which introduce spacing between the
 * element and the center element.
 * <p>
 * This class is immutable.
 * 
 * @author Green Lightning
 */
public class HCPBorderContainer implements HCPElement {

	/**
	 * Creates a new builder.
	 * 
	 * @return a new builder for a {@link HCPBorderContainer}
	 */
	public static Builder create() {
		return new Builder();
	}

	/**
	 * Mutable class used to construct {@link HCPBorderContainer} instances.
	 *
	 * @author Green Lightning
	 */
	public static final class Builder {

		private HCPElement top, bottom, left, right, center;
		private float topSpacing, bottomSpacing, leftSpacing, rightSpacing;
		
		// Hide the constructor so that HCPBorderContainer.create() must be used
		private Builder() {}

		/**
		 * Sets the top element, overwriting previous calls to this method. If {@code null}, removes the top element in
		 * case it was set.
		 * 
		 * @param top may be {@code null}
		 * @return this builder for chaining
		 */
		public Builder top(HCPElement top) {
			this.top = top;
			return this;
		}

		/**
		 * Sets the bottom element, overwriting previous calls to this method. If {@code null}, removes the bottom
		 * element in case it was set.
		 * 
		 * @param bottom may be {@code null}
		 * @return this builder for chaining
		 */
		public Builder bottom(HCPElement bottom) {
			this.bottom = bottom;
			return this;
		}

		/**
		 * Sets the left element, overwriting previous calls to this method. If {@code null}, removes the left element
		 * in case it was set.
		 * 
		 * @param left may be {@code null}
		 * @return this builder for chaining
		 */
		public Builder left(HCPElement left) {
			this.left = left;
			return this;
		}

		/**
		 * Sets the right element, overwriting previous calls to this method. If {@code null}, removes the right element
		 * in case it was set.
		 * 
		 * @param right may be {@code null}
		 * @return this builder for chaining
		 */
		public Builder right(HCPElement right) {
			this.right = right;
			return this;
		}

		/**
		 * Sets the center element, overwriting previous calls to this method. If {@code null}, removes the center
		 * element in case it was set.
		 * 
		 * @param center may be {@code null}
		 * @return this builder for chaining
		 */
		public Builder center(HCPElement center) {
			this.center = center;
			return this;
		}

		/**
		 * Sets the top spacing, replacing any previously set value. If less than zero the {@linkplain #build()} method
		 * will throw an {@linkplain IllegalArgumentException}.
		 * 
		 * @param topSpacing must be {@literal >= 0}
		 * @return this builder for chaining
		 */
		public Builder topSpacing(float topSpacing) {
			this.topSpacing = topSpacing;
			return this;
		}

		/**
		 * Sets the bottom spacing, replacing any previously set value. If less than zero the {@linkplain #build()}
		 * method will throw an {@linkplain IllegalArgumentException}.
		 * 
		 * @param bottomSpacing must be {@literal >= 0}
		 * @return this builder for chaining
		 */
		public Builder bottomSpacing(float bottomSpacing) {
			this.bottomSpacing = bottomSpacing;
			return this;
		}

		/**
		 * Sets the left spacing, replacing any previously set value. If less than zero the {@linkplain #build()} method
		 * will throw an {@linkplain IllegalArgumentException}.
		 * 
		 * @param leftSpacing must be {@literal >= 0}
		 * @return this builder for chaining
		 */
		public Builder leftSpacing(float leftSpacing) {
			this.leftSpacing = leftSpacing;
			return this;
		}

		/**
		 * Sets the right spacing, replacing any previously set value. If less than zero the {@linkplain #build()}
		 * method will throw an {@linkplain IllegalArgumentException}.
		 * 
		 * @param rightSpacing must be {@literal >= 0}
		 * @return this builder for chaining
		 */
		public Builder rightSpacing(float rightSpacing) {
			this.rightSpacing = rightSpacing;
			return this;
		}

		/**
		 * Sets the top and bottom spacing, replacing any previously set value. If less than zero the
		 * {@linkplain #build()} method will throw an {@linkplain IllegalArgumentException}.
		 * 
		 * @param spacing must be {@literal >= 0}
		 * @return this builder for chaining
		 */
		public Builder horizontalSpacing(float spacing) {
			this.topSpacing = spacing;
			this.bottomSpacing = spacing;
			return this;
		}

		/**
		 * Sets the left and right spacing, replacing any previously set value. If less than zero the
		 * {@linkplain #build()} method will throw an {@linkplain IllegalArgumentException}.
		 * 
		 * @param spacing must be {@literal >= 0}
		 * @return this builder for chaining
		 */
		public Builder verticalSpacing(float spacing) {
			this.leftSpacing = spacing;
			this.rightSpacing = spacing;
			return this;
		}

		/**
		 * Sets the top, bottom, left and right spacing, replacing any previously set value. If less than zero the
		 * {@linkplain #build()} method will throw an {@linkplain IllegalArgumentException}.
		 * 
		 * @param spacing must be {@literal >= 0}
		 * @return this builder for chaining
		 */
		public Builder allSpacings(float spacing) {
			this.topSpacing = spacing;
			this.bottomSpacing = spacing;
			this.leftSpacing = spacing;
			this.rightSpacing = spacing;
			return this;
		}

		/**
		 * Creates the {@link HCPBorderContainer}.
		 * 
		 * @return a new {@link HCPBorderContainer} for the elements and spacing values set on this builder
		 * @throws IllegalArgumentException if any spacing value is {@literal < 0}
		 */
		public HCPBorderContainer build() {
			return new HCPBorderContainer(top, bottom, left, right, center, topSpacing, bottomSpacing, leftSpacing,
				rightSpacing);
		}

	}

	private final Optional<HCPElement> top, bottom, left, right, center;
	private final float topSpacing, bottomSpacing, leftSpacing, rightSpacing;

	// Used only for drawing.
	private final PDRectangle elementShape = new PDRectangle();
	private float leftPos, rightPos, topPos, bottomPos;

	private HCPBorderContainer(HCPElement top, HCPElement bottom, HCPElement left, HCPElement right, HCPElement center,
		float topSpacing, float bottomSpacing, float leftSpacing, float rightSpacing) {
		this.top = Optional.ofNullable(top);
		this.bottom = Optional.ofNullable(bottom);
		this.left = Optional.ofNullable(left);
		this.right = Optional.ofNullable(right);
		this.center = Optional.ofNullable(center);
		this.topSpacing = checkSpacing(topSpacing, "Top");
		this.bottomSpacing = checkSpacing(bottomSpacing, "Bottom");
		this.leftSpacing = checkSpacing(leftSpacing, "Left");
		this.rightSpacing = checkSpacing(rightSpacing, "Right");
	}

	private float checkSpacing(float spacing, String name) {
		if (spacing < 0)
			throw new IllegalArgumentException(name + " spacing must be equal to or greater than zero," + " but was "
				+ spacing + ".");
		return spacing;
	}

	@Override
	public float getWidth() throws IOException {
		float width = getWidth(left) + leftSpacing + getWidth(center) + rightSpacing + getWidth(right);
		width = Math.max(width, getWidth(top));
		width = Math.max(width, getWidth(bottom));
		return width;
	}

	private float getWidth(Optional<HCPElement> element) throws IOException {
		return element.isPresent() ? element.get().getWidth() : 0;
	}

	@Override
	public float getHeight() throws IOException {
		float height = 0;
		height = Math.max(height, getHeight(left));
		height = Math.max(height, getHeight(center));
		height = Math.max(height, getHeight(right));
		return getHeight(top) + topSpacing + height + bottomSpacing + getHeight(bottom);
	}

	private float getHeight(Optional<HCPElement> element) throws IOException {
		return element.isPresent() ? element.get().getHeight() : 0;
	}

	@Override
	public void paint(PDPageContentStream content, PDRectangle shape) throws IOException {
		leftPos = shape.getLowerLeftX();
		rightPos = shape.getUpperRightX();
		bottomPos = shape.getLowerLeftY();
		topPos = shape.getUpperRightY();

		paintTopBottom(content);
		paintLeftRightCenter(content);
	}

	private void paintTopBottom(PDPageContentStream content) throws IOException {
		if (leftPos < rightPos) {
			elementShape.setLowerLeftX(leftPos);
			elementShape.setUpperRightX(rightPos);
			paintTop(content);
			paintBottom(content);
		}
	}

	private void paintTop(PDPageContentStream content) throws IOException {
		if (top.isPresent() && bottomPos < topPos) {
			elementShape.setUpperRightY(topPos);
			topPos = Math.max(topPos - top.get().getHeight(), bottomPos);
			if (topPos - topSpacing <= bottomPos)
				topPos = bottomPos;
			elementShape.setLowerLeftY(topPos);
			if (topPos > bottomPos)
				topPos -= topSpacing;
			top.get().paint(content, elementShape);
		}
	}

	private void paintBottom(PDPageContentStream content) throws IOException {
		if (bottom.isPresent() && bottomPos < topPos) {
			elementShape.setLowerLeftY(bottomPos);
			bottomPos = Math.min(bottomPos + bottom.get().getHeight(), topPos);
			if (bottomPos + bottomSpacing >= topPos)
				bottomPos = topPos;
			elementShape.setUpperRightY(bottomPos);
			if (bottomPos < topPos)
				bottomPos += bottomSpacing;
			bottom.get().paint(content, elementShape);
		}
	}

	private void paintLeftRightCenter(PDPageContentStream content) throws IOException {
		if (bottomPos < topPos) {
			elementShape.setLowerLeftY(bottomPos);
			elementShape.setUpperRightY(topPos);
			paintLeft(content);
			paintRight(content);
			paintCenter(content);
		}
	}

	private void paintLeft(PDPageContentStream content) throws IOException {
		if (left.isPresent() && leftPos < rightPos) {
			elementShape.setLowerLeftX(leftPos);
			leftPos = Math.min(leftPos + left.get().getWidth(), rightPos);
			if (leftPos + leftSpacing >= rightPos)
				leftPos = rightPos;
			elementShape.setUpperRightX(leftPos);
			if (leftPos < rightPos)
				leftPos += leftSpacing;
			left.get().paint(content, elementShape);
		}
	}

	private void paintRight(PDPageContentStream content) throws IOException {
		if (right.isPresent() && leftPos < rightPos) {
			elementShape.setUpperRightX(rightPos);
			rightPos = Math.max(rightPos - right.get().getWidth(), leftPos);
			if (rightPos - rightSpacing <= leftPos)
				rightPos = leftPos;
			elementShape.setLowerLeftX(rightPos);
			if (rightPos > leftPos)
				rightPos -= rightSpacing;
			right.get().paint(content, elementShape);
		}
	}

	private void paintCenter(PDPageContentStream content) throws IOException {
		if (center.isPresent() && leftPos < rightPos) {
			elementShape.setLowerLeftX(leftPos);
			elementShape.setUpperRightX(rightPos);
			center.get().paint(content, elementShape);
		}
	}

}
