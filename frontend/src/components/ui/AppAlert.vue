<script setup lang="ts">
import { computed } from 'vue'

const props = withDefaults(
  defineProps<{
    title?: string
    message?: string
    messages?: string[]
    tone?: 'success' | 'error' | 'info'
  }>(),
  {
    title: undefined,
    message: undefined,
    messages: undefined,
    tone: 'info',
  },
)

const toneClass = computed(() => {
  if (props.tone === 'success') {
    return 'border-emerald-200 bg-emerald-50/90 text-emerald-900 shadow-sm'
  }

  if (props.tone === 'error') {
    return 'border-rose-200 bg-rose-50/90 text-rose-900 shadow-sm'
  }

  return 'border-primary-200 bg-primary-50/90 text-primary-900 shadow-sm'
})
</script>

<template>
  <div :class="toneClass" aria-live="polite" class="rounded-2xl border px-4 py-3 text-sm">
    <p v-if="title" class="font-semibold">{{ title }}</p>
    <ul v-if="messages?.length" class="space-y-1">
      <li v-for="item in messages" :key="item">{{ item }}</li>
    </ul>
    <p v-else-if="message">{{ message }}</p>
  </div>
</template>
