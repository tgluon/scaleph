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

package cn.sliew.scaleph.project.service.dto;

import cn.sliew.scaleph.common.dict.flink.FlinkJobType;
import cn.sliew.scaleph.system.model.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "FlinkArtifact对象", description = "flink artifact")
public class WsFlinkArtifactDTO extends BaseDTO {

    @NotNull
    @Schema(description = "项目id")
    private Long projectId;

    @Schema(description = "`type`")
    private FlinkJobType type;

    @NotBlank
    @Length(max = 200)
    @Schema(description = "名称")
    private String name;

    @Schema(description = "备注")
    private String remark;
}
