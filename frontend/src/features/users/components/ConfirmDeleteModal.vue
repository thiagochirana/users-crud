<script setup lang="ts">
import AppButton from '@/components/ui/AppButton.vue'
import AppModal from '@/components/ui/AppModal.vue'

defineProps<{
  open: boolean
  loading?: boolean
  userName?: string
}>()

const emit = defineEmits<{
  close: []
  confirm: []
}>()
</script>

<template>
  <AppModal
    :description="userName ? `Tem certeza que deseja excluir ${userName}?` : 'Tem certeza que deseja excluir este usuario?'"
    :open="open"
    title="Confirmar exclusao"
    @close="emit('close')"
  >
    <div class="flex flex-col-reverse gap-3 sm:flex-row sm:justify-end">
      <AppButton :disabled="loading" variant="secondary" @click="emit('close')">Cancelar</AppButton>
      <AppButton :loading="loading" variant="danger" @click="emit('confirm')">Excluir usuario</AppButton>
    </div>
  </AppModal>
</template>
