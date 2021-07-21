import request from '@/utils/request'

export function getHistoryMetric(params) {
  return request({
    url: `/metric/history?${params}`,
    method: 'get',
    params
  })
}

export function getRealtimeMetric(params) {
  return request({
    url: `/metric/realtime?${params}`,
    method: 'get',
    params
  })
}
