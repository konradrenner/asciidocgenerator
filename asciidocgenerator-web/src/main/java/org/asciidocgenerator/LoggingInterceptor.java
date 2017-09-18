package org.asciidocgenerator;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@Logged
@Interceptor
public class LoggingInterceptor {

	private final static Logger LOG = Logger.getLogger("asciidoctorgenerator-web");

	@AroundInvoke
	public Object log(InvocationContext ctx) throws Exception {
		long start = System.currentTimeMillis();
		LOG.log(Level.FINEST, startMsg(ctx));
		LOG.log(Level.FINEST, paramsMsg(ctx));
		try {
			return ctx.proceed();
		} catch (Exception e) {
			LOG.log(Level.WARNING, exceptionMsg(ctx, e));
			throw e;
		} finally {
			LOG.log(Level.FINEST, endMsg(ctx, start));
		}
	}

	private String evaluateClassName(InvocationContext ctx) {
		if (ctx.getTarget() == null) {
			return ctx.getMethod().getDeclaringClass().getSimpleName();
		}
		return ctx.getTarget().getClass().getSimpleName().toString();
	}

	private String startMsg(InvocationContext ctx) {
		StringBuilder sb = create(ctx);
		sb.append("start method");
		return sb.toString();
	}

	private String paramsMsg(InvocationContext ctx) {
		StringBuilder sb = create(ctx);
		sb.append("Parameters: ");
		sb.append(Arrays.toString(ctx.getParameters()));
		return sb.toString();
	}

	private String endMsg(InvocationContext ctx, long millis) {
		StringBuilder sb = create(ctx);
		sb.append("end method, duration: " + (System.currentTimeMillis() - millis) + "ms");
		return sb.toString();
	}

	private String exceptionMsg(InvocationContext ctx, Throwable t) {
		StringBuilder sb = create(ctx);
		sb.append("Exception: ");
		Arrays.stream(t.getStackTrace()).forEach(stackElement -> sb.append(stackElement.toString()));
		return sb.toString();
	}

	private StringBuilder create(InvocationContext ctx) {
		StringBuilder sb = new StringBuilder(evaluateClassName(ctx));
		sb.append(".");
		sb.append(ctx.getMethod().getName());
		sb.append(" - ");
		return sb;
	}
}
