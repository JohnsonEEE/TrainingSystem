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