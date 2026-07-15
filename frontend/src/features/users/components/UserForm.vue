<script setup lang="ts">
import AppButton from '@/components/ui/AppButton.vue'
import AppInput from '@/components/ui/AppInput.vue'
import AppSelect from '@/components/ui/AppSelect.vue'
import { formatDocument } from '@/features/users/api/mappers'
import type { UserFieldErrors, UserRequest } from '@/features/users/types/user'

const props = withDefaults(
  defineProps<{
    modelValue: UserRequest
    errors?: UserFieldErrors
    submitting?: boolean
    submitLabel?: string
    cancelLabel?: string
  }>(),
  {
    errors: () => ({}),
    submitting: false,
    submitLabel: 'Salvar',
    cancelLabel: 'Cancelar',
  },
)

const emit = defineEmits<{
  'update:modelValue': [value: UserRequest]
  submit: []
  cancel: []
}>()

const stateOptions = [{ label: 'Selecione', value: '' }].concat(
  ['AC', 'AL', 'AP', 'AM', 'BA', 'CE', 'DF', 'ES', 'GO', 'MA', 'MT', 'MS', 'MG', 'PA', 'PB', 'PR', 'PE', 'PI', 'RJ', 'RN', 'RS', 'RO', 'RR', 'SC', 'SP', 'SE', 'TO'].map(
    (state) => ({ label: state, value: state }),
  ),
)

function updateField<K extends keyof UserRequest>(field: K, value: UserRequest[K]) {
  const nextValue = field === 'document' ? formatDocument(String(value).replace(/\D/g, '')) : value

  emit('update:modelValue', {
    ...props.modelValue,
    [field]: nextValue,
  })
}
</script>

<template>
  <form class="panel p-6" @submit.prevent="emit('submit')">
    <div class="grid gap-5 md:grid-cols-2">
      <div class="md:col-span-2">
        <AppInput
          :error="errors.name"
          :model-value="modelValue.name"
          autocomplete="name"
          label="Nome completo"
          :maxlength="255"
          required
          @update:model-value="updateField('name', $event)"
        />
      </div>

      <AppInput
        :error="errors.birth_date"
        hint="Use uma data valida e anterior ou igual a hoje."
        :model-value="modelValue.birth_date"
        label="Data de nascimento"
        required
        type="date"
        @update:model-value="updateField('birth_date', $event)"
      />

      <AppInput
        :error="errors.document"
        hint="Informe um CPF com 11 digitos."
        inputmode="numeric"
        :model-value="modelValue.document"
        label="Documento (CPF)"
        :maxlength="14"
        required
        @update:model-value="updateField('document', $event)"
      />

      <div class="md:col-span-2">
        <AppInput
          :error="errors.address_line"
          :model-value="modelValue.address_line"
          autocomplete="street-address"
          label="Endereco"
          :maxlength="255"
          required
          @update:model-value="updateField('address_line', $event)"
        />
      </div>

      <AppInput
        :error="errors.address_number"
        :model-value="modelValue.address_number"
        label="Numero"
        :maxlength="50"
        required
        @update:model-value="updateField('address_number', $event)"
      />

      <AppInput
        :error="errors.city"
        :model-value="modelValue.city"
        autocomplete="address-level2"
        label="Cidade"
        :maxlength="255"
        required
        @update:model-value="updateField('city', $event)"
      />

      <AppSelect
        :error="errors.state"
        :model-value="modelValue.state"
        :options="stateOptions"
        label="Estado"
        @update:model-value="updateField('state', $event)"
      />

      <AppInput
        :error="errors.zip"
        hint="Informe um CEP com 8 digitos."
        :model-value="modelValue.zip"
        autocomplete="postal-code"
        label="CEP"
        :maxlength="9"
        required
        @update:model-value="updateField('zip', $event)"
      />
    </div>

    <div class="mt-6 flex flex-col-reverse gap-3 sm:flex-row sm:justify-end">
      <AppButton :disabled="submitting" variant="secondary" @click="emit('cancel')">
        {{ cancelLabel }}
      </AppButton>
      <AppButton :loading="submitting" type="submit">
        {{ submitLabel }}
      </AppButton>
    </div>
  </form>
</template>
