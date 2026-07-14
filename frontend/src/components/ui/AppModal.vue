<script setup lang="ts">
import { X } from '@lucide/vue'
import { nextTick, ref, watch } from 'vue'

const props = defineProps<{
  open: boolean
  title: string
  description?: string
}>()

const emit = defineEmits<{
  close: []
}>()

const dialogRef = ref<HTMLDivElement | null>(null)

watch(
  () => props.open,
  async (isOpen) => {
    if (!isOpen) return
    await nextTick()
    dialogRef.value?.focus()
  },
)
</script>

<template>
  <teleport to="body">
    <div v-if="open" class="fixed inset-0 z-40 flex items-end justify-center bg-slate-950/50 p-4 sm:items-center" @click.self="emit('close')">
      <div
        ref="dialogRef"
        aria-modal="true"
        class="w-full max-w-lg rounded-3xl bg-white p-6 shadow-2xl"
        role="dialog"
        tabindex="-1"
        @keydown.esc="emit('close')"
      >
        <div class="flex items-start justify-between gap-4">
          <div>
            <h2 class="text-lg font-semibold text-slate-900">{{ title }}</h2>
            <p v-if="description" class="mt-1 text-sm text-app-muted">{{ description }}</p>
          </div>

          <button
            aria-label="Fechar modal"
            class="rounded-xl p-2 text-slate-500 transition hover:bg-slate-100"
            type="button"
            @click="emit('close')"
          >
            <X :size="18" />
          </button>
        </div>

        <div class="mt-6">
          <slot />
        </div>
      </div>
    </div>
  </teleport>
</template>
