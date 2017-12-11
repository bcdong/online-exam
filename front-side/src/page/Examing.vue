<template>
  <div v-if="isLoadingFail">
    <el-alert
      title="试卷加载失败"
      type="error"
      :closable="false"
      center
      show-icon>
    </el-alert>
  </div>
  <div
    v-else="isLoadingFail"
    v-loading="loading"
    element-loading-text="加载中">
    <exam-nav
      :endTime="endTime"
      @submitPaper="submitPaper(false)"
      @openAnsCard="openAnsCard"
      @timeEnd="submitPaper(true)">
    </exam-nav>
    <div class="question-list">
      <el-card
        v-for="(q, index) in questions"
        :key="q.id"
        :id="'q' + index"
        :class="[stars[index] ? 'star-card' : '', questionCardClass]">
        <div slot="header">
          <span>
            {{ index + 1 }}.
            {{ q.type === 0 ? '(单选题)' : '(多选题)' }}&nbsp;
            {{ q.content }}
          </span>
          <el-button class="star-btn" @click="toggleStar(index, $event)">
            <i :class="[stars[index] ? 'el-icon-star-on' : 'el-icon-star-off']"></i>
            <span >{{ stars[index] ? '取消标记' : '&nbsp;标&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;记&nbsp;'}}</span>
          </el-button>
        </div>
        <el-radio-group v-if="q.type === 0" v-model="answers[index].ans[0]" @change="selectChange(index)">
          <el-radio v-for="(opt, idx) in q.options" :label="idx" :key="opt.key" class="option">{{ int2Char(idx) }}. {{ opt.content }}</el-radio>
        </el-radio-group>
        <el-checkbox-group v-if="q.type === 1" v-model="answers[index].ans" @change="selectChange(index)">
          <el-checkbox v-for="(opt, idx) in q.options" :label="idx" :key="opt.key" class="option">{{ int2Char(idx) }}. {{ opt.content }}</el-checkbox>
        </el-checkbox-group>
      </el-card>
    </div>
    <el-dialog title="答题卡"
               :visible.sync="dialogVisible"
                width="40%">
      <div style="margin: auto;display: inline-block">
        <div v-for="row in rowCount" class="ans-card-line">
          <el-button
            size="small"
            v-for="c in columnCount"
            :key="c"
            v-if="(row-1)*columnCount+c <= questions.length"
            :type="btnTypeArr[(row-1)*columnCount+c-1]"
            class="ans-card-btn"
            @click="jump((row-1)*columnCount+c-1)">
            {{ (row - 1) * columnCount + c }}
          </el-button>
        </div>
      </div>
      <span slot="footer">
        <el-button type="primary" @click="dialogVisible = false">关 闭</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
  import ExamNav from '@/components/student/ExamNav.vue'
  import {httpJson} from '@/utils/http.js'
  import {getAndRefreshJwt} from '@/utils/auth.js'

  export default {
    data () {
      return {
        examId: null,
        endTime: null,
        questions: [],
        answers: null,
        isLoadingFail: false,
        stars: [],
        questionCardClass: 'question-card',
        dialogVisible: false,
        rowCount: 0,
        columnCount: 5,
        btnTypeArr: [],
        loading: false
      }
    },
    methods: {
      int2Char: function (n) {
        // 65代表A
        return String.fromCharCode(65 + n)
      },
      jump: function (index) {
        this.dialogVisible = false
        let element = document.getElementById('q' + index)
        let top = element.offsetTop
        // 74是question-list部分原本的margin-top，为了不和顶部栏重合
        window.scrollTo(0, top - 74)
      },
      initPaper: function () {
        let paper = this.$examPaper
        if (!paper) {
          this.isLoadingFail = true
        } else {
          this.isLoadingFail = false
          // 从全局变量中读取试卷数据
          this.examId = paper.examId
          this.endTime = paper.endTime
          this.questions = paper.questions
          // 初始化答题卡
          this.rowCount = Math.ceil(this.questions.length / this.columnCount)
          this.answers = []
          for (let q of this.questions) {
            this.answers.push({qid: q.id, ans: []})
            this.stars.push(false)
            // 默认按钮样式
            this.btnTypeArr.push('')
          }
        }
        // 从全局对象中删除examPaper
        this.$examPaper = undefined
      },
      toggleStar: function (index) {
        let newVal = !this.stars[index]
        this.stars.splice(index, 1, newVal)
        if (newVal) {
          this.btnTypeArr.splice(index, 1, 'warning')
        } else {
          if (this.answers[index].ans.length > 0) {
            this.btnTypeArr.splice(index, 1, 'success')
          } else {
            this.btnTypeArr.splice(index, 1, '')
          }
        }
      },
      submitPaper: function (isTimeOut) {
        this.loading = true
        let token = getAndRefreshJwt()
        if (token) {
          let realAns = []
          for (let i = 0; i < this.answers.length; ++i) {
            let qid = this.answers[i].qid
            let opts = this.questions[i].options
            let arr = []
            for (let idx of this.answers[i].ans) {
              arr.push(opts[idx].key)
            }
            realAns.push({
              qid: qid,
              ans: arr
            })
          }
          let ansObj = {
            examId: this.examId,
            answers: realAns
          }
          let vm = this
          let authHeader = {Authorization: 'Bearer ' + token}
          httpJson.post('examing/commit', ansObj, {headers: authHeader})
            .then(function (res) {
              vm.loading = false
              if (res.data.code === 200) {
                if (res.data.data) {
                  vm.$message({
                    message: '提交成功，请前往邮箱查看考试成绩',
                    type: 'success'
                  })
                } else {
                  console.error(res.data)
                  vm.$message.error('提交失败')
                }
              }
              if (isTimeOut) {
                vm.$router.push({path: '/home/exam-on'})
              }
            })
            .catch(function (err) {
              vm.loading = false
              vm.$message.error('提交失败')
              console.error(err)
              if (isTimeOut) {
                vm.$router.push({path: '/home/exam-on'})
              }
            })
        } else {
          this.loading = false
          this.$router.push({path: '/login', query: {errMsg: '登录超时，请重新登录'}})
        }
      },
      openAnsCard: function () {
        this.dialogVisible = true
      },
      selectChange: function (index) {
        if (this.answers[index].ans.length > 0) {
          if (!this.btnTypeArr[index]) {
            this.btnTypeArr.splice(index, 1, 'success')
          }
        } else {
          if (this.btnTypeArr[index] === 'success') {
            this.btnTypeArr.splice(index, 1, '')
          }
        }
      }
    },
    created: function () {
      this.initPaper()
    },
    components: {
      ExamNav
    }
  }
</script>

<style scoped>
  .question-list {
    width:50%;
    margin: 74px auto auto;
  }

  .question-card {
    margin-top: 24px;
    text-align: left;
  }

  .option {
    display: block;
    margin-left: 30px;
    padding: 5px 0;
    font-size: 16px;
  }

  .star-btn {
    float: right;
    color: orange;
    border: none;
    background-color: transparent;
  }

  .el-button:focus, .el-button:hover {
    /*color: orange;*/
    border-color: inherit;
    /*background-color: transparent;*/
  }

  .star-card {
    background-color: rgba(255, 234, 173, 0.6);
  }

  .ans-card-line {
    text-align: left;
  }

  .ans-card-btn {
    margin-top: 10px;
    padding: 15px;
    width: 50px;
    height: 50px;
    border-radius: 50%;
  }
</style>
