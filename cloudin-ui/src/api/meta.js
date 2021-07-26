import request from '@/utils/request'

const api = {
    datasource_list: '/meta/datasources',
    resolver_pager: '/meta/resolverPager',
    save_datasource: '/meta/saveDS',
    save_resolver: '/meta/saveRs'
}

export function queryDatasources (parameter) {
    return request({
        url: api.datasource_list,
        method: 'get',
        params: parameter
    })
}

export function queryResolverPager (parameter) {
    return request({
        url: api.resolver_pager,
        method: 'get',
        params: parameter
    })
}

export function saveDatasource (parameter) {
    return request({
        url: api.save_datasource,
        method: 'post',
        data: parameter,
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
}

export function saveResolver (parameter) {
    return request({
        url: api.save_resolver,
        method: 'post',
        data: parameter,
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    })
}

export default api
