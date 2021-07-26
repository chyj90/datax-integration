<template>
  <page-header-wrapper>
    <a-button class="editable-add-btn" @click="handleAdd"> 添加 </a-button>
    <s-table
      ref="table"
      size="default"
      rowKey="seqId"
      :columns="columns"
      :data="loadData"
      :showPagination="false"
    >
      <span slot="operation" slot-scope="text, record">
        <template>
          <a-popconfirm
            title="删除该数据源？"
            cancelText="取消"
            okText="确定"
            @confirm="() => onDelete(record)"
          >
            <a-button type="danger" size="small">删除</a-button>
          </a-popconfirm>
          <a-divider type="vertical" />
          <a-button
            @click="handleEdit(record)"
            type="primary"
            size="small"
          >编辑</a-button>
        </template>
      </span>
    </s-table>
    <a-modal
      :title="modalTitle"
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
        <a-form-model-item label="名称">
          <a-input v-model="form.dsName" placeholder="名称" />
        </a-form-model-item>
        <a-form-model-item label="链接">
          <a-input v-model="form.url" placeholder="url" />
        </a-form-model-item>
        <a-form-model-item label="用户名">
          <a-input v-model="form.userName" placeholder="用户名" />
        </a-form-model-item>
        <a-form-model-item label="密码">
          <a-input v-model="form.passWord" placeholder="密码" />
        </a-form-model-item>
        <a-form-model-item label="驱动">
          <a-input v-model="form.driverName" placeholder="驱动" />
        </a-form-model-item>
      </a-form-model>
    </a-modal>
  </page-header-wrapper>
</template>
<script>
import { STable } from '@/components'
import { queryDatasources, saveDatasource } from '@/api/meta'
export default {
  components: {
    STable
  },
  data () {
    return {
      modalTitle: '新增数据源',
      visible: false,
      confirmLoading: false,
      form: {
        seqId: Number,
        dsName: String,
        url: String,
        userName: String,
        passWord: String,
        driverName: String,
        owner: Number,
        status: Boolean
      },
      queryParam: {},
      loadData: (parameter) => {
        const requestParameters = Object.assign({}, parameter, this.queryParam)
        return queryDatasources(requestParameters).then((res) => {
          console.log(res)
          return res
        })
      },
      columns: [
        {
          title: 'ID',
          dataIndex: 'seqId',
          width: '5%'
        },
        {
          title: '名称',
          dataIndex: 'dsName',
          width: '15%'
        },
        {
          title: 'URL',
          dataIndex: 'url',
          width: '40%',
          ellipsis: true
        },
        {
          title: '用户名',
          dataIndex: 'userName',
          width: '10%'
        },
        {
          title: '驱动',
          dataIndex: 'driverName',
          width: '15%'
        },
        {
          title: '操作',
          scopedSlots: { customRender: 'operation' }
        }
      ]
    }
  },
  methods: {
    handleOk (e) {
      this.confirmLoading = true
      saveDatasource(this.form)
        .then((res) => {
          this.visible = false
          this.confirmLoading = false
          this.$refs.table.refresh()
        })
        .catch((err) => {
          this.confirmLoading = false
          console.log(err)
        })
    },
    handleEdit (record) {
      this.modalTitle = '修改数据源'
      Object.assign(this.form, record)
      this.visible = true
    },
    handleAdd () {
      this.modalTitle = '新增数据源'
      this.visible = true
      this.form = {
        dsName: '',
        url: '',
        userName: '',
        passWord: '',
        driverName: '',
        owner: 0,
        status: true
      }
    },
    handleCancel (e) {
      this.visible = false
    },
    onDelete (record) {
      record.status = false
      saveDatasource(record).then((res) => {
        this.$message.info('保存成功')
        this.$refs.table.refresh()
      })
    }
  }
}
</script>
<style scoped>
.editable-add-btn {
  margin-bottom: 8px;
}
</style>
