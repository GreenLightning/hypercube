package eu.greenlightning.hypercubepdf;

import java.awt.Color;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;

public class HCPArea implements HCPElement {

	public static final byte NO_BORDER = 0;

	public static final byte TOP_BORDER = 0b1000;
	public static final byte RIGHT_BORDER = 0b0100;
	public static final byte BOTTOM_BORDER = 0b0010;
	public static final byte LEFT_BORDER = 0b0001;

	public static final byte TOP_RIGHT_BORDER = TOP_BORDER | RIGHT_BORDER;
	public static final byte TOP_LEFT_BORDER = TOP_BORDER | LEFT_BORDER;
	public static final byte BOTTOM_RIGHT_BORDER = BOTTOM_BORDER | RIGHT_BORDER;
	public static final byte BOTTOM_LEFT_BORDER = BOTTOM_BORDER | LEFT_BORDER;

	public static final byte HORIZONTAL_BORDER = TOP_BORDER | BOTTOM_BORDER;
	public static final byte VERTICAL_BORDER = LEFT_BORDER | RIGHT_BORDER;

	public static final byte NO_TOP_BORDER = RIGHT_BORDER | BOTTOM_BORDER | LEFT_BORDER;
	public static final byte NO_RIGHT_BORDER = BOTTOM_BORDER | LEFT_BORDER | TOP_BORDER;
	public static final byte NO_BOTTOM_BORDER = LEFT_BORDER | TOP_BORDER | RIGHT_BORDER;
	public static final byte NO_LEFT_BORDER = TOP_BORDER | RIGHT_BORDER | BOTTOM_BORDER;

	public static final byte FULL_BORDER = TOP_BORDER | RIGHT_BORDER | BOTTOM_BORDER | LEFT_BORDER;

	private static final float LINE_WIDTH = 1;

	private final Color contentColor;
	private final Color borderColor;
	private final byte borders;

	/**
	 * Creates an {@link HCPArea} with no border.
	 * 
	 * @param contentColor the color used to fill the background of the area. If {@code null} the background
	 *            is not painted.
	 */
	public HCPArea(Color contentColor) {
		this(contentColor, null, NO_BORDER);
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
		this(contentColor, borderColor, FULL_BORDER);
	}

	/**
	 * Creates an {@link HCPArea} with the given borders.
	 * <p>
	 * Note that for the border to be painted, {@code borderColor} must not be {@code null} and
	 * {@code borders} must contain at least one border, i. e. must not be {@code NO_BORDER}.
	 * 
	 * @param contentColor the color used to fill the background of the area. If {@code null} the background
	 *            is not painted.
	 * @param borderColor the color used to draw the border of the area. If {@code null} the border is not
	 *            painted.
	 * @param borders any combination of the border constants in this class.
	 * @throws IllegalArgumentException if borders is invalid.
	 */
	public HCPArea(Color contentColor, Color borderColor, byte borders) {
		this.borderColor = borderColor;
		this.contentColor = contentColor;
		if (borders < 0 || borders >= (1 << 4))
			throw new IllegalArgumentException("Borders.");
		this.borders = borders;
	}

	private boolean isBorder(byte border) {
		return borders == border;
	}

	private boolean containsBorder(byte border) {
		return (borders & border) == border;
	}

	@Override
	public float getWidth() {
		float width = 1;
		if (borderColor != null) {
			if (containsBorder(LEFT_BORDER))
				width += 0.5 * LINE_WIDTH;
			if (containsBorder(RIGHT_BORDER))
				width += 0.5 * LINE_WIDTH;
		}
		return width;
	}

	@Override
	public float getHeight() {
		float height = 1;
		if (borderColor != null) {
			if (containsBorder(TOP_BORDER))
				height += 0.5 * LINE_WIDTH;
			if (containsBorder(BOTTOM_BORDER))
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
			content.fillRect(shape.getLowerLeftX(), shape.getLowerLeftY(), shape.getWidth(),
					shape.getHeight());
		}
	}

	private void paintBorder(PDPageContentStream content, PDRectangle shape) throws IOException {
		if (borderColor != null && !isBorder(NO_BORDER)) {
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

		if (isBorder(FULL_BORDER)) {
			content.addRect(leftX, lowerY, shape.getWidth(), shape.getHeight());
			return;
		}

		if (containsBorder(TOP_BORDER))
			content.addLine(leftX, upperY, rightX, upperY);
		if (containsBorder(RIGHT_BORDER))
			content.addLine(rightX, upperY, rightX, lowerY);
		if (containsBorder(BOTTOM_BORDER))
			content.addLine(rightX, lowerY, leftX, lowerY);
		if (containsBorder(LEFT_BORDER))
			content.addLine(leftX, lowerY, leftX, upperY);
	}

}
