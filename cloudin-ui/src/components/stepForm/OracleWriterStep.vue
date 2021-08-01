<template>
  <div>
    <a-steps :current="current" size="small">
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
          <a-form-model-item
            v-for="(entry, index) in form.preSql"
            :key="entry.key"
            :prop="'preSql.' + index + '.value'"
            v-bind="formItemLayout"
            :rules="{
              required: true,
              message: 'SQL不能为空',
              trigger: 'blur',
            }"
          >
            <a-input
              v-model="entry.value"
              placeholder="SQL"
              style="width: 90%; margin-right: 8px"
            />
            <a-icon
              v-if="form.preSql.length > 0"
              class="dynamic-delete-button"
              type="minus-circle-o"
              @click="removePreSql(entry)"
            />
          </a-form-model-item>
          <a-form-model-item v-bind="formItemLayout">
            <a-button type="dashed" style="width: 100%" @click="addPreSql">
              <a-icon type="plus" /> Add PreSQL
            </a-button>
          </a-form-model-item>
          <a-form-model-item
            v-for="(entry, index) in form.postSql"
            :key="entry.key"
            :prop="'postSql.' + index + '.value'"
            v-bind="formItemLayout"
            :rules="{
              required: true,
              message: 'SQL不能为空',
              trigger: 'blur',
            }"
          >
            <a-input
              v-model="entry.value"
              placeholder="SQL"
              style="width: 90%; margin-right: 8px"
            />
            <a-icon
              v-if="form.postSql.length > 0"
              class="dynamic-delete-button"
              type="minus-circle-o"
              @click="removePostSql(entry)"
            />
          </a-form-model-item>
          <a-form-model-item v-bind="formItemLayout">
            <a-button type="dashed" style="width: 100%" @click="addPostSql">
              <a-icon type="plus" /> Add PostSQL
            </a-button>
          </a-form-model-item>
        </div>
        <div v-show="current == 3 || current == 100">
          <vue-json-editor
            v-model="template"
            :showBtns="false"
            :mode="'text'"
            lang="zh"
          />
        </div>
      </a-form-model>
    </div>
    <div class="steps-action">
      <a-button v-if="current < steps.length - 1" type="primary" @click="next">
        下一步
      </a-button>
      <a-button v-if="current > 0" style="margin-left: 8px" @click="prev">
        上一步
      </a-button>
      <a-button
        v-if="current == steps.length - 1"
        type="primary"
        @click="emitJSON"
        style="float: right"
      >
        保存
      </a-button>
    </div>
  </div>
</template>
<script>
import vueJsonEditor from 'vue-json-editor'
export default {
  name: 'OracleWriterStep',
  components: {
    vueJsonEditor
  },
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
        preSql: [],
        postSql: []
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
        },
        {
          title: '预览'
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
      if (this.current > this.steps.length - 1) {
        this.current = this.steps.length - 1
      }
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
    removePreSql (item) {
      const index = this.form.preSql.indexOf(item)
      if (index !== -1) {
        this.form.preSql.splice(index, 1)
      }
    },
    addPreSql () {
      this.form.preSql.push({
        value: '',
        key: Date.now()
      })
    },
    removePostSql (item) {
      const index = this.form.postSql.indexOf(item)
      if (index !== -1) {
        this.form.postSql.splice(index, 1)
      }
    },
    addPostSql () {
      this.form.postSql.push({
        value: '',
        key: Date.now()
      })
    },
    genJSON () {
      const ds = this.datasources.find(
        (ele) => ele.seqId === this.form.datasourceId
      )
      if (ds) {
        const jsonOBJ = {}
        jsonOBJ.columns = []
        this.form.columns.forEach((ele) => {
          jsonOBJ.columns.push(ele.value)
        })
        if (jsonOBJ.columns.length === 0) {
          jsonOBJ.columns.length.push('*')
        }
        jsonOBJ.username = ds.userName
        jsonOBJ.password = ds.passWord
        jsonOBJ.connection = [
          {
            table: [],
            jdbcUrl: ds.url
          }
        ]
        this.form.tables.forEach((ele) => {
          jsonOBJ.connection[0].table.push(ele.value)
        })
        if (this.form.preSql.length > 0) {
          jsonOBJ.preSql = []
          this.form.preSql.forEach((ele) => {
            jsonOBJ.preSql.push(ele.value)
          })
        }

        if (this.form.postSql.length > 0) {
          jsonOBJ.postSql = []
          this.form.postSql.forEach((ele) => {
            jsonOBJ.postSql.push(ele.value)
          })
        }
        this.template = { name: this.name, parameter: jsonOBJ }
      }
    },
    emitJSON () {
      this.$emit('finish', this.template)
      this.current = 100
    }
  },
  watch: {
    current (val, oldVal) {
      if (val === 3) {
        this.genJSON()
      }
    }
  }
}
</script>
<style lang="less" scoped>
/deep/.steps-content {
  margin-top: 5px;
  border: 1px dashed #e9e9e9;
  border-radius: 6px;
  background-color: #fafafa;
  min-height: 200px;
  text-align: center;
  padding-top: 20px;
}

.steps-action {
  margin-top: 10px;
}
/deep/ div.jsoneditor-menu {
  display: none;
}
/deep/ div.jsoneditor-tree table.jsoneditor-tree {
  display: inline-block;
  height: 100%;
}
/deep/ textarea.jsoneditor-text {
  display: inline-block;
}
</style>
