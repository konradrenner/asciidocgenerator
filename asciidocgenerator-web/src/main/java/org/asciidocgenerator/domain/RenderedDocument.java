package org.asciidocgenerator.domain;

import java.sql.Timestamp;
import java.util.Set;

public interface RenderedDocument {

	String getMainNavigation();

	String getGroup();

	String getNavigationPath();

	String getStorageLocation();

	String getNavigationKey();

	MetaInformation getMetaInformation();

	Timestamp getRenderTimestamp();

	Set<? extends Keyword> getCategories();
}
