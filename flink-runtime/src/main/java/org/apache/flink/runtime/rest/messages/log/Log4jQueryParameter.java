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

import org.apache.flink.runtime.rest.messages.MessageQueryParameter;

public class Log4jQueryParameter extends MessageQueryParameter<String> {

	// type|loggerName|level|expireSecs
	// type : jobManager,taskManager
	// loggerName : com.autohome.xxx
	// level : trace, debug, info, warn, error, fatal
	// expireSecs : 回退回info级别
	private static final String KEY = "params";

	public Log4jQueryParameter() {
		super(KEY, MessageParameterRequisiteness.OPTIONAL);
	}

	@Override
	public String convertStringToValue(final String value) {
		return value;
	}

	@Override
	public String convertValueToString(final String value) {
		return value;
	}

	@Override
	public String getDescription() {
		return "log4j manager";
	}
}
