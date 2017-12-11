<template>
  <el-form :model="examForm" :rules="rules" ref="examForm" class="formBody" label-width="100px"
           v-loading="loading"
           element-loading-text="上传中">
    <el-form-item label="考试标题" prop="title">
      <el-input v-model="examForm.title"></el-input>
    </el-form-item>
    <el-form-item label="题目数量" prop="questionCount">
      <el-input v-model.number="examForm.questionCount"></el-input>
    </el-form-item>
    <el-form-item label="每道题分数" prop="questionScore">
      <el-input v-model.number="examForm.questionScore"></el-input>
    </el-form-item>
    <el-form-item label="考试时间" required>
      <el-col :span="11">
        <el-form-item prop="startDate">
          <el-date-picker type="datetime" placeholder="选择开始时间" v-model="examForm.startDate" style="width: 100%"></el-date-picker>
        </el-form-item>
      </el-col>
      <el-col class="line" :span="2">-</el-col>
      <el-col :span="11">
        <el-form-item prop="endDate">
          <el-date-picker type="datetime" placeholder="选择结束时间" v-model="examForm.endDate" style="width: 100%"></el-date-picker>
        </el-form-item>
      </el-col>
    </el-form-item>
    <el-form-item label="考试名单" prop="students">
      <el-col :span="16">
        <el-input type="textarea" v-model="examForm.students" :rows="4"></el-input>
      </el-col>
      <el-col :span="8">
        <el-button type="primary" size="small" icon="el-icon-download" @click="downloadTemplate">下载模板</el-button>
        <input id="upload" type="file" name="upload" class="inputfile" @change="uploadStudent($event)"/>
        <label for="upload"><i class="el-icon-upload"></i><span style="margin-left: 5px">导入学号</span></label>
      </el-col>
    </el-form-item>
    <el-form-item>
      <el-button type="success" @click="submitForm('examForm')">新建考试</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
  import XLSX from 'xlsx'
  import {getAndRefreshJwt} from '@/utils/auth.js'
  import {httpJson} from '@/utils/http.js'
  import {downloadFile} from '@/utils/downloader.js'

  export default {
    data () {
      let checkNumber = (rule, value, callback) => {
        if (value < 1) {
          callback(new Error('不能小于1'))
        } else {
          callback()
        }
      }
      let checkStartDate = (rule, value, callback) => {
        if (!value) {
          callback(new Error('开始时间不能为空'))
        } else {
          let now = new Date()
          if (value < now) {
            callback(new Error('开始时间不能早于现在'))
          } else {
            callback()
          }
        }
      }
      let checkEndDate = (rule, value, callback) => {
        if (!value) {
          callback(new Error('结束时间不能为空'))
        } else {
          let startDate = this.examForm.startDate
          if (!startDate) {
            callback(new Error('请先设置开始时间'))
          } else {
            if (startDate > value) {
              callback(new Error('结束时间必须晚于开始时间'))
            } else {
              callback()
            }
          }
        }
      }
      return {
        loading: false,
        examForm: {
          title: '',
          questionCount: null,
          questionScore: null,
          startDate: '',
          endDate: '',
          students: ''
        },
        rules: {
          title: [
            {required: true, message: '请输入考试标题', trigger: 'blur'}
          ],
          questionCount: [
            {required: true, type: 'number', message: '题目数量必须为数字', trigger: 'blur'},
            {validator: checkNumber, trigger: 'blur'}
          ],
          questionScore: [
            {required: true, type: 'number', message: '题目分数必须为数字', trigger: 'blur'},
            {validator: checkNumber, trigger: 'blur'}
          ],
          startDate: [
            {required: true, validator: checkStartDate, trigger: 'blur'}
          ],
          endDate: [
            {required: true, validator: checkEndDate, trigger: 'blur'}
          ],
          students: [
            {required: true, message: '考试名单不能为空', trigger: 'blur'}
          ]
        }
      }
    },
    methods: {
      downloadTemplate: function () {
        let token = getAndRefreshJwt()
        if (token) {
          downloadFile('exam/template', token)
        } else {  // 登录超时
          this.$router.push({path: '/login', query: {errMsg: '登录超时，请重新登录'}})
        }
      },
      uploadStudent: function (event) {
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
          let studentArray = XLSX.utils.sheet_to_json(wb.Sheets[wb.SheetNames[0]])
          let numberArr = []
          for (let obj of studentArray) {
            numberArr.push(obj.studentId)
          }
          let studentsStr = numberArr.join('\n')
          vm.examForm.students = studentsStr
          // reset input元素，不然下一次的onchange事件不会触发
          obj.value = ''
        }
        reader.readAsBinaryString(f)
      },
      formatData: function () {
        let data = {}
        data.courseId = parseInt(localStorage.getItem('courseId'))
        data.title = this.examForm.title
        data.questionCount = this.examForm.questionCount
        data.questionScore = this.examForm.questionScore
        data.startTime = this.examForm.startDate.getTime()
        data.endTime = this.examForm.endDate.getTime()
        data.students = this.examForm.students.split(/\s+/)
        return data
      },
      submitForm: function (formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            this.loading = true
            let data = this.formatData()
            this.postExam(data)
          } else {
            return false
          }
        })
      },
      postExam: function (data) {
        let vm = this
        let token = getAndRefreshJwt()
        if (token) {
          let authHeader = {Authorization: 'Bearer ' + token}
          httpJson.post('exam/add', data, {headers: authHeader})
            .then(function (res) {
              if (res.data.code === 200) {
                // 上传exam完成
                vm.loading = false
                vm.$message({
                  message: '新建考试成功！',
                  type: 'success'
                })
                vm.$refs['examForm'].resetFields()
              } else {
                vm.loading = false
                vm.$message.error('新建考试失败！')
              }
            })
            .catch(function (err) {
              console.error(err)
              vm.loading = false
              vm.$message.error('新建考试失败！')
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
  .formBody {
    margin: auto;
    width: 520px;
  }

  .inputfile {
    width: 0.1px;
    height: 0.1px;
    opacity: 0;
    overflow: hidden;
    position: absolute;
    z-index: -1;
  }

  .inputfile + label {
    border-radius: 3px;
    font-size: 12px;
    font-weight: 500;
    padding: 9px 15px;
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
    margin: 0;
  }
</style>
