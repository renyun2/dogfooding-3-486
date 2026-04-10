import request from './request'

export const clazzApi = {
  getAll: () => request({ url: '/api/classes', method: 'get' }),
  getById: (id) => request({ url: `/api/classes/${id}`, method: 'get' }),
  create: (data) => request({ url: '/api/classes', method: 'post', data }),
  update: (id, data) => request({ url: `/api/classes/${id}`, method: 'put', data }),
  delete: (id) => request({ url: `/api/classes/${id}`, method: 'delete' })
}
