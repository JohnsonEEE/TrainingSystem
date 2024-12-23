import request from '@/utils/request'

// 查询课程列表
export function listClass(query) {
  return request({
    url: '/training/classList',
    method: 'post',
    params: query
  })
}
