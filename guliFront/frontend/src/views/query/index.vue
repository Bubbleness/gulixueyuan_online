<template>
  <div class="app-container">
    <div class="filter-container">
      <el-cascader
        v-model="value"
        :options="options"
        class="filter-item"
        filterable
        clearable
        style="width: 150px;"
        placeholder="cluster"
        :props="{ expandTrigger: 'hover' }"
        @change="handleChange"
      />
      <el-input v-model="queryId" clearable placeholder="query_id" style="width: 120px;" class="filter-item" @keyup.enter.native="getQueryList" />
      <el-input v-model="query" clearable placeholder="query" style="width: 120px;" class="filter-item" @keyup.enter.native="getQueryList" />
      <el-date-picker
        v-model="timeValue"
        type="datetimerange"
        class="filter-item"
        start-placeholder="start time"
        end-placeholder="end time"
        value-format="yyyy-MM-dd HH:mm:ss"
        :default-time="['00:00:00', '23:59:59']"
      />

      <el-select v-model="sort" class="filter-item" filterable placeholder="sort">
        <el-option
          v-for="item in sortOptions"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        />
      </el-select>
      <el-button class="filter-item" type="primary" icon="el-icon-search" @click="getQueryList">
        search
      </el-button>
    </div>
    <el-table v-loading="listLoading" element-loading-text="Loading" :data="queries" :header-cell-style="{background:'#eee',color:'#606266'}" border highlight-current-row>
      <el-table-column property="queryId" label="query_id" width="200" />
      <el-table-column property="query" label="query" />
      <el-table-column property="address" label="address" width="150" />
      <el-table-column property="port" label="port" width="120" />
      <el-table-column property="elapsed" label="elapsed_time(s)" width="140" />
      <el-table-column v-if="typeShow==1" property="type" label="type" width="120" />
      <el-table-column label="operation" width="120">
        <template slot-scope="{row}">
          <el-button type="primary" @click="getQueryInfo(row.queryId, row.query, row.type)">
            detail
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="page" :limit.sync="limit" @pagination="getQueryList" />

    <el-dialog title="Query Info" :visible.sync="dialogTableVisible">
      basic info
      <el-table :show-header="false" :data="gridData" style="margin-bottom:20px;" border highlight-current-row>
        <el-table-column property="key" label="属性名称" width="150">
          <template slot-scope="{row}">
            {{ row.key }}
            <el-button v-if="row.key==''" size="mini" type="primary" @click="explainQuery">
              explain
            </el-button>
          </template>
        </el-table-column>
        <el-table-column property="value" :show-overflow-tooltip="false" label="属性值" style="white-space:pre" />
      </el-table>
      child queries
      <el-table :data="childQuery" style="margin-bottom:20px;" border highlight-current-row>
        <el-table-column property="address" label="IP address" width="140" />
        <el-table-column property="port" label="port" width="70" />
        <el-table-column property="query" label="query" />
      </el-table>
    </el-dialog>
  </div>
</template>

<script>
import { getQueries, getQueryDetail, getQueryPlan } from '@/api/query'
import { getClusterList } from '@/api/cluster'
import Pagination from '@/components/Pagination'

export default {
  components: { Pagination },
  data() {
    return {
      listLoading: true,
      value: '',
      options: [],
      sortOptions: [],
      sortOption1: [{ label: 'elapsed descending', value: 'query_duration_ms DESC' }, { label: 'elapsed ascending', value: 'query_duration_ms ASC' },
        { label: 'start time descending', value: 'query_start_time DESC' }, { label: 'start time ascending', value: 'query_start_time ASC' },
        { label: 'end time descending', value: '(query_start_time + query_duration_ms/1000) DESC' }, { label: 'end time ascending', value: '(query_start_time + query_duration_ms/1000) ASC' }],
      sortOption2: [{ label: 'elapsed descending', value: 'elapsed DESC' }, { label: 'elapsed ascending', value: 'elapsed ASC' }],
      sort: 'query_duration_ms DESC',
      queries: [],
      gridData: [],
      queryId: '',
      query: '',
      currentQuery: '',
      childQuery: [],
      timeValue: '',
      typeShow: 0,
      limit: 30,
      total: 0,
      page: 1,
      dialogTableVisible: false,
      map: { 'isRunning': true, 'history': false }
    }
  },
  created() {
    this.fetchData()
    this.sortOptions = this.sortOption1
  },
  methods: {
    fetchData() {
      this.listLoading = true
      getClusterList().then(response => {
        response.data.forEach(cluster => {
          this.options.push({
            label: cluster,
            value: cluster,
            children: [{ label: 'current queries', value: 'isRunning' }, { label: 'history queries', value: 'history' }]
          })
        })
        this.listLoading = false
      })
    },
    getQueryList() {
      this.queries = []
      this.listLoading = true
      if (this.value.length === 0) {
        this.$message.error('Please select cluster!')
        this.listLoading = false
        return
      }
      if (this.timeValue.length === 0) {
        this.timeValue = ['', '']
      }
      if (this.value[1] === 'history') {
        this.typeShow = 1
      } else {
        this.typeShow = 0
      }
      getQueries(this.value[0], this.timeValue[0], this.timeValue[1], this.query, this.queryId, this.map[this.value[1]], this.limit, this.page, this.sort)
        .then(response => {
          this.total = response.data.count
          this.listLoading = false
          response.data.res.forEach(element => {
            element['query'] = element['query'].replace(/[\n\r]/g, ' ')
            this.queries.push(element)
          })
        })
    },
    handleChange() {
      this.queries = []
      this.total = 0
      if (this.value.length === 0) {
        this.sortOptions = this.sortOption1
      } else if (this.value[1] === 'isRunning') {
        this.sortOptions = this.sortOption2
        if (this.sort.endsWith('DESC')) {
          this.sort = 'elapsed DESC'
        } else {
          this.sort = 'elapsed ASC'
        }
      } else {
        this.sortOptions = this.sortOption1
        if (this.sort === 'elapsed DESC') {
          this.sort = 'query_duration_ms DESC'
        } else if (this.sort === 'elapsed ASC') {
          this.sort = 'query_duration_ms ASC'
        }
      }
    },
    explainQuery() {
      getQueryPlan(this.value[0], this.currentQuery).then(res => {
        this.gridData[this.gridData.length - 1].value = res.data
      })
    },
    getQueryInfo(id, query, type) {
      getQueryDetail(`pclusterName=${this.value[0]}&queryId=${id}&type=${type}&isRunning=${this.map[this.value[1]]}`).then(res => {
        this.dialogTableVisible = true
        this.gridData = []
        const data = res.data
        this.childQuery = []
        this.currentQuery = query
        data.childQuery.forEach(element => {
          this.childQuery.push({
            'query': element['query'].replace(/[\n\r]/g, ' '),
            'address': element['address'],
            'port': element['port']
          })
        })
        if (data.detail == null) { // realtime query在进入详情显示框可能已经结束
          this.gridData.push({ key: 'query_start_time', value: '' }, { key: 'elapsed', value: '' }, { key: 'readRows', value: '' },
            { key: 'writtenRows', value: '' }, { key: 'memoryUsage', value: '' }, { key: 'threadIds', value: [] })
        }

        for (var k in data.detail) {
          if (data.detail[k] !== null) {
            this.gridData.push({
              key: k,
              value: data.detail[k].toString()
            })
          }
        }
        this.gridData.push({
          key: '',
          value: ''
        })
      })
    }
  }
}
</script>

<style type="text/css">
.el-table .cell {
  white-space: pre-line;
  text-overflow: inherit;
}
</style>
