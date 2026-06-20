<template>
  <div>
    <div class="alert alert-success" v-if="successMessage" v-text="successMessage"></div>
    <div class="alert alert-danger" v-if="errorMessage" v-text="errorMessage"></div>
    <h2 class="mb-4">无人机列表</h2>
    <form class="form-inline mb-4">
      <div class="form-group mr-2">
        <input class="form-control" v-model="keyword" placeholder="编号 / 型号 / 厂商" />
      </div>
      <button type="button" class="btn btn-secondary mr-2" @click="handleSearch">查询</button>
      <button type="button" class="btn btn-primary" @click="goToNew">新建</button>
    </form>
    <table class="table table-striped table-bordered">
      <thead class="thead-dark">
        <tr>
          <th>ID</th>
          <th>机身编号</th>
          <th>型号</th>
          <th>制造厂商</th>
          <th>状态</th>
          <th>操作</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="uav in items" :key="uav.id">
          <td v-text="uav.id"></td>
          <td v-text="uav.frameSn"></td>
          <td v-text="uav.modelName"></td>
          <td v-text="uav.manufacturer || '—'"></td>
          <td>
            <span :class="getStatusClass(uav.status)" v-text="getStatusText(uav.status)"></span>
          </td>
          <td>
            <button class="btn btn-sm btn-info" @click="goToDetail(uav.id)">详细</button>
            <button class="btn btn-sm btn-warning ml-1" @click="goToEdit(uav.id)">编辑</button>
            <button class="btn btn-sm btn-danger ml-1" @click="confirmDelete(uav.id, uav.frameSn)">删除</button>
          </td>
        </tr>
        <tr v-if="items.length === 0">
          <td colspan="6" class="text-center text-muted">暂无数据</td>
        </tr>
      </tbody>
    </table>
    <nav v-if="totalPages > 1">
      <ul class="pagination">
        <li class="page-item" :class="{ 'disabled': page <= 1 }">
          <button class="page-link" @click="changePage(page - 1)">&laquo;</button>
        </li>
        <li class="page-item disabled"><span class="page-link">第 {{ page }} / {{ totalPages }} 页</span></li>
        <li class="page-item" :class="{ 'disabled': page >= totalPages }">
          <button class="page-link" @click="changePage(page + 1)">&raquo;</button>
        </li>
      </ul>
    </nav>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'

const items = ref([])
const page = ref(1)
const size = ref(10)
const totalPages = ref(1)
const keyword = ref('')
const successMessage = ref('')
const errorMessage = ref('')
//查询
const handleSearch = () => {
  page.value = 1
  loadData()
}
//查询核心
const loadData = async () => {
  try {
    const url = `/api/uavs?keyword=${encodeURIComponent(keyword.value)}&page=${page.value}&size=${size.value}`
    const response = await axios.get(url)
    items.value = response.data.items || []
    page.value = response.data.page || 1
    totalPages.value = response.data.totalPages || 1
  } catch (e) {
    console.error('加载数据失败:', e)
  }
}
//分页
const changePage = (newPage) => {
  if (newPage >= 1 && newPage <= totalPages.value) {
    page.value = newPage
    loadData()
  }
}
//跳转
const goToDetail = (id) => {
  window.location.href = `/uavs/${id}`
}
//
const goToEdit = (id) => {
  window.location.href = `/uavs/${id}/edit`
}
//
const goToNew = () => {
  window.location.href = '/new'
}

const confirmDelete = (id, frameSn) => {
  if (confirm(`确定要删除无人机 ${frameSn} 吗？此操作无法撤销。`)) {
    deleteUav(id)
  }
}

const deleteUav = async (id) => {
  try {
    await axios.delete(`/api/uavs/${id}`)
    successMessage.value = '删除成功'
    errorMessage.value = ''//清空
    loadData()
    setTimeout(() => successMessage.value = '', 3000)//消失
  } catch (e) {
    errorMessage.value = '删除失败'
    successMessage.value = ''
    setTimeout(() => errorMessage.value = '', 3000)
  }
}
//状态颜色
const getStatusClass = (status) => {
  if (!status) return 'badge badge-secondary'
  switch(status) {
    case 'IDLE': return 'badge badge-success'
    case 'FLYING': return 'badge badge-primary'
    case 'CHARGING': return 'badge badge-info'
    case 'FAULT': return 'badge badge-danger'
    case 'MAINTENANCE': return 'badge badge-warning'
    case 'SCRAPPED': return 'badge badge-secondary'
    default: return 'badge badge-secondary'
  }
}
//翻译
const getStatusText = (status) => {
  if (!status) return '未知'
  switch(status) {
    case 'IDLE': return '空闲'
    case 'FLYING': return '飞行中'
    case 'CHARGING': return '充电中'
    case 'FAULT': return '故障'
    case 'MAINTENANCE': return '维修中'
    case 'SCRAPPED': return '报废'
    default: return status
  }
}

onMounted(() => {
  loadData()
})
</script>