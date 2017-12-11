<template>
  <div
    v-loading="loading"
    element-loading-text="试题上传中">
    <el-button type="primary" size="large" icon="el-icon-download" @click="downloadTemplate">下载试题模板</el-button>
    <!--<el-button type="primary" size="large" icon="el-icon-upload" @click="uploadQuestion">上传试题</el-button>-->
    <input id="upload" type="file" name="upload" class="inputfile" @change="uploadQuestion($event)"/>
    <label for="upload"><i class="el-icon-upload"></i><span style="margin-left: 5px">上传试题</span></label>
  </div>
</template>

<script>
  import XLSX from 'xlsx'
  import {getAndRefreshJwt} from '@/utils/auth.js'
  import {httpJson} from '@/utils/http.js'
  import {downloadFile} from '@/utils/downloader.js'

  export default {
    data () {
      return {
        loading: false
      }
    },
    methods: {
      downloadTemplate: function () {
        let token = getAndRefreshJwt()
        let vm = this
        if (token) {
          downloadFile('questions/templateUrl', token, function (err) {
            console.error(err)
            vm.$message.error('下载模板失败')
          })
        } else {  // 登录超时
          this.$router.push({path: '/login', query: {errMsg: '登录超时，请重新登录'}})
        }
      },
      uploadQuestion: function (event) {
        let vm = this
        let obj = event.target
        if (!obj.files) {
          return
        }
        let f = obj.files[0]
        let reader = new FileReader()
        reader.onload = function (e) {
          let data = e.target.result
          // 解析excel
          let wb = XLSX.read(data, {type: 'binary'})
          let questionArray = XLSX.utils.sheet_to_json(wb.Sheets[wb.SheetNames[0]])
          let formatQuestions = vm.formatQuestion(questionArray)
          // reset input元素，不然下一次的onchange事件不会触发
          obj.value = ''
          // 发送http请求
          vm.postQuestion(formatQuestions)
        }
        this.loading = true
        reader.readAsBinaryString(f)
      },
      // 规范化从excel中读取出来的数据
      formatQuestion: function (arr) {
        let rt = []
        for (let q of arr) {
          // 问题类型是整数
          q.type = parseInt(q.type)
          if (isNaN(q.type) || !q.content || !q.answer || !q.options) {
            continue
          }
          let opt = q.options
          if (!opt) {
            continue
          }
          let opts = opt.split(/\r?\n/)
          let optArr = []
          for (let o of opts) {
            let optObj = {}
            let optA = o.split(/[ \t]+/)
            optObj['key'] = optA[0]
            optObj['content'] = optA[1]
            optArr.push(optObj)
          }
          q.options = JSON.stringify(optArr)
          let ansArr = q.answer.split(/\s+/)
          q.answer = JSON.stringify(ansArr)
          rt.push(q)
        }
        return rt
      },
      postQuestion: function (arr) {
        let vm = this
        let token = getAndRefreshJwt()
        if (token) {
          let authHeader = {Authorization: 'Bearer ' + token}
          httpJson.post('questions/upload', arr, {headers: authHeader})
            .then(function (res) {
              if (res.data.code === 200) {
                // 上传试题完成
                vm.loading = false
                vm.$message({
                  message: '试题上传成功！',
                  type: 'success'
                })
              } else {
                vm.loading = false
                vm.$message.error('试题上传失败！')
              }
            })
            .catch(function (err) {
              console.error(err)
              vm.loading = false
              vm.$message.error('试题上传失败！')
            })
        } else {
          this.loading = false
          this.$router.push({path: '/login', query: {errMsg: '登录超时，请重新登录'}})
        }
      }
    }
  }
</script>

<style scoped>
  .inputfile {
    width: 0.1px;
    height: 0.1px;
    opacity: 0;
    overflow: hidden;
    position: absolute;
    z-index: -1;
  }

  .inputfile + label {
    border-radius: 4px;
    font-size: 14px;
    font-weight: 500;
    padding: 12px 20px;
    transition: .1s;
    outline: 0;
    box-sizing: border-box;
    text-align: center;
    -webkit-appearance: none;
    cursor: pointer;
    white-space: nowrap;
    line-height: 1;
    display: inline-block;
    border: 1px solid #409eff;
    background: #409eff;
    color: #fff;
    margin: 0 0 0 10px;
  }

  /*.inputfile:focus + label,*/
  /*.inputfile + label:hover {*/
    /*background-color: red;*/
  /*}*/

  /*.inputfile:focus + label {*/
    /*outline: 1px dotted #000;*/
    /*outline: -webkit-focus-ring-color auto 5px;*/
  /*}*/
</style>
