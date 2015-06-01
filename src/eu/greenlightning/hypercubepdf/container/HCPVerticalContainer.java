package eu.greenlightning.hypercubepdf.container;

import java.io.IOException;
import java.util.*;

import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;

import eu.greenlightning.hypercubepdf.HCPElement;
import eu.greenlightning.hypercubepdf.layout.*;

class HCPVerticalContainer implements HCPElement {

	private final HCPLayout layout;
	private final HCPElements elements;
	
	private HCPVerticalContainer(HCPLayout layout, HCPElements elements){
		this.layout = Objects.requireNonNull(layout, "Layout must not be null.");
		this.elements = Objects.requireNonNull(elements, "Elements must not be null.");
	}

	public HCPVerticalContainer(HCPLayout layout, Collection<? extends HCPElement> elements) {
		this(layout, new HCPElements(elements));
	}

	public HCPVerticalContainer(HCPLayout layout, HCPElement... elements) {
		this(layout, new HCPElements(elements));
	}

	@Override
	public float getWidth() throws IOException {
		return elements.getMaxWidth();
	}

	@Override
	public float getHeight() throws IOException {
		return layout.getSize(elements.getHeights());
	}

	@Override
	public void paint(PDPageContentStream content, PDRectangle shape) throws IOException {
		HCPLayoutSpace space = new HCPLayoutSpace(shape.getUpperRightY(), shape.getLowerLeftY());
		HCPLayoutResult[] results = layout.apply(space, elements.getHeights());

		PDRectangle elementShape = new PDRectangle();
		elementShape.setLowerLeftX(shape.getLowerLeftX());
		elementShape.setUpperRightX(shape.getUpperRightX());

		for (int index = 0; index < results.length && index < elements.size(); index++) {
			elementShape.setLowerLeftY(results[index].getLow());
			elementShape.setUpperRightY(results[index].getHigh());
			elements.get(index).paint(content, elementShape);
		}
	}

}
