package org.asciidocgenerator.ui;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;


@ApplicationScoped
@Named("urlEncoder")
public class URLEncoderService {

	public String encode(String value) {
		try {
			return URLEncoder.encode(value, StandardCharsets.UTF_8.name());
		} catch (UnsupportedEncodingException e) {
			Logger.getLogger("URLEncoderService").log(	Level.SEVERE,
														"unable to encode because UTF8 is not supported",
														e);
			return value;
		}
	}

	public String decode(String value) {
		try {
			return URLDecoder.decode(value, StandardCharsets.UTF_8.name());
		} catch (UnsupportedEncodingException e) {
			Logger.getLogger("URLEncoderService").log(	Level.SEVERE,
														"unable to encode because UTF8 is not supported",
														e);
			return value;
		}
	}
}
