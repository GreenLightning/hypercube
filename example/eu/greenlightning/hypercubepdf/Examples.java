package eu.greenlightning.hypercubepdf;

import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import eu.greenlightning.hypercubepdf.border.HCPEmptyBorder;
import eu.greenlightning.hypercubepdf.container.HCPBorderContainer;
import eu.greenlightning.hypercubepdf.container.HCPContainers;
import eu.greenlightning.hypercubepdf.page.HCPPage;
import eu.greenlightning.hypercubepdf.page.HCPPages;
import eu.greenlightning.hypercubepdf.text.HCPNormalText;
import eu.greenlightning.hypercubepdf.text.HCPStyle;

public final class Examples {

	public static void paintOnNewPage(PDDocument document, String title, HCPElement left, HCPElement right)
		throws IOException {
		paintOnNewPage(document, title, HCPContainers.getHorizontalSplit(40, left, right));
	}

	public static void paintOnNewPage(PDDocument document, String title, HCPElement element) throws IOException {
		HCPStyle style = new HCPStyle(PDType1Font.HELVETICA_BOLD, 24);
		HCPElement text = new HCPNormalText(title, style);
		element = HCPBorderContainer.builder().top(text).topSpacing(30).center(element).build();
		element = HCPEmptyBorder.getAllSidesInstance(element, 50);
		HCPPage page = HCPPages.addLandscapePage(document, PDPage.PAGE_SIZE_A4);
		page.paint(element);
	}

	// Prevent instantiation
	private Examples() {
		throw new UnsupportedOperationException();
	}

}
