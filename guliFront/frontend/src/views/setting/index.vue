<template>
  <div class="app-container">
    <div class="filter-container">
      <el-select v-model="cluster" class="filter-item" filterable clearable placeholder="cluster" @change="handleChange">
        <el-option
          v-for="item in options"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        />
      </el-select>
      <el-input v-model="name" clearable placeholder="name" style="width: 200px;" class="filter-item" @keyup.enter.native="selectSetting" />
      <el-select v-model="status" class="filter-item" filterable clearable placeholder="type">
        <el-option
          v-for="item in statusOptions"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        />
      </el-select>
      <el-button class="filter-item" type="primary" icon="el-icon-search" @click="selectSetting">
        search
      </el-button>
    </div>
    <el-table
      v-loading="listLoading"
      :data="list"
      :header-cell-style="{background:'#eee',color:'#606266'}"
      element-loading-text="Loading"
      border
      fit
      highlight-current-row
    >
      <el-table-column label="name" width="150">
        <template slot-scope="scope">
          {{ scope.row.name }}
        </template>
      </el-table-column>
      <el-table-column label="value" width="150">
        <template slot-scope="scope">
          {{ scope.row.values }}
        </template>
      </el-table-column>
      <el-table-column label="description">
        <template slot-scope="scope">
          {{ scope.row.description }}
        </template>
      </el-table-column>
      <el-table-column label="type" width="200">
        <template slot-scope="scope">
          <span>{{ scope.row.type }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="status"
        width="120"
      >
        <template slot-scope="scope">
          <el-button v-if="scope.row.status==1" size="mini" type="success" @click="handleStatus(scope.row.name)">
            consistent
          </el-button>
          <el-button v-if="scope.row.status!=1" size="mini" type="danger" @click="handleStatus(scope.row.name)">
            inconsistent
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="page" :limit.sync="limit" @pagination="selectPage" />

    <el-dialog title="Setting Info" :visible.sync="dialogTableVisible">
      <el-table :show-header="true" :data="gridData" style="margin-bottom:20px;" border fit highlight-current-row>
        <el-table-column property="key" label="value" width="150" />
        <el-table-column property="value" label="node list" />
      </el-table>
    </el-dialog>
  </div>
</template>

<script>
import { getList, getStatus } from '@/api/setting'
import { getClusterList } from '@/api/cluster'
import Pagination from '@/components/Pagination'

export default {
  components: { Pagination },
  filters: {
    statusFilter(status) {
      const statusMap = {
        published: 'success',
        draft: 'gray',
        deleted: 'danger'
      }
      return statusMap[status]
    }
  },
  data() {
    return {
      list: null,
      listAll: null,
      listLoading: true,
      options: [],
      cluster: '',
      name: '',
      dialogTableVisible: false,
      gridData: [],
      status: '',
      limit: 30,
      total: 0,
      page: 1,
      statusOptions: [{
        value: 1,
        label: 'consistent'
      }, {
        value: 0,
        label: 'inconsistent'
      }]
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    fetchData() {
      this.listLoading = true
      getClusterList().then(response => {
        response.data.forEach(element => {
          const arr = {
            value: element,
            label: element
          }
          this.options.push(arr)
          this.listLoading = false
        })
      })
    },

    selectSetting() {
      this.list = []
      this.listLoading = true
      if (this.cluster === '') {
        this.$message.error('Please select cluster!')
        this.listLoading = false
        return
      }
      getList(`pclusterName=${this.cluster}&settingName=${this.name}&status=${this.status}`).then(response => {
        this.listAll = response.data // get the whole data
        this.listLoading = false
        this.total = this.listAll.length
        this.list = this.listAll.slice(0, this.limit) // slice to the first page
      })
    },

    selectPage() {
      this.list = this.listAll.slice((this.page - 1) * this.limit, this.page * this.limit)
    },

    handleChange() {
      this.list = []
      this.total = 0
    },

    handleStatus(val) {
      this.dialogTableVisible = true
      this.gridData = []
      this.listLoading = true
      getStatus(`pclusterName=${this.cluster}&settingName=${val}`).then(response => {
        for (var k in response.data) {
          var data = ''
          response.data[k].forEach(e => {
            data += 'hostName: ' + e['hostName'] + ' hostAddress: ' + e['hostAddress'] + ' port: ' + e['tcpPort'] + '\n'
          })
          this.gridData.push({
            key: k,
            value: data
          })
        }
        this.listLoading = false
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
