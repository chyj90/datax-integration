import request from '@/utils/request'

const api = {
    datax_task_list: '/task/datax/list',
    pipe_line_list: '/task/pipeline/list',
    pipe_task_detail: '/task/pipeline/detail',
    all_task: '/task/all',
    save_task: '/task/saveTask',
    del_task: '/task/del/task',
    del_pipeline: '/task/del/pipeline',
    switch_pipeline_status: '/task/switch/pipeline/status',
    save_pipeline: '/task/savePipeline',
    save_pipeline_task: '/task/savePipeTask',
    del_pipeline_task: '/task/del/pipelineTask'
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

export function saveTask (parameter) {
    return request({
        url: api.save_task,
        method: 'post',
        data: parameter,
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
}

export function savePipeline (parameter) {
    return request({
        url: api.save_pipeline,
        method: 'post',
        data: parameter,
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
}

export function savePipelineTask (parameter) {
    return request({
        url: api.save_pipeline_task,
        method: 'post',
        data: parameter,
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
}

export function delTask (parameter) {
    return request({
        url: api.del_task,
        method: 'get',
        params: parameter
    })
}

export function delPipeline (parameter) {
    return request({
        url: api.del_pipeline,
        method: 'get',
        params: parameter
    })
}

export function delPipelineTask (parameter) {
    return request({
        url: api.del_pipeline_task,
        method: 'get',
        params: parameter
    })
}

export function switchPipelineStatus (parameter) {
    return request({
        url: api.switch_pipeline_status,
        method: 'get',
        params: parameter
    })
}
