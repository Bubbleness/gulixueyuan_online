import request from '@/utils/request'

export function getQueries(pclusterName, start, end, query, queryId, isRunning, pageSize, pageNum, sortString) {
  return request({
    url: `/queries`,
    method: 'post',
    data: {
      'pclusterName': pclusterName,
      'start': start,
      'end': end,
      'query': query,
      'queryId': queryId,
      'isRunning': isRunning,
      'pageSize': pageSize,
      'pageNum': pageNum,
      'sortString': sortString
    }
  })
}

export function getQueryDetail(params) {
  return request({
    url: `/queries/query?${params}`,
    method: 'get',
    params
  })
}

export function getQueryPlan(pclusterName, query) {
  return request({
    url: `/queries/query_plan`,
    method: 'post',
    data: {
      'pclusterName': pclusterName,
      'query': query
    }
  })
}
