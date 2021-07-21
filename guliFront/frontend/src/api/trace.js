import request from '@/utils/request'

export function getRealtimeTrace(params) {
  return request({
    url: `/trace/realtime?${params}`,
    method: 'get',
    params
  })
}

export function getHistoryTrace(params) {
  return request({
    url: `/trace/history?${params}`,
    method: 'get',
    params
  })
}

export function getRealtimeTraceDetail(params) {
  return request({
    url: `/trace/realtime_trace?${params}`,
    method: 'get',
    params
  })
}

export function getHistoryTraceDetail(params) {
  return request({
    url: `/trace/history_trace?${params}`,
    method: 'get',
    params
  })
}
