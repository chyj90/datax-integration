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
        <a-form-model-item label="账号">
          <a-input v-model="form.username" placeholder="账号" />
        </a-form-model-item>
        <a-form-model-item label="密码">
          <a-input v-model="form.password" placeholder="密码" />
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
          width: '10%'
        },
        {
          title: '名称',
          dataIndex: 'dsName',
          width: '20%'
        },
        {
          title: 'URL',
          dataIndex: 'url',
          width: '40%'
        },
        {
          title: '用户名',
          dataIndex: 'userName',
          width: '10%'
        },
        {
          title: '驱动',
          dataIndex: 'driverName',
          width: '20%'
        }
      ]
    }
  },
  methods: {
    onDelete (record) {
      record.status = false
      saveDatasource(record).then((res) => {
        this.$message.info('保存成功')
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
