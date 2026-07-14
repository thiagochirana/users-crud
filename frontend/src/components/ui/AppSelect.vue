<script setup lang="ts">
import { ChevronDown } from '@lucide/vue'
import { computed, useId } from 'vue'

interface Option {
  label: string
  value: string
}

const props = defineProps<{
  modelValue: string
  label: string
  options: Option[]
  error?: string[]
  disabled?: boolean
}>()

const emit = defineEmits<{
  'update:modelValue': [value: string]
}>()

const id = useId()

const describedBy = computed(() => (props.error?.length ? `${id}-error` : undefined))
</script>

<template>
  <label class="block space-y-2" :for="id">
    <span class="text-sm font-medium text-slate-800">{{ label }}</span>
    <div class="relative">
      <select
        :id="id"
        :aria-describedby="describedBy"
        :aria-invalid="Boolean(error?.length)"
        :class="[
          'w-full appearance-none rounded-2xl border bg-white/95 px-3.5 py-3 pr-10 text-sm text-slate-900 shadow-sm transition duration-200 focus:border-primary-300 focus:bg-white',
          error?.length ? 'border-rose-300 bg-rose-50/40' : 'border-app-border hover:border-primary-200',
        ]"
        :disabled="disabled"
        :value="modelValue"
        @change="emit('update:modelValue', ($event.target as HTMLSelectElement).value)"
      >
        <option v-for="option in options" :key="option.value" :value="option.value">
          {{ option.label }}
        </option>
      </select>
      <ChevronDown class="pointer-events-none absolute top-1/2 right-3 -translate-y-1/2 text-slate-400" :size="18" />
    </div>
    <ul v-if="error?.length" :id="`${id}-error`" class="space-y-1 text-xs font-medium text-rose-700">
      <li v-for="item in error" :key="item">{{ item }}</li>
    </ul>
  </label>
</template>
