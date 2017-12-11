<template>
  <div class="nav">
    <img src="../../assets/exam-64.png" class="exam-icon">
    <el-button round icon="el-icon-document" @click="openCard" id="ans-card-btn">答题卡</el-button>
    <span style="float: right">
      <el-button type="warning" @click="submitPaper" id="submit-btn">提交试卷</el-button>
      <time-down :end-time="endTime" @timeout="timeEnd" id="timer"></time-down>
    </span>
  </div>
</template>

<script>
  import TimeDown from './TimeDown.vue'

  export default {
    props: {
      endTime: Number
    },
    components: {
      TimeDown
    },
    methods: {
      openCard () {
        this.$emit('openAnsCard')
      },
      submitPaper () {
        this.$emit('submitPaper')
      },
      timeEnd () {
        this.$emit('timeEnd')
      }
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

  img.exam-icon {
    width: 50px;
    height: 50px;
  }

  #ans-card-btn {
    margin-left: 20px;
    margin-top: 4px;
    position: absolute;
  }

  #submit-btn {
    /*margin-right: 20px;*/
    margin-top: 4px;
  }

  #timer {
    margin-left: 20px;
  }
</style>
