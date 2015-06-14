package eu.greenlightning.hypercubepdf;

import java.awt.Color;
import java.io.IOException;
import java.util.Objects;

import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;

/**
 * A filled area with a border. Both the filling of the background and the drawing of the border are optional operations
 * and a separate color can be configured for each one.
 * <p>
 * Furthermore, which parts of the border are drawn can be configured using {@link HCPBorderType}. However, the width of
 * the border is currently fixed to be 1 default use space unit. Also note that the border is drawn exactly on the edges
 * of the {@link PDRectangle} supplied to the {@link #paint(PDPageContentStream, PDRectangle)} method, meaning half a
 * unit of the border will be outside the boundary and the other half will be inside the boundary. This also means that
 * if two {@link HCPArea}s are drawn side by side their borders will completely overlap.
 * <p>
 * The {@link #getWidth()} and {@link #getHeight()} methods will make the background 1 unit big in each direction,
 * accounting for the insets of the border if it is drawn.
 * <p>
 * This class is immutable.
 * 
 * @author Green Lightning
 * @see HCPUnits Default User Space
 */
public class HCPArea implements HCPElement {

	private static final float LINE_WIDTH = 1;

	private final Color contentColor;
	private final Color borderColor;
	private final HCPBorderType border;

	/**
	 * Creates an {@link HCPArea} with no border.
	 * <p>
	 * If {@code contentColor} is {@code null} the background will not be filled.
	 * <p>
	 * Note that the element will be invisible if {@code contentColor} is {@code null}, because the border is not drawn
	 * either.
	 * 
	 * @param contentColor the color to use for filling the background or {@code null}
	 */
	public HCPArea(Color contentColor) {
		this(contentColor, null, HCPBorderType.NO_BORDER);
	}

	/**
	 * Creates an {@link HCPArea} with full border.
	 * <p>
	 * If {@code contentColor} is {@code null} the background will not be filled. If {@code borderColor} is {@code null}
	 * the border will not be drawn.
	 * <p>
	 * Note that the element will be invisible if both {@code contentColor} and {@code borderColor} are {@code null}.
	 * 
	 * @param contentColor the color to use for filling the background or {@code null}
	 * @param borderColor the color to use for drawing the border or {@code null}
	 */
	public HCPArea(Color contentColor, Color borderColor) {
		this(contentColor, borderColor, HCPBorderType.FULL_BORDER);
	}

	/**
	 * Creates an {@link HCPArea} with the given border.
	 * <p>
	 * If {@code contentColor} is {@code null} the background will not be filled.
	 * <p>
	 * Note that for the border to be drawn, {@code borderColor} must not be {@code null} and {@code border} must not be
	 * {@link HCPBorderType#NO_BORDER}.
	 * <p>
	 * Note that the element will be invisible if {@code contentColor} is {@code null} and the border is not drawn.
	 * 
	 * @param contentColor the color to use for filling the background or {@code null}
	 * @param borderColor the color used for drawing the border or {@code null}
	 * @param border the border type to use; not {@code null}
	 * @throws NullPointerException if border is {@code null}
	 * @see HCPBorderType
	 */
	public HCPArea(Color contentColor, Color borderColor, HCPBorderType border) {
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
			content.fillRect(shape.getLowerLeftX(), shape.getLowerLeftY(), shape.getWidth(), shape.getHeight());
		}
	}

	private void paintBorder(PDPageContentStream content, PDRectangle shape) throws IOException {
		if (borderColor != null && border != HCPBorderType.NO_BORDER) {
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

		if (border == HCPBorderType.FULL_BORDER) {
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
