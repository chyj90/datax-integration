<template>
  <page-header-wrapper>
    <s-table
      ref="table"
      size="default"
      rowKey="seqId"
      :columns="columns"
      :data="loadData"
      showPagination="auto"
    >
    </s-table>
    <a-modal
      title="任务详情"
      :visible="visible"
      :confirm-loading="confirmLoading"
      @cancel="handleCancel"
      width="820px"
    >
    </a-modal>
  </page-header-wrapper>
</template>
<script>
import { STable } from '@/components'
import { taskLog, switchTaskStatus } from '@/api/task'
export default {
  components: {
    STable
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
        return taskLog(requestParameters).then((res) => {
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
    switchStatus (record) {
      switchTaskStatus({
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
