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

package cn.sliew.scaleph.engine.flink.kubernetes.resource.template;

import cn.sliew.scaleph.engine.flink.kubernetes.operator.spec.FlinkVersion;
import cn.sliew.scaleph.engine.flink.kubernetes.service.dto.WsFlinkKubernetesTemplateDTO;
import cn.sliew.scaleph.engine.flink.kubernetes.service.vo.KubernetesOptionsVO;
import cn.sliew.scaleph.kubernetes.Constant;
import cn.sliew.scaleph.kubernetes.resource.ResourceConverter;
import io.fabric8.kubernetes.api.model.ObjectMetaBuilder;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.util.StringUtils;

import java.util.Map;

public enum FlinkTemplateConverter implements ResourceConverter<WsFlinkKubernetesTemplateDTO, FlinkTemplate> {
    INSTANCE;

    @Override
    public FlinkTemplate convertTo(WsFlinkKubernetesTemplateDTO source) {
        FlinkTemplate template = new FlinkTemplate();
        ObjectMetaBuilder builder = new ObjectMetaBuilder(true);
        String name = StringUtils.hasText(source.getTemplateId()) ? source.getTemplateId() : source.getName();
        builder.withName(name);
        builder.withNamespace(source.getNamespace());
        builder.withLabels(Map.of(Constant.SCALEPH_NAME, source.getName()));
        template.setMetadata(builder.build());
        FlinkTemplateSpec spec = new FlinkTemplateSpec();
        KubernetesOptionsVO kuberenetesOptions = source.getKubernetesOptions();
        if (kuberenetesOptions != null) {
            spec.setImage(kuberenetesOptions.getImage());
            spec.setImagePullPolicy(kuberenetesOptions.getImagePullPolicy());
            spec.setServiceAccount(kuberenetesOptions.getServiceAccount());
            spec.setFlinkVersion(EnumUtils.getEnum(FlinkVersion.class, kuberenetesOptions.getFlinkVersion()));
        }
        spec.setFlinkConfiguration(source.getFlinkConfiguration());
        spec.setJobManager(source.getJobManager());
        spec.setTaskManager(source.getTaskManager());
        spec.setPodTemplate(source.getPodTemplate());
        spec.setIngress(source.getIngress());
        template.setSpec(spec);
        return template;
    }

    @Override
    public WsFlinkKubernetesTemplateDTO convertFrom(FlinkTemplate target) {
        WsFlinkKubernetesTemplateDTO dto = new WsFlinkKubernetesTemplateDTO();
        String name = target.getMetadata().getName();
        if (target.getMetadata().getLabels() != null) {
            Map<String, String> labels = target.getMetadata().getLabels();
            name = labels.computeIfAbsent(Constant.SCALEPH_NAME, key -> target.getMetadata().getName());
        }
        dto.setName(name);
        dto.setTemplateId(target.getMetadata().getName());
        dto.setNamespace(target.getMetadata().getNamespace());
        FlinkTemplateSpec spec = target.getSpec();
        KubernetesOptionsVO kuberenetesOptions = new KubernetesOptionsVO();
        if (kuberenetesOptions != null) {
            kuberenetesOptions.setImage(spec.getImage());
            kuberenetesOptions.setImagePullPolicy(spec.getImagePullPolicy());
            kuberenetesOptions.setServiceAccount(spec.getServiceAccount());
            if (spec.getFlinkVersion() != null) {
                kuberenetesOptions.setFlinkVersion(spec.getFlinkVersion().name());
            }
        }
        dto.setKubernetesOptions(kuberenetesOptions);
        dto.setFlinkConfiguration(spec.getFlinkConfiguration());
        dto.setJobManager(spec.getJobManager());
        dto.setTaskManager(spec.getTaskManager());
        dto.setPodTemplate(spec.getPodTemplate());
        dto.setIngress(spec.getIngress());
        return dto;
    }
}
