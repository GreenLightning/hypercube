package eu.greenlightning.hypercubepdf.text;

import java.io.IOException;

import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;

public class HCPSideText extends HCPAbstractText {

	public HCPSideText(String text, HCPStyle style) {
		super(text, style);
	}

	@Override
	public float getWidth() throws IOException {
		return style.getHeight();
	}

	@Override
	public float getHeight() throws IOException {
		return style.getStringWidth(text);
	}

	@Override
	protected void paintText(PDPageContentStream content, float x, float y) throws IOException {
		content.beginText();
		style.apply(content);
		content.setTextRotation(Math.PI / 2, x + getWidth() + style.getDescent(), y);
		content.drawString(text);
		content.endText();
	}

}
