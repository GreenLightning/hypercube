package eu.greenlightning.hypercubepdf.container;

import java.io.IOException;
import java.util.Optional;

import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;

import eu.greenlightning.hypercubepdf.HCPElement;

public class HCPBorderContainer implements HCPElement {

	public static Builder builder() {
		return new Builder();
	}

	public static final class Builder {

		private HCPElement top, bottom, left, right, center;
		private float topSpacing, bottomSpacing, leftSpacing, rightSpacing;

		public Builder top(HCPElement top) {
			this.top = top;
			return this;
		}

		public Builder bottom(HCPElement bottom) {
			this.bottom = bottom;
			return this;
		}

		public Builder left(HCPElement left) {
			this.left = left;
			return this;
		}

		public Builder right(HCPElement right) {
			this.right = right;
			return this;
		}

		public Builder center(HCPElement center) {
			this.center = center;
			return this;
		}

		public Builder topSpacing(float topSpacing) {
			this.topSpacing = topSpacing;
			return this;
		}

		public Builder bottomSpacing(float bottomSpacing) {
			this.bottomSpacing = bottomSpacing;
			return this;
		}

		public Builder leftSpacing(float leftSpacing) {
			this.leftSpacing = leftSpacing;
			return this;
		}

		public Builder rightSpacing(float rightSpacing) {
			this.rightSpacing = rightSpacing;
			return this;
		}

		public Builder horizontalSpacing(float spacing) {
			this.topSpacing = spacing;
			this.bottomSpacing = spacing;
			return this;
		}

		public Builder verticalSpacing(float spacing) {
			this.leftSpacing = spacing;
			this.rightSpacing = spacing;
			return this;
		}

		public Builder allSpacings(float spacing) {
			this.topSpacing = spacing;
			this.bottomSpacing = spacing;
			this.leftSpacing = spacing;
			this.rightSpacing = spacing;
			return this;
		}

		public HCPBorderContainer build() {
			return new HCPBorderContainer(top, bottom, left, right, center, topSpacing, bottomSpacing,
					leftSpacing, rightSpacing);
		}

	}

	private final Optional<HCPElement> top, bottom, left, right, center;
	private final float topSpacing, bottomSpacing, leftSpacing, rightSpacing;

	// Used only for drawing.
	private final PDRectangle elementShape = new PDRectangle();
	private float leftPos, rightPos, topPos, bottomPos;

	private HCPBorderContainer(HCPElement top, HCPElement bottom, HCPElement left, HCPElement right,
			HCPElement center, float topSpacing, float bottomSpacing, float leftSpacing, float rightSpacing) {
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
			throw new IllegalArgumentException(name + " spacing must be equal to or greater than zero,"
					+ " but was " + spacing + ".");
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
