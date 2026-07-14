<script setup lang="ts">
import AppAlert from '@/components/ui/AppAlert.vue'
import { useAppFeedbackStore } from '@/stores/appFeedback'

const feedbackStore = useAppFeedbackStore()
</script>

<template>
  <div class="pointer-events-none fixed right-4 bottom-4 z-50 flex w-full max-w-sm flex-col gap-3">
    <div
      v-for="item in feedbackStore.items"
      :key="item.id"
      class="pointer-events-auto"
    >
      <AppAlert :message="item.description ?? item.title" :title="item.description ? item.title : undefined" :tone="item.tone" />
    </div>
  </div>
</template>
