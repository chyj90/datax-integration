import request from '@/utils/request'

const api = {
    datax_task_list: '/task/datax/list',
    pipe_line_list: '/task/pipeline/list',
    pipe_task_detail: '/task/pipeline/task/detail',
    all_task: '/task/all'
}

export default api

export function getDataxTaskList (parameter) {
    return request({
        url: api.datax_task_list,
        method: 'get',
        params: parameter
    })
}

export function getPipelineList (parameter) {
    return request({
        url: api.pipe_line_list,
        method: 'get',
        params: parameter
    })
}

export function getPipelineTask (parameter) {
    return request({
        url: api.pipe_task_detail,
        method: 'get',
        params: parameter
    })
}
export function getAllTask (parameter) {
    return request({
        url: api.all_task,
        method: 'get',
        params: parameter
    })
}
