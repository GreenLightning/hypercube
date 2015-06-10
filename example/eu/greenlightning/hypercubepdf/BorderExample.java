package eu.greenlightning.hypercubepdf;

import java.awt.Color;
import java.io.IOException;

import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;

import eu.greenlightning.hypercubepdf.border.HCPEmptyBorder;
import eu.greenlightning.hypercubepdf.border.HCPLineBorder;
import eu.greenlightning.hypercubepdf.container.HCPBorderContainer;

public class BorderExample {

	public static void main(String[] args) throws IOException, COSVisitorException {
		try (PDDocument document = new PDDocument()) {
			demoEmptyBorder(document);
			demoLineBorder(document);
			document.save("examples/borders.pdf");
		}
	}

	private static void demoEmptyBorder(PDDocument document) throws IOException {
		HCPElement red = HCPSized.withHeight(new HCPArea(Color.RED), 30);
		HCPElement blue = new HCPArea(Color.BLUE);

		HCPElement border = HCPEmptyBorder.getHorizontalVerticalInstance(blue, 100, 50);

		HCPElement with = HCPBorderContainer.builder().top(red).center(border).bottom(red).build();
		HCPElement without = HCPBorderContainer.builder().top(red).center(blue).bottom(red).build();

		Examples.paintOnNewPage(document, "Empty Border", with, without);
	}

	private static void demoLineBorder(PDDocument document) throws IOException {
		final float size = 25;
		HCPElement content = new HCPArea(Color.RED);

		content = new HCPLineBorder(content, Color.ORANGE, size);
		content = new HCPLineBorder(content, Color.YELLOW, size);
		content = new HCPLineBorder(content, Color.GREEN, size);
		content = new HCPLineBorder(content, Color.CYAN, size);
		content = new HCPLineBorder(content, Color.BLUE, size);
		content = new HCPLineBorder(content, Color.MAGENTA, size);

		Examples.paintOnNewPage(document, "Line Border", content);
	}

}
