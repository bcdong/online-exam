<template>
  <div class="form">
    <el-card>
      <div slot="header" class="header">
        修改密码
      </div>
      <el-alert
        :closable="false"
        v-if="errMsg"
        :title="errMsg"
        type="error">
      </el-alert>
      <el-form ref="passwordForm" :model="passwordForm" :rules="rules">
        <el-form-item label="旧密码" prop="oldPassword">
          <el-input type="password" v-model="passwordForm.oldPassword" auto-complete="off" placeholder="请输入旧密码"></el-input>
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword">
          <el-input type="password" v-model="passwordForm.newPassword"  auto-complete="off" placeholder="请输入新密码"  @keyup.enter.native="submitForm('passwordForm')"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitForm('passwordForm')">确认</el-button>
          <el-button @click="resetForm('passwordForm')">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
  import {getAndRefreshJwt} from '@/utils/auth.js'

  export default {
    props: {
      errMsgProp: {
        type: String,
        default: ''
      }
    },
    data () {
      return {
        passwordForm: {
          oldPassword: '',
          newPassword: ''
        },
        errMsg: this.errMsgProp,
        rules: {
          oldPassword: [
            {required: true, message: '请输入密码', trigger: 'blur'}
          ],
          newPassword: [
            {required: true, message: '请输入密码', trigger: 'blur'},
            {min: 8, message: '密码长度不得少于8位', trigger: 'blur'}
          ]
        }
      }
    },
    methods: {
      submitForm (formName) {
        this.errMsg = ''
        let vm = this
        this.$refs[formName].validate((valid) => {
          if (valid) {
            let data = {
              oldPassword: this.passwordForm.oldPassword,
              newPassword: this.passwordForm.newPassword
            }
            let token = getAndRefreshJwt()
            let authHeader = {Authorization: 'Bearer ' + token}
            if (token) {
              this.$http.post('auth/updatePassword', data, {headers: authHeader})
                .then(function (res) {
                  if (res.data.code === 200) {
                    vm.$router.push('/login')
                    vm.$message({
                      type: 'success',
                      message: '修改密码,请重新登录'
                    })
                  } else {
                    if (res.data.code === 701) {
                      vm.errMsg = '用户不存在'
                    } else if (res.data.code === 401) {
                      vm.errMsg = '旧密码错误'
                    }
                  }
                })
                .catch(function (err) {
                  console.error(err)
                })
            } else {
              this.$router.push({path: '/login', query: {errMsg: '登录超时，请重新登录'}})
            }
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
