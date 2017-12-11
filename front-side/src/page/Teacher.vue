<template>
  <div>
    <el-menu class="navbar" :default-active="activeIndex" mode="horizontal" @select="handleSelect" ref="menu">
      <el-submenu index="1">
        <template slot="title">考试列表</template>
        <el-menu-item index="1-1">进行中</el-menu-item>
        <el-menu-item index="1-2">未开始</el-menu-item>
        <el-menu-item index="1-3">已结束</el-menu-item>
      </el-submenu>
      <el-menu-item index="2">试题管理</el-menu-item>
      <el-menu-item index="3">发布考试</el-menu-item>
      <el-submenu index="4" class="user-info">
        <template slot="title"><img class="avatar" src="../assets/teacher-64.png" width="64" height="64"/>{{ name }}</template>
        <el-menu-item index="4-1">修改密码</el-menu-item>
        <el-menu-item index="4-2">退出登录</el-menu-item>
      </el-submenu>
    </el-menu>
    <div class="main-body">
      <router-view></router-view>
    </div>
  </div>
</template>

<script>
  export default {
    data () {
      return {
        name: '',
        activeIndex: ''
      }
    },
    methods: {
      handleSelect (key, keyPath) {
        if (keyPath[0] === '1') {
          if (keyPath[1] === '1-1') {
            this.$router.push('/teacherhome/exam-on')
          } else if (keyPath[1] === '1-2') {
            this.$router.push('/teacherhome/exam-created')
          } else if (keyPath[1] === '1-3') {
            this.$router.push('/teacherhome/exam-end')
          }
        } else if (keyPath[0] === '2') {
          this.$router.push('/teacherhome/questions')
        } else if (keyPath[0] === '3') {
          this.$router.push('/teacherhome/newexam')
        } else if (keyPath[0] === '4') {
          if (keyPath[1] === '4-1') {
            this.$router.push('/teacherhome/update-password')
          } else if (keyPath[1] === '4-2') {
            localStorage.clear()
            this.$router.push('/login')
          }
        }
      },
      updateActiveIndex: function (path) {
        let start = path.lastIndexOf('/', path.length - 2)
        let name = path.substring(start + 1)
        if (name.charAt(name.length - 1) === '/') {
          name = name.slice(0, name.length - 1)
        }
        let rt = ''
        if (name === 'exam-on') {
          rt = '1-1'
        } else if (name === 'exam-created') {
          rt = '1-2'
        } else if (name === 'exam-end') {
          rt = '1-3'
        } else if (name === 'questions') {
          rt = '2'
        } else if (name === 'newexam') {
          rt = '3'
        }
        this.activeIndex = rt
      }
    },
    created: function () {
      this.name = localStorage.getItem('name')
      this.updateActiveIndex(this.$route.path)
    },
    watch: {
      '$route' (to) {
        this.updateActiveIndex(to.path)
      }
    }
  }
</script>

<style scoped>
  .navbar {
    padding: 0 200px;
  }

  .avatar {
    border-radius: 50%;
    background-color: white;
  }

  .user-info {
    float: right;
  }

  .main-body {
    margin: 30px 200px 0;
  }
</style>
