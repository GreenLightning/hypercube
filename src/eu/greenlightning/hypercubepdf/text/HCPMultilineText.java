package eu.greenlightning.hypercubepdf.text;

import java.io.IOException;
import java.util.Objects;

import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;

import eu.greenlightning.hypercubepdf.align.HCPHorizontalAlignment;

public class HCPMultilineText extends HCPAbstractText {

	private static final float DEFAULT_LINE_SPACING = 1.2f;
	private static final HCPHorizontalAlignment DEFAULT_ALIGNMENT = HCPHorizontalAlignment.LEFT;

	private final String[] lines;
	private final float lineSpacing;
	private final HCPHorizontalAlignment alignment;

	public HCPMultilineText(String text, HCPStyle style) {
		this(text, style, DEFAULT_LINE_SPACING, DEFAULT_ALIGNMENT);
	}

	public HCPMultilineText(String text, HCPStyle style, float lineSpacing) {
		this(text, style, lineSpacing, DEFAULT_ALIGNMENT);
	}

	public HCPMultilineText(String text, HCPStyle style, HCPHorizontalAlignment alignment) {
		this(text, style, DEFAULT_LINE_SPACING, alignment);
	}

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
