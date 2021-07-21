import request from '@/utils/request'

export function getList(params) {
  return request({
    url: `/setting?${params}`,
    method: 'get',
    params
  })
}

export function getStatus(params) {
  return request({
    url: `/setting/status?${params}`,
    method: 'get',
    params
  })
}
