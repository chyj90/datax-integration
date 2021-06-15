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
      <span slot="operation" slot-scope="text, record">
        <template>
          <a-popconfirm
            title="删除此任务？"
            @confirm="() => onDelete(record.key)"
          >
            <a href="javascript:;">删除</a>
          </a-popconfirm>
          <a-divider type="vertical" />
          <a @click="handleEdit(record)">编辑</a>
        </template>
      </span>
    </s-table>
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
      <a-form-model :form="form" layout="vertical">
        <a-form-model-item>
          <a-input style="display: none" :value="form['ID']" />
        </a-form-model-item>
        <a-form-model-item label="任务描述">
          <a-input v-model="form.name" placeholder="任务描述" />
        </a-form-model-item>
        <a-form-model-item label="Json">
          <a-row>
            <a-col :span="12">
              <a-textarea
                v-model="form.jsonstr"
                placeholder="任务详情"
                :auto-size="{ minRows: 8, maxRows: 8 }"
              />
            </a-col>
            <a-col :span="12">
              <json-viewer :value="formatJSON" style="height:200px;overflow:scroll;"></json-viewer>
            </a-col>
          </a-row>
        </a-form-model-item>
      </a-form-model>
    </a-modal>
  </page-header-wrapper>
</template>
<script>
import { STable } from '@/components'
import { getDataxTaskList } from '@/api/task'
import JsonViewer from 'vue-json-viewer'
export default {
  components: {
    STable,
    JsonViewer
  },
  data () {
    return {
      visible: false,
      formatJSON: [],
      confirmLoading: false,
      form: {
        ID: '',
        name: '',
        owner: '',
        jsonstr: ''
      },
      queryParam: {},
      loadData: (parameter) => {
        const requestParameters = Object.assign({}, parameter, this.queryParam)
        console.log('loadData request parameters:', requestParameters)
        return getDataxTaskList(requestParameters).then((res) => {
          return res
        })
      },
      columns: [
        {
          title: 'ID',
          dataIndex: 'ID',
          width: '15%'
        },
        {
          title: '任务描述',
          dataIndex: 'name',
          width: '15%'
        },
        {
          title: '所属用户',
          dataIndex: 'owner',
          width: '15%'
        },
        {
          title: 'Json',
          dataIndex: 'jsonstr',
          width: '30%'
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
  watch: {
    'form.jsonstr': function (newVal, oldVal) {
      var jsonRS = []
      try {
          jsonRS = JSON.parse(newVal)
      } catch (error) {
      }
      this.formatJSON = jsonRS
    }
  },
  methods: {
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
      this.form = {
        ID: '',
        name: '',
        owner: '',
        jsonstr: ''
      }
    },
    handleEdit (record) {
      this.form = record
      console.log(this.form)
      this.visible = true
    },
    saveTask () {}
  }
}
</script>
<style>
.editable-add-btn {
  margin-bottom: 8px;
}
.jv-container .jv-code {
  overflow: scroll;
  padding: 0px 20px;
}
</style>
