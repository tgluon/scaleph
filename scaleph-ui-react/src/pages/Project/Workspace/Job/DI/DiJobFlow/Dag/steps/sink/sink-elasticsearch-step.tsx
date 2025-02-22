import {ModalFormProps} from '@/app.d';
import {WsDiJobService} from '@/services/project/WsDiJobService';
import {WsDiJob} from '@/services/project/typings';
import {ProForm, ProFormDigit, ProFormGroup, ProFormList, ProFormText} from '@ant-design/pro-components';
import {NsGraph} from '@antv/xflow';
import {Button, Drawer, Form, message} from 'antd';
import {useEffect} from 'react';
import {getIntl, getLocale} from 'umi';
import {ElasticsearchParams, STEP_ATTR_TYPE} from '../../constant';
import DataSourceItem from "@/pages/Project/Workspace/Job/DI/DiJobFlow/Dag/steps/dataSource";
import {StepSchemaService} from "@/pages/Project/Workspace/Job/DI/DiJobFlow/Dag/steps/helper";

const SinkElasticsearchStepForm: React.FC<ModalFormProps<{
  node: NsGraph.INodeConfig;
  graphData: NsGraph.IGraphData;
  graphMeta: NsGraph.IGraphMeta;
}>> = ({data, visible, onCancel, onOK}) => {
  const nodeInfo = data.node.data;
  const jobInfo = data.graphMeta.origin as WsDiJob;
  const jobGraph = data.graphData;
  const intl = getIntl(getLocale(), true);
  const [form] = Form.useForm();
  useEffect(() => {
    form.setFieldValue(STEP_ATTR_TYPE.stepTitle, nodeInfo.data.displayName);
  }, []);

  return (
    <Drawer
      open={visible}
      title={nodeInfo.data.displayName}
      width={780}
      bodyStyle={{overflowY: 'scroll'}}
      destroyOnClose={true}
      onClose={onCancel}
      extra={
        <Button
          type="primary"
          onClick={() => {
            form.validateFields().then((values) => {
              let map: Map<string, any> = new Map();
              map.set(STEP_ATTR_TYPE.jobId, jobInfo.id);
              map.set(STEP_ATTR_TYPE.jobGraph, JSON.stringify(jobGraph));
              map.set(STEP_ATTR_TYPE.stepCode, nodeInfo.id);
              StepSchemaService.formatEsPrimaryKeys(values);
              map.set(STEP_ATTR_TYPE.stepAttrs, values);
              WsDiJobService.saveStepAttr(map).then((resp) => {
                if (resp.success) {
                  message.success(intl.formatMessage({id: 'app.common.operate.success'}));
                  onOK ? onOK(values) : null;
                }
              });
            });
          }}
        >
          {intl.formatMessage({id: 'app.common.operate.confirm.label'})}
        </Button>
      }
    >
      <ProForm form={form} initialValues={nodeInfo.data.attrs} grid={true} submitter={false}>
        <ProFormText
          name={STEP_ATTR_TYPE.stepTitle}
          label={intl.formatMessage({id: 'pages.project.di.step.stepTitle'})}
          rules={[{required: true}, {max: 120}]}
        />
        <DataSourceItem dataSource={'Elasticsearch'}/>
        <ProFormText
          name={ElasticsearchParams.index}
          label={intl.formatMessage({id: 'pages.project.di.step.elasticsearch.index'})}
          rules={[{required: true}]}
        />
        <ProFormGroup
          label={intl.formatMessage({id: 'pages.project.di.step.elasticsearch.primaryKeys'})}
        >
          <ProFormList
            name={ElasticsearchParams.primaryKeyArray}
            copyIconProps={false}
            creatorButtonProps={{
              creatorButtonText: intl.formatMessage({
                id: 'pages.project.di.step.elasticsearch.primaryKeys.list',
              }),
              type: 'text',
            }}
          >
            <ProFormText name={ElasticsearchParams.primaryKey} colProps={{span: 16, offset: 4}}/>
          </ProFormList>
        </ProFormGroup>

        <ProFormDigit
          name={ElasticsearchParams.maxRetrySize}
          label={intl.formatMessage({id: 'pages.project.di.step.elasticsearch.maxRetrySize'})}
          initialValue={0}
          fieldProps={{
            min: 0,
          }}
        />
        <ProFormDigit
          name={ElasticsearchParams.maxBatchSize}
          label={intl.formatMessage({id: 'pages.project.di.step.elasticsearch.maxBatchSize'})}
          initialValue={1000}
          fieldProps={{
            step: 1000,
            min: 0,
          }}
        />
      </ProForm>
    </Drawer>
  );
};

export default SinkElasticsearchStepForm;
