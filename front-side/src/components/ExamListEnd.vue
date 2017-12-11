<template>
  <div>
    <h3>已结束的考试</h3>
    <el-table :data="tableData" style="width: 100%"
              v-loading="loading"
              element-loading-text="加载中">
      <el-table-column label="考试名称" prop="title" header-align="center"></el-table-column>
      <el-table-column label="问题数量" prop="questionCount" width="80" header-align="center"></el-table-column>
      <el-table-column label="问题分数(分/题)" prop="score" width="80" header-align="center"></el-table-column>
      <el-table-column label="开始时间" prop="startTime" header-align="center"></el-table-column>
      <el-table-column label="结束时间" prop="endTime" header-align="center"></el-table-column>
      <el-table-column label="操作" v-if="isTeacher" header-align="center">
        <template slot-scope="scope">
          <el-button size="mini" @click="downloadScore(scope.row)">下载成绩单</el-button>
        </template>
      </el-table-column>
      <el-table-column label="生成试卷" v-if="isStudent" header-align="center">
        <template slot-scope="scope">
          <el-button size="mini" @click="reviewPaper(scope.row, false)">考前</el-button>
          <el-button size="mini" @click="reviewPaper(scope.row, true)">考后</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
  import {getAndRefreshJwt} from '@/utils/auth.js'
  import {http} from '@/utils/http.js'
  import {downloadFile} from '@/utils/downloader.js'

  export default {
    data () {
      return {
        tableData: [],
        loading: false
      }
    },
    computed: {
      isTeacher: function () {
        return localStorage.getItem('userType') === '1'
      },
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
          http.post('exam/query', {status: 1}, {headers: authHeader})
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
      downloadScore: function (row) {
        let examId = row.id
        let token = getAndRefreshJwt()
        let vm = this
        if (token) {
          downloadFile('statistic/scoreReport/' + examId, token, function (err) {
            console.error(err)
            vm.$message.error('下载成绩单失败')
          })
        } else {
          this.$router.push({path: '/login', query: {errMsg: '登录超时，请重新登录'}})
        }
      },
      reviewPaper: function (row, answered) {
        this.$router.push({
          path: '/paperReview',
          query: {
            examId: row.id,
            examName: row.title,
            startTime: row.startTime,
            answered: answered
          }
        })
      }
    },
    created: function () {
      this.getData()
    }
  }
</script>
