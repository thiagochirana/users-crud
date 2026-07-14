<script setup lang="ts">
defineProps<{
  title: string
  description?: string
}>()
</script>

<template>
  <div
    class="soft-surface flex flex-col gap-5 px-5 py-5 sm:flex-row sm:items-end sm:justify-between sm:px-6"
  >
    <div class="space-y-2">
      <h1 class="text-2xl font-semibold tracking-tight text-slate-950 sm:text-3xl">{{ title }}</h1>
      <p v-if="description" class="max-w-2xl text-sm leading-6 text-app-muted">{{ description }}</p>
    </div>

    <div class="shrink-0">
      <slot />
    </div>
  </div>
</template>
