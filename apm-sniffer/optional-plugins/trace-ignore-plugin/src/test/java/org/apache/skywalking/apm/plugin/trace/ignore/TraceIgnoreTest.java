/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.apache.skywalking.apm.plugin.trace.ignore;

import org.apache.skywalking.apm.agent.core.boot.ServiceManager;
import org.apache.skywalking.apm.agent.core.context.AbstractTracerContext;
import org.apache.skywalking.apm.agent.core.context.ContextManagerExtendService;
import org.apache.skywalking.apm.agent.core.context.IgnoredTracerContext;
import org.apache.skywalking.apm.agent.core.context.TracingContext;
import org.apache.skywalking.apm.agent.test.tools.AgentServiceRule;
import org.apache.skywalking.apm.plugin.trace.ignore.conf.IgnoreConfig;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author liujc [liujunc1993@163.com]
 */
public class TraceIgnoreTest {

    @Rule
    public AgentServiceRule serviceRule = new AgentServiceRule();

    @Test
    public void testServiceOverrideFromPlugin() {
        ContextManagerExtendService service = ServiceManager.INSTANCE.findService(ContextManagerExtendService.class);
        Assert.assertEquals(TraceIgnoreExtendService.class, service.getClass());
    }

    @Test
    public void testTraceIgnore() {
        ContextManagerExtendService service = ServiceManager.INSTANCE.findService(ContextManagerExtendService.class);
        IgnoreConfig.Trace.IGNORE_PATH = "/eureka/**";
        AbstractTracerContext ignoredTracerContext = service.createTraceContext("/eureka/apps", false);
        Assert.assertEquals(IgnoredTracerContext.class, ignoredTracerContext.getClass());

        AbstractTracerContext traceContext = service.createTraceContext("/consul/apps", false);
        Assert.assertEquals(TracingContext.class, traceContext.getClass());
    }

}
