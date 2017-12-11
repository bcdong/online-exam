import Vue from 'vue'
import Router from 'vue-router'
import Login from '@/page/Login'
import Register from '@/page/Register'
import Student from '@/page/Student'
import Teacher from '@/page/Teacher'
import Examing from '@/page/Examing'
import PaperReview from '@/page/PaperReview'
import ErrorPage from '@/page/Error'
import QuestionManage from '@/components/teacher/QuestionManage'
import CreateExam from '@/components/teacher/CreateExam'
import ExamListEnd from '@/components/ExamListEnd'
import ExamListOn from '@/components/ExamListOn'
import ExamListCreated from '@/components/ExamListCreated'
import UpdatePassword from '@/components/UpdatePassword'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      redirect: '/login'
    },
    {
      path: '/login',
      name: 'login',
      component: Login,
      props: (route) => ({
        errMsgProp: route.query.errMsg
      })
    },
    {
      path: '/register',
      name: 'register',
      component: Register
    },
    {
      path: '/error',
      name: 'error',
      component: ErrorPage,
      props: (route) => ({
        msg: route.query.msg
      })
    },
    {
      // student home
      path: '/home',
      name: 'studentHome',
      component: Student,
      beforeEnter: (to, from, next) => {
        if (localStorage.getItem('userType') !== '0') {
          next('/error?msg=对不起，您无权访问该页面')
        } else {
          next()
        }
      },
      children: [
        {
          path: 'exam-on',
          component: ExamListOn
        },
        {
          path: 'exam-created',
          component: ExamListCreated
        },
        {
          path: 'exam-end',
          component: ExamListEnd
        },
        {
          path: 'update-password',
          component: UpdatePassword
        }
      ]
    },
    {
      path: '/examing',
      name: 'examing',
      component: Examing,
      beforeEnter: (to, from, next) => {
        if (localStorage.getItem('userType') !== '0') {
          next('/error?msg=对不起，您无权访问该页面')
        } else {
          next()
        }
      }
    },
    {
      path: '/paperReview',
      name: 'paperReview',
      component: PaperReview,
      props: (route) => ({
        examId: route.query.examId,
        examName: route.query.examName,
        startTime: route.query.startTime,
        answered: route.query.answered
      })
    },
    {
      path: '/teacherhome',
      name: 'teacherHome',
      component: Teacher,
      beforeEnter: (to, from, next) => {
        if (localStorage.getItem('userType') !== '1') {
          next('/error?msg=对不起，您无权访问该页面')
        } else {
          next()
        }
      },
      children: [
        {
          path: 'exam-on',
          component: ExamListOn
        },
        {
          path: 'exam-created',
          component: ExamListCreated
        },
        {
          path: 'exam-end',
          component: ExamListEnd
        },
        {
          path: 'newexam',
          component: CreateExam
        },
        {
          path: 'questions',
          component: QuestionManage
        },
        {
          path: 'update-password',
          component: UpdatePassword
        }
      ]
    }
  ]
})
