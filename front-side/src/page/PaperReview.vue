<template>
  <div>
    <div class="nav">
      <span class="exam-description">考试名称： {{ examName }}</span>
      <span class="exam-description">考试时间： {{ startTime }}</span>
    </div>
    <div v-if="isLoadingFail">
      <el-alert
        title="试卷加载失败"
        type="error"
        :closable="false"
        center
        show-icon
        style="margin-top: 51px">
      </el-alert>
    </div>
    <div
      v-else="isLoadingFail"
      v-loading="loading"
      element-loading-text="加载中">
      <div class="question-list">
        <el-card
          v-for="(q, index) in questions"
          :key="q.id"
          class="question-card">
          <div slot="header">
          <span>
            {{ index + 1 }}.
            {{ q.type === 0 ? '(单选题)' : '(多选题)' }}&nbsp;
            {{ q.content }}
          </span>
          </div>
          <el-radio-group v-if="q.type === 0" v-model="answers[index][0]">
            <el-radio v-for="(opt, idx) in q.options" :label="opt.key" :key="opt.key" class="option">{{ int2Char(idx) }}. {{ opt.content }}</el-radio>
          </el-radio-group>
          <el-checkbox-group v-if="q.type === 1" v-model="answers[index]">
            <el-checkbox v-for="(opt, idx) in q.options" :label="opt.key" :key="opt.key" class="option">{{ int2Char(idx) }}. {{ opt.content }}</el-checkbox>
          </el-checkbox-group>
        </el-card>
      </div>
    </div>
  </div>
</template>

<script>
  import {http} from '@/utils/http.js'
  import {getAndRefreshJwt} from '@/utils/auth.js'

  export default {
    data () {
      return {
        isLoadingFail: false,
        loading: true,
        questions: [],
        answers: null
      }
    },
    props: {
      examName: String,
      startTime: String,
      examId: Number,
      // 是否时有答案的试卷
      answered: Boolean
    },
    methods: {
      int2Char: function (n) {
        // 65代表A
        return String.fromCharCode(65 + n)
      },
      initPaper: function () {
        let token = getAndRefreshJwt()
        if (token) {
          let authHeader = {Authorization: 'Bearer ' + token}
          let vm = this
          let url = this.answered ? 'statistic/answeredPaper' : 'statistic/blankPaper'
          http.post(url, {examId: this.examId}, {headers: authHeader})
            .then(function (res) {
              if (res.data.code === 200) {
                console.log(res)
                // 初始化试卷数据
                let paper = null
                let answerList = null
                if (vm.answered) {
                  paper = res.data.data.paperBlank
                  answerList = res.data.data.studentAnswer
                } else {
                  paper = res.data.data
                }
                // TODO
                console.log(answerList)
                console.log(paper)
                vm.questions = paper.questions
                vm.answers = []
                for (let i = 0; i < vm.questions.length; ++i) {
                  let qid = vm.questions[i].id
                  let ans = []
                  if (answerList.length > 0) {
                    let a = answerList[i]
                    if (a.qid === qid) {
                      ans = a.ans
                    } else {
                      for (let as of answerList) {
                        if (as.qid === qid) {
                          ans = as.ans
                          break
                        }
                      }
                    }
                  }
                  vm.answers.push(ans)
                }
              } else {
                console.error(res.data)
                vm.isLoadingFail = true
                if (res.data.code === 702) {
                  vm.$message.error('您未参加本场考试')
                }
              }
              vm.loading = false
            })
            .catch(function (err) {
              console.error(err)
              vm.loading = false
              vm.isLoadingFail = true
            })
        } else {
          this.loading = false
          this.$router.push({path: '/login', query: {errMsg: '登录超时，请重新登录'}})
        }
      }
    },
    created: function () {
      this.initPaper()
    }
  }
</script>

<style scoped>
  .nav {
    position: fixed;
    top: 0;
    right: 0;
    width: 70%;
    height: 50px;
    margin: 0;
    padding: 0 15%;
    background-color: white;
    border-bottom: solid 1px #D8DCE5;
    z-index: 2;
    text-align: left;
  }

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

  .exam-description {
    /*position: absolute;*/
    float: left;
    font-size: 18px;
    padding: 10px 10px;
    margin-left: 50px;
    color: #2D2F33;
  }
</style>
