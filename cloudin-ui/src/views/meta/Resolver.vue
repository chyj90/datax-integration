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
          <a-input v-model="form.resolverName" placeholder="名称" />
        </a-form-model-item>
        <a-form-model-item label="数据源">
          <a-select v-model="form.datasourceId" placeholder="请选择数据源">
            <a-select-option v-for="d in datasources" :key="d.seqId">
              {{ d.dsName }}
            </a-select-option>
          </a-select>
        </a-form-model-item>
        <a-form-model-item label="SQL">
          <a-input v-model="form.express" placeholder="SQL" />
        </a-form-model-item>
      </a-form-model>
    </a-modal>
  </page-header-wrapper>
</template>
<script>
import { STable } from '@/components'
import { queryDatasources, queryResolverPager, saveResolver } from '@/api/meta'
export default {
  components: {
    STable
  },
  mounted () {
      queryDatasources().then(res => {
          this.datasources = res.data
      })
  },
  data () {
    return {
      modalTitle: '新增占位符',
      visible: false,
      confirmLoading: false,
      datasources: [],
      form: {
        seqId: Number,
        resolverName: String,
        datasourceId: Number,
        express: String,
        owner: Number,
        status: Boolean
      },
      queryParam: {},
      loadData: (parameter) => {
        const requestParameters = Object.assign({}, parameter, this.queryParam)
        return queryResolverPager(requestParameters).then((res) => {
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
          dataIndex: 'resolverName',
          width: '20%'
        },
        {
          title: '数据源',
          dataIndex: 'dsName',
          width: '20%',
          ellipsis: true
        },
        {
          title: 'SQL',
          dataIndex: 'express',
          width: '40%'
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
      saveResolver(this.form)
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
      this.modalTitle = '修改占位符'
      Object.assign(this.form, record)
      this.visible = true
    },
    handleAdd () {
      this.modalTitle = '新增占位符'
      this.visible = true
      this.form = {
        resolverName: '',
        express: '',
        owner: 0,
        status: true
      }
    },
    handleCancel (e) {
      this.visible = false
    },
    onDelete (record) {
      record.status = false
      saveResolver(record).then((res) => {
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
