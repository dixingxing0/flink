package org.apache.flink.runtime.rest.handler.logconfig;

import org.apache.flink.api.common.time.Time;
import org.apache.flink.runtime.resourcemanager.ResourceManagerGateway;
import org.apache.flink.runtime.rest.handler.HandlerRequest;
import org.apache.flink.runtime.rest.handler.RestHandlerException;
import org.apache.flink.runtime.rest.handler.resourcemanager.AbstractResourceManagerHandler;
import org.apache.flink.runtime.rest.messages.EmptyMessageParameters;
import org.apache.flink.runtime.rest.messages.EmptyRequestBody;
import org.apache.flink.runtime.rest.messages.MessageHeaders;
import org.apache.flink.runtime.rest.messages.logconfig.LogConfigCancellationRequestBody;
import org.apache.flink.runtime.rest.messages.logconfig.LogConfigCancellationResponseBody;
import org.apache.flink.runtime.rest.messages.taskmanager.TaskManagerDetailsInfo;
import org.apache.flink.runtime.rest.messages.taskmanager.TaskManagerMessageParameters;
import org.apache.flink.runtime.webmonitor.RestfulGateway;
import org.apache.flink.runtime.webmonitor.retriever.GatewayRetriever;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class LogConfigCancellationHandler extends AbstractResourceManagerHandler<RestfulGateway, LogConfigCancellationRequestBody, LogConfigCancellationResponseBody, EmptyMessageParameters> {
	public LogConfigCancellationHandler(GatewayRetriever<? extends RestfulGateway> leaderRetriever, Time timeout, Map<String, String> responseHeaders, MessageHeaders<LogConfigCancellationRequestBody, LogConfigCancellationResponseBody, EmptyMessageParameters> messageHeaders, GatewayRetriever<ResourceManagerGateway> resourceManagerGatewayRetriever) {
		super(leaderRetriever, timeout, responseHeaders, messageHeaders, resourceManagerGatewayRetriever);
	}

	@Override
	protected CompletableFuture<LogConfigCancellationResponseBody> handleRequest(@Nonnull HandlerRequest<LogConfigCancellationRequestBody, EmptyMessageParameters> request, @Nonnull ResourceManagerGateway gateway) throws RestHandlerException {
		return null;
	}
}
