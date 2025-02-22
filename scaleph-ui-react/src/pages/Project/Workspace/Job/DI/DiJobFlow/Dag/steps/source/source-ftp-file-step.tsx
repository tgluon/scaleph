import {NsGraph} from '@antv/xflow';
import {ModalFormProps} from '@/app.d';
import {WsDiJobService} from '@/services/project/WsDiJobService';
import {Button, Drawer, Form, message} from 'antd';
import {WsDiJob} from '@/services/project/typings';
import {getIntl, getLocale} from 'umi';
import {ProForm, ProFormSelect, ProFormSwitch, ProFormText, ProFormTextArea} from '@ant-design/pro-components';
import {useEffect} from 'react';
import {BaseFileParams, STEP_ATTR_TYPE} from '../../constant';
import {StepSchemaService} from '../helper';
import DataSourceItem from "@/pages/Project/Workspace/Job/DI/DiJobFlow/Dag/steps/dataSource";
import SchemaItem from "@/pages/Project/Workspace/Job/DI/DiJobFlow/Dag/steps/schema";

const SourceFtpFileStepForm: React.FC<ModalFormProps<{
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
              StepSchemaService.formatSchema(values);
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
        <DataSourceItem dataSource={'Ftp'}/>
        <ProFormText
          name={BaseFileParams.path}
          label={intl.formatMessage({id: 'pages.project.di.step.baseFile.path'})}
          rules={[{required: true}]}
        />
        <ProFormTextArea
          name={BaseFileParams.readColumns}
          label={intl.formatMessage({id: 'pages.project.di.step.baseFile.readColumns'})}
        />
        <SchemaItem/>
        <ProFormSwitch
          name={BaseFileParams.parsePartitionFromPath}
          label={intl.formatMessage({
            id: 'pages.project.di.step.baseFile.parsePartitionFromPath',
          })}
          initialValue={true}
        />
        <ProFormSelect
          name={BaseFileParams.dateFormat}
          label={intl.formatMessage({id: 'pages.project.di.step.baseFile.dateFormat'})}
          initialValue={'yyyy-MM-dd'}
          options={['yyyy-MM-dd', 'yyyy.MM.dd', 'yyyy/MM/dd']}
        />
        <ProFormSelect
          name={BaseFileParams.timeFormat}
          label={intl.formatMessage({id: 'pages.project.di.step.baseFile.timeFormat'})}
          initialValue={'HH:mm:ss'}
          options={['HH:mm:ss', 'HH:mm:ss.SSS']}
        />
        <ProFormSelect
          name={BaseFileParams.datetimeFormat}
          label={intl.formatMessage({id: 'pages.project.di.step.baseFile.datetimeFormat'})}
          initialValue={'yyyy-MM-dd HH:mm:ss'}
          options={[
            'yyyy-MM-dd HH:mm:ss',
            'yyyy.MM.dd HH:mm:ss',
            'yyyy/MM/dd HH:mm:ss',
            'yyyyMMddHHmmss',
          ]}
        />
      </ProForm>
    </Drawer>
  );
};

export default SourceFtpFileStepForm;
