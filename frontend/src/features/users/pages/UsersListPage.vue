<script setup lang="ts">
import { Plus } from '@lucide/vue'
import { computed, onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import AppAlert from '@/components/ui/AppAlert.vue'
import AppButton from '@/components/ui/AppButton.vue'
import AppPagination from '@/components/ui/AppPagination.vue'
import AppSpinner from '@/components/ui/AppSpinner.vue'
import PageHeader from '@/components/ui/PageHeader.vue'
import { asApiError, deleteUser, listUsers } from '@/features/users/api/users'
import ConfirmDeleteModal from '@/features/users/components/ConfirmDeleteModal.vue'
import UserTable from '@/features/users/components/UserTable.vue'
import type { User } from '@/features/users/types/user'
import { useAppFeedbackStore } from '@/stores/appFeedback'

const router = useRouter()
const feedbackStore = useAppFeedbackStore()

const users = ref<User[]>([])
const page = ref(1)
const size = ref(10)
const totalPages = ref(0)
const totalElements = ref(0)
const loading = ref(false)
const loadingError = ref('')
const deletingId = ref<number | null>(null)
const userPendingDeletion = ref<User | null>(null)

const isEmpty = computed(() => !loading.value && users.value.length === 0 && !loadingError.value)

async function fetchUsers() {
  loading.value = true
  loadingError.value = ''

  try {
    const response = await listUsers({ page: page.value, size: size.value })
    users.value = response.data
    totalPages.value = Math.max(response.pagination.total_pages, 1)
    totalElements.value = response.pagination.total_elements
    page.value = response.pagination.page || 1
    size.value = response.pagination.size || size.value
  } catch (error) {
    loadingError.value = asApiError(error).message
  } finally {
    loading.value = false
  }
}

function handlePageChange(nextPage: number) {
  page.value = nextPage
  void fetchUsers()
}

function handleSizeChange(nextSize: number) {
  page.value = 1
  size.value = nextSize
  void fetchUsers()
}

function openDeleteModal(id: number) {
  userPendingDeletion.value = users.value.find((user) => user.id === id) ?? null
}

async function confirmDelete() {
  if (!userPendingDeletion.value) return

  deletingId.value = userPendingDeletion.value.id

  try {
    const response = await deleteUser(userPendingDeletion.value.id)
    feedbackStore.push('success', response.message)
    userPendingDeletion.value = null

    if (users.value.length === 1 && page.value > 1) {
      page.value -= 1
    }

    await fetchUsers()
  } catch (error) {
    loadingError.value = asApiError(error).message
  } finally {
    deletingId.value = null
  }
}

onMounted(() => {
  void fetchUsers()
})
</script>

<template>
  <section class="space-y-6">
    <PageHeader description="Gerencie o cadastro de usuarios integrados com a API." title="Usuarios">
      <AppButton @click="router.push('/users/new')">
        <Plus :size="16" />
        Novo usuario
      </AppButton>
    </PageHeader>

    <AppAlert v-if="loadingError" :message="loadingError" tone="error" />

    <div v-if="loading" class="panel flex min-h-64 items-center justify-center">
      <div class="flex flex-col items-center gap-3 text-sm text-app-muted">
        <AppSpinner size="lg" />
        <p>Carregando usuarios...</p>
      </div>
    </div>

    <div v-else-if="isEmpty" class="panel flex min-h-64 flex-col items-center justify-center gap-4 p-6 text-center">
      <div class="space-y-2">
        <h2 class="text-lg font-semibold text-slate-900">Nenhum usuario cadastrado</h2>
        <p class="text-sm text-app-muted">Crie o primeiro registro para comecar a gerenciar a base.</p>
      </div>

      <AppButton @click="router.push('/users/new')">
        <Plus :size="16" />
        Novo usuario
      </AppButton>
    </div>

    <template v-else>
      <UserTable
        :deleting-id="deletingId"
        :users="users"
        @edit="router.push(`/users/${$event}/edit`)"
        @remove="openDeleteModal"
        @view="router.push(`/users/${$event}`)"
      />

      <AppPagination
        :disabled="loading"
        :page="page"
        :size="size"
        :total-elements="totalElements"
        :total-pages="totalPages"
        @page-change="handlePageChange"
        @size-change="handleSizeChange"
      />
    </template>

    <ConfirmDeleteModal
      :loading="Boolean(deletingId)"
      :open="Boolean(userPendingDeletion)"
      :user-name="userPendingDeletion?.name"
      @close="userPendingDeletion = null"
      @confirm="confirmDelete"
    />
  </section>
</template>
