// console.log(process.env)
var proxyObj = {}
// console.log(process.env.VUE_PROXY)
switch (process.env.NODE_ENV) {
	case 'development': // 开发环境代理地址
		proxyObj = {
			'/Riskmanage': {
				target: 'http://localhost:80', // 开发环境
				changeOrigin: true, // 是否跨域
				pathRewrite: {
					'^/Riskmanage': '/Riskmanage'
				},

			},
			'/trading': {
				target: 'http://localhost:80', // 开发环境
				changeOrigin: true, // 是否跨域
				pathRewrite: {
					'^/trading': '/trading'
				},

			},
		}
		break
	case 'test': // 测试环境代理地址
		proxyObj = {
			'/Riskmanage': {
				target: 'http://localhost:8080', // 测试环境
				changeOrigin: true, // 是否跨域
				pathRewrite: {
					'^/Riskmanage': '/Riskmanage'
				}
			}
		}
		break
	case 'release': // 军环境
		proxyObj = {
			'/Riskmanage': {
				target: 'http://192.168.3.155:8080', // 谭环境
				changeOrigin: true, // 是否跨域
				pathRewrite: {
					'^/Riskmanage': '/Riskmanage'
				}
			}
		}
		break
	case 'niu': // 牛环境
		proxyObj = {
			'/Riskmanage': {
				target: 'http://ex.fibo.cn:80', // 生产环境
				changeOrigin: true, // 是否跨域
				pathRewrite: {
					'^/Riskmanage': '/Riskmanage'
				}
			}
		}
		break
	case 'jia': // 贾环境
		proxyObj = {
			'/Riskmanage': {
				target: 'http://zhangzj.vip:8000', // 生产环境
				changeOrigin: true, // 是否跨域
				pathRewrite: {
					'^/Riskmanage': '/Riskmanage'
				}
			}
			// '/list': {
			// 	target: 'http://127.0.0.1:8888', // 生产环境
			// 	changeOrigin: true, // 是否跨域
			// 	pathRewrite: {
			// 		'^/list': '/list'
			// 	}
			// },
		}
		break
	case 'wang': // 汪环境
		proxyObj = {
			'/Riskmanage': {
				target: 'http://192.168.50.228:8080', // 生产环境
				changeOrigin: true, // 是否跨域
				pathRewrite: {
					'^/Riskmanage': '/Riskmanage'
				}
			}
		}
		break
}

module.exports = proxyObj
