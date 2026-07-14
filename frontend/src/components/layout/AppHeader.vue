<script setup lang="ts">
import { RouterLink } from 'vue-router'
</script>

<template>
  <header class="sticky top-0 z-30 border-b border-white/70 bg-white/75 backdrop-blur-xl">
    <div class="page-container py-4">
      <div class="flex items-center justify-between gap-4">
        <div class="space-y-1">
          <RouterLink class="inline-flex items-center gap-3 text-lg font-semibold text-slate-950" to="/users">
            <span>Sistema de Usuarios</span>
          </RouterLink>
        </div>

        <div class="hidden rounded-full border border-primary-100 bg-primary-50/70 px-3 py-1.5 text-xs font-medium text-primary-900 sm:block">
          Painel administrativo
        </div>
      </div>
    </div>
  </header>
</template>
