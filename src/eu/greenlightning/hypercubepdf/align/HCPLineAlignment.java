package eu.greenlightning.hypercubepdf.align;

enum HCPLineAlignment {

	BEGINNING {
		@Override
		public float align(float size, float start, float end) {
			return start;
		}
	},
	MIDDLE {
		@Override
		public float align(float size, float start, float end) {
			return (start + end - size) / 2;
		}
	},
	END {
		@Override
		public float align(float size, float start, float end) {
			return end - size;
		}
	};

	public abstract float align(float size, float start, float end);

}
