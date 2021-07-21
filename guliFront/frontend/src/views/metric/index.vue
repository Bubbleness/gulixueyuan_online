<template>
  <div class="app-container">
    <div class="filter-container">
      <el-cascader
        v-model="value"
        :options="options"
        class="filter-item"
        filterable
        clearable
        placeholder="cluster/node"
        :props="{ expandTrigger: 'hover' }"
      />
      <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleChange">
        search
      </el-button>
    </div>

    <el-table :data="realtimeData" :header-cell-style="{background:'#eee',color:'#606266'}" border fit highlight-current-row>
      <el-table-column property="name" label="realtime metric" width="150" />
      <el-table-column property="value" label="value" width="150" />
      <el-table-column property="description" label="description" />
    </el-table>

    <el-table :data="historyData" :header-cell-style="{background:'#eee',color:'#606266'}" style="margin-top:20px;" border fit highlight-current-row>
      <el-table-column property="name" label="history event" width="150" />
      <el-table-column property="value" label="value" width="150" />
      <el-table-column property="description" label="description" />
    </el-table>
  </div>
</template>

<script>
import { getClusterList, getReplicaList } from '@/api/cluster'
import { getHistoryMetric, getRealtimeMetric } from '@/api/metric'

export default {
  data() {
    return {
      value: '',
      options: [],
      realtimeData: [],
      historyData: []
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    fetchData() {
      getClusterList().then(response => {
        response.data.forEach(cluster => {
          getReplicaList(`pclusterName=${cluster}`).then(res => {
            var child = []
            child.push({
              label: 'all',
              value: 'all',
              children: [{ label: 'all', value: 'all' }, { label: 'realtime metrics', value: 'realtime' }, { label: 'history metrics', value: 'history' }]
            })
            res.data.forEach(e => {
              child.push({
                label: 'hostName: ' + e['hostName'] + ' hostAddress: ' + e['hostAddr'] + ' port: ' + e['port'],
                value: e['hostName'] + ',' + e['hostAddr'] + ',' + e['port'],
                children: [{ label: 'all', value: 'all' }, { label: 'realtime metrics', value: 'realtime' }, { label: 'history metrics', value: 'history' }]
              })
            })
            this.options.push({
              label: cluster,
              value: cluster,
              children: child
            })
          })
        })
      })
    },
    handleRealtimeMetric(cluster, node) {
      getRealtimeMetric(`pclusterName=${cluster}&node=${node}`).then(res => {
        res.data.forEach(e => {
          this.realtimeData.push({
            name: e['metric'],
            value: e['value'],
            description: e['description']
          })
        })
      })
    },
    handleHistoryMetric(cluster, node) {
      getHistoryMetric(`pclusterName=${cluster}&node=${node}`).then(res => {
        res.data.forEach(e => {
          this.historyData.push({
            name: e['metric'],
            value: e['value'],
            description: e['description']
          })
        })
      })
    },
    handleChange() {
      this.realtimeData = []
      this.historyData = []
      if (this.value.length === 0) {
        this.$message.error('Please select cluster/node!')
        return
      }
      if (this.value[2] === 'realtime') {
        this.handleRealtimeMetric(this.value[0], this.value[1])
      } else if (this.value[2] === 'history') {
        this.handleHistoryMetric(this.value[0], this.value[1])
      } else {
        this.handleRealtimeMetric(this.value[0], this.value[1])
        this.handleHistoryMetric(this.value[0], this.value[1])
      }
    }
  }
}
</script>
