package org.asciidocgenerator.ui;

import java.io.IOException;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.asciidocgenerator.DokuGeneratorException;
import org.asciidocgenerator.DokuGeneratorException.ErrorCode;
import org.asciidocgenerator.Logged;

@Logged
public class NavigationStateFilter
		implements Filter {

	@Inject
	private Event<NavigationSelectedEvent> event;

	@Inject
	private URLEncoderService encoderService;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// nothing
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)	throws IOException,
																								ServletException {
		HttpServletRequest req = null;

		if (request instanceof HttpServletRequest) {
			req = (HttpServletRequest) request;
		} else {
			throw new DokuGeneratorException(ErrorCode.REQUESTED_NOT_HTTP);
		}

		PageService pageServive = new PageService(req);
		NavigationPathService navigationPathService = new NavigationPathService(pageServive.getRequestedRelativeUrl());

		NavigationSelectedEvent selectedEvent = navigationPathService.createNavigationSelectedEvent(encoderService::decode);

		try {
			event.fire(selectedEvent);
		} catch (DokuGeneratorException e) {
			((HttpServletResponse) response).sendError(HTMLErrorCodes.NOT_FOUND);
		}

		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		// nothing
	}
}
