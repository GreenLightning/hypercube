package eu.greenlightning.hypercubepdf.page;

import java.io.IOException;
import java.util.Objects;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;

import eu.greenlightning.hypercubepdf.HCPElement;

abstract class HCPAbstractPage implements HCPPage {

	protected final PDDocument document;
	protected final PDPage page;

	private HCPAbstractPage(PDDocument document, PDPage page) {
		this.document = Objects.requireNonNull(document, "Document must not be null.");
		this.page = Objects.requireNonNull(page, "Page must not be null.");
		document.addPage(page);
	}

	public HCPAbstractPage(PDDocument document) {
		this(document, new PDPage());
	}

	public HCPAbstractPage(PDDocument document, PDRectangle size) {
		this(document, new PDPage(Objects.requireNonNull(size, "Size must not be null.")));
	}

	@Override
	public PDPage asPDPage() {
		return page;
	}

	@Override
	public void paint(HCPElement element) throws IOException {
		try (PDPageContentStream content = createPageContentStream()) {
			element.paint(content, getPageSize());
		}
	}

	protected abstract PDRectangle getPageSize();

	protected abstract PDPageContentStream createPageContentStream() throws IOException;

}