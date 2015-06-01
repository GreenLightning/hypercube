package eu.greenlightning.hypercubepdf.align;

import org.apache.pdfbox.pdmodel.common.PDRectangle;

public enum HCPVerticalAlignment {

	TOP {
		@Override
		public float getY(float height, PDRectangle parent) {
			return parent.getUpperRightY() - height;
		}
	},

	CENTER {
		@Override
		public float getY(float height, PDRectangle parent) {
			return parent.getLowerLeftY() +  (parent.getHeight() - height) / 2;
		}
	},

	BOTTOM {
		@Override
		public float getY(float height, PDRectangle parent) {
			return parent.getLowerLeftY();
		}
	};

	public abstract float getY(float height, PDRectangle parent);

}
