package eu.greenlightning.hypercubepdf;

import static eu.greenlightning.hypercubepdf.align.HCPHorizontalAlignment.CENTER;

import java.io.IOException;

import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import eu.greenlightning.hypercubepdf.page.*;
import eu.greenlightning.hypercubepdf.text.*;

public class PageSetupExample {

	public static void main(String[] args) throws IOException, COSVisitorException {
		HCPStyle style = new HCPStyle(PDType1Font.HELVETICA_BOLD, 48);

		try (PDDocument document = new PDDocument()) {
			HCPPage page = HCPPages.addPortraitPage(document);
			String message = "This page has\nLETTER size and\nPORTRAIT orientation.";
			page.paint(new HCPMultilineText(message, style, CENTER));
			document.save("examples/page-setup-letter-portrait.pdf");
		}

		try (PDDocument document = new PDDocument()) {
			HCPPage page = HCPPages.addLandscapePage(document);
			String message = "This page has\nLETTER size and\nLANDSCAPE orientation.";
			page.paint(new HCPMultilineText(message, style, CENTER));
			document.save("examples/page-setup-letter-landscape.pdf");
		}

		try (PDDocument document = new PDDocument()) {
			HCPPage page = HCPPages.addPortraitPage(document, PDPage.PAGE_SIZE_A4);
			String message = "This page has\nA4 size and\nPORTRAIT orientation.";
			page.paint(new HCPMultilineText(message, style, CENTER));
			document.save("examples/page-setup-A4-portrait.pdf");
		}

		try (PDDocument document = new PDDocument()) {
			HCPPage page = HCPPages.addLandscapePage(document, PDPage.PAGE_SIZE_A4);
			String message = "This page has\nA4 size and\nLANDSCAPE orientation.";
			page.paint(new HCPMultilineText(message, style, CENTER));
			document.save("examples/page-setup-A4-landscape.pdf");
		}
	}

}
