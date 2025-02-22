import {useIntl} from "umi";
import React from "react";
import {ProCard, ProFormSelect, ProFormText, ProFormTextArea} from "@ant-design/pro-components";
import {WsFlinkKubernetesTemplateService} from "@/services/project/WsFlinkKubernetesTemplateService";
import {WsFlinkKubernetesTemplateParam} from "@/services/project/typings";
import {ClusterCredentialService} from "@/services/resource/clusterCredential.service";
import {ClusterCredentialListParam} from "@/services/resource/typings";
import {WORKSPACE_CONF} from "@/constant";
import {connect} from "umi";
import {DeploymentKind} from "@/constants/enum";

const SessionClusterClusterStepForm: React.FC = (props: any) => {
  const intl = useIntl();
  const projectId = localStorage.getItem(WORKSPACE_CONF.projectId);

  return (
    <ProCard>
      <ProFormText
        name="name"
        label={intl.formatMessage({id: 'pages.project.flink.kubernetes.session-cluster.name'})}
        rules={[{required: true}]}
      />
      <ProFormSelect
        name="clusterCredentialId"
        label={intl.formatMessage({id: 'pages.project.flink.kubernetes.session-cluster.steps.cluster.cluster'})}
        rules={[{required: true}]}
        allowClear={false}
        request={((params, props) => {
          const param: ClusterCredentialListParam = {
            name: params.keyWords,
          };
          return ClusterCredentialService.list(param).then((response) => {
            return response.data.map((item) => {
              return {label: item.name, value: item.id, item: item}
            })
          })
        })}
      />
      <ProFormText
        name="namespace"
        label={intl.formatMessage({id: 'pages.project.flink.kubernetes.session-cluster.namespace'})}
        rules={[{required: true}]}
        initialValue={"default"}
      />
      <ProFormSelect
        name="templateId"
        label={intl.formatMessage({id: 'pages.project.flink.kubernetes.session-cluster.steps.cluster.template'})}
        rules={[{required: true}]}
        allowClear={false}
        request={((params, props) => {
          const param: WsFlinkKubernetesTemplateParam = {
            projectId: projectId,
            name: params.keyWords,
            deploymentKind: DeploymentKind.SessionCluster
          };
          return WsFlinkKubernetesTemplateService.list(param).then((response) => {
            return response.data.map((item) => {
              return {label: item.name, value: item.id, item: item}
            })
          })
        })}
      />
      <ProFormTextArea
        name="remark"
        label={intl.formatMessage({id: 'app.common.data.remark'})}
      />
    </ProCard>
  )
}

const mapModelToProps = ({sessionClusterStep2}: any) => ({sessionClusterStep2})
export default connect(mapModelToProps)(SessionClusterClusterStepForm);
