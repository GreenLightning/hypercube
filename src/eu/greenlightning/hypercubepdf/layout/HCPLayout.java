package eu.greenlightning.hypercubepdf.layout;

public interface HCPLayout {

	float getSize(float[] sizes);
	HCPLayoutResult[] apply(HCPLayoutSpace space, float[] sizes);

}
