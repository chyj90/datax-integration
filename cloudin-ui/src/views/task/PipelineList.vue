<template>
  <page-header-wrapper>
    <a-button class="editable-add-btn" @click="handleAdd"> 添加 </a-button>
    <s-table
      ref="table"
      size="default"
      rowKey="ID"
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
            @confirm="() => onDelete(record.key)"
          >
            <a href="javascript:;">删除</a>
          </a-popconfirm>
          <a-divider type="vertical" />
          <a @click="handleEdit(record)">配置任务</a>
        </template>
      </span>
    </s-table>
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
        <a-select-option v-for="d in taskOptions" :key="d.ID">
          {{ d.name }}
        </a-select-option>
      </a-select>
    </a-modal>
    <a-modal
      title="任务详情"
      :visible="visible"
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
        <a-form-model-item>
          <a-input style="display: none" :value="dynamicValidateForm['ID']" />
        </a-form-model-item>
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
        <a-form-model-item
          v-for="(domain, index) in dynamicValidateForm.tasks"
          :key="domain.ID"
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
            v-if="dynamicValidateForm.tasks.length > 1"
            class="dynamic-delete-button"
            type="minus-circle-o"
            :disabled="dynamicValidateForm.tasks.length === 1"
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
import { getPipelineList, getPipelineTask, getAllTask } from '@/api/task'
export default {
  components: {
    STable
  },
  data () {
    return {
      visible: false,
      selectVisible: false,
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
        ID: Number,
        name: String,
        cron: String,
        owner: String,
        tasks: Array
      },
      taskSelected: {},
      columns: [
        {
          title: 'ID',
          dataIndex: 'ID',
          width: '15%'
        },
        {
          title: '流水线描述',
          dataIndex: 'name',
          width: '15%'
        },
        {
          title: 'Cron',
          dataIndex: 'cron',
          width: '15%'
        },
        {
          title: '所属用户',
          dataIndex: 'owner',
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
      const index = this.dynamicValidateForm.tasks.indexOf(item)
      if (index !== -1) {
        this.dynamicValidateForm.tasks.splice(index, 1)
      }
    },
    handleOk (e) {
      this.confirmLoading = true
      setTimeout(() => {
        this.visible = false
        this.confirmLoading = false
      }, 2000)
    },
    handleCancel (e) {
      this.visible = false
    },
    onDelete (key) {},
    handleAdd () {
      this.visible = true
      this.dynamicValidateForm = {
        name: '',
        cron: '',
        owner: '',
        tasks: []
      }
    },
    handleEdit (record) {
      getPipelineTask().then((res) => {
        this.dynamicValidateForm = res
        this.visible = true
      })
    },
    addTask () {
      if (this.taskSelected && this.taskSelected.ID) {
        var that = this
        var has = this.dynamicValidateForm.tasks.some(function (
          value,
          index,
          array
        ) {
          return value.ID === that.taskSelected.ID
        })
        if (has) {
          this.$notification['error']({
            message: '错误',
            description: '该任务已在流水线中',
            duration: 4
          })
        } else {
          this.dynamicValidateForm.tasks.push(this.taskSelected)
          this.selectVisible = false
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
      this.taskSelected = this.taskOptions.find((ele) => ele['ID'] === value)
    },
    filterOption (input, option) {
      return (
        option.componentOptions.children[0].text
          .toLowerCase()
          .indexOf(input.toLowerCase()) >= 0
      )
    },
    switchStatus (record) {
      record.status = record.status ^ 1
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
