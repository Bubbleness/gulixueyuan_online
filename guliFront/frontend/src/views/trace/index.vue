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
        @change="clusterChange"
      />
      <el-input
        v-model="id"
        class="filter-item"
        clearable
        placeholder="query id or thread id"
        style="width:200px"
        @keyup.enter.native="handleChange"
      />
      <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleChange">
        search
      </el-button>
    </div>

    <el-table v-show="typeShow!=0" :data="realtimeData" :header-cell-style="{background:'#eee',color:'#606266'}" border highlight-current-row>
      <el-table-column property="threadId" label="thread_id" width="200" />
      <el-table-column property="queryId" label="query_id" width="200" />
      <el-table-column property="trace" label="trace" />
      <el-table-column property="detail" label="detail" width="300">
        <template slot-scope="{row}">
          <el-button type="primary" @click="selectRealtimeTrace(row.trace)">
            detail
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-table v-show="typeShow!=1" :data="historyData" :header-cell-style="{background:'#eee',color:'#606266'}" style="margin-top:20px;" border highlight-current-row>
      <el-table-column property="eventTime" label="event_time" width="200" />
      <el-table-column property="threadId" label="thread_id" width="100" />
      <el-table-column property="queryId" label="query_id" width="200" />
      <el-table-column property="traceType" label="trace_type" width="100" />
      <el-table-column property="trace" label="trace" />
      <el-table-column property="detail" label="detail" width="200">
        <template slot-scope="{row}">
          <el-button type="primary" @click="selectHistoryTrace(row.trace)">
            detail
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="page" :limit.sync="limit" @pagination="handleChange" />

    <el-dialog title="Trace Info" :visible.sync="diaglogTableVisible">
      <div class="text-wrapper">{{ trace }}</div>
    </el-dialog>
  </div>
</template>

<script>
import { getClusterList, getReplicaList } from '@/api/cluster'
import { getHistoryTrace, getRealtimeTrace, getRealtimeTraceDetail, getHistoryTraceDetail } from '@/api/trace'
import Pagination from '@/components/Pagination'

export default {
  components: { Pagination },
  data() {
    return {
      listLoading: true,
      value: '',
      options: [],
      realtimeData: [],
      historyData: [],
      id: '',
      diaglogTableVisible: false,
      typeShow: -1,
      trace: '',
      limit: 30,
      total: 0,
      page: 1
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    fetchData() {
      this.listLoading = true
      getClusterList().then(response => {
        response.data.forEach(cluster => {
          getReplicaList(`pclusterName=${cluster}`).then(res => {
            var child = []
            res.data.forEach(e => {
              child.push({
                label: 'hostName: ' + e['hostName'] + ' hostAddress: ' + e['hostAddr'] + ' port: ' + e['port'],
                value: e['hostName'] + ',' + e['hostAddr'] + ',' + e['port'],
                children: [{ label: 'realtime trace', value: 'realtime' }, { label: 'history trace', value: 'history' }]
              })
            })
            this.options.push({
              label: cluster,
              value: cluster,
              children: child
            })
          })
        })
        this.listLoading = false
      })
    },
    selectRealtimeTrace(trace) {
      getRealtimeTraceDetail(`pclusterName=${this.value[0]}&node=${this.value[1]}&trace=${trace}`).then(res => {
        this.diaglogTableVisible = true
        this.trace = res.data
      })
    },
    selectHistoryTrace(trace) {
      console.log(trace)
      getHistoryTraceDetail(`pclusterName=${this.value[0]}&node=${this.value[1]}&trace=${trace}`).then(res => {
        this.diaglogTableVisible = true
        this.trace = res.data
      })
    },
    handleRealtimeTrace(cluster, node, queryId, threadId) {
      this.listLoading = true
      getRealtimeTrace(`node=${node}&queryId=${queryId}&threadId=${threadId}&pageSize=${this.limit}&pageNum=${this.page}`).then(res => {
        this.total = res.data.count
        res.data.trace.forEach(e => {
          this.realtimeData.push({
            threadId: e['threadId'],
            queryId: e['queryId'],
            trace: e['trace'].toString()
          })
        })
        this.listLoading = false
      })
    },
    handleHistoryTrace(cluster, node, queryId, threadId) {
      this.listLoading = true
      getHistoryTrace(`node=${node}&queryId=${queryId}&threadId=${threadId}&pageSize=${this.limit}&pageNum=${this.page}`).then(res => {
        this.total = res.data.count
        res.data.trace.forEach(e => {
          this.historyData.push({
            eventTime: e['eventTime'],
            threadId: e['threadId'],
            queryId: e['queryId'],
            traceType: e['traceType'],
            trace: e['trace'].toString()
          })
        })
        this.listLoading = false
      })
    },
    handleChange() {
      this.realtimeData = []
      this.historyData = []
      if (this.value.length === 0) {
        this.$message.error('Please select cluster/node!')
        return
      }
      if (this.value[2] === 'history') {
        this.typeShow = 0
      } else {
        this.typeShow = 1
      }
      const pcluster = this.value[0]
      const node = this.value[1]
      if (this.value[2] === 'realtime') {
        if (this.id === '') {
          this.handleRealtimeTrace(pcluster, node, '', -1)
        } else if (isNaN(Number(this.id))) {
          this.handleRealtimeTrace(pcluster, node, this.id, -1)
        } else {
          this.handleRealtimeTrace(pcluster, node, '', Number(this.id))
        }
      } else {
        if (this.id === '') {
          this.handleHistoryTrace(pcluster, node, '', -1)
        } else if (isNaN(Number(this.id))) {
          this.handleHistoryTrace(pcluster, node, this.id, -1)
        } else {
          this.handleHistoryTrace(pcluster, node, '', Number(this.id))
        }
      }
    },
    clusterChange() {
      this.realtimeData = []
      this.historyData = []
      this.total = 0
      this.typeShow = -1
    }
  }
}
</script>

<style type="text/css">
.text-wrapper {
  white-space: pre-line;
  text-overflow: inherit;
}
</style>
