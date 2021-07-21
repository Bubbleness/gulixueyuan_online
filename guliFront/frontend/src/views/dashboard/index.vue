<template>
  <div class="dashboard-container">
    <a class="document-btn" target="_blank" @click="cluster">Cluster</a>
    <a class="document-btn" target="_blank" @click="meta">Meta</a>
    <a class="document-btn" target="_blank" @click="metric">Metrics</a>
    <a class="document-btn" target="_blank" @click="query">Queries</a>
    <a class="document-btn" target="_blank" @click="setting">Settings</a>
    <a class="document-btn" target="_blank" @click="trace">Trace</a>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'

export default {
  name: 'Dashboard',
  computed: {
    ...mapGetters([
      'name'
    ])
  },
  methods: {
    cluster() {
      this.$router.push({ path: '/Cluster' })
    },
    meta() {
      this.$router.push({ path: '/Meta' })
    },
    metric() {
      this.$router.push({ path: '/Metrics' })
    },
    query() {
      this.$router.push({ path: '/Queries' })
    },
    setting() {
      this.$router.push({ path: '/Setting' })
    },
    trace() {
      this.$router.push({ path: '/Trace' })
    }
  }
}
</script>

<style lang="scss" scoped>
.dashboard-container {
  display: flex;
  flex-wrap: wrap;
  justify-content: flex-start;
  margin: 30px;

  .document-btn {
    flex-shrink: 0;
    display: block;
    cursor: pointer;
    background: black;
    color: white;
    height: 60px;
    padding: 0 16px;
    margin: 16px;
    line-height: 60px;
    font-size: 20px;
    text-align: center;
  }
}
</style>
