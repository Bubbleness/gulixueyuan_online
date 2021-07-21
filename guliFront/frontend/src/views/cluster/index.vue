<template>
  <div class="app-container">
    <el-input v-model="filterText" placeholder="Filter keyword" style="margin-bottom:20px;" />
    <div class="div-half">
      <el-tree
        ref="tree2"
        :data="data2"
        :props="defaultProps"
        :filter-node-method="filterNode"
        class="filter-tree"
        @node-click="handleNodeClick"
      />
    </div>

    <div class="div-half">
      <el-table v-if="nodeData.length>0" :data="nodeData" :header-cell-style="{background:'#eee',color:'#606266'}" border fit highlight-current-row>
        <el-table-column property="name" label="Replica Info" width="150" />
        <el-table-column property="value" label="" />
      </el-table>
    </div>
  </div>
</template>

<script>
import { getCluster, getReplicaDetail } from '@/api/cluster'

export default {

  data() {
    return {
      filterText: '',
      data2: [],
      defaultProps: {
        children: 'children',
        label: 'label'
      },
      nodeData: [],
      diskData: []
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
    filterNode(value, data) {
      if (!value) return true
      return data.label.indexOf(value) !== -1
    },
    handleNodeClick(val, node) {
      const node_arr = node.label.split(' ')
      const node_str = node_arr[3] + ',' + node_arr[5] + ',' + node_arr[7]
      if (node.level === 4) {
        this.nodeData = []
        getReplicaDetail(`node=${node_str}`).then(response => {
          var tables = []
          var diskString = ''
          response.data.diskInfo.forEach(element => {
            for (var key in element) {
              if (key === 'freeSpace' || key === 'totalSpace') {
                element[key] = (Number(element[key]) / 1024 / 1024 / 1024).toFixed(2) + ' GB'
              }
              diskString += key + ': ' + element[key] + '\n'
            }
            diskString += '\n'
          })
          response.data.tableList.forEach(element => {
            tables.push(element)
          })
          this.nodeData.push({
            name: 'tables',
            value: tables.length
          }, {
            name: 'disk',
            value: diskString.trim()
          })
        })
      } else {
        this.nodeData = []
      }
    },
    toTree(map) {
      var data = []
      for (var key in map) {
        if (key === 'replicas') {
          const x = map[key]
          for (var kkey in x) {
            data.push({
              label: 'replicaID: ' + x[kkey]['replicaID'] + ' hostName: ' + x[kkey]['hostName'] + ' hostAddr: ' + x[kkey]['hostAddr'] + ' port: ' + x[kkey]['port']
            })
          }
        } else if (key === 'name') {
          continue
        } else if (key === 'clusters') {
          return this.toTree(map['clusters'])
        } else if (key === 'shards') {
          for (var num in map[key]) {
            data.push({
              label: 'shard_' + num,
              children: this.toTree(map[key][num])
            })
          }
        } else {
          data.push({
            label: key,
            children: this.toTree(map[key])
          })
        }
      }
      return data
    },
    fetchData() {
      getCluster().then(response => {
        const recv = response.data
        this.data2 = this.toTree(recv)
      })
    }
  }
}
</script>

<style type="text/css">
.el-table .cell {
  white-space: pre-line;
}
.div-half {
  width: 50%;
  float: left;
  height: 800px;
  overflow: auto;
}
</style>
