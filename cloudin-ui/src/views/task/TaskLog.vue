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
      queryParam: {},
      loadData: (parameter) => {
        const requestParameters = Object.assign({}, parameter, this.queryParam)
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
          dataIndex: 'taskName',
          width: '15%'
        },
        {
          title: '开始时间',
          dataIndex: 'execTime',
          width: '15%'
        },
        {
          title: '结束时间',
          dataIndex: 'endTime',
          width: '15%',
          ellipsis: true
        },
        {
          title: '状态',
          dataIndex: 'status',
          width: '15%'
        },
        {
          title: '备注',
          dataIndex: 'notes'
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
</style>
