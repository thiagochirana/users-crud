<script setup lang="ts">
import { Eye, Pencil, Trash2 } from '@lucide/vue'
import AppButton from '@/components/ui/AppButton.vue'
import { formatDocument } from '@/features/users/api/mappers'
import type { User } from '@/features/users/types/user'

defineProps<{
  users: User[]
  deletingId?: number | null
}>()

const emit = defineEmits<{
  view: [id: number]
  edit: [id: number]
  remove: [id: number]
}>()
</script>

<template>
  <div class="space-y-4">
    <div class="grid gap-5 md:hidden">
      <article v-for="user in users" :key="user.id" class="panel p-5">
        <div class="space-y-1">
          <h2 class="text-base font-semibold text-slate-900">{{ user.name }}</h2>
          <p class="text-sm text-app-muted">Documento: {{ formatDocument(user.document) }}</p>
          <p class="text-sm text-app-muted">Cidade: {{ user.city }} / {{ user.state }}</p>
        </div>

        <div class="mt-4 grid grid-cols-1 gap-2 sm:grid-cols-3">
          <AppButton variant="secondary" @click="emit('view', user.id)">
            <Eye :size="16" />
            Visualizar
          </AppButton>
          <AppButton variant="secondary" @click="emit('edit', user.id)">
            <Pencil :size="16" />
            Editar
          </AppButton>
          <AppButton :loading="deletingId === user.id" variant="danger" @click="emit('remove', user.id)">
            <Trash2 :size="16" />
            Excluir
          </AppButton>
        </div>
      </article>
    </div>

    <div class="panel hidden overflow-hidden md:block">
      <div class="overflow-x-auto">
        <table class="min-w-full divide-y divide-app-border">
          <thead class="bg-slate-50 text-left text-xs uppercase tracking-wide text-slate-500">
            <tr>
              <th class="px-5 py-4 font-semibold">Nome</th>
              <th class="px-5 py-4 font-semibold">Documento</th>
              <th class="px-5 py-4 font-semibold">Nascimento</th>
              <th class="px-5 py-4 font-semibold">Cidade</th>
              <th class="px-5 py-4 font-semibold">Acoes</th>
            </tr>
          </thead>
          <tbody class="divide-y divide-app-border bg-white text-sm">
            <tr v-for="user in users" :key="user.id" class="text-slate-700">
              <td class="px-5 py-4 font-medium text-slate-900">{{ user.name }}</td>
              <td class="px-5 py-4">{{ formatDocument(user.document) }}</td>
              <td class="px-5 py-4">{{ user.birth_date }}</td>
              <td class="px-5 py-4">{{ user.city }} / {{ user.state }}</td>
              <td class="px-5 py-4">
                <div class="flex flex-wrap gap-2">
                  <AppButton variant="secondary" @click="emit('view', user.id)">
                    <Eye :size="16" />
                    Visualizar
                  </AppButton>
                  <AppButton variant="secondary" @click="emit('edit', user.id)">
                    <Pencil :size="16" />
                    Editar
                  </AppButton>
                  <AppButton :loading="deletingId === user.id" variant="danger" @click="emit('remove', user.id)">
                    <Trash2 :size="16" />
                    Excluir
                  </AppButton>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>
