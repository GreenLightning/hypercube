package eu.greenlightning.hypercubepdf.align;

/**
 * One-dimensional alignment which is independent of the direction of the axis.
 *
 * @author Green Lightning
 */
enum HCPLineAlignment {

	/** The start of the child segment will be aligned with the start of the parent segment. */
	BEGINNING {
		@Override
		public float align(float size, float start, float end) {
			return start;
		}
	},
	
	/** The center of the child segment will be aligned with the center of the parent segment. */
	MIDDLE {
		@Override
		public float align(float size, float start, float end) {
			return (start + end - size) / 2;
		}
	},
	
	/** The end of the child segment will be aligned with the end of the parent segment. */
	END {
		@Override
		public float align(float size, float start, float end) {
			return end - size;
		}
	};

	/**
	 * Aligns a segment of the specified size inside the segment specified by the {@code start} and {@code end}
	 * coordinates.
	 * <p>
	 * The {@code start} argument should define the smaller and the {@code end} argument the larger coordinate of the
	 * parent segment. The {@code size} argument should define the length of the child segment (which should be
	 * positive).
	 * <p>
	 * If the arguments do not satisfy the given constraints the return value of this method is unspecified. Otherwise,
	 * it returns the <i>start</i> (the smaller) coordinate of the aligned segment based on the origin of {@code start}
	 * and {@code end}.
	 * 
	 * @param size should be {@literal >= 0}
	 * @param start should be {@literal <= end}
	 * @param end should be {@literal >= start}
	 * @return the <i>start</i> coordinate of the aligned segment
	 */
	public abstract float align(float size, float start, float end);

}
