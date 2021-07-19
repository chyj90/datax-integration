<template>
  <page-header-wrapper>
    <div>
      <div :style="{ borderBottom: '1px solid #E9E9E9' }">
        <a-checkbox :indeterminate="indeterminate" :checked="checkAll" @change="onCheckAllChange">
          全选/反选
        </a-checkbox>
      </div>
      <br />
      <a-checkbox-group v-model="checkedList" :options="metrics" @change="onChange" />
    </div>
    <s-table
      ref="table"
      size="default"
      rowKey="seqId"
      :columns="columns"
      :data="loadData"
    >
    </s-table>
  </page-header-wrapper>
</template>
<script>
import { STable } from '@/components'
import { metricList } from '@/api/manage'
export default {
  components: {
    STable
  },
  data () {
    return {
      checkAll: false,
      indeterminate: false,
      fixcolumns: {
        title: '服务名',
        dataIndex: 'seqId',
        width: '8%'
      },
      metrics: [],
      checkedList: [],
      loadData: (parameter) => {},
      columns: []
    }
  },
  computed: {},
  watch: {},
  methods: {
    onChange (checkedList) {
      this.indeterminate = !!checkedList.length && checkedList.length < this.metrics.length
      this.checkAll = checkedList.length === this.metrics.length
    },
    onCheckAllChange (e) {
      Object.assign(this, {
        checkedList: e.target.checked ? this.metrics : [],
        indeterminate: false,
        checkAll: e.target.checked
      })
    },
    getMetrics () {
      metricList().then((res) => {
        this.metrics = res
      })
    }
  }
}
</script>
