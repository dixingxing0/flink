package org.apache.flink.runtime.rest.messages.logconfig;

import org.apache.flink.runtime.rest.messages.RequestBody;

public class LogConfigRequestBody implements RequestBody {
	//	{
//		"instanceType":"TM",
//		"taskManagerId":"container_e02_1576582691395_4999_01_000004",
//		"logger":"org.apache.flink.runtime.jobgraph.JobGraph",
//		"targetLevel":"DEBUG",
//		"timeout":"30",
//		"resetLevel":"INFO"
//	}
	private String instanceType;
	private String taskManagerId;
	private String logger;
	private String targetLevel;
	private int timeout;
	private String resetLevel;

}
