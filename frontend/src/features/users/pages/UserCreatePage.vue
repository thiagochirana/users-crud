<script setup lang="ts">
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import AppAlert from '@/components/ui/AppAlert.vue'
import PageHeader from '@/components/ui/PageHeader.vue'
import { asApiError, createUser } from '@/features/users/api/users'
import {
  formatDocument,
  formatZip,
  mapApiErrorsToFieldErrors,
} from '@/features/users/api/mappers'
import UserForm from '@/features/users/components/UserForm.vue'
import {
  initialUserForm,
  type UserFieldErrors,
  type UserRequest,
} from '@/features/users/types/user'
import { useAppFeedbackStore } from '@/stores/appFeedback'

const router = useRouter()
const feedbackStore = useAppFeedbackStore()

const form = reactive<UserRequest>({ ...initialUserForm })
const errors = ref<UserFieldErrors>({})
const formError = ref('')
const submitting = ref(false)

function updateForm(nextValue: UserRequest) {
  Object.assign(form, {
    ...nextValue,
    document: formatDocument(nextValue.document),
    zip: formatZip(nextValue.zip),
    state: nextValue.state.toUpperCase().slice(0, 2),
  })
}

async function handleSubmit() {
  errors.value = {}
  formError.value = ''
  if (submitting.value) return

  submitting.value = true

  try {
    const response = await createUser({ ...form })
    feedbackStore.push('success', response.message)
    await router.push('/users')
  } catch (error) {
    const apiError = asApiError(error)
    errors.value = mapApiErrorsToFieldErrors(apiError.fieldErrors)
    formError.value = apiError.errors.length === 0 && Object.keys(apiError.fieldErrors).length === 0 ? apiError.message : ''
  } finally {
    submitting.value = false
  }
}
</script>

<template>
  <section class="space-y-6">
    <PageHeader description="Preencha os campos para criar um novo usuario." title="Novo usuario" />

    <AppAlert v-if="formError" :message="formError" tone="error" />

    <UserForm
      :errors="errors"
      :model-value="form"
      :submitting="submitting"
      submit-label="Salvar usuario"
      @cancel="router.push('/users')"
      @submit="handleSubmit"
      @update:model-value="updateForm"
    />
  </section>
</template>
