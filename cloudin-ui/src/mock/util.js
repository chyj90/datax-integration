const responseBody = {
    message: {},
    code: 200
  }

  export const builder = (data, message, code = 0, headers = {}) => {
    responseBody.message = data
    return responseBody
  }

  export const getQueryParameters = (options) => {
    const url = options.url
    const search = url.split('?')[1]
    if (!search) {
      return {}
    }
    return JSON.parse('{"' + decodeURIComponent(search)
      .replace(/"/g, '\\"')
      .replace(/&/g, '","')
      .replace(/=/g, '":"') + '"}')
  }

  export const getBody = (options) => {
    return options.body && JSON.parse(options.body)
  }
