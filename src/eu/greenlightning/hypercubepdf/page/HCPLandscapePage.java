package eu.greenlightning.hypercubepdf.page;

import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;

class HCPLandscapePage extends HCPAbstractPage {

	public HCPLandscapePage(PDDocument document) {
		super(document);
	}

	public HCPLandscapePage(PDDocument document, PDRectangle size) {
		super(document, size);
	}

	{
		page.setRotation(90);
	}

	@Override
	protected PDRectangle getPageSize() {
		PDRectangle mediaBox = page.findMediaBox();
		return new PDRectangle(mediaBox.getHeight(), mediaBox.getWidth());
	}

	@Override
	protected PDPageContentStream createPageContentStream() throws IOException {
		PDPageContentStream content = new PDPageContentStream(document, page);
		content.concatenate2CTM(0, 1, -1, 0, getPageSize().getHeight(), 0);
		return content;
	}

}