import { createRouter, createWebHistory } from 'vue-router'
import AppLayout from '@/layouts/AppLayout.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      component: AppLayout,
      children: [
        {
          path: '',
          redirect: '/users',
        },
        {
          path: 'users',
          name: 'users-list',
          component: () => import('@/features/users/pages/UsersListPage.vue'),
        },
        {
          path: 'users/new',
          name: 'users-create',
          component: () => import('@/features/users/pages/UserCreatePage.vue'),
        },
        {
          path: 'users/:id',
          name: 'users-details',
          component: () => import('@/features/users/pages/UserDetailsPage.vue'),
        },
        {
          path: 'users/:id/edit',
          name: 'users-edit',
          component: () => import('@/features/users/pages/UserEditPage.vue'),
        },
      ],
    },
    {
      path: '/:pathMatch(.*)*',
      redirect: '/users',
    },
  ],
})

export default router
