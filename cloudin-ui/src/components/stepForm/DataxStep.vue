<template>
  <div>
    <div>
      <a-form-item
        label="Reader"
        :label-col="labelCol"
        :wrapper-col="wrapperCol"
        labelAlign="left"
      >
        <a-select default-value="" style="width: 100%" @change="selectReader">
          <a-select-option key="oraclereader"> Oracle Reader </a-select-option>
        </a-select>
      </a-form-item>

      <oracle-reader-step
        v-if="this.readerStepName === 'oraclereader'"
        :datasources="datasources"
        :name="readerName"
        @finish="readerConfirm"
      ></oracle-reader-step>
      <a-form-item
        label="Writer"
        :label-col="labelCol"
        :wrapper-col="wrapperCol"
        labelAlign="left"
      >
        <a-select style="width: 100%;margin-top:10px" @change="selectWriter">
          <a-select-option key="streamwriter"> Stream Writer </a-select-option>
          <a-select-option key="oraclewriter"> Oracle Writer </a-select-option>
        </a-select>
      </a-form-item>
      <stream-writer-step v-if="this.writerStepName==='streamwriter'" @finish="writerConfirm"></stream-writer-step>
      <oracle-writer-step v-if="this.writerStepName==='oraclewriter'" :datasources="datasources" :name="writerName" @finish="writerConfirm"></oracle-writer-step>
      <div style="height:15px;margin-top:10px">
        <a-button
          type="primary"
          @click="emitJSON"
          style="float: right"
        >
          生成JSON
        </a-button>
      </div>
    </div>
  </div>
</template>
<script>
import OracleReaderStep from './OracleReaderStep'
import OracleWriterStep from './OracleWriterStep'
import StreamWriterStep from './StreamWriterStep'
export default {
  name: 'DataxStep',
  components: {
    OracleReaderStep,
    StreamWriterStep,
    OracleWriterStep
  },
  props: {
    datasources: {
      type: Array,
      default: () => []
    }
  },
  data () {
    return {
      labelCol: { span: 2 },
      wrapperCol: { span: 10 },
      readerName: '',
      readerStepName: '',
      writerName: '',
      writerStepName: '',
      config: {
        setting: {},
        job: {
          setting: {
            speed: {
              channel: 2
            }
          },
          contents: [{}]
        }
      }
    }
  },
  methods: {
    readerConfirm (template) {
      this.config.job.contents[0]['reader'] = template
    },
    writerConfirm (template) {
      this.config.job.contents[0]['writer'] = template
    },
    emitJSON () {
      const rs = {}
      Object.assign(rs, this.config)
      this.$emit('finish', rs)
    },
    selectReader (option) {
      this.readerName = option
      if (this.readerName === 'oraclereader') {
        this.readerStepName = 'oraclereader'
      }
    },
    selectWriter (option) {
      this.writerName = option
      if (this.writerName === 'streamwriter') {
        this.writerStepName = 'streamwriter'
      } else if (this.writerName === 'oraclewriter') {
        this.writerStepName = 'oraclewriter'
      }
    }
  }
}
</script>
