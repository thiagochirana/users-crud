<script setup lang="ts">
import { RouterView } from 'vue-router'
import AppHeader from '@/components/layout/AppHeader.vue'
import AppAlertStack from '@/components/ui/AppAlertStack.vue'
</script>

<template>
  <div class="min-h-screen bg-app-bg">
    <AppHeader />

    <main class="page-container">
      <RouterView />
    </main>

    <AppAlertStack />
  </div>
</template>
