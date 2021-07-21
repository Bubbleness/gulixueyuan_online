import request from '@/utils/request'

export function getDatabase(params) {
  return request({
    url: `/meta/databases?${params}`,
    method: 'get',
    params
  })
}

export function getTable(params) {
  return request({
    url: `/meta/tables?${params}`,
    method: 'get',
    params
  })
}

export function getNodeList(params) {
  return request({
    url: `/meta/tables/node_list?${params}`,
    method: 'get',
    params
  })
}

export function getTableInfo(params) {
  return request({
    url: `/meta/tables/details?${params}`,
    method: 'get',
    params
  })
}

export function getTotalRows(params) {
  return request({
    url: `/meta/tables/total_rows?${params}`,
    method: 'get',
    params
  })
}

export function getTotalBytes(params) {
  return request({
    url: `/meta/tables/total_bytes?${params}`,
    method: 'get',
    params
  })
}

export function getDataPart(params) {
  return request({
    url: `/meta/tables/data_parts?${params}`,
    method: 'get',
    params
  })
}

export function getReplica(params) {
  return request({
    url: `/meta/tables/replicas?${params}`,
    method: 'get',
    params
  })
}
