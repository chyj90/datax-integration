import Mock from 'mockjs2'
import { builder, getQueryParameters } from '../util'

const totalCount = 2056
const dataxTaskList = (options) => {
    const parameters = getQueryParameters(options)

    const result = []
    const pageNo = parseInt(parameters.pageNo)
    const pageSize = parseInt(parameters.pageSize)
    const totalPage = Math.ceil(totalCount / pageSize)
    const key = (pageNo - 1) * pageSize
    const next = (pageNo >= totalPage ? (totalCount % pageSize) : pageSize) + 1

    for (let i = 1; i < next; i++) {
        const tmpKey = key + i
        result.push({
            ID: tmpKey,
            name: '测试模版' + tmpKey,
            owner: 'admin',
            jsonstr: '{"in":"A","out":"B"}'
        }
        )
    }

    return builder({
        pageSize: pageSize,
        pageNo: pageNo,
        totalCount: totalCount,
        totalPage: totalPage,
        data: result
    })
}

const pipelineList = (options) => {
    const parameters = getQueryParameters(options)

    const result = []
    const pageNo = parseInt(parameters.pageNo)
    const pageSize = parseInt(parameters.pageSize)
    const totalPage = Math.ceil(totalCount / pageSize)
    const key = (pageNo - 1) * pageSize
    const next = (pageNo >= totalPage ? (totalCount % pageSize) : pageSize) + 1

    for (let i = 1; i < next; i++) {
        const tmpKey = key + i
        result.push({
            ID: tmpKey,
            name: '测试流水线' + tmpKey,
            owner: 'admin',
            cron: '* * * * * * ？',
            status: tmpKey % 3 === 0 ? 1 : 0
        }
        )
    }

    return builder({
        pageSize: pageSize,
        pageNo: pageNo,
        totalCount: totalCount,
        totalPage: totalPage,
        data: result
    })
}

const pipelineTaskDetail = () => {
    return builder({
        ID: 1,
        name: '测试任务详情',
        cron: '* * * * * * ?',
        owner: 'admin',
        tasks: [
            {
                ID: 1,
                name: '任务1'
            },
            {
                ID: 2,
                name: '任务2'
            }
        ]
    })
}
Mock.mock(/\/task\/datax\/list/, 'get', dataxTaskList)
Mock.mock(/\/task\/pipeline\/list/, 'get', pipelineList)
Mock.mock(/\/pipeline\/task\/detail/, 'get', pipelineTaskDetail)
