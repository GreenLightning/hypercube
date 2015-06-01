package eu.greenlightning.hypercubepdf.page;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.common.PDRectangle;

public final class HCPPages {

	public static HCPPage addPortraitPage(PDDocument document) {
		return new HCPPortraitPage(document);
	}

	public static HCPPage addLandscapePage(PDDocument document) {
		return new HCPLandscapePage(document);
	}

	public static HCPPage addPortraitPage(PDDocument document, PDRectangle size) {
		return new HCPPortraitPage(document, size);
	}

	public static HCPPage addLandscapePage(PDDocument document, PDRectangle size) {
		return new HCPLandscapePage(document, size);
	}

	// Prevent instantiation
	private HCPPages() {
		throw new UnsupportedOperationException();
	}

}
