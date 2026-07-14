<script setup lang="ts">
import AppButton from '@/components/ui/AppButton.vue'

defineProps<{
  page: number
  size: number
  totalPages: number
  totalElements: number
  disabled?: boolean
}>()

const emit = defineEmits<{
  pageChange: [page: number]
  sizeChange: [size: number]
}>()

const sizeOptions = ['10', '20', '50', '100']
</script>

<template>
  <div class="soft-surface flex flex-col gap-4 px-4 py-4 sm:flex-row sm:items-center sm:justify-between sm:px-5">
    <div class="space-y-1 text-sm text-app-muted">
      <p class="font-medium text-slate-700">Pagina {{ page }} de {{ totalPages || 1 }}</p>
      <p>{{ totalElements }} registro(s) no total</p>
    </div>

    <div class="flex flex-col gap-3 sm:flex-row sm:items-center">
      <label class="flex items-center gap-2 text-sm text-slate-700">
        <span>Por pagina</span>
        <select
          :disabled="disabled"
          class="rounded-2xl border border-app-border bg-white px-3 py-2 text-sm shadow-sm transition hover:border-primary-200"
          :value="String(size)"
          @change="emit('sizeChange', Number(($event.target as HTMLSelectElement).value))"
        >
          <option v-for="option in sizeOptions" :key="option" :value="option">{{ option }}</option>
        </select>
      </label>

      <div class="flex items-center gap-2">
        <AppButton :disabled="disabled || page <= 1" variant="secondary" @click="emit('pageChange', page - 1)">
          Anterior
        </AppButton>
        <AppButton :disabled="disabled || page >= totalPages" variant="secondary" @click="emit('pageChange', page + 1)">
          Proxima
        </AppButton>
      </div>
    </div>
  </div>
</template>
