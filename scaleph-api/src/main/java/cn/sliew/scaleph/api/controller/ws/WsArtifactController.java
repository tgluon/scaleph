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

package cn.sliew.scaleph.api.controller.ws;

import cn.sliew.scaleph.api.annotation.Logging;
import cn.sliew.scaleph.common.exception.ScalephException;
import cn.sliew.scaleph.project.service.WsFlinkArtifactService;
import cn.sliew.scaleph.project.service.dto.WsFlinkArtifactDTO;
import cn.sliew.scaleph.project.service.param.WsFlinkArtifactListParam;
import cn.sliew.scaleph.system.model.ResponseVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "Flink管理-artifact管理")
@RestController
@RequestMapping(path = "/api/flink/artifact")
public class WsArtifactController {

    @Autowired
    private WsFlinkArtifactService wsFlinkArtifactService;

    @Logging
    @GetMapping
    @Operation(summary = "查询 artifact 列表", description = "查询 artifact 列表")
    public ResponseEntity<Page<WsFlinkArtifactDTO>> list(@Valid WsFlinkArtifactListParam param) {
        final Page<WsFlinkArtifactDTO> result = wsFlinkArtifactService.list(param);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Logging
    @PutMapping
    @Operation(summary = "新增 artifact", description = "新增 artifact")
    public ResponseEntity<ResponseVO> insert(@Valid @RequestBody WsFlinkArtifactDTO param) {
        wsFlinkArtifactService.insert(param);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @PostMapping
    @Operation(summary = "修改 artifact", description = "修改 artifact")
    public ResponseEntity<ResponseVO> update(@Valid @RequestBody WsFlinkArtifactDTO param) {
        wsFlinkArtifactService.update(param);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping("{id}")
    @Operation(summary = "删除 artifact", description = "删除 artifact")
    public ResponseEntity<ResponseVO> deleteById(@PathVariable("id") Long id) throws ScalephException {
        wsFlinkArtifactService.deleteById(id);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

}
