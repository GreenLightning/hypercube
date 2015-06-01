package eu.greenlightning.hypercubepdf.align;

import org.apache.pdfbox.pdmodel.common.PDRectangle;

public enum HCPHorizontalAlignment {

	LEFT {
		@Override
		public float getX(float width, PDRectangle parent) {
			return parent.getLowerLeftX();
		}
	},
	CENTER {
		@Override
		public float getX(float width, PDRectangle parent) {
			return parent.getLowerLeftX() + (parent.getWidth() - width) / 2;
		}
	},
	RIGHT {
		@Override
		public float getX(float width, PDRectangle parent) {
			return parent.getUpperRightX() - width;
		}
	};

	public abstract float getX(float width, PDRectangle parent);

}
