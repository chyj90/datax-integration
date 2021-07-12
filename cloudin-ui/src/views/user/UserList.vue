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
      <span slot="operation" slot-scope="text, record">
        <template>
          <a-popconfirm
            title="删除此用户？"
            cancelText="取消"
            okText="确定"
            @confirm="() => onDelete(record.seqId)"
          >
            <a-button type="danger" size="small">删除</a-button>
          </a-popconfirm>
          <a-divider type="vertical" />
          <a-button @click="handleEdit(record)" type="primary" size="small">{{
            record.status == 1 ? "禁用" : "恢复"
          }}</a-button>
        </template>
      </span>
    </s-table>
  </page-header-wrapper>
</template>
<script>
import { STable } from '@/components'
import { deleteUser, listUser, changeUserStatus } from '@/api/manage'
export default {
  components: {
    STable
  },
  data () {
    return {
      visible: false,
      confirmLoading: false,
      form: {
        username: '',
        password: ''
      },
      queryParam: {},
      loadData: (parameter) => {
        const requestParameters = Object.assign({}, parameter, this.queryParam)
        return listUser(requestParameters).then((res) => {
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
          title: '账号',
          dataIndex: 'username',
          width: '15%'
        },
        {
          title: '状态',
          dataIndex: 'status',
          width: '10%'
        },
        {
          title: '操作',
          scopedSlots: { customRender: 'operation' }
        }
      ]
    }
  },
  computed: {},
  watch: {},
  methods: {
    onDelete (key) {
      deleteUser({ seqId: key })
        .then((res) => {
          this.$message.info(res)
          this.$refs.table.refresh()
        })
        .catch((error) => {
          this.$message.error(error)
        })
    },
    handleEdit (record) {
      var targetStatus = 1
      if (record.status === 1) {
        targetStatus = 2
      }
      changeUserStatus({ seqId: record.seqId, status: targetStatus }).then(
        (res) => {
          this.$message.info(res)
          this.$refs.table.refresh()
        }
      )
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
