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

package org.apache.flink.runtime.rest.messages.log;

import org.apache.flink.runtime.rest.HttpMethodWrapper;
import org.apache.flink.runtime.rest.handler.taskmanager.Log4jHandler;
import org.apache.flink.runtime.rest.messages.EmptyRequestBody;
import org.apache.flink.runtime.rest.messages.EmptyResponseBody;
import org.apache.flink.runtime.rest.messages.MessageHeaders;
import org.apache.flink.shaded.netty4.io.netty.handler.codec.http.HttpResponseStatus;

/**
 * Message headers for the {@link Log4jHandler}.
 */
public final class Log4jHeaders implements MessageHeaders<EmptyRequestBody, EmptyResponseBody, Log4jMessageParameters> {

	private static final Log4jHeaders INSTANCE = new Log4jHeaders();

	public static final String LOG4J_REST_PATH = "/log4j";

	private Log4jHeaders() {
	}

	@Override
	public Class<EmptyRequestBody> getRequestClass() {
		return EmptyRequestBody.class;
	}


	@Override
	public HttpMethodWrapper getHttpMethod() {
		return HttpMethodWrapper.GET;
	}

	@Override
	public String getTargetRestEndpointURL() {
		return LOG4J_REST_PATH;
	}

	@Override
	public Class<EmptyResponseBody> getResponseClass() {
		return EmptyResponseBody.class;
	}

	@Override
	public HttpResponseStatus getResponseStatusCode() {
		return HttpResponseStatus.OK;
	}

	@Override
	public Log4jMessageParameters getUnresolvedMessageParameters() {
		return Log4jMessageParameters.getInstance();
	}

	@Override
	public boolean acceptsFileUploads() {
		return false;
	}

	public static Log4jHeaders getInstance() {
		return INSTANCE;
	}

	@Override
	public String getDescription() {
		return "Log4j dynamic log level setting.";
	}
}
