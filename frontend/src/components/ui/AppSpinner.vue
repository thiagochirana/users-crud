<script setup lang="ts">
import { computed } from 'vue'

const props = withDefaults(
  defineProps<{
    size?: 'sm' | 'md' | 'lg'
  }>(),
  {
    size: 'md',
  },
)

const classes = computed(() => {
  if (props.size === 'sm') return 'h-4 w-4 border-2'
  if (props.size === 'lg') return 'h-10 w-10 border-[3px]'
  return 'h-6 w-6 border-2'
})
</script>

<template>
  <span :class="classes" aria-hidden="true" class="inline-block animate-spin rounded-full border-current border-t-transparent" />
</template>
