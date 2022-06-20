const path = require('path')
const proxyObj = require('./baseUrl')
// 依赖包分析工具
const WebpackBundleAnalyzer = require('webpack-bundle-analyzer').BundleAnalyzerPlugin
const TerserPlugin = require('terser-webpack-plugin')
const isAnalyze = false // 是否启用bundleAnalyzer
const resolve = dir => path.join(__dirname, dir)



module.exports = {
	publicPath: './',
	assetsDir: 'assets',
	productionSourceMap: false,
	devServer: {
		contentBase: path.join(__dirname, `../public/`),
		host: '0.0.0.0', // 'lhl.zcsmart.com', // 'lhl.zcsmart.com',//'lhl.zcsmart.com',//'localhost',
		port: 80,
		open: false,
		proxy: process.env.OPEN_PROXY === false ? {} : proxyObj, //线上环境
		overlay: {
			warnings: false,
			errors: false
		},
		disableHostCheck: true
	},
	lintOnSave: false,
	css: {
		// 是否使用css分离插件 ExtractTextPlugin
		extract: process.env.NODE_ENV === 'production',
		// 启用 CSS modules for all css / pre-processor files.
		modules: false,
		// loaderOptions: {
		// 	less: {
		// 		globalVars: {
		// 			hack: `true; @import '~@/assets/style/var.less';`
		// 		}
		// 	}
		// }
	},


	pluginOptions: {
		lintStyleOnBuild: process.env.NODE_ENV !== 'production', // 添加了插件(@ascendancyy/vue-cli-plugin-stylelint), 所以需要配置
		stylelint: {
			files: ['src/**/*.vue', 'src/assets/style/*.l?(e|c)ss']
		},
		'style-resources-loader': {
			preProcessor: 'less',
			patterns: [path.resolve(__dirname, './src/assets/style/css/treeNode.less')] // less所在文件路径
		},
	},
	pwa: {
		iconPaths: {
			favicon32: 'logo.ico',
			favicon16: 'logo.ico',
			appleTouchIcon: 'logo.ico',
			maskIcon: 'logo.ico',
			msTileImage: 'logo.ico'
		}
	},
	chainWebpack: config => {
		if (isAnalyze) { // 设置bundle-analyzer
			config
				.plugin('bundle-analyzer')
				.use(WebpackBundleAnalyzer)
		}
		// 多加几个别名，节省代码
		config.resolve.alias
			.set('@', resolve('src'))
			.set('assets', resolve('src/assets'))
			.set('components', resolve('src/components'))
	},
	configureWebpack: config => {
		config.optimization = {
			splitChunks: {
				cacheGroups: {
					theme: { // element包打包时拆分
						name: 'chunk-theme',
						test: /[\\/]node_modules[\\/]element-ui[\\/]/,
						chunks: 'all',
						priority: 1,
						reuseExistingChunk: true,
						enforce: true
					}
				}
			}
		}
		if (process.env.NODE_ENV === 'production') {
			config.plugins.push(
				new TerserPlugin({
					terserOptions: {
						warnings: false,
						compress: {
							drop_debugger: true,
							drop_console: true
						}
					},
					sourceMap: false,
					parallel: true
				})
			)
		}
	}
}
console.log(module.exports.devServer.proxy)
