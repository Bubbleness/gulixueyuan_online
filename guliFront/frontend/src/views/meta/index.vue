<template>
  <div class="app-container">
    <el-input v-model="filterText" placeholder="Filter keyword" style="margin-bottom:20px;" />
    <div class="div-left">
      <el-tree
        ref="tree2"
        :data="data2"
        node-click="id"
        :props="defaultProps"
        :filter-node-method="filterNode"
        class="filter-tree"
        @node-click="handleNodeClick"
      />
    </div>
    <div v-if="gridData.length>0" class="div-right">
      <el-table :header-cell-style="{background:'#eee',color:'#606266'}" :show-header="true" :data="gridData" style="margin-bottom:20px;" border highlight-current-row>
        <el-table-column property="key" label="Table Info" width="160">
          <template slot-scope="{row}">
            {{ row.key }}
            <el-button v-if="row.key=='totalRows'||row.key=='totalBytes'" size="mini" type="primary" @click="searchForRowsBytes(row.key)">
              search
            </el-button>
          </template>
        </el-table-column>
        <el-table-column property="value" label="">
          <template slot-scope="{row}">
            {{ row.value }}
          </template>
        </el-table-column>
      </el-table>
      DataParts
      <el-select
        v-model="value"
        filterable
        clearable=""
        placeholder="select node"
        style="margin-bottom:20px;margin-right:10px;margin-left:10px"
      >
        <el-option
          v-for="item in options"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        />
      </el-select>
      <el-button class="filter-item" type="primary" icon="el-icon-search" @click="selectNode">
        search
      </el-button>
      <el-table :data="dataPart" :header-cell-style="{color:'#606266'}" style="margin-bottom:20px;" border>
        <el-table-column property="partition" label="partition" width="100" />
        <el-table-column property="name" label="name" width="150" />
        <el-table-column property="type" label="type" width="100" />
        <el-table-column property="rows" label="rows" width="100" />
        <el-table-column property="path" label="path" />
        <el-table-column property="bytesOnDisk" label="bytesOnDisk" width="120" />
      </el-table>
    </div>
    <el-dialog title="Table Info" :visible.sync="dialogTableVisible">
      <el-table :show-header="false" :data="gridData" style="margin-bottom:20px;" border highlight-current-row>
        <el-table-column property="key" label="属性名称" width="150" />
        <el-table-column property="value" label="属性值" />
      </el-table>
      DataParts
      <el-select
        v-model="value"
        filterable
        clearable=""
        placeholder="select node"
        style="margin-bottom:20px;margin-right:10px;margin-left:10px"
      >
        <el-option
          v-for="item in options"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        />
      </el-select>
      <el-button class="filter-item" type="primary" icon="el-icon-search" @click="selectNode">
        search
      </el-button>
      <el-table :data="dataPart" style="margin-bottom:20px;" border>
        <el-table-column property="partition" label="partition" width="100" />
        <el-table-column property="name" label="name" width="150" />
        <el-table-column property="type" label="type" width="100" />
        <el-table-column property="rows" label="rows" width="100" />
        <el-table-column property="path" label="path" />
        <el-table-column property="bytesOnDisk" label="bytesOnDisk" width="120" />
      </el-table>

    </el-dialog>
  </div>
</template>

<script>
import { getDatabase, getDataPart, getNodeList, getTable, getTableInfo, getTotalRows, getTotalBytes } from '@/api/meta'
import { getClusterList } from '@/api/cluster'

export default {

  data() {
    return {
      filterText: '',
      data2: [],
      defaultProps: {
        children: 'children',
        label: 'label',
        isLeaf: 'leaf'
      },
      gridData: [],
      dataPart: [],
      dialogTableVisible: false,
      options: [],
      value: '',
      cluster: '',
      db: '',
      table: ''
    }
  },
  watch: {
    filterText(val) {
      this.$refs.tree2.filter(val)
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    selectNode() {
      if (this.value === '') {
        this.$message.error('Please select node!')
        return
      } else if (this.value === 'all') {
        getDataPart(`pclusterName=${this.cluster}&node=${this.value}&dbName=${this.db}&tableName=${this.table}`).then(res => {
          this.dataPart = res.data
          if (this.dataPart.length === 0) {
            this.$message.warning('this table has no data parts')
          }
        })
      } else {
        const str = this.value.split(' ')
        getDataPart(`pclusterName=${this.cluster}&node=${str[1]},${str[3]},${str[5]}&dbName=${this.db}&tableName=${this.table}`).then(res => {
          this.dataPart = res.data
          if (this.dataPart.length === 0) {
            this.$message.warning('this table has no data parts')
          }
        })
      }
    },

    searchForRowsBytes(val) {
      if (val === 'totalRows') {
        getTotalRows(`pclusterName=${this.cluster}&dbName=${this.db}&tableName=${this.table}`).then(res => {
          this.gridData[10].value = res.data
        })
      } else {
        getTotalBytes(`pclusterName=${this.cluster}&dbName=${this.db}&tableName=${this.table}`).then(res => {
          this.gridData[9].value = res.data === null ? 'NULL' : res.data
        })
      }
    },

    handleNodeClick(val, node) {
      if (node.level === 3) { // click the table name
        this.table = node.data.label
        this.db = node.parent.data.label
        this.cluster = node.parent.parent.data.label
        this.options = [{ label: 'all', value: 'all' }]
        this.gridData = []
        this.dataPart = []
        this.value = ''
        // this.dialogTableVisible = true
        getNodeList(`pclusterName=${this.cluster}&dbName=${this.db}&tableName=${this.table}`).then(response => {
          response.data.forEach(e => {
            this.options.push({
              label: 'hostName: ' + e['hostName'] + ' hostAddress: ' + e['hostAddress'] + ' port: ' + e['tcpPort'],
              value: 'hostName: ' + e['hostName'] + ' hostAddress: ' + e['hostAddress'] + ' port: ' + e['tcpPort']
            })
          })
        })
        getTableInfo(`pclusterName=${this.cluster}&dbName=${this.db}&tableName=${this.table}`).then(res => {
          var str = ''
          res.data.schema.forEach(e => {
            str += e['name'] + ' ' + e['type'] + ', '
          })
          if (str !== '') {
            str = str.substr(0, str.length - 2)
          }
          this.gridData.push({ key: 'schema', value: str })
          const data = res.data.detail
          var isDistributed = false
          for (var k in data) {
            console.log(k, data[k])
            if (k === 'totalRows') {
              continue
            }
            if (data[k] === null) {
              this.gridData.push({ key: k, value: '' })
            } else if (k === 'engine' && data[k][0] === 'Distributed') {
              isDistributed = true
              this.gridData.push({ key: k, value: data[k].toString() })
            } else if (data[k] instanceof Array) {
              var tmp = ''
              data[k].forEach(element => {
                if (element !== '') {
                  tmp += element.toString() + '\n'
                }
              })
              this.gridData.push({ key: k, value: tmp.trim() })
            } else {
              this.gridData.push({ key: k, value: data[k].toString() })
            }
          }
          if (isDistributed) {
            this.gridData.push({ key: 'totalRows', value: '' })
          }
          // current no replica info exists
          // getReplica(`pclusterName=${this.cluster}&dbName=${this.db}&tableName=${this.table}`).then(r => {
          //   var replica = ''
          //   r.data.forEach(element => {
          //     replica += 'replicaName: ' + element['replicaName'] + ' replicaPath: ' + element['replicaPath'] + '\n'
          //   })
          //   this.gridData.push({ key: 'replicas', value: replica.trim() })
          // })
        })
      } else {
        this.gridData = []
      }
    },
    filterNode(value, data) {
      if (!value) return true
      return data.label.indexOf(value) !== -1
    },
    fetchData() {
      getClusterList().then(response => {
        response.data.forEach(clusterName => {
          const cluster = {}
          cluster['label'] = clusterName
          getDatabase(`pclusterName=${clusterName}`).then(res => {
            cluster['children'] = []
            res.data.forEach(dbName => {
              const db = {}
              db['label'] = dbName
              getTable(`pclusterName=${clusterName}&dbName=${dbName}`).then(r => {
                db['children'] = []
                r.data.forEach(tableName => {
                  db['children'].push({
                    label: tableName
                  })
                })
                cluster['children'].push(db)
                this.data2.push(cluster)
              })
            })
          })
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
.div-left {
  width: 35%;
  float: left;
  height: 800px;
  overflow: auto;
}
.div-right {
  width: 65%;
  float: left;
  height: 800px;
  overflow: auto;
}
</style>
