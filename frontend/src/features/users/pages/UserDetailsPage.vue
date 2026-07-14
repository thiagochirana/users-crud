<script setup lang="ts">
import { ArrowLeft, Pencil } from '@lucide/vue'
import { onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import AppAlert from '@/components/ui/AppAlert.vue'
import AppButton from '@/components/ui/AppButton.vue'
import AppSpinner from '@/components/ui/AppSpinner.vue'
import PageHeader from '@/components/ui/PageHeader.vue'
import { formatDocument } from '@/features/users/api/mappers'
import { asApiError, getUser } from '@/features/users/api/users'
import type { User } from '@/features/users/types/user'

const route = useRoute()
const router = useRouter()

const user = ref<User | null>(null)
const loading = ref(true)
const errorMessage = ref('')

async function fetchUser() {
  loading.value = true
  errorMessage.value = ''

  try {
    user.value = await getUser(Number(route.params.id))
  } catch (error) {
    errorMessage.value = asApiError(error).message
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  void fetchUser()
})
</script>

<template>
  <section class="space-y-6">
    <PageHeader description="Visualize os dados completos do usuario." title="Detalhes do usuario">
      <div class="flex flex-col gap-3 sm:flex-row">
        <AppButton variant="secondary" @click="router.push('/users')">
          <ArrowLeft :size="16" />
          Voltar
        </AppButton>
        <AppButton v-if="user" @click="router.push(`/users/${user.id}/edit`)">
          <Pencil :size="16" />
          Editar usuario
        </AppButton>
      </div>
    </PageHeader>

    <div v-if="loading" class="panel flex min-h-64 items-center justify-center">
      <div class="flex flex-col items-center gap-3 text-sm text-app-muted">
        <AppSpinner size="lg" />
        <p>Carregando usuario...</p>
      </div>
    </div>

    <AppAlert v-else-if="errorMessage" :message="errorMessage" tone="error" />

    <div v-else-if="user" class="panel p-6">
      <dl class="grid gap-5 md:grid-cols-2">
        <div>
          <dt class="text-sm font-medium text-app-muted">Nome completo</dt>
          <dd class="mt-1 text-base text-slate-900">{{ user.name }}</dd>
        </div>
        <div>
          <dt class="text-sm font-medium text-app-muted">Data de nascimento</dt>
          <dd class="mt-1 text-base text-slate-900">{{ user.birth_date }}</dd>
        </div>
        <div>
          <dt class="text-sm font-medium text-app-muted">Documento</dt>
          <dd class="mt-1 text-base text-slate-900">{{ formatDocument(user.document) }}</dd>
        </div>
        <div>
          <dt class="text-sm font-medium text-app-muted">CEP</dt>
          <dd class="mt-1 text-base text-slate-900">{{ user.zip }}</dd>
        </div>
        <div class="md:col-span-2">
          <dt class="text-sm font-medium text-app-muted">Endereco</dt>
          <dd class="mt-1 text-base text-slate-900">{{ user.address_line }}, {{ user.address_number }}</dd>
        </div>
        <div>
          <dt class="text-sm font-medium text-app-muted">Cidade</dt>
          <dd class="mt-1 text-base text-slate-900">{{ user.city }}</dd>
        </div>
        <div>
          <dt class="text-sm font-medium text-app-muted">Estado</dt>
          <dd class="mt-1 text-base text-slate-900">{{ user.state }}</dd>
        </div>
      </dl>
    </div>
  </section>
</template>
