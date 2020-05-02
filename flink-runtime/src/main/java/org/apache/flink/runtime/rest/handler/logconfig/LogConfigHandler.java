package org.apache.flink.runtime.rest.handler.logconfig;

import org.apache.flink.api.common.time.Time;
import org.apache.flink.runtime.resourcemanager.ResourceManagerGateway;
import org.apache.flink.runtime.rest.handler.HandlerRequest;
import org.apache.flink.runtime.rest.handler.RestHandlerException;
import org.apache.flink.runtime.rest.handler.resourcemanager.AbstractResourceManagerHandler;
import org.apache.flink.runtime.rest.messages.EmptyMessageParameters;
import org.apache.flink.runtime.rest.messages.MessageHeaders;
import org.apache.flink.runtime.rest.messages.logconfig.LogConfigRequestBody;
import org.apache.flink.runtime.rest.messages.logconfig.LogConfigResponseBody;
import org.apache.flink.runtime.webmonitor.RestfulGateway;
import org.apache.flink.runtime.webmonitor.retriever.GatewayRetriever;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class LogConfigHandler extends AbstractResourceManagerHandler<RestfulGateway, LogConfigRequestBody, LogConfigResponseBody, EmptyMessageParameters> {
	public LogConfigHandler(GatewayRetriever<? extends RestfulGateway> leaderRetriever, Time timeout, Map<String, String> responseHeaders, MessageHeaders<LogConfigRequestBody, LogConfigResponseBody, EmptyMessageParameters> messageHeaders, GatewayRetriever<ResourceManagerGateway> resourceManagerGatewayRetriever) {
		super(leaderRetriever, timeout, responseHeaders, messageHeaders, resourceManagerGatewayRetriever);
	}

	@Override
	protected CompletableFuture<LogConfigResponseBody> handleRequest(@Nonnull HandlerRequest<LogConfigRequestBody, EmptyMessageParameters> request, @Nonnull ResourceManagerGateway gateway) throws RestHandlerException {
		return null;
	}
}
