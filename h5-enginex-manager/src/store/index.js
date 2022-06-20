import Vue from 'vue'
import Vuex from 'vuex'
// import request from '../utils/request.js'
import {
	GetdeepObj
} from '@/utils/GetdeepObj.js'
import {
	getFieldUser, // 获取指标
	getDataSourcelist, //获取数据库列表
	getType2tree, // 获取规则集树
	getType2, //获取规则集
	getSCOList, //获取评分卡
	getDecisionTablesList, //获取决策表
	getDecisionTreeList, //获取决策树
	getEngineList, //获取引擎
	getInterfaceList, //获取接口
	getListOperationList, // 获取集合
	getMqSourceList ,// 获取消息队列源
	getBlackWhiteList , //获取黑白名单
} from '../api/index.js'
Vue.use(Vuex)
export default new Vuex.Store({
	state: {
		barShrink: false,
		isFieldUser: true,
		FieldUser: null,
		FieldUserObj: null,
		Sourcelist: null,
		blackWhiteId: null,
		isRuleList: true,
		RuleList: null,
		SCO: null,
		decisionTable: null,
		decisionTree: null,
		Engine: null,
		Interface: null,
		listOperation: null,
		Mqlist: null,
		BlackWhiteList:null,
		cacheList: {
			Engine: {
				allow: true,
				url: getEngineList,
				dataStr: 'enginelist',
				body: {
					pageNo: 0,
					pageSize: 0,
				}
			},
			// listOperation: {
			// 	allow: true,
			// 	url: getListOperationList,
			// 	dataStr: 'klist',
			// 	body: {
			// 		pageNo: 0,
			// 		pageSize: 0,
			// 		entity: {}
			// 	}
			// },
			Interface: {
				allow: true,
				url: getInterfaceList,
				dataStr: 'klist',
				body: {
					pageNum: 0,
					pageSize: 0,
				}
			},
			SCO: {
				allow: true,
				url: getSCOList,
				dataStr: 'scorecardList',
				body: {
					pageNum: 0,
					pageSize: 0,
					parentId: 0,
					search: false,
					status: [0, 1],
					value: ""
				}
			},
			decisionTable: {
				allow: true,
				url: getDecisionTablesList,
				dataStr: 'klist',
				body: {
					pageNum: 0,
					pageSize: 0,
					decisionTables: {},
					pageNum: 1,
					status: "0,1"
				}
			},
			decisionTree: {
				allow: true,
				url: getDecisionTreeList,
				dataStr: 'klist',
				body: {
					decisionTree: {},
					pageNum: 0,
					pageSize: 0,
					status: "0,1"
				}
			},
			Sourcelist: {
				allow: true,
				url: getDataSourcelist,
				dataStr: 'data',
				body: {
					pageNum: 0,
					pageSize: 0,
				}
			},
			Mqlist: {
				allow: true,
				url: getMqSourceList,
				dataStr: 'list',
				body: {
					pageNum: 0,
					pageSize: 0,
				}
			},
			BlackWhiteList: {
				allow: true,
				url: getBlackWhiteList,
				dataStr: 'listDbs',
				body: {
					pageNum: 0,
					pageSize: 0,
				}
			},
		},
		operator: [{
				label: 'begin',
				value: 'begin',
				valueType:1  //  1为固定值型  2为 复杂规则型   0 为无值
			},
			{
				label: 'where',
				value: 'where',
				valueType:2
			},
			{
				label: 'or',
				value: 'or',
				valueType:2
			},
			{
				label: 'within',
				value: 'within',
				valueType:1
			},
			{
				label: 'next',
				value: 'next',
				valueType:1
			},
			{
				label: 'followdBy',
				value: 'followdBy',
				valueType:1
			},
			{
				label: 'oneOrMore',
				value: 'oneOrMore',
				valueType:0
			},
			{
				label: 'times',
				value: 'times',
				valueType:1
			},
			{
				label: 'timesOrMore',
				value: 'timesOrMore',
				valueType:1
			},
		]





	},
	mutations: {
		setCache(state, res) {

			state[res.str] = res.data
		},
		setbarShrink(state, res) {
			state.barShrink = res
		},
		setFieldUser(state, res) {
			// debugger
			state.FieldUser = res
		},
		setFieldUserObj(state, res) {
			state.FieldUserObj = res
		},
		setBlackWhiteId(state, res) {
			state.blackWhiteId = res
		},
		setRuleList(state, res) {
			state.RuleList = res
		},
		getfielduserObj(state, res) {
			if (res.status !== '1') return
			res.data.fieldList = res.data.fieldList.map(value => {
				if (value.valueType === 6) {
					return {
						label: value.fieldCn,
						value: value.fieldEn,
						valueType: value.valueType,
						jsonValue: value.jsonValue,
						children: GetdeepObj(JSON.parse(value.jsonValue))
					}
				} else {
					return {
						label: value.fieldCn,
						value: value.fieldEn,
						valueType: value.valueType,
					}
				}

			})
			state.FieldUserObj = res

			localStorage.setItem("fielduserObj", JSON.stringify(res))
		}
	},
	actions: {
		getcache(context, str) {

			let obj = context.state.cacheList[str]

			if (context.state[str] != null || !obj.allow) {
				return
			}
			obj.allow = false
			if (window.localStorage.getItem(str)) {
				if (JSON.parse(window.localStorage.getItem(str))) {

					context.commit('setCache', {
						str: str,
						data: JSON.parse(window.localStorage.getItem(str))
					})
				}
				obj.allow = true
				return
			}

			obj.url(obj.body).then(res => {
				if (res.status == 1) {


					context.commit('setCache', {
						str: str,
						data: res.data[obj.dataStr]
					})
					localStorage.setItem(str, JSON.stringify(res.data[obj.dataStr]))

				}
				obj.allow = true
			})
		},
		regetcache(context, str) {
			context.commit('setCache', {
				str: str,
				data: null
			})
			localStorage.setItem(str, "")
			context.dispatch('getcache', str)
		},
		getfielduser(context) {
			if (context.state.FieldUser == null && context.state.isFieldUser) {
				context.state.isFieldUser = false
				let success = {
					fielduser: false,
					fielduserObj: false
				}

				if (window.localStorage.getItem("fielduser")) {
					if (JSON.parse(window.localStorage.getItem("fielduser")).status == "1") {

						context.commit('setFieldUser', JSON.parse(window.localStorage.getItem("fielduser")))
						success.fielduser = true
					}
				}
				if (window.localStorage.getItem("fielduserObj")) {
					if (JSON.parse(window.localStorage.getItem("fielduserObj")).status == "1") {
						context.commit('setFieldUserObj', JSON.parse(window.localStorage.getItem(
							"fielduserObj")))
						success.fielduserObj = true
					}
				}

				if (success.fielduser && success.fielduserObj) {
					context.state.isFieldUser = true
					return
				}
				getFieldUser({}).then(res => {
					if (res.status == 1) {
						// debugger
						context.commit('setFieldUser', res)
						localStorage.setItem("fielduser", JSON.stringify(res))
						// debugger
						context.commit('getfielduserObj', JSON.parse(JSON.stringify(res)))
					}
					context.state.isFieldUser = true
				})
			}
		},
		async getRuleList(context) {
			// console.log(context,context.state.RuleList,context.state.isRuleList)
			if (context.state.RuleList == null && context.state.isRuleList) {
				context.state.isRuleList = false

				if (window.localStorage.getItem("RuleList")) {
					context.commit('setRuleList', JSON.parse(window.localStorage.getItem("RuleList")))
					context.state.isRuleList = true
					return
				}

				const cont = await getType2tree({
					status: 1,
					parentIds: ''
				})
				if (cont.status != "1") {
					setCatch()
					context.state.isRuleList = true
					return
				}
				context.commit('setRuleList', cont.data.rlist)
				localStorage.setItem("RuleList", JSON.stringify(
					cont.data.rlist))
				context.state.isRuleList = true



			}


			function setCatch() {
				context.commit('setRuleList', [])
				localStorage.setItem("RuleList", JSON.stringify([]))
				context.state.isRuleList = true
			}


		},

		reGetfielduser(context) {
			// debugger
			context.commit('setFieldUser', null)
			localStorage.setItem("fielduser", "")

			localStorage.setItem("fielduserObj", "")
			context.dispatch('getfielduser')
		},
		reGetRuleList(context) {
			context.commit('setRuleList', null)
			localStorage.setItem("RuleList", "")
			context.dispatch('getRuleList')

		}

	},
	modules: {},
	getters: {


	}
})
