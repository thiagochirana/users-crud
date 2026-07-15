<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import AppAlert from '@/components/ui/AppAlert.vue'
import AppSpinner from '@/components/ui/AppSpinner.vue'
import PageHeader from '@/components/ui/PageHeader.vue'
import { mapApiErrorsToFieldErrors, mapUserResponseToForm, formatDocument, formatZip } from '@/features/users/api/mappers'
import { asApiError, getUser, updateUser } from '@/features/users/api/users'
import UserForm from '@/features/users/components/UserForm.vue'
import { initialUserForm, type UserFieldErrors, type UserRequest } from '@/features/users/types/user'
import { validateUserForm } from '@/features/users/validation'
import { useAppFeedbackStore } from '@/stores/appFeedback'

const route = useRoute()
const router = useRouter()
const feedbackStore = useAppFeedbackStore()

const form = reactive<UserRequest>({ ...initialUserForm })
const loading = ref(true)
const submitting = ref(false)
const loadError = ref('')
const formError = ref('')
const errors = ref<UserFieldErrors>({})

function updateForm(nextValue: UserRequest) {
  Object.assign(form, {
    ...nextValue,
    document: formatDocument(nextValue.document),
    zip: formatZip(nextValue.zip),
    state: nextValue.state.toUpperCase().slice(0, 2),
  })
}

async function fetchUser() {
  loading.value = true
  loadError.value = ''

  try {
    const user = await getUser(Number(route.params.id))
    Object.assign(form, mapUserResponseToForm(user), {
      document: formatDocument(user.document),
      zip: formatZip(user.zip),
    })
  } catch (error) {
    loadError.value = asApiError(error).message
  } finally {
    loading.value = false
  }
}

async function handleSubmit() {
  errors.value = {}
  formError.value = ''
  if (submitting.value) return

  const validationErrors = validateUserForm(form)
  if (Object.keys(validationErrors).length > 0) {
    errors.value = validationErrors
    formError.value = 'Corrija os campos destacados e tente novamente.'
    return
  }

  submitting.value = true

  try {
    const response = await updateUser(Number(route.params.id), { ...form })
    feedbackStore.push('success', response.message)
    await router.push(`/users/${route.params.id}`)
  } catch (error) {
    const apiError = asApiError(error)
    errors.value = mapApiErrorsToFieldErrors(apiError.fieldErrors)
    formError.value = apiError.errors.length === 0 && Object.keys(apiError.fieldErrors).length === 0 ? apiError.message : ''
  } finally {
    submitting.value = false
  }
}

onMounted(() => {
  void fetchUser()
})
</script>

<template>
  <section class="space-y-6">
    <PageHeader description="Atualize os dados do usuario selecionado." title="Editar usuario" />

    <div v-if="loading" class="panel flex min-h-64 items-center justify-center">
      <div class="flex flex-col items-center gap-3 text-sm text-app-muted">
        <AppSpinner size="lg" />
        <p>Carregando formulario...</p>
      </div>
    </div>

    <template v-else>
      <AppAlert v-if="loadError" :message="loadError" tone="error" />
      <AppAlert v-if="formError" :message="formError" tone="error" />

      <UserForm
        v-if="!loadError"
        :errors="errors"
        :model-value="form"
        :submitting="submitting"
        submit-label="Salvar alteracoes"
        @cancel="router.push(`/users/${route.params.id}`)"
        @submit="handleSubmit"
        @update:model-value="updateForm"
      />
    </template>
  </section>
</template>
