<template>
  <page-header-wrapper>
    <a-button class="editable-add-btn" @click="handleAdd"> 添加 </a-button>
    <s-table
      ref="table"
      size="default"
      rowKey="seqId"
      :columns="columns"
      :data="loadData"
      showPagination="auto"
    >
      <a-switch
        slot="status"
        slot-scope="text, record"
        checked-children="开"
        un-checked-children="关"
        :checked="record.status == 1"
        @change="switchStatus(record)"
      />
      <span slot="operation" slot-scope="text, record">
        <template>
          <a-popconfirm
            title="删除此任务？"
            cancelText="取消"
            okText="确定"
            @confirm="() => onDelete(record.seqId)"
          >
            <a-button type="danger" size="small">删除</a-button>
          </a-popconfirm>
          <a-divider type="vertical" />
          <a-button
            @click="handleEdit(record)"
            type="primary"
            size="small"
          >配置任务</a-button
          >
        </template>
      </span>
    </s-table>
    <a-modal
      title="新增流水线"
      :visible="addVisible"
      :confirm-loading="confirmLoading"
      @cancel="handleCancel"
    >
      <template slot="footer">
        <a-button key="cancel" @click="handleCancel"> 取消 </a-button>
        <a-button key="submit" type="primary" @click="handleOk">
          保存
        </a-button>
      </template>
      <a-form-model
        ref="dynamicValidateForm"
        :model="dynamicValidateForm"
        v-bind="formItemLayoutWithOutLabel"
      >
        <a-form-model-item label="流水线描述">
          <a-input
            v-model="dynamicValidateForm.name"
            placeholder="流水线描述"
          />
        </a-form-model-item>
        <a-form-model-item label="Cron">
          <a-input
            v-model="dynamicValidateForm.cron"
            placeholder="Cron表达式"
          />
        </a-form-model-item>
      </a-form-model>
    </a-modal>
    <a-modal
      title="选择任务"
      :visible="selectVisible"
      @cancel="closeTaskSelect"
    >
      <template slot="footer">
        <a-button key="cancel" @click="closeTaskSelect"> 取消 </a-button>
        <a-button key="submit" type="primary" @click="addTask"> 保存 </a-button>
      </template>
      <a-select
        show-search
        placeholder="选择任务"
        option-filter-prop="children"
        style="width: 200px"
        :filter-option="filterOption"
        @change="chooseTask"
      >
        <a-select-option v-for="d in taskOptions" :key="d.seqId">
          {{ d.name }}
        </a-select-option>
      </a-select>
    </a-modal>
    <a-modal
      title="任务详情"
      :visible="visible"
      :confirm-loading="confirmLoading"
      :footer="null"
      @cancel="handleCancel"
    >
      <a-form-model
        ref="dynamicValidateForm"
        :model="dynamicValidateForm"
        v-bind="formItemLayoutWithOutLabel"
      >
        <a-form-model-item>
          <a-input style="display: none" :value="dynamicValidateForm['seqId']" />
        </a-form-model-item>
        <a-form-model-item label="流水线描述">
          <a-input
            v-model="dynamicValidateForm.name"
            placeholder="流水线描述"
            :disabled="1==1"
          />
        </a-form-model-item>
        <a-form-model-item label="Cron">
          <a-input
            v-model="dynamicValidateForm.cron"
            placeholder="Cron表达式"
            :disabled="1==1"
          />
        </a-form-model-item>
        <a-form-model-item
          v-for="(domain, index) in dynamicValidateForm.tasks"
          :key="domain.ptid"
          :label="index === 0 ? '任务' : ''"
          :prop="'tasks.' + index + '.name'"
          :rules="{
            required: true,
            message: '至少添加一个任务',
            trigger: 'blur',
          }"
        >
          <a-input
            v-model="domain.name"
            placeholder="请选择任务"
            style="width: 60%; margin-right: 8px"
          />
          <a-icon
            v-if="dynamicValidateForm.tasks.length > 0"
            class="dynamic-delete-button"
            type="minus-circle-o"
            @click="removeDomain(domain)"
          />
        </a-form-model-item>
        <a-form-model-item v-bind="formItemLayoutWithOutLabel">
          <a-button type="dashed" style="width: 60%" @click="openTaskSelect">
            <a-icon type="plus" /> 增加任务
          </a-button>
        </a-form-model-item>
      </a-form-model>
    </a-modal>
  </page-header-wrapper>
</template>
<script>
import { STable } from '@/components'
import { getPipelineList, getPipelineTask, getAllTask, switchPipelineStatus, savePipeline, delPipeline, delPipelineTask, savePipelineTask } from '@/api/task'
export default {
  components: {
    STable
  },
  data () {
    return {
      visible: false,
      selectVisible: false,
      addVisible: false,
      confirmLoading: false,
      queryParam: {},
      loadData: (parameter) => {
        const requestParameters = Object.assign({}, parameter, this.queryParam)
        console.log('loadData request parameters:', requestParameters)
        return getPipelineList(requestParameters).then((res) => {
          return res
        })
      },
      formItemLayout: {
        labelCol: {
          xs: { span: 24 },
          sm: { span: 4 }
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 20 }
        }
      },
      formItemLayoutWithOutLabel: {
        wrapperCol: {
          xs: { span: 24, offset: 0 },
          sm: { span: 24, offset: 0 }
        }
      },
      taskOptions: [],
      dynamicValidateForm: {
        seqId: Number,
        name: String,
        cron: String,
        tasks: Array
      },
      taskSelected: {},
      columns: [
        {
          title: 'ID',
          dataIndex: 'seqId',
          width: '10%'
        },
        {
          title: '流水线描述',
          dataIndex: 'name',
          width: '25%'
        },
        {
          title: 'Cron',
          dataIndex: 'cron',
          width: '15%'
        },
        {
          title: '状态',
          dataIndex: 'status',
          width: '15%',
          scopedSlots: { customRender: 'status' }
        },
        {
          title: '操作',
          dataIndex: 'operation',
          scopedSlots: { customRender: 'operation' }
        }
      ]
    }
  },
  computed: {},
  watch: {},
  methods: {
    removeDomain (item) {
      console.log(item)
      delPipelineTask({
        seqId: item.ptid
      }).then(res => {
        this.reloadForm(this.dynamicValidateForm.seqId)
      })
    },
    handleOk (e) {
      this.confirmLoading = true
      savePipeline(this.dynamicValidateForm).then(res => {
        this.$message.info(res)
        this.addVisible = false
        this.confirmLoading = false
        this.$refs.table.refresh()
      })
    },
    handleCancel (e) {
      this.visible = false
      this.addVisible = false
    },
    onDelete (key) {
      delPipeline({
        seqId: key
      }).then(res => {
        this.$message.info(res)
        this.$refs.table.refresh()
      })
    },
    handleAdd () {
      this.addVisible = true
      this.dynamicValidateForm = {
        name: '',
        cron: ''
      }
    },
    handleEdit (record) {
      this.reloadForm(record.seqId)
    },
    reloadForm (seqId) {
        getPipelineTask({
        seqId: seqId
      }).then((res) => {
        this.dynamicValidateForm = res
        this.visible = true
      })
    },
    addTask () {
      if (this.taskSelected && this.taskSelected.seqId) {
        var that = this
        var has = this.dynamicValidateForm.tasks.some(function (
          value,
          index,
          array
        ) {
          return value.seqId === that.taskSelected.seqId
        })
        if (has) {
          this.$notification['error']({
            message: '错误',
            description: '该任务已在流水线中',
            duration: 4
          })
        } else {
          this.selectVisible = false
          savePipelineTask({
            pipelineId: this.dynamicValidateForm.seqId,
            taskId: this.taskSelected.seqId
          }).then(res => {
              this.reloadForm(this.dynamicValidateForm.seqId)
          })
        }
      }
    },
    openTaskSelect () {
      getAllTask().then((res) => {
        this.taskOptions = res
        this.selectVisible = true
      })
    },
    closeTaskSelect () {
      this.selectVisible = false
    },
    chooseTask (value) {
      this.taskSelected = this.taskOptions.find((ele) => ele['seqId'] === value)
    },
    filterOption (input, option) {
      return (
        option.componentOptions.children[0].text
          .toLowerCase()
          .indexOf(input.toLowerCase()) >= 0
      )
    },
    switchStatus (record) {
      switchPipelineStatus({
        seqId: record.seqId,
        status: !record.status
      }).then(res => {
        this.$message.info(res)
        this.$refs.table.refresh()
      })
    }
  }
}
</script>
<style>
.editable-add-btn {
  margin-bottom: 8px;
}
.dynamic-delete-button {
  cursor: pointer;
  position: relative;
  top: 4px;
  font-size: 24px;
  color: #999;
  transition: all 0.3s;
}
.dynamic-delete-button:hover {
  color: #777;
}
.dynamic-delete-button[disabled] {
  cursor: not-allowed;
  opacity: 0.5;
}
</style>
