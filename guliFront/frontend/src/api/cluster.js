import request from '@/utils/request'

export function getCluster(params) {
  return request({
    url: '/cluster/all',
    method: 'get',
    params
  })
}

export function getClusterList(params) {
  return request({
    url: '/cluster/names',
    method: 'get',
    params
  })
}

export function getReplicaList(params) {
  return request({
    url: `/cluster/replica_list?${params}`,
    method: 'get',
    params
  })
}

export function getReplicaDetail(params) {
  return request({
    url: `/cluster/replica_detail?${params}`,
    method: 'get',
    params
  })
}
