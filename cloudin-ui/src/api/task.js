import request from '@/utils/request'

const api = {
    datax_task_list: '/task/datax/list'
}

export default api

export function getDataxTaskList (parameter) {
    return request({
        url: api.datax_task_list,
        method: 'get',
        params: parameter
    })
}
