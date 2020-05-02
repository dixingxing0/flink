package org.apache.flink.runtime.rest.messages.logconfig;

import org.apache.flink.runtime.rest.HttpMethodWrapper;
import org.apache.flink.runtime.rest.messages.EmptyMessageParameters;
import org.apache.flink.runtime.rest.messages.MessageHeaders;
import org.apache.flink.shaded.netty4.io.netty.handler.codec.http.HttpResponseStatus;

public class LogConfigHeaders  implements MessageHeaders<LogConfigRequestBody, LogConfigResponseBody, EmptyMessageParameters> {
	@Override
	public Class<LogConfigResponseBody> getResponseClass() {
		return null;
	}

	@Override
	public HttpResponseStatus getResponseStatusCode() {
		return null;
	}

	@Override
	public String getDescription() {
		return null;
	}

	@Override
	public Class<LogConfigRequestBody> getRequestClass() {
		return null;
	}

	@Override
	public EmptyMessageParameters getUnresolvedMessageParameters() {
		return null;
	}

	@Override
	public HttpMethodWrapper getHttpMethod() {
		return null;
	}

	@Override
	public String getTargetRestEndpointURL() {
		return null;
	}
}
