<template>
  <page-header-wrapper>
    <div>
      <div :style="{ borderBottom: '1px solid #E9E9E9' }">
        <a-checkbox
          :indeterminate="indeterminate"
          :checked="checkAll"
          @change="onCheckAllChange"
        >
          全选/反选
        </a-checkbox>
      </div>
      <br />
      <div style="height: 80px; overflow-y: auto">
        <a-checkbox-group
          v-model="checkedList"
          :options="metrics"
          @change="onChange"
        >
        </a-checkbox-group>
      </div>
    </div>
    <a-table
      ref="table"
      size="default"
      rowKey="seqId"
      :columns="columns"
      :scroll="{ x: 1800 }"
      :data-source="tableData"
      :pagination="false"
    >
    </a-table>
  </page-header-wrapper>
</template>
<script>
import { STable } from '@/components'
import { metricList, metricValue } from '@/api/manage'
import { metricName } from '@/utils/util'
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
        width: '280px',
        fixed: 'left'
      },
      tableData: [],
      metrics: [],
      checkedList: [],
      columns: [],
      timer: ''
    }
  },
  computed: {},
  watch: {},
  mounted () {
    this.getMetrics()
  },
  beforeDestroy () {
    clearInterval(this.timer)
  },
  methods: {
    loadData () {
      metricValue(this.checkedList).then((res) => {
        this.tableData = res.data
      })
    },
    onChange (checkedList) {
      this.indeterminate =
        !!checkedList.length && checkedList.length < this.metrics.length
      this.checkAll = checkedList.length === this.metrics.length
      this.setColumns()
    },
    onCheckAllChange (e) {
      Object.assign(this, {
        checkedList: e.target.checked ? this.metrics : [],
        indeterminate: false,
        checkAll: e.target.checked
      })
      this.setColumns()
    },
    getMetrics () {
      metricList().then((res) => {
        this.metrics = res.filter((ele) => metricName(ele) !== undefined)
        this.indeterminate = false
        this.checkAll = true
        this.checkedList = this.metrics
        this.setColumns()
        this.timer = setInterval(this.loadData, 3000)
      })
    },
    setColumns () {
      const columns = []
      columns.push(this.fixcolumns)
      this.checkedList.forEach((ele) => {
        columns.push({
          title: metricName(ele),
          dataIndex: ele
        })
      })
      this.columns = columns
      this.loadData()
    }
  }
}
</script>
<style scoped>
</style>
