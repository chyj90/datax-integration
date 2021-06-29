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
          >编辑</a-button
          >
        </template>
      </span>
    </s-table>
    <a-modal
      title="任务详情"
      :visible="visible"
      :confirm-loading="confirmLoading"
      @cancel="handleCancel"
      width="820px"
    >
      <template slot="footer">
        <a-button key="cancel" @click="handleCancel"> 取消 </a-button>
        <a-button key="submit" type="primary" @click="handleOk">
          保存
        </a-button>
      </template>
      <a-form-model :form="form" layout="vertical">
        <a-form-model-item>
          <a-input style="display: none" :value="form['ID']" />
        </a-form-model-item>
        <a-form-model-item label="任务描述">
          <a-input v-model="form.name" placeholder="任务描述" />
        </a-form-model-item>
        <a-form-model-item label="Cron">
          <a-input v-model="form.cron" placeholder="Cron表达式" />
        </a-form-model-item>
        <a-form-model-item label="Json">
          <a-row>
            <a-col :span="24">
              <vue-json-editor
                v-model="form.jsonStr"
                :showBtns="false"
                lang="zh"
              />
            </a-col>
          </a-row>
        </a-form-model-item>
      </a-form-model>
    </a-modal>
  </page-header-wrapper>
</template>
<script>
import { STable } from '@/components'
import { getDataxTaskList, saveTask, delTask } from '@/api/task'
import vueJsonEditor from 'vue-json-editor'
export default {
  components: {
    STable,
    vueJsonEditor
  },
  data () {
    return {
      visible: false,
      confirmLoading: false,
      form: {
        seqId: '',
        name: '',
        jsonStr: ''
      },
      queryParam: {},
      loadData: (parameter) => {
        const requestParameters = Object.assign({}, parameter, this.queryParam)
        console.log('loadData request parameters:', requestParameters)
        return getDataxTaskList(requestParameters).then((res) => {
          console.log(res)
          return res
        })
      },
      columns: [
        {
          title: 'ID',
          dataIndex: 'seqId',
          width: '8%'
        },
        {
          title: '任务描述',
          dataIndex: 'name',
          width: '15%'
        },
        {
          title: 'Cron',
          dataIndex: 'cron',
          width: '15%'
        },
        {
          title: 'Json',
          dataIndex: 'jsonStr',
          width: '55%',
          ellipsis: true
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
    handleOk (e) {
      this.confirmLoading = true
      this.postTask(() => {
        this.visible = false
        this.confirmLoading = false
        this.$refs.table.refresh()
      })
    },
    handleCancel (e) {
      this.visible = false
    },
    onDelete (key) {
      delTask({ seqId: key })
        .then((res) => {
          this.$message.info(res)
          this.$refs.table.refresh()
        })
        .catch((error) => {
          this.$message.error(error)
        })
    },
    handleAdd () {
      this.visible = true
      this.form = {
        seqId: '',
        name: '',
        jsonStr: '',
        cron: ''
      }
    },
    handleEdit (record) {
      this.form = Object.assign({}, record)
      this.form.jsonStr = JSON.parse(record.jsonStr)
      this.visible = true
    },
    postTask (callback) {
      const parameter = {
        seqId: this.form.seqId,
        name: this.form.name,
        jsonStr: this.form.jsonStr,
        cron: this.form.cron
      }
      parameter.jsonStr = JSON.stringify(parameter.jsonStr)
      saveTask(parameter).then((res) => {
        this.$message.info('保存成功')
        callback()
      })
    }
  }
}
</script>
<style>
.editable-add-btn {
  margin-bottom: 8px;
}
.ant-form-item {
  margin-bottom: 0px;
}
.ant-modal-body {
  padding-top: 2px;
}
.jsoneditor-poweredBy {
  display: none;
}
</style>
