<template>
  <div v-if="uav">
    <h2 class="mb-4">无人机详情</h2>
    
    <!-- 基本信息 -->
    <div class="card mb-4">
      <div class="card-header">基本信息</div>
      <div class="card-body">
        <dl class="row">
          <dt class="col-sm-3">ID</dt>
          <dd class="col-sm-9" v-text="uav.id"></dd>
          <dt class="col-sm-3">机身编号</dt>
          <dd class="col-sm-9" v-text="uav.frameSn"></dd>
          <dt class="col-sm-3">型号</dt>
          <dd class="col-sm-9" v-text="uav.modelName"></dd>
          <dt class="col-sm-3">制造厂商</dt>
          <dd class="col-sm-9" v-text="uav.manufacturer || '—'"></dd>
          <dt class="col-sm-3">状态</dt>
          <dd class="col-sm-9">
            <span :class="getStatusClass(uav.status)" v-text="getStatusText(uav.status)"></span>
          </dd>
        </dl>
      </div>
    </div>

    <!-- 硬件参数 -->
    <div class="card mb-4">
      <div class="card-header">硬件参数</div>
      <div class="card-body">
        <dl class="row">
          <dt class="col-sm-3">最大飞行高度</dt>
          <dd class="col-sm-9" v-text="uav.maxFlightAltitude ? uav.maxFlightAltitude + ' 米' : '—'"></dd>
          <dt class="col-sm-3">最大飞行速度</dt>
          <dd class="col-sm-9" v-text="uav.maxFlightSpeed ? uav.maxFlightSpeed + ' km/h' : '—'"></dd>
          <dt class="col-sm-3">续航时间</dt>
          <dd class="col-sm-9" v-text="uav.flightDuration ? uav.flightDuration + ' 分钟' : '—'"></dd>
          <dt class="col-sm-3">电池容量</dt>
          <dd class="col-sm-9" v-text="uav.batteryCapacity ? uav.batteryCapacity + ' mAh' : '—'"></dd>
          <dt class="col-sm-3">载重能力</dt>
          <dd class="col-sm-9" v-text="uav.payloadCapacity ? uav.payloadCapacity + ' 克' : '—'"></dd>
        </dl>
      </div>
    </div>

    <!-- 使用信息 -->
    <div class="card mb-4">
      <div class="card-header">使用信息</div>
      <div class="card-body">
        <dl class="row">
          <dt class="col-sm-3">总飞行时长</dt>
          <dd class="col-sm-9" v-text="uav.totalFlightHours ? uav.totalFlightHours + ' 小时' : '—'"></dd>
          <dt class="col-sm-3">累计飞行次数</dt>
          <dd class="col-sm-9" v-text="uav.totalFlightCount ? uav.totalFlightCount + ' 次' : '—'"></dd>
          <dt class="col-sm-3">最后飞行时间</dt>
          <dd class="col-sm-9" v-text="formatDateTime(uav.lastFlightTime) || '—'"></dd>
          <dt class="col-sm-3">首次启用日期</dt>
          <dd class="col-sm-9" v-text="formatDate(uav.firstUseDate) || '—'"></dd>
        </dl>
      </div>
    </div>

    <!-- 维保信息 -->
    <div class="card mb-4">
      <div class="card-header">维保信息</div>
      <div class="card-body">
        <dl class="row">
          <dt class="col-sm-3">上次维护日期</dt>
          <dd class="col-sm-9" v-text="formatDate(uav.lastMaintenanceDate) || '—'"></dd>
          <dt class="col-sm-3">下次维护日期</dt>
          <dd class="col-sm-9" v-text="formatDate(uav.nextMaintenanceDate) || '—'"></dd>
          <dt class="col-sm-3">维护周期</dt>
          <dd class="col-sm-9" v-text="uav.maintenanceInterval ? uav.maintenanceInterval + ' 小时' : '—'"></dd>
        </dl>
      </div>
    </div>

    <!-- 其他信息 -->
    <div class="card mb-4">
      <div class="card-header">其他信息</div>
      <div class="card-body">
        <dl class="row">
          <dt class="col-sm-3">备注</dt>
          <dd class="col-sm-9" v-text="uav.notes || '—'"></dd>
          <dt class="col-sm-3">创建时间</dt>
          <dd class="col-sm-9" v-text="formatDateTime(uav.createdAt) || '—'"></dd>
          <dt class="col-sm-3">更新时间</dt>
          <dd class="col-sm-9" v-text="formatDateTime(uav.updatedAt) || '—'"></dd>
        </dl>
      </div>
    </div>

    <button class="btn btn-secondary" @click="goBack">返回列表</button>
    <button class="btn btn-warning ml-2" @click="goToEdit">编辑</button>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'

const uav = ref(null)
//加载数据
const loadData = async () => {
  const id = window.location.pathname.split('/')[2]
  try {
    const response = await axios.get(`/api/uavs/${id}`)
    uav.value = response.data
  } catch (e) {
    console.error('加载数据失败:', e)
  }
}

const goBack = () => {
  window.location.href = '/'
}
//跳出编辑页面
const goToEdit = () => {
  const id = window.location.pathname.split('/')[2]
  window.location.href = `/uavs/${id}/edit`
}
//color
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
//日期格式化
const formatDate = (dateStr) => {
  if (!dateStr) return null
  try {
    return new Date(dateStr).toLocaleDateString('zh-CN')
  } catch (e) {
    return dateStr
  }
}
//时间格式化
const formatDateTime = (dateTimeStr) => {
  if (!dateTimeStr) return null
  try {
    return new Date(dateTimeStr).toLocaleString('zh-CN')
  } catch (e) {
    return dateTimeStr
  }
}

onMounted(() => {
  loadData()
})
</script>