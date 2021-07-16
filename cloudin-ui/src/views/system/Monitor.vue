<template>
  <page-header-wrapper>
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
    getMetrics () {
      metricList().then((res) => {
        this.metrics = res
      })
    }
  }
}
</script>
