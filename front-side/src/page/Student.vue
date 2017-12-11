<template>
  <div>
    <el-menu class="navbar" :default-active="activeIndex" mode="horizontal" @select="handleSelect">
      <el-menu-item index="1">进行中考试</el-menu-item>
      <el-menu-item index="2">未开始考试</el-menu-item>
      <el-menu-item index="3">已结束考试</el-menu-item>
      <el-submenu index="4" class="user-info">
        <template slot="title"><img class="avatar" src="../assets/student-64.png" width="64" height="64"/>{{ name }}</template>
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
          this.$router.push('/home/exam-on')
        } else if (keyPath[0] === '2') {
          this.$router.push('/home/exam-created')
        } else if (keyPath[0] === '3') {
          this.$router.push('/home/exam-end')
        } else if (keyPath[0] === '4') {
          if (keyPath[1] === '4-1') {
            this.$router.push('/home/update-password')
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
          rt = '1'
        } else if (name === 'exam-created') {
          rt = '2'
        } else if (name === 'exam-end') {
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
