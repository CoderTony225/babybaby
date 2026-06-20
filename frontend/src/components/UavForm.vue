<template>
  <div>
    <h2 class="mb-4">{{ isEdit ? '编辑无人机' : '新建无人机' }}</h2>
    <div class="alert alert-danger" v-if="error" v-text="error"></div>
    <form @submit.prevent="submit" class="needs-validation" novalidate>
      <!-- 基本信息 -->
      <div class="card mb-4">
        <div class="card-header">基本信息</div>
        <div class="card-body">
          <div class="row">
            <div class="col-md-6">
              <div class="form-group">
                <label>机身编号 *</label>
                <input type="text" class="form-control" v-model="form.frameSn" required />
              </div>
            </div>
            <div class="col-md-6">
              <div class="form-group">
                <label>型号 *</label>
                <input type="text" class="form-control" v-model="form.modelName" required />
              </div>
            </div>
            <div class="col-md-6">
              <div class="form-group">
                <label>制造厂商</label>
                <input type="text" class="form-control" v-model="form.manufacturer" />
              </div>
            </div>
            <div class="col-md-6">
              <div class="form-group">
                <label>状态</label>
                <select class="form-control" v-model="form.status">
                  <option value="">请选择状态</option>
                  <option value="IDLE">空闲</option>
                  <option value="FLYING">飞行中</option>
                  <option value="CHARGING">充电中</option>
                  <option value="FAULT">故障</option>
                  <option value="MAINTENANCE">维修中</option>
                  <option value="SCRAPPED">报废</option>
                </select>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 硬件参数 -->
      <div class="card mb-4">
        <div class="card-header">硬件参数</div>
        <div class="card-body">
          <div class="row">
            <div class="col-md-4">
              <div class="form-group">
                <label>最大飞行高度（米）</label>
                <input type="number" class="form-control" v-model.number="form.maxFlightAltitude" />
              </div>
            </div>
            <div class="col-md-4">
              <div class="form-group">
                <label>最大飞行速度（km/h）</label>
                <input type="number" class="form-control" v-model.number="form.maxFlightSpeed" />
              </div>
            </div>
            <div class="col-md-4">
              <div class="form-group">
                <label>续航时间（分钟）</label>
                <input type="number" class="form-control" v-model.number="form.flightDuration" />
              </div>
            </div>
            <div class="col-md-4">
              <div class="form-group">
                <label>电池容量（mAh）</label>
                <input type="number" class="form-control" v-model.number="form.batteryCapacity" />
              </div>
            </div>
            <div class="col-md-4">
              <div class="form-group">
                <label>载重能力（克）</label>
                <input type="number" class="form-control" v-model.number="form.payloadCapacity" />
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 使用信息 -->
      <div class="card mb-4">
        <div class="card-header">使用信息</div>
        <div class="card-body">
          <div class="row">
            <div class="col-md-4">
              <div class="form-group">
                <label>总飞行时长（小时）</label>
                <input type="number" class="form-control" v-model.number="form.totalFlightHours" />
              </div>
            </div>
            <div class="col-md-4">
              <div class="form-group">
                <label>累计飞行次数</label>
                <input type="number" class="form-control" v-model.number="form.totalFlightCount" />
              </div>
            </div>
            <div class="col-md-4">
              <div class="form-group">
                <label>最后飞行时间</label>
                <input type="datetime-local" class="form-control" v-model="form.lastFlightTime" />
              </div>
            </div>
            <div class="col-md-4">
              <div class="form-group">
                <label>首次启用日期</label>
                <input type="date" class="form-control" v-model="form.firstUseDate" />
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 维保信息 -->
      <div class="card mb-4">
        <div class="card-header">维保信息</div>
        <div class="card-body">
          <div class="row">
            <div class="col-md-4">
              <div class="form-group">
                <label>上次维护日期</label>
                <input type="date" class="form-control" v-model="form.lastMaintenanceDate" />
              </div>
            </div>
            <div class="col-md-4">
              <div class="form-group">
                <label>下次维护日期</label>
                <input type="date" class="form-control" v-model="form.nextMaintenanceDate" />
              </div>
            </div>
            <div class="col-md-4">
              <div class="form-group">
                <label>维护周期（小时）</label>
                <input type="number" class="form-control" v-model.number="form.maintenanceInterval" />
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 其他信息 -->
      <div class="card mb-4">
        <div class="card-header">其他信息</div>
        <div class="card-body">
          <div class="form-group">
            <label>备注</label>
            <textarea class="form-control" v-model="form.notes" rows="3"></textarea>
          </div>
        </div>
      </div>

      <button type="submit" class="btn btn-primary">保存</button>
      <button type="button" class="btn btn-secondary ml-2" @click="goBack">取消</button>
    </form>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import axios from 'axios'
//编辑or新建
const isEdit = computed(() => {
  const path = window.location.pathname
  return path.includes('/edit') || (path.includes('/uavs/') && !path.includes('/new'))
})

const form = ref({
  id: null,
  frameSn: '',
  modelName: '',
  manufacturer: '',
  status: '',
  maxFlightAltitude: null,
  maxFlightSpeed: null,
  flightDuration: null,
  batteryCapacity: null,
  payloadCapacity: null,
  totalFlightHours: null,
  totalFlightCount: null,
  lastFlightTime: '',
  firstUseDate: '',
  lastMaintenanceDate: '',
  nextMaintenanceDate: '',
  maintenanceInterval: null,
  notes: ''
})

const error = ref('')
//进入编辑页面时，从网址里拿到无人机 ID → 请求后端数据 → 自动填到表单里
const loadData = async () => {
  if (!isEdit.value) return//不是编辑直接
  const pathParts = window.location.pathname.split('/')//切割
  const id = pathParts[pathParts.length - 2] || pathParts[pathParts.length - 1]//取id
  try {
    const response = await axios.get(`/api/uavs/${id}`)
    const uav = response.data
    form.value = {
      id: uav.id,
      frameSn: uav.frameSn || '',
      modelName: uav.modelName || '',
      manufacturer: uav.manufacturer || '',
      status: uav.status || '',
      maxFlightAltitude: uav.maxFlightAltitude,
      maxFlightSpeed: uav.maxFlightSpeed,
      flightDuration: uav.flightDuration,
      batteryCapacity: uav.batteryCapacity,
      payloadCapacity: uav.payloadCapacity,
      totalFlightHours: uav.totalFlightHours,
      totalFlightCount: uav.totalFlightCount,
      lastFlightTime: formatDateTimeForInput(uav.lastFlightTime),
      firstUseDate: formatDateForInput(uav.firstUseDate),
      lastMaintenanceDate: formatDateForInput(uav.lastMaintenanceDate),
      nextMaintenanceDate: formatDateForInput(uav.nextMaintenanceDate),
      maintenanceInterval: uav.maintenanceInterval,
      notes: uav.notes || ''
    }
  } catch (e) {
    console.error('加载数据失败:', e)
  }
}
//时间格式化
const formatDateForInput = (dateStr) => {
  if (!dateStr) return ''
  try {
    const date = new Date(dateStr)
    return date.toISOString().split('T')[0]
  } catch (e) {
    return dateStr
  }
}
//
const formatDateTimeForInput = (dateTimeStr) => {
  if (!dateTimeStr) return ''
  try {
    const date = new Date(dateTimeStr)
    return date.toISOString().slice(0, 16)
  } catch (e) {
    return dateTimeStr
  }
}
//编辑新增
const submit = async () => {
  try {
    error.value = ''
    if (isEdit.value) {
      await axios.put(`/api/uavs/${form.value.id}`, form.value)
    } else {
      await axios.post('/api/uavs', form.value)
    }
    window.location.href = '/'
  } catch (e) {
    error.value = e.response?.data?.message || '保存失败'
  }
}
//返回
const goBack = () => {
  window.location.href = '/'
}

onMounted(() => {
  loadData()
})
</script>