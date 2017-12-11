<template>
  <span class="clock">
    <i class="el-icon-time"></i> {{ remainTime }}
  </span>
</template>

<script>
  export default {
    data () {
      return {
        remainTime: '',
        flag: false
      }
    },
    props: {
      endTime: Number
    },
    methods: {
      countDown () {
        const endTime = new Date(this.endTime)
        const nowTime = new Date()
        let leftTime = Math.floor((endTime.getTime() - nowTime.getTime()) / 1000)
        let h = this.format(Math.floor(leftTime / 3600))
        let m = this.format(Math.floor(leftTime / 60 % 60))
        let s = this.format(Math.floor(leftTime % 60))
        this.remainTime = `${h}:${m}:${s}`
        if (leftTime <= 0) {
          this.flag = true
        }
      },
      format (t) {
        if (t >= 10 || t < 0) {
          return t.toString()
        } else {
          return `0${t}`
        }
      }
    },
    mounted () {
      let clock = setInterval(() => {
        if (this.flag === true) {
          clearInterval(clock)
          this.$emit('timeout')
        }
        this.countDown()
      }, 1000)
    }
  }
</script>

<style scoped>
  .clock {
    font-size: 18px;
    color: #878D99;
  }
</style>
