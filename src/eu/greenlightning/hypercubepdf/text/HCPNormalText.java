package eu.greenlightning.hypercubepdf.text;

import java.io.IOException;

import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;

public class HCPNormalText extends HCPAbstractText {
	
	public HCPNormalText(String text, HCPStyle style) {
		super(text, style);
	}

	@Override
	public float getWidth() throws IOException {
		return style.getStringWidth(text);
	}

	@Override
	public float getHeight() throws IOException {
		return style.getHeight();
	}

	@Override
	protected void paintText(PDPageContentStream content, float x, float y) throws IOException {
		content.beginText();
		style.apply(content);
		content.setTextTranslation(x, y - style.getDescent());
		content.drawString(text);
		content.endText();
	}

}
