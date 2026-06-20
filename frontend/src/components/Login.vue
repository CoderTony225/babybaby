<template>
  <div class="row justify-content-center">
    <div class="col-md-4">
      <div class="card">
        <div class="card-header text-center">
          <h3>登录</h3>
        </div>
        <div class="card-body">
          <div class="alert alert-danger" v-if="error" v-text="error"></div>
          <form @submit.prevent="submit">
            <div class="form-group">
              <label>用户名</label>
              <input type="text" class="form-control" v-model="username" placeholder="请输入用户名" required />
            </div>
            <div class="form-group">
              <label>密码</label>
              <input type="password" class="form-control" v-model="password" placeholder="请输入密码" required />
            </div>
            <button type="submit" class="btn btn-primary btn-block">登录</button>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import axios from 'axios'

const emit = defineEmits(['login'])
// 存储
const username = ref('')
const password = ref('')
const error = ref('')

const submit = async () => {
  try {
    error.value = ''
    // 调用后端接口'/api/login'
    const response = await axios.post('/api/login', {
      username: username.value,
      password: password.value
    })
    if (response.data.success) {
      emit('login', username.value)
    } else {
      error.value = '登录失败'
    }
  } catch (e) {
    error.value = '用户名或密码错误'
  }
}
</script>