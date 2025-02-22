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
import cn.sliew.scaleph.common.util.I18nUtil;
import cn.sliew.scaleph.common.util.PropertyUtil;
import cn.sliew.scaleph.meta.service.MetaDatasourceService;
import cn.sliew.scaleph.meta.service.dto.MetaDatasourceDTO;
import cn.sliew.scaleph.meta.service.param.MetaDatasourceParam;
import cn.sliew.scaleph.plugin.framework.property.Property;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;
import cn.sliew.scaleph.system.model.ResponseVO;
import cn.sliew.scaleph.system.service.vo.DictVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * @author gleiyu
 */
@Deprecated
@Tag(name = "数据开发-数据源")
@RestController
@RequestMapping(path = {"/api/datadev/datasource", "/api/di/datasource"})
public class DataSourceController {

    @Autowired
    private MetaDatasourceService metaDatasourceService;

    @Logging
    @GetMapping
    @Operation(summary = "分页查询数据源", description = "分页查询数据源信息")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DATADEV_DATASOURCE_SELECT)")
    public ResponseEntity<Page<MetaDatasourceDTO>> listDataSource(MetaDatasourceParam param) {
        Page<MetaDatasourceDTO> page = this.metaDatasourceService.selectPage(param);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @Logging
    @GetMapping(path = "type/{type}")
    @Operation(summary = "按类型查询数据源列表", description = "按类型查询数据源信息")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DATADEV_DATASOURCE_SELECT)")
    public ResponseEntity<List<DictVO>> listDataSourceByType(@PathVariable("type") String type) {
        List<DictVO> dsList = new ArrayList<>();
        if (StringUtils.hasText(type)) {
            List<MetaDatasourceDTO> list = this.metaDatasourceService.listByType(type);
            for (MetaDatasourceDTO dto : list) {
                DictVO d = new DictVO(String.valueOf(dto.getId()), dto.getDatasourceName());
                dsList.add(d);
            }
        }
        return new ResponseEntity<>(dsList, HttpStatus.OK);
    }

    @Logging
    @PostMapping
    @Operation(summary = "新增数据源", description = "新增数据源")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DATADEV_DATASOURCE_ADD)")
    public ResponseEntity<ResponseVO> addDataSource(@Validated @RequestBody MetaDatasourceDTO metaDatasourceDTO) {
        if (StringUtils.hasText(metaDatasourceDTO.getAdditionalPropsStr())) {
            metaDatasourceDTO.setAdditionalProps(
                    PropertyUtil.formatPropFromStr(
                            metaDatasourceDTO.getAdditionalPropsStr(), "\n", ":")
            );
        }
        this.metaDatasourceService.insert(metaDatasourceDTO);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.CREATED);
    }

    @Logging
    @PutMapping
    @Operation(summary = "修改数据源", description = "修改数据源")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DATADEV_DATASOURCE_EDIT)")
    public ResponseEntity<ResponseVO> editDataSource(@Validated @RequestBody MetaDatasourceDTO metaDatasourceDTO) {
        if (StringUtils.hasText(metaDatasourceDTO.getAdditionalPropsStr())) {
            metaDatasourceDTO.setAdditionalProps(
                    PropertyUtil.formatPropFromStr(
                            metaDatasourceDTO.getAdditionalPropsStr(), "\n", ":")
            );
        }
        this.metaDatasourceService.update(metaDatasourceDTO);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @DeleteMapping(path = "/{id}")
    @Operation(summary = "删除数据源", description = "删除数据源")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DATADEV_DATASOURCE_DELETE)")
    public ResponseEntity<ResponseVO> deleteDataSource(@PathVariable("id") Long id) {
        this.metaDatasourceService.deleteById(id);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @PostMapping(path = "/batch")
    @Operation(summary = "批量删除数据源", description = "批量删除数据源")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DATADEV_DATASOURCE_DELETE)")
    public ResponseEntity<ResponseVO> deleteDataSource(@RequestBody List<Long> ids) {
        this.metaDatasourceService.deleteBatch(ids);
        return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
    }

    @Logging
    @GetMapping(path = "/passwd/{id}")
    @Operation(summary = "查看数据源密码", description = "查看数据源明文密码")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DATADEV_DATASOURCE_SECURITY)")
    public ResponseEntity<ResponseVO> showPassword(@PathVariable("id") Long id) throws Exception {
        MetaDatasourceDTO metaDatasourceDTO = this.metaDatasourceService.selectOne(id, false);
        String pluginName = metaDatasourceDTO.getDatasourceType().getValue();
        List<PropertyDescriptor> propDescList = this.metaDatasourceService.getSupportedProperties(pluginName);
        StringBuffer buffer = new StringBuffer();
        for (PropertyDescriptor prop : propDescList) {
            EnumSet<Property> propEnumSet = prop.getProperties();
            if (propEnumSet.contains(Property.Sensitive)) {
                String value = (String) metaDatasourceDTO.getProps().get(prop.getName());
                buffer.append(I18nUtil.get("datadev.datasource.props." + prop.getName()))
                        .append(":")
                        .append(value)
                        .append("\n");
            }
        }
        return new ResponseEntity<>(ResponseVO.success(buffer.toString()), HttpStatus.OK);
    }

    @Logging
    @PostMapping("/test")
    @Operation(summary = "测试数据源连通性", description = "测试数据源连通性")
    @PreAuthorize("@svs.validate(T(cn.sliew.scaleph.common.constant.PrivilegeConstants).DATADEV_DATASOURCE_SELECT)")
    public ResponseEntity<ResponseVO> connectionTest(@Validated @RequestBody MetaDatasourceDTO metaDatasourceDTO) throws Exception {
        try {
            if (StringUtils.hasText(metaDatasourceDTO.getAdditionalPropsStr())) {
                metaDatasourceDTO.setAdditionalProps(
                        PropertyUtil.formatPropFromStr(
                                metaDatasourceDTO.getAdditionalPropsStr(), "\n", ":")
                );
            }
            if (!metaDatasourceDTO.getPasswdChanged()) {
                MetaDatasourceDTO oldDsInfo = this.metaDatasourceService.selectOne(metaDatasourceDTO.getId(), false);
                metaDatasourceDTO = oldDsInfo;
            }
            this.metaDatasourceService.testConnection(metaDatasourceDTO);
            return new ResponseEntity<>(ResponseVO.success(), HttpStatus.OK);
        } catch (Exception e) {
            throw new ScalephException(e.getMessage(), e);
        }
    }
}
