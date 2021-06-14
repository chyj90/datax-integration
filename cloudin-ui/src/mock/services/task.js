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
            cron: '* 0/4 * * * *',
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
Mock.mock(/\/task\/datax\/list/, 'get', dataxTaskList)
