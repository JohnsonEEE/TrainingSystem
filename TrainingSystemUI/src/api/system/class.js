import request from '@/utils/request'

// 查询课程列表
export function listClass(query) {
  return request({
    url: '/training/classList',
    method: 'post',
    params: query
  })
}

// 新建课程
export function addClass(trainingClass) {
  return request({
    url: '/training/addClass',
    method: 'post',
    params: trainingClass
  })
}

// 删除课程
export function delClass(classId) {
  return request({
    url: '/training/delClass',
    method: 'post',
    params: {'classId':classId}
  })
}

// 查询课程
export function getClass(classId) {
  return request({
    url: '/training/getClass',
    method: 'post',
    params: {'classId':classId}
  })
}

// 更新课程
export function updateClass(trainingClass) {
  return request({
    url: '/training/updateClass',
    method: 'post',
    params: trainingClass
  })
}

// 报名课程
export function signUp(trainingSignUp) {
  return request({
    url: '/training/signUp',
    method: 'post',
    params: trainingSignUp
  })
}

// 签到
export function check(trainingSignUp) {
  return request({
    url: '/training/check',
    method: 'post',
    params: trainingSignUp
  })
}

// 完成
export function complete(trainingSignUp) {
  return request({
    url: '/training/complete',
    method: 'post',
    params: trainingSignUp
  })
}

// 取消报名课程
export function cancelSignUp(trainingSignUp) {
  return request({
    url: '/training/cancelSignUp',
    method: 'post',
    params: trainingSignUp
  })
}

// 查询报名列表
export function listSignUp(query) {
  return request({
    url: '/training/signList',
    method: 'post',
    params: query
  })
}