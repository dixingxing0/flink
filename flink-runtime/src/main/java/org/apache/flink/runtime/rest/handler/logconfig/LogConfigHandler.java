package org.apache.flink.runtime.rest.handler.logconfig;

import org.apache.flink.api.common.time.Time;
import org.apache.flink.runtime.logconfig.LogConfigManager;
import org.apache.flink.runtime.logconfig.LogConfigManagerFactory;
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

	private LogConfigManagerFactory logConfigManagerFactory = new LogConfigManagerFactory();
	@Override
	protected CompletableFuture<LogConfigResponseBody> handleRequest(@Nonnull HandlerRequest<LogConfigRequestBody, EmptyMessageParameters> request, @Nonnull ResourceManagerGateway gateway) throws RestHandlerException {

		LogConfigManager manager = null;
		String loggerFactoryClassStr = null;
		try {
			loggerFactoryClassStr = LogConfigManagerFactory.getLoggerFactoryClassStr();
			manager = logConfigManagerFactory.createManager();
		} catch (Throwable e) {
			log.error(e.getMessage(), e);
		}
		if (manager == null) {
			LogConfigResponseBody response = new LogConfigResponseBody();
			response.setStatus(LogConfigResponseBody.STATUS_ERROR);
			response.setMessage(String.format("Detect current logging backend is '%s' which do not support dynamic log level setting", loggerFactoryClassStr));
			return CompletableFuture.completedFuture(response);
		}
		final LogConfigManager finalLogConfigManager = manager;
		return CompletableFuture.completedFuture(finalLogConfigManager.changeLogLevel(logConfig));
	}
}
