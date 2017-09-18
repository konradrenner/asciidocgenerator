package org.asciidocgenerator.domain.rendering;


public enum RenderingPlaceHolder {

									CONTEXT_ROOT("###contextroot###");

	private final String value;

	private RenderingPlaceHolder(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

}
