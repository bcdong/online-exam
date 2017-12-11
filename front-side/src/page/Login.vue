<template>
  <div class="form">
    <img src="../assets/logo.png" style="padding-bottom: 30px">
    <el-card>
      <div slot="header" class="header">
        Sign in to Online-exam System
      </div>
      <el-alert
        :closable="false"
        v-if="errMsg"
        :title="errMsg"
        type="error">
      </el-alert>
      <el-form ref="loginForm" :model="loginForm" :rules="rules">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="loginForm.username" placeholder="请输入用户名"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input type="password" v-model="loginForm.password" auto-complete="off" placeholder="请输入密码" @keyup.enter.native="submitForm('loginForm')"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitForm('loginForm')">登录</el-button>
          <el-button @click="resetForm('loginForm')">重置</el-button>
        </el-form-item>
      </el-form>
      <div>
        No account? <router-link to="/register">Sign up now.</router-link>
      </div>
    </el-card>
  </div>
</template>

<script>
export default {
  props: {
    errMsgProp: {
      type: String,
      default: ''
    }
  },
  data () {
    return {
      loginForm: {
        username: '',
        password: ''
      },
      errMsg: this.errMsgProp,
      rules: {
        username: [
          {required: true, message: '请输入用户名', trigger: 'blur'}
        ],
        password: [
          {required: true, message: '请输入密码', trigger: 'blur'},
          {min: 8, message: '密码长度不得少于8位', trigger: 'blur'}
        ]
      }
    }
  },
  methods: {
    submitForm (formName) {
      this.errMsg = ''
      var vm = this
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.$http.post('auth/login', {username: this.loginForm.username, password: this.loginForm.password})
            .then(function (res) {
              if (res.data.code === 200) {
                // 登录成功
                let userVo = res.data.data
                localStorage.setItem('userId', userVo.id)
                localStorage.setItem('username', userVo.username)
                localStorage.setItem('userType', userVo.type)
                localStorage.setItem('name', userVo.name)
                localStorage.setItem('number', userVo.number)
                localStorage.setItem('courseId', userVo.courseId)
                localStorage.setItem('courseName', userVo.courseName)
                localStorage.setItem('token', userVo.token)
                // 跳转路由到home
                if (userVo.type === 1) {
                  vm.$router.push('/teacherhome/exam-on')
                } else {
                  vm.$router.push('/home/exam-on')
                }
              }
            })
              .catch(function (err) {
                if (err.response) {
                  if (err.response.status === 401 || err.response.status === 403) {
                    vm.errMsg = '对不起，用户名或密码错误'
                  }
                } else if (err.request) {
                  vm.errMsg = '请求超时，请稍后再试'
                }
              })
        } else {
          return false
        }
      })
    },
    resetForm (formName) {
      this.$refs[formName].resetFields()
    }
  }
}
</script>

<style scoped>
  .form {
    margin: auto;
    padding-top: 50px;
    width: 400px;
  }

  .header {
    font-size: 24px;
  }
</style>
