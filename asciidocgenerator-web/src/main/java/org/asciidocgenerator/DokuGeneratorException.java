package org.asciidocgenerator;

public class DokuGeneratorException
		extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private final ErrorCode code;

	public DokuGeneratorException(ErrorCode code, Throwable cause) {
		super(cause);
		this.code = code;
	}

	public DokuGeneratorException(ErrorCode code) {
		super();
		this.code = code;
	}

	@Override
	public synchronized Throwable fillInStackTrace() {
		return this;
	}

	public ErrorCode getCode() {
		return code;
	}

	public enum ErrorCode {
							UNABLE_TO_DOWNLOAD_PROJECT,
							ARTICLE_NOT_FOUND,
							REQUESTED_NOT_HTTP,
							MALFORMED_GITLAB_JSON,
							MALFORMED_REQUESTED_DOWNLOAD_URL,
							REQUESTED_IS_NOT_IMAGE,
							IMAGE_PATH_NOT_VALID,
							IMAGE_NOT_FOUND,
							MAIN_NAVIGATION_NOT_VALID,
							MAIN_NAVIGATION_NOT_DEFINED,
							SIDE_NAVIGATION_NOT_VALID,
							UNABLE_TO_GENERATE_PDF;
	}
}
