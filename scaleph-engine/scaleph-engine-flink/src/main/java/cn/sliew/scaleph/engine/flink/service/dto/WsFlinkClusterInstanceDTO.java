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

package cn.sliew.scaleph.engine.flink.service.dto;

import cn.sliew.scaleph.common.dict.flink.FlinkClusterStatus;
import cn.sliew.scaleph.system.model.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * flink 集群实例
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "FlinkClusterConfig对象", description = "flink 集群配置")
public class WsFlinkClusterInstanceDTO extends BaseDTO {

    @NotNull
    @Schema(description = "项目ID")
    private Long projectId;

    @NotNull
    @Schema(description = "集群配置")
    private Long flinkClusterConfigId;

    @Schema(description = "集群名称")
    private String name;

    @Schema(description = "集群id")
    private String clusterId;

    @Schema(description = "集群 web-ui 链接")
    private String webInterfaceUrl;

    @Schema(description = "集群状态。0: 已创建, 1: 运行中, 2: 停止")
    private FlinkClusterStatus status;

}
