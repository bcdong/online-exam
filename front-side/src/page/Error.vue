<template>
  <div>
    <el-alert
      title="访问错误"
      type="error"
      :closable="false"
      :description="msg"
      center
      show-icon>
    </el-alert>
  </div>
</template>

<script>
  export default {
    props: {
      msg: {
        type: String,
        default: '未知错误'
      }
    }
  }
</script>
