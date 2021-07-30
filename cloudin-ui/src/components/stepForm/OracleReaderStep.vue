<template>
  <div>
    <a-steps :current="current">
      <a-step v-for="item in steps" :key="item.title" :title="item.title" />
    </a-steps>
    <div class="steps-content">
      <a-form-model :model="form" layout="vertical">
        <div v-show="current == 0">
          <a-form-model-item v-bind="formItemLayout">
            <a-select v-model="form.datasourceId" placeholder="请选择数据源">
              <a-select-option v-for="d in datasources" :key="d.seqId">
                {{ d.dsName }}
              </a-select-option>
            </a-select>
          </a-form-model-item>
          <a-form-model-item
            v-for="(domain, index) in form.tables"
            :key="domain.key"
            :prop="'tables.' + index + '.value'"
            v-bind="formItemLayout"
            :rules="{
              required: true,
              message: '表名不能为空',
              trigger: 'blur',
            }"
          >
            <a-input
              v-model="domain.value"
              placeholder="请输入表名"
              style="width: 90%; margin-right: 8px"
            />
            <a-icon
              v-if="form.tables.length > 0"
              class="dynamic-delete-button"
              type="minus-circle-o"
              @click="removeTable(domain)"
            />
          </a-form-model-item>
          <a-form-model-item v-bind="formItemLayout">
            <a-button type="dashed" style="width: 100%" @click="addTable">
              <a-icon type="plus" /> Add table
            </a-button>
          </a-form-model-item>
        </div>
        <div v-show="current == 1">
          <a-form-model-item
            v-for="(entry, index) in form.columns"
            :key="entry.key"
            :prop="'columns.' + index + '.value'"
            v-bind="formItemLayout"
            :rules="{
              required: true,
              message: '列名不能为空',
              trigger: 'blur',
            }"
          >
            <a-input
              v-model="entry.value"
              placeholder="请输入列名"
              style="width: 90%; margin-right: 8px"
            />
            <a-icon
              v-if="form.columns.length > 0"
              class="dynamic-delete-button"
              type="minus-circle-o"
              @click="removeColumn(entry)"
            />
          </a-form-model-item>
          <a-form-model-item v-bind="formItemLayout">
            <a-button type="dashed" style="width: 100%" @click="addColumn">
              <a-icon type="plus" /> Add column
            </a-button>
          </a-form-model-item>
        </div>
        <div v-show="current == 2">
          <a-form-model-item v-bind="formItemLayout">
            <a-input v-model="form.splitPk" placeholder="splitPk" />
          </a-form-model-item>
          <a-form-model-item v-bind="formItemLayout">
            <a-input v-model="form.where" placeholder="where" />
          </a-form-model-item>
          <a-form-model-item v-bind="formItemLayout">
            <a-input v-model="form.querySql" placeholder="querySql" />
          </a-form-model-item>
        </div>
      </a-form-model>
    </div>
    <div class="steps-action">
      <a-button v-if="current < steps.length - 1" type="primary" @click="next">
        下一步
      </a-button>
      <a-button
        v-if="current == steps.length - 1"
        type="primary"
        @click="$message.success('Processing complete!')"
      >
        预览
      </a-button>
      <a-button v-if="current > 0" style="margin-left: 8px" @click="prev">
        上一步
      </a-button>
    </div>
  </div>
</template>
<script>
export default {
  name: 'OracleReaderStep',
  props: {
    name: {
      type: String,
      default: ''
    },
    datasources: {
      type: Array,
      default: () => []
    }
  },
  data () {
    return {
      current: 0,
      form: {
        tables: [],
        columns: [],
        splitPk: '',
        where: '',
        querySql: ''
      },
      formItemLayout: {
        wrapperCol: {
          xs: { span: 10, offset: 1 },
          sm: { span: 10, offset: 1 }
        }
      },
      steps: [
        {
          title: '选择数据源'
        },
        {
          title: '列配置'
        },
        {
          title: '其他配置'
        }
      ],
      template: {}
    }
  },
  methods: {
    next () {
      this.current++
    },
    prev () {
      this.current--
    },
    removeTable (item) {
      const index = this.form.tables.indexOf(item)
      if (index !== -1) {
        this.form.tables.splice(index, 1)
      }
    },
    addTable () {
      this.form.tables.push({
        value: '',
        key: Date.now()
      })
    },
    removeColumn (item) {
      const index = this.form.columns.indexOf(item)
      if (index !== -1) {
        this.form.columns.splice(index, 1)
      }
    },
    addColumn () {
      this.form.columns.push({
        value: '',
        key: Date.now()
      })
    },
    genJSON () {
      const ds = this.datasources.find(
        (ele) => ele.seqId === this.form.datasourceId
      )
      this.template.name = this.name
      this.template.columns = this.form.columns
      this.template.username = ds.userName
      this.template.password = ds.passWord
      this.template.connection = [
        {
          table: this.form.tables,
          jdbcUrl: [
            this.ds.url
          ]
        }
      ]
      if (this.form.splitPk) {
        this.template.splitPk = this.form.splitPk
      }
      if (this.form.where) {
        this.template.where = this.form.where
      }
      if (this.form.querySql) {
        this.template.querySql = this.form.querySql
      }
    }
  }
}
</script>
<style scoped>
.steps-content {
  margin-top: 16px;
  border: 1px dashed #e9e9e9;
  border-radius: 6px;
  background-color: #fafafa;
  min-height: 200px;
  text-align: center;
  padding-top: 80px;
}

.steps-action {
  margin-top: 24px;
}
</style>
