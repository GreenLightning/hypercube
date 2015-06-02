package eu.greenlightning.hypercubepdf;

import java.awt.Color;
import java.io.IOException;
import java.util.Objects;

import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;

public class HCPArea implements HCPElement {

	private static final float LINE_WIDTH = 1;

	private final Color contentColor;
	private final Color borderColor;
	private final HCPAreaBorder border;

	/**
	 * Creates an {@link HCPArea} with no border.
	 * 
	 * @param contentColor the color used to fill the background of the area. If {@code null} the background
	 *            is not painted.
	 */
	public HCPArea(Color contentColor) {
		this(contentColor, null, HCPAreaBorder.NO_BORDER);
	}

	/**
	 * Creates an {@link HCPArea} with full border.
	 * 
	 * @param contentColor the color used to fill the background of the area. If {@code null} the background
	 *            is not painted.
	 * @param borderColor the color used to draw the border of the area. If {@code null} the border is not
	 *            painted.
	 */
	public HCPArea(Color contentColor, Color borderColor) {
		this(contentColor, borderColor, HCPAreaBorder.FULL_BORDER);
	}

	/**
	 * Creates an {@link HCPArea} with the given borders.
	 * <p>
	 * Note that for the border to be painted, {@code borderColor} must not be {@code null} and
	 * {@code border} must not be {@code NO_BORDER}.
	 * 
	 * @param contentColor the color used to fill the background of the area. If {@code null} the background
	 *            is not painted.
	 * @param borderColor the color used to draw the border of the area. If {@code null} the border is not
	 *            painted.
	 * @param border the border type to use; not {@code null}
	 * @throws NullPointerException if border is null
	 */
	public HCPArea(Color contentColor, Color borderColor, HCPAreaBorder border) {
		this.borderColor = borderColor;
		this.contentColor = contentColor;
		this.border = Objects.requireNonNull(border, "Border must not be null. Maybe use NO_BORDER?");
	}

	@Override
	public float getWidth() {
		float width = 1;
		if (borderColor != null) {
			if (border.paintLeft())
				width += 0.5 * LINE_WIDTH;
			if (border.paintRight())
				width += 0.5 * LINE_WIDTH;
		}
		return width;
	}

	@Override
	public float getHeight() {
		float height = 1;
		if (borderColor != null) {
			if (border.paintTop())
				height += 0.5 * LINE_WIDTH;
			if (border.paintBottom())
				height += 0.5 * LINE_WIDTH;
		}
		return height;
	}

	@Override
	public void paint(PDPageContentStream content, PDRectangle shape) throws IOException {
		paintContent(content, shape);
		paintBorder(content, shape);
	}

	private void paintContent(PDPageContentStream content, PDRectangle shape) throws IOException {
		if (contentColor != null) {
			content.setNonStrokingColor(contentColor);
			content.fillRect(shape.getLowerLeftX(), shape.getLowerLeftY(), shape.getWidth(), shape
				.getHeight());
		}
	}

	private void paintBorder(PDPageContentStream content, PDRectangle shape) throws IOException {
		if (borderColor != null && border != HCPAreaBorder.NO_BORDER) {
			content.setStrokingColor(borderColor);
			content.setLineWidth(LINE_WIDTH);
			addBorder(content, shape);
			content.stroke();
		}
	}

	private void addBorder(PDPageContentStream content, PDRectangle shape) throws IOException {
		float leftX = shape.getLowerLeftX();
		float rightX = shape.getUpperRightX();
		float lowerY = shape.getLowerLeftY();
		float upperY = shape.getUpperRightY();

		if (border == HCPAreaBorder.FULL_BORDER) {
			content.addRect(leftX, lowerY, shape.getWidth(), shape.getHeight());
			return;
		}

		if (border.paintTop())
			content.addLine(leftX, upperY, rightX, upperY);
		if (border.paintRight())
			content.addLine(rightX, upperY, rightX, lowerY);
		if (border.paintBottom())
			content.addLine(rightX, lowerY, leftX, lowerY);
		if (border.paintLeft())
			content.addLine(leftX, lowerY, leftX, upperY);
	}

}
