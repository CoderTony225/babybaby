<template>
  <div class="min-vh-100 bg-light">
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
      <div class="container-fluid">
        <a class="navbar-brand" href="#">无人机台账管理系统</a>
        <div class="navbar-nav ml-auto">
          <span class="nav-link text-light">欢迎, {{ currentUser }}</span>
          <button class="btn btn-outline-light ml-2" @click="logout">退出</button>
        </div>
      </div>
    </nav>
    <div class="container mt-4" v-if="isLoggedIn">
      <router-view />
    </div>
    <div class="container mt-4" v-else>
      <Login @login="handleLogin" />
    </div>
  </div>
</template>

<script setup>
import { ref, provide } from 'vue'
import { createRouter, createWebHistory } from 'vue-router'
import axios from 'axios'
import Login from './components/Login.vue'
import UavList from './components/UavList.vue'
import UavDetail from './components/UavDetail.vue'
import UavForm from './components/UavForm.vue'

const isLoggedIn = ref(false)
const currentUser = ref('')

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/', component: UavList },
    { path: '/uavs', component: UavList },
    { path: '/uavs/:id', component: UavDetail },
    { path: '/uavs/:id/edit', component: UavForm },
    { path: '/new', component: UavForm }
  ]
})

const handleLogin = (user) => {
  isLoggedIn.value = true
  currentUser.value = user
  router.push('/')
}

const logout = () => {
  axios.post('/api/logout').then(() => {
    isLoggedIn.value = false
    currentUser.value = ''
    router.push('/')
  })
}

provide('router', router)
provide('axios', axios)
</script>