/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.flink.runtime.rest.handler.taskmanager;

import org.apache.flink.api.common.time.Time;
import org.apache.flink.runtime.resourcemanager.ResourceManagerGateway;
import org.apache.flink.runtime.rest.handler.HandlerRequest;
import org.apache.flink.runtime.rest.handler.RestHandlerException;
import org.apache.flink.runtime.rest.messages.EmptyRequestBody;
import org.apache.flink.runtime.rest.messages.EmptyResponseBody;
import org.apache.flink.runtime.rest.messages.MessageHeaders;
import org.apache.flink.runtime.rest.messages.log.Log4jMessageParameters;
import org.apache.flink.runtime.rest.messages.log.Log4jQueryParameter;
import org.apache.flink.runtime.rest.messages.taskmanager.TaskManagerInfo;
import org.apache.flink.runtime.rest.messages.taskmanager.TaskManagersInfo;
import org.apache.flink.runtime.util.log.LogLevelWorker;
import org.apache.flink.runtime.webmonitor.RestfulGateway;
import org.apache.flink.runtime.webmonitor.retriever.GatewayRetriever;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Handler for log4j dynamic log level setting.
 * <pre>
 * For job manager:
 * /log4j?params=jobManager|com.xxx.xxx|debug|2|info
 * </pre>
 *
 * <pre>
 * For task manager:
 * /log4j?params=taskManager|com.xxx.xxx|debug|2|info
 * </pre>
 */
public class Log4jHandler extends AbstractTaskManagerHandler<RestfulGateway, EmptyRequestBody, EmptyResponseBody, Log4jMessageParameters> {

	public Log4jHandler(GatewayRetriever<? extends RestfulGateway> leaderRetriever,
						Time timeout,
						Map<String, String> responseHeaders,
						MessageHeaders<EmptyRequestBody, EmptyResponseBody, Log4jMessageParameters> messageHeaders,
						GatewayRetriever<ResourceManagerGateway> resourceManagerGatewayRetriever) {
		super(leaderRetriever, timeout, responseHeaders, messageHeaders, resourceManagerGatewayRetriever);
	}

	@Override
	protected CompletableFuture<EmptyResponseBody> handleRequest(@Nonnull HandlerRequest<EmptyRequestBody, Log4jMessageParameters> request, @Nonnull ResourceManagerGateway gateway) throws RestHandlerException {
		List<String> params = request.getQueryParameter(Log4jQueryParameter.class);
		logger.info("Log level params : {}", params);
		if (!params.isEmpty()) {
			String paramString = params.get(0);
			LogLevelWorker worker = new LogLevelWorker(paramString);
			if (worker.forJobManager()) {
				logger.info("Changing log level for job manager : {}", worker);
				worker.changeLogLevel();
			} else if (worker.forTaskManager()) {
				logger.info("Changing log level for task manager : {}", worker);
				CompletableFuture<TaskManagersInfo> taskManagersInfoCompletableFuture = gateway
					.requestTaskManagerInfo(timeout)
					.thenApply(TaskManagersInfo::new);
				try {
					TaskManagersInfo taskManagersInfo = taskManagersInfoCompletableFuture.get();
					if (taskManagersInfo != null) {
						for (TaskManagerInfo taskManagerInfo : taskManagersInfo.getTaskManagerInfos()) {
							gateway.changeTaskManagerLogLevel(taskManagerInfo.getResourceId(), paramString);
							logger.info("Called task manager {} with param : {}", taskManagerInfo.getResourceId(), paramString);
						}
					}
				} catch (InterruptedException | ExecutionException e) {
					logger.error(e.getMessage(), e);
					e.printStackTrace();
				}
			} else {
				logger.warn("Unknown type : {}", worker.getType());
			}
		}
		return CompletableFuture.completedFuture(EmptyResponseBody.getInstance());
	}
}
