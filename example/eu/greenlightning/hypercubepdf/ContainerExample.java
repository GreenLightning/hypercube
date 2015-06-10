package eu.greenlightning.hypercubepdf;

import java.awt.Color;
import java.io.IOException;

import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import eu.greenlightning.hypercubepdf.border.HCPEmptyBorder;
import eu.greenlightning.hypercubepdf.container.*;
import eu.greenlightning.hypercubepdf.layout.HCPFlowLayout;
import eu.greenlightning.hypercubepdf.layout.HCPStretchLayout;
import eu.greenlightning.hypercubepdf.text.HCPNormalText;
import eu.greenlightning.hypercubepdf.text.HCPStyle;

public class ContainerExample {

	public static void main(String[] args) throws IOException, COSVisitorException {
		try (PDDocument document = new PDDocument()) {
			demoSequentialContainers(document);
			demoBorderContainer(document);
			demoGridContainer(document);
			document.save("examples/containers.pdf");
		}
	}

	private static void demoSequentialContainers(PDDocument document) throws IOException {
		HCPElement a = createBox("A", Color.RED, Color.ORANGE);
		HCPElement b = createBox("B", new Color(0, 64, 0), Color.GREEN);
		HCPElement c = createBox("C", Color.BLUE, Color.CYAN);

		a = HCPSized.withSize(a,  50, 200);
		b = HCPSized.withSize(b, 100, 100);
		c = HCPSized.withSize(c, 150,  50);

		HCPElement horizontal = HCPContainers.getHorizontalSplit(a, b, c);
		HCPElement vertical = HCPContainers.getVerticalSplit(10, a, b, c);
		Examples.paintOnNewPage(document, "Split", horizontal, vertical);

		horizontal = HCPContainers.getHorizontalFlow(a, b, c);
		vertical = HCPContainers.getVerticalFlow(10, a, b, c);
		Examples.paintOnNewPage(document, "Flow", horizontal, vertical);

		horizontal = HCPContainers.getHorizontalStretch(a, b, c);
		vertical = HCPContainers.getVerticalStretch(10, a, b, c);
		Examples.paintOnNewPage(document, "Stretch", horizontal, vertical);
	}

	private static void demoBorderContainer(PDDocument document) throws IOException {
		HCPElement top = createBox("TOP", Color.BLACK, Color.RED);
		HCPElement left = createBox("LEFT", Color.BLACK, Color.GREEN);
		HCPElement center = createBox("CENTER", Color.BLACK, Color.CYAN);
		HCPElement right = createBox("RIGHT", Color.BLACK, Color.BLUE);
		HCPElement bottom = createBox("BOTTOM", Color.BLACK, Color.ORANGE);

		HCPElement border = HCPBorderContainer.builder().top(top).left(left).center(center).right(right)
				.bottom(bottom).allSpacings(10).build();
		Examples.paintOnNewPage(document, "Border", border);
	}

	private static void demoGridContainer(PDDocument document) throws IOException {
		HCPElement a = createBox("A", Color.BLACK, Color.RED);
		HCPElement b = createBox("B", Color.BLACK, Color.MAGENTA);
		HCPElement c = createBox("C", Color.BLACK, Color.ORANGE);
		HCPElement d = createBox("D", Color.BLACK, Color.BLUE);
		HCPElement e = createBox("E", Color.BLACK, Color.CYAN);
		HCPElement f = createBox("F", Color.BLACK, Color.GREEN);

		a = HCPSized.withWidth(a, 150);
		e = HCPSized.withHeight(e, 100);

		HCPElement[][] elements = { { a, b, c }, { d, e, f } };
		HCPStretchLayout horizontal = HCPStretchLayout.getInstance();
		HCPFlowLayout vertical = HCPFlowLayout.getInstance();
		HCPElement grid = new HCPGridContainer(horizontal, vertical, elements);
		Examples.paintOnNewPage(document, "Grid", grid);
	}

	private static HCPElement createBox(String label, Color stroking, Color nonStroking) {
		HCPElement background = new HCPArea(nonStroking, stroking);
		HCPStyle style = new HCPStyle(PDType1Font.HELVETICA_BOLD, 24, stroking);
		HCPElement text = new HCPNormalText(label, style);
		text = HCPEmptyBorder.getHorizontalVerticalInstance(text, 20, 10);
		return new HCPStack(background, text);
	}

}
