<template>
  <div>
    <a-steps :current="current" size="small">
      <a-step v-for="item in steps" :key="item.title" :title="item.title" />
    </a-steps>
    <div class="steps-content">
      <a-form-model :model="form" layout="vertical">
        <div v-show="current == 0">
          <a-form-model-item v-bind="formItemLayout">
            <a-input v-model="form.encoding" placeholder="字符集" />
          </a-form-model-item>
          <a-form-model-item v-bind="formItemLayout">
            <a-select
              style="width: 100%; margin-top: 10px"
              @change="selectWriter"
            >
              <a-select-option key="1"> True </a-select-option>
              <a-select-option key="0"> False </a-select-option>
            </a-select>
          </a-form-model-item>
        </div>
        <div v-show="current == 1 || current == 100">
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
  name: 'StreamWriterStep',
  components: {
    vueJsonEditor
  },
  props: {},
  data () {
    return {
      current: 0,
      form: {
        encoding: 'UTF-8',
        print: 0
      },
      formItemLayout: {
        wrapperCol: {
          xs: { span: 10, offset: 1 },
          sm: { span: 10, offset: 1 }
        }
      },
      steps: [
        {
          title: '参数选择'
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
    selectWriter (option) {
      this.form.print = option
    },
    genJSON () {
      const jsonOBJ = {
        name: 'streamwriter',

        parameter: {
          encoding: this.form.encoding,

          print: this.form.print === 1
        }
      }
      this.template = jsonOBJ
    },
    emitJSON () {
      this.$emit('finish', this.template)
      this.current = 100
    }
  },
  watch: {
    current (val, oldVal) {
      if (val === 1) {
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
