<script setup lang="ts">
import { computed, useId } from 'vue'

type AppInputMode = 'none' | 'text' | 'tel' | 'url' | 'email' | 'numeric' | 'decimal' | 'search'

const props = withDefaults(
  defineProps<{
    modelValue: string
    label: string
    type?: string
    placeholder?: string
    error?: string[]
    hint?: string
    maxlength?: number
    disabled?: boolean
    autocomplete?: string
    inputmode?: AppInputMode
    pattern?: string
  }>(),
  {
    type: 'text',
    placeholder: undefined,
    error: undefined,
    hint: undefined,
    maxlength: undefined,
    disabled: false,
    autocomplete: undefined,
    inputmode: undefined,
    pattern: undefined,
  },
)

const emit = defineEmits<{
  'update:modelValue': [value: string]
  blur: []
}>()

const id = useId()

const describedBy = computed(() => {
  const ids: string[] = []
  if (props.hint) ids.push(`${id}-hint`)
  if (props.error?.length) ids.push(`${id}-error`)
  return ids.join(' ') || undefined
})
</script>

<template>
  <label class="block space-y-2" :for="id">
    <span class="text-sm font-medium text-slate-800">{{ label }}</span>
    <input
      :id="id"
      :aria-describedby="describedBy"
      :aria-invalid="Boolean(error?.length)"
      :autocomplete="autocomplete"
      :class="[
        'w-full rounded-2xl border bg-white/95 px-3.5 py-3 text-sm text-slate-900 shadow-sm transition duration-200 placeholder:text-slate-400 focus:border-primary-300 focus:bg-white',
        error?.length ? 'border-rose-300 bg-rose-50/40' : 'border-app-border hover:border-primary-200',
      ]"
      :disabled="disabled"
      :inputmode="inputmode"
      :maxlength="maxlength"
      :pattern="pattern"
      :placeholder="placeholder"
      :type="type"
      :value="modelValue"
      @blur="emit('blur')"
      @input="emit('update:modelValue', ($event.target as HTMLInputElement).value)"
    />
    <p v-if="hint" :id="`${id}-hint`" class="text-xs leading-5 text-app-muted">{{ hint }}</p>
    <ul v-if="error?.length" :id="`${id}-error`" class="space-y-1 text-xs font-medium text-rose-700">
      <li v-for="item in error" :key="item">{{ item }}</li>
    </ul>
  </label>
</template>
