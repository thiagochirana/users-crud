import { defineStore } from 'pinia'
import { ref } from 'vue'

export type FeedbackTone = 'success' | 'error' | 'info'

export interface FeedbackItem {
  id: number
  tone: FeedbackTone
  title: string
  description?: string
}

export const useAppFeedbackStore = defineStore('app-feedback', () => {
  const items = ref<FeedbackItem[]>([])
  let nextId = 1

  function push(tone: FeedbackTone, title: string, description?: string) {
    const id = nextId++
    items.value.push({ id, tone, title, description })

    window.setTimeout(() => {
      remove(id)
    }, 5000)
  }

  function remove(id: number) {
    items.value = items.value.filter((item) => item.id !== id)
  }

  return { items, push, remove }
})
