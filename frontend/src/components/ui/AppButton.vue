<script setup lang="ts">
import { computed } from 'vue'
import AppSpinner from '@/components/ui/AppSpinner.vue'

const props = withDefaults(
  defineProps<{
    type?: 'button' | 'submit' | 'reset'
    variant?: 'primary' | 'secondary' | 'danger' | 'ghost'
    loading?: boolean
    disabled?: boolean
    fullWidth?: boolean
  }>(),
  {
    type: 'button',
    variant: 'primary',
    loading: false,
    disabled: false,
    fullWidth: false,
  },
)

const classes = computed(() => {
  const base = 'inline-flex items-center justify-center gap-2 rounded-2xl px-4 py-2.5 text-sm font-semibold transition duration-200 disabled:cursor-not-allowed disabled:opacity-60'
  const width = props.fullWidth ? ' w-full' : ''

  if (props.variant === 'secondary') {
    return `${base}${width} border border-app-border bg-white text-slate-700 shadow-sm hover:border-primary-200 hover:bg-primary-50/50 hover:text-primary-900`
  }

  if (props.variant === 'danger') {
    return `${base}${width} bg-rose-600 text-white shadow-lg shadow-rose-600/20 hover:bg-rose-700`
  }

  if (props.variant === 'ghost') {
    return `${base}${width} text-slate-700 hover:bg-primary-50 hover:text-primary-900`
  }

  return `${base}${width} bg-linear-to-r from-primary-600 to-primary-700 text-white shadow-lg shadow-primary-600/25 hover:from-primary-700 hover:to-primary-900`
})
</script>

<template>
  <button :class="classes" :disabled="disabled || loading" :type="type">
    <AppSpinner v-if="loading" class="text-current" size="sm" />
    <slot />
  </button>
</template>
