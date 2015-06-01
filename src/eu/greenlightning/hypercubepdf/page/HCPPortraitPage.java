package eu.greenlightning.hypercubepdf.page;

import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;

class HCPPortraitPage extends HCPAbstractPage {

	public HCPPortraitPage(PDDocument document) {
		super(document);
	}

	public HCPPortraitPage(PDDocument document, PDRectangle size) {
		super(document, size);
	}

	@Override
	protected PDRectangle getPageSize() {
		return page.findMediaBox();
	}

	@Override
	protected PDPageContentStream createPageContentStream() throws IOException {
		return new PDPageContentStream(document, page);
	}

}