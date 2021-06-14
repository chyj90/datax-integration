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
          <a @click="onDelete(record.key)">编辑</a>
        </template>
      </span>
    </s-table>
  </page-header-wrapper>
</template>
<script>
import { STable } from '@/components'
import { getDataxTaskList } from '@/api/task'
export default {
  components: {
    STable
  },
  data () {
    return {
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
          width: '10%'
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
          title: '所属用户',
          dataIndex: 'owner',
          width: '15%'
        },
        {
          title: 'Json',
          dataIndex: 'jsonstr',
          width: '25%'
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
  methods: {
    onDelete (key) {
    },
    handleAdd () {
    }
  }
}
</script>
<style>
.editable-add-btn {
  margin-bottom: 8px;
}
</style>
