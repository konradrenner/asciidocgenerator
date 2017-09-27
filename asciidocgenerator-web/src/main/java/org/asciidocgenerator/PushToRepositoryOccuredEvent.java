package org.asciidocgenerator;

import java.net.URL;

public final class PushToRepositoryOccuredEvent {

	public enum ObjectKind {
							TAG_PUSH,
							PUSH
	}

	private String repositoryName;
	private String projectName;
	private String reference;
	private ObjectKind objectKind;
	private URL url;

	private PushToRepositoryOccuredEvent() {
		// create via builder
	}

	public static WithRepositoryName newInstance() {
		return new Builder(new PushToRepositoryOccuredEvent());
	}

	public String getRepositoryName() {
		return repositoryName;
	}

	public String getReference() {
		return reference;
	}

	public String getProjectName() {
		return projectName;
	}

	public ObjectKind getObjectKind() {
		return objectKind;
	}

	public URL getUrl() {
		return url;
	}

	@Override
	public String toString() {
		return "PushToRepositoryOccuredEvent [repositoryName="	+ repositoryName
				+ ", projectName="
				+ projectName
				+ ", reference="
				+ reference
				+ ", objectKind="
				+ objectKind
				+ ", url="
				+ url
				+ "]";
	}

	public interface WithRepositoryName {

		WithProjectName repositoryName(String value);
	}

	public interface WithProjectName {

		WithURL projectName(String value);
	}

	public interface WithReference {

		WithObjectKind reference(String value);
	}

	public interface WithURL {

		WithReference url(URL value);
	}

	public interface WithObjectKind {

		Finish objectKind(ObjectKind value);
	}

	public interface Finish {

		PushToRepositoryOccuredEvent build();
	}

	@SuppressWarnings("synthetic-access")
	public static final class Builder
			implements WithObjectKind, WithProjectName, WithReference, WithRepositoryName, WithURL, Finish {

		private final PushToRepositoryOccuredEvent event;

		public Builder(PushToRepositoryOccuredEvent event) {
			super();
			this.event = event;
		}

		@Override
		public PushToRepositoryOccuredEvent build() {
			return event;
		}

		@Override
		public WithReference url(URL value) {
			event.url = value;
			return this;
		}

		@Override
		public WithProjectName repositoryName(String value) {
			event.repositoryName = value;
			return this;
		}

		@Override
		public WithObjectKind reference(String value) {
			event.reference = value;
			return this;
		}

		@Override
		public WithURL projectName(String value) {
			event.projectName = value;
			return this;
		}

		@Override
		public Finish objectKind(ObjectKind value) {
			event.objectKind = value;
			return this;
		}
	}
}
