package org.asciidocgenerator.ui.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.asciidocgenerator.Trail;
import org.asciidocgenerator.Validate;

public class PageTrail
		implements Trail {

	private static final String SEPARATOR = "/";

	private final String trail;
	private final List<String> fragements;

	public PageTrail(String trail) {
		String tempTrail = Validate.notEmpty(trail);
		this.trail = Validate.valueNotStartsWithPrefix(tempTrail.trim(), "/");
		this.fragements = Arrays.asList(trail.split(SEPARATOR));
	}

	@Override
	public String getFullPath() {
		return trail;
	}

	@Override
	public List<String> getFragments() {
		return fragements;
	}

	@Override
	public String getSeparator() {
		return SEPARATOR;
	}

	@Override
	public String toString() {
		return "PageTrail [trail=" + trail + ", fragements=" + fragements + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((trail == null) ? 0 : trail.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PageTrail other = (PageTrail) obj;
		return Objects.equals(trail, other.getFullPath());
	}

}
