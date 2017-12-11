<template>
  <div class="form">
    <img src="../assets/logo.png" style="padding-bottom: 30px">
    <el-card>
      <div slot="header" class="header">
        Sign up to Online-exam System
      </div>
      <el-alert
        :closable="false"
        v-if="errMsg"
        :title="errMsg"
        type="error">
      </el-alert>
      <el-form ref="registerForm" :model="registerForm" :rules="rules">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="registerForm.username" placeholder="请输入用户名"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input type="password" v-model="registerForm.password" auto-complete="off" placeholder="请输入密码"></el-input>
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input v-model="registerForm.name" placeholder="请输入姓名"></el-input>
        </el-form-item>
        <el-form-item label="学号" prop="number">
          <el-input v-model="registerForm.number" placeholder="请输入学号"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="registerForm.email" placeholder="请输入邮箱"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitForm('registerForm')">注册</el-button>
          <el-button @click="resetForm('registerForm')">重置</el-button>
        </el-form-item>
      </el-form>
      <div>
        Already have an account? <router-link to="/login">Sign in now.</router-link>
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
      let checkEmail = (rule, value, callback) => {
        let reg = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
        if (reg.test(value)) {
          callback()
        } else {
          callback(new Error('邮箱格式错误'))
        }
      }
      return {
        registerForm: {
          username: '',
          password: '',
          name: '',
          email: '',
          number: ''
        },
        errMsg: this.errMsgProp,
        rules: {
          username: [
            {required: true, message: '请输入用户名', trigger: 'blur'}
          ],
          password: [
            {required: true, message: '请输入密码', trigger: 'blur'},
            {min: 8, message: '密码长度不得少于8位', trigger: 'blur'}
          ],
          name: [
            {required: true, message: '请输入姓名', trigger: 'blur'}
          ],
          number: [
            {required: true, message: '请输入学号', trigger: 'blur'}
          ],
          email: [
            {required: true, validator: checkEmail, trigger: 'blur'}
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
            let userJson = {
              username: this.registerForm.username,
              password: this.registerForm.password,
              name: this.registerForm.name,
              studentNumber: this.registerForm.number,
              email: this.registerForm.email
            }
            this.$httpJson.post('auth/register', userJson)
              .then(function (res) {
                if (res.data.code === 200) {
                  // 注册成功
                  vm.$router.push('/login')
                  vm.$message({
                    type: 'success',
                    message: '注册成功,请重新登录'
                  })
                } else {
                  if (res.data.code === 700) {
                    vm.errMsg = '该用户已存在'
                  }
                }
              })
              .catch(function (err) {
                console.error(err)
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
    /*padding-top: 50px;*/
    width: 400px;
  }

  .header {
    font-size: 24px;
  }
</style>
