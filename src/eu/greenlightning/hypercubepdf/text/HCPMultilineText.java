package eu.greenlightning.hypercubepdf.text;

import java.io.IOException;
import java.util.Objects;

import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;

import eu.greenlightning.hypercubepdf.align.HCPHorizontalAlignment;

/**
 * A text element which handles line breaks.
 * <p>
 * Additionally, the spacing between the lines can be configured using the line spacing parameter. The line spacing is
 * measured in 'line heights', i.&nbsp;e. 1 means each line is directly below the other and 2 means that between two
 * consecutive lines is a gap with the height of one line.
 * <p>
 * The alignment of the individual lines can also be configured. Note however, that the (imaginary) rectangle
 * surrounding all lines is always centered inside the text element.
 * <p>
 * This class is immutable.
 *
 * @author Green Lightning
 */
public class HCPMultilineText extends HCPText {

	private static final float DEFAULT_LINE_SPACING = 1.2f;
	private static final HCPHorizontalAlignment DEFAULT_ALIGNMENT = HCPHorizontalAlignment.LEFT;

	private final String[] lines;
	private final float lineSpacing;
	private final HCPHorizontalAlignment alignment;

	/**
	 * Creates an {@link HCPMultilineText} instance with the specified text and style and the default line spacing of
	 * 1.2 lines, which aligns the text on the left.
	 * 
	 * @param text not {@code null}
	 * @param style not {@code null}
	 * @throws NullPointerException if text or style is {@code null}
	 */
	public HCPMultilineText(String text, HCPStyle style) {
		this(text, style, DEFAULT_LINE_SPACING, DEFAULT_ALIGNMENT);
	}

	/**
	 * Creates an {@link HCPMultilineText} instance with the specified text, style and line spacing, which aligns the
	 * text on the left.
	 * <p>
	 * The line spacing is measured in 'line heights', i.&nbsp;e. 1 means each line is directly below the other and 2
	 * means that between two consecutive lines is a gap with the height of one line.
	 * 
	 * @param text not {@code null}
	 * @param style not {@code null}
	 * @param lineSpacing must be {@literal >= 1}
	 * @throws NullPointerException if text or style is {@code null}
	 * @throws IllegalArgumentException if lineSpacing is {@literal < 1}
	 */
	public HCPMultilineText(String text, HCPStyle style, float lineSpacing) {
		this(text, style, lineSpacing, DEFAULT_ALIGNMENT);
	}

	/**
	 * Creates an {@link HCPMultilineText} instance with the specified text, style and text alignment and the default
	 * line spacing of 1.2 lines.
	 * 
	 * @param text not {@code null}
	 * @param style not {@code null}
	 * @param alignment not {@code null}
	 * @throws NullPointerException if text, style or alignment is {@code null}
	 */
	public HCPMultilineText(String text, HCPStyle style, HCPHorizontalAlignment alignment) {
		this(text, style, DEFAULT_LINE_SPACING, alignment);
	}

	/**
	 * Creates an {@link HCPMultilineText} instance with the specified text, style, line spacing and text alignment.
	 * <p>
	 * The line spacing is measured in 'line heights', i.&nbsp;e. 1 means each line is directly below the other and 2
	 * means that between two consecutive lines is a gap with the height of one line.
	 * 
	 * @param text not {@code null}
	 * @param style not {@code null}
	 * @param lineSpacing must be {@literal >= 1}
	 * @param alignment not {@code null}
	 * @throws NullPointerException if text, style or alignment is {@code null}
	 * @throws IllegalArgumentException if lineSpacing is {@literal < 1}
	 */
	public HCPMultilineText(String text, HCPStyle style, float lineSpacing, HCPHorizontalAlignment alignment) {
		super(text, style);
		this.lines = text.split("\\n", -1); // -1 -> do not discard trailing empty strings
		this.lineSpacing = checkLineSpacing(lineSpacing);
		this.alignment = Objects.requireNonNull(alignment, "Alignment must not be null.");
	}

	private float checkLineSpacing(float lineSpacing) {
		if (lineSpacing < 1)
			throw new IllegalArgumentException("Line spacing must be equal to or greater than one, but was "
				+ lineSpacing + ".");
		return lineSpacing;
	}

	@Override
	protected HCPText createInstance(String text, HCPStyle style) {
		return new HCPMultilineText(text, style, lineSpacing, alignment);
	}

	@Override
	public float getWidth() throws IOException {
		float width = 0;
		for (String line : lines) {
			width = Math.max(width, style.getStringWidth(line));
		}
		return width;
	}

	@Override
	public float getHeight() throws IOException {
		return ((lines.length - 1) * lineSpacing + 1) * style.getHeight();
	}

	@Override
	protected void paintText(PDPageContentStream content, float x, float y) throws IOException {
		PDRectangle size = new PDRectangle(getWidth(), getHeight());
		content.beginText();
		style.apply(content);
		float verticalOffset = (lineSpacing - 1) * style.getHeight() - style.getDescent();
		content.setTextTranslation(x, y + getHeight() + verticalOffset);
		for (String line : lines) {
			float lineOffset = alignment.alignWithParent(style.getStringWidth(line), size);
			content.moveTextPositionByAmount(lineOffset, -lineSpacing * style.getHeight());
			content.drawString(line);
			content.moveTextPositionByAmount(-lineOffset, 0);
		}
		content.endText();
	}

}
