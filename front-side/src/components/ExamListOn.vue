<template>
  <div>
    <h3>进行中的考试</h3>
    <el-table :data="tableData" style="width: 100%"
              v-loading="loading"
              element-loading-text="加载中">
      <el-table-column label="考试名称" prop="title" header-align="center"></el-table-column>
      <el-table-column label="问题数量" prop="questionCount" width="80" header-align="center"></el-table-column>
      <el-table-column label="问题分数(分/题)" prop="score" width="80" header-align="center"></el-table-column>
      <el-table-column label="开始时间" prop="startTime" header-align="center"></el-table-column>
      <el-table-column label="结束时间" prop="endTime" header-align="center"></el-table-column>
      <el-table-column label="操作" v-if="isStudent" header-align="center">
        <template slot-scope="scope">
          <el-button size="mini" @click="joinExam(scope.row)">参加考试</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog title="请输入考试密码"
               :visible.sync="dialogVisible"
               width="40%"
               v-loading="loadingPaper"
               element-loading-text="获取试卷中">
      <span>考试密码：</span>
      <el-input v-model="examPassword" style="width: 400px; margin-left: 24px"></el-input>
      <span slot="footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="loadPaper">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
  import {getAndRefreshJwt} from '@/utils/auth.js'
  import {http} from '@/utils/http.js'
  import Vue from 'vue'

  export default {
    data () {
      return {
        tableData: [],
        loading: false,
        loadingPaper: false,
        dialogVisible: false,
        examPassword: '',
        selectedExamId: null
      }
    },
    computed: {
      isStudent: function () {
        return localStorage.getItem('userType') === '0'
      }
    },
    methods: {
      getData: function () {
        let vm = this
        let token = getAndRefreshJwt()
        if (token) {
          vm.loading = true
          let authHeader = {Authorization: 'Bearer ' + token}
          http.post('exam/query', {status: 0}, {headers: authHeader})
            .then(function (res) {
              vm.loading = false
              if (res.data.code === 200) {
                let arr = res.data.data
                for (let o of arr) {
                  o.startTime = new Date(o.startTime).toLocaleString()
                  o.endTime = new Date(o.endTime).toLocaleString()
                }
                vm.tableData = arr
              } else {
                vm.$message.error(res.data.msg)
              }
            })
            .catch(function (err) {
              vm.loading = false
              console.error(err)
              vm.$message.error('加载失败')
            })
        } else {
          this.$router.push({path: '/login', query: {errMsg: '登录超时，请重新登录'}})
        }
      },
      joinExam: function (row) {
        this.selectedExamId = row.id
        this.dialogVisible = true
      },
      loadPaper: function () {
        let token = getAndRefreshJwt()
        if (token) {
          let vm = this
          this.loadingPaper = true
          let authHeader = {Authorization: 'Bearer ' + token}
          http.post('examing/generate',
            {examId: this.selectedExamId, password: this.examPassword},
            {headers: authHeader})
            .then(function (res) {
              vm.loadingPaper = false
              if (res.data.code === 200) {
                let blankPaper = res.data.data
                vm.dialogVisible = false
                // 全局变量
                Vue.prototype.$examPaper = blankPaper
                vm.$router.push('/examing')
              } else if (res.data.code === 400) {
                vm.$message.error('考试密码错误')
              }
            })
            .catch(function (err) {
              vm.loadingPaper = false
              console.error(err)
              vm.$message.error('请求失败')
            })
        } else {  // 登录超时
          this.$router.push({path: '/login', query: {errMsg: '登录超时，请重新登录'}})
        }
      }
    },
    created: function () {
      this.getData()
    }
  }
</script>
