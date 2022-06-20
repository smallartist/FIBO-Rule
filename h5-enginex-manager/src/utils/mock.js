var Mock = require('mockjs')


// Mock.mock("/Riskmanage/datasource/save", {
// 	"status": "1",
// 	data: {

// 	}
// })
Mock.mock("/Riskmanage/v3/qvshi2", {
	"status": "1",
	data: {
		dycs: [{
			dycslabel: '0609',
			'dycsvalue|10-100': 1
		},{
			dycslabel: '0608',
			'dycsvalue|10-100': 1
		},{
			dycslabel: '0607',
			'dycsvalue|10-100': 1
		},{
			dycslabel: '0606',
			'dycsvalue|10-100': 1
		},{
			dycslabel: '0605',
			'dycsvalue|10-100': 1
		}],
		'jcjg':[{
			'jcjglabel':Mock.mock('@cname()'),
			'jcjgvalue|20-39':1
		},{
			'jcjglabel':Mock.mock('@cname()'),
			'jcjgvalue|20-39':1
		},{
			'jcjglabel':Mock.mock('@cname()'),
			'jcjgvalue|20-39':1
		},{
			'jcjglabel':Mock.mock('@cname()'),
			'jcjgvalue|20-39':1
		},{
			'jcjglabel':Mock.mock('@cname()'),
			'jcjgvalue|20-39':1
		},{
			'jcjglabel':Mock.mock('@cname()'),
			'jcjgvalue|20-39':1
		},{
			'jcjglabel':Mock.mock('@cname()'),
			'jcjgvalue|20-39':1
		},{
			'jcjglabel':Mock.mock('@cname()'),
			'jcjgvalue|20-39':1
		},{
			'jcjglabel':Mock.mock('@cname()'),
			'jcjgvalue|20-39':1
		},{
			'jcjglabel':Mock.mock('@cname()'),
			'jcjgvalue|20-39':1
		},{
			'jcjglabel':Mock.mock('@cname()'),
			'jcjgvalue|20-39':1
		},{
			'jcjglabel':Mock.mock('@cname()'),
			'jcjgvalue|20-39':1
		}],
		gzmz:{
			gzmztime:['0603','0604','0605','0606','0607','0608','0609'],
			gzmzdata:[
				{
					gzmzlabel:'规则1',
					gzmzvalue:[parseInt(Math.random()*70),parseInt(Math.random()*70),parseInt(Math.random()*70),parseInt(Math.random()*70),parseInt(Math.random()*70),parseInt(Math.random()*70),parseInt(Math.random()*70)]
				},
				{
					gzmzlabel:Mock.mock('@cname()'),
					gzmzvalue:[parseInt(Math.random()*70),parseInt(Math.random()*70),parseInt(Math.random()*70),parseInt(Math.random()*70),parseInt(Math.random()*70),parseInt(Math.random()*70),parseInt(Math.random()*70)]
				},
				{
					gzmzlabel:Mock.mock('@cname()'),
					gzmzvalue:[parseInt(Math.random()*70),parseInt(Math.random()*70),parseInt(Math.random()*70),parseInt(Math.random()*70),parseInt(Math.random()*70),parseInt(Math.random()*70),parseInt(Math.random()*70)]
				},
				{
					gzmzlabel:Mock.mock('@cname()'),
					gzmzvalue:[parseInt(Math.random()*70),parseInt(Math.random()*70),parseInt(Math.random()*70),parseInt(Math.random()*70),parseInt(Math.random()*70),parseInt(Math.random()*70),parseInt(Math.random()*70)]
				},
				{
					gzmzlabel:Mock.mock('@cname()'),
					gzmzvalue:[parseInt(Math.random()*70),parseInt(Math.random()*70),parseInt(Math.random()*70),parseInt(Math.random()*70),parseInt(Math.random()*70),parseInt(Math.random()*70),parseInt(Math.random()*70)]
				}
			]
		}
	}
})


Mock.mock("/Riskmanage/v3/qvshi1", {
	"status": "1",
	data: {
		klist: [{ // 每一个模板
			engindId: 1, //引擎ID
			engName: 'XX引擎', //引擎name
			time: { //时间
				startTime: '', // 开始时间
				endTime: '' // 结束时间
			},
			dimensions: [ // 统计维度们
				{
					dimension: 'call', //  调用次数维度
					field: [] //  此维度下的指标
				},
				{
					dimension: 'rule',
					field: []
				},
			],
			chart: [{
				Time: '4.15',
				value: {
					call: 20,
					rule: 80,
				}

			}, {
				Time: '4.15',
				value: {
					call: 30,
					rule: 90,
				}
			}, {
				Time: '4.15',
				value: {
					call: 40,
					rule: 100,
				}
			}, {
				Time: '4.15',
				value: {
					call: 50,
					rule: 200,
				}
			}, {
				Time: '4.15',
				value: {
					call: 60,
					rule: 400,
				}
			}, {
				Time: '4.15',
				value: {
					call: 70,
					rule: 800,
				}
			}, {
				Time: '4.15',
				value: {
					call: 80,
					rule: 1000,
				}
			}]



		}]
	}
})
Mock.mock("/Riskmanage/v3/1", {
	"status": "1",
	data: {
		klist: [{
			name: '1引擎', //引擎名
			id: 1, //id 
			top: false, // 是否置顶
			call: { // 调用次数
				'yesterday': 1, // 昨天
				'today': 456, // 今天
				'todayPredict': 789, // 今天预计
			},
			result: { // 决策结果
				yesterday: 123,
				today: 456,
				todayPredict: 789,
			},
			hit: { // 命中规则
				yesterday: 123,
				today: 456,
				todayPredict: 789,
			}

		}, {
			name: '2引擎',
			id: 2,
			top: false,
			call: {
				yesterday: 123,
				today: 456,
				todayPredict: 789,
			},
			result: {
				yesterday: 123,
				today: 456,
				todayPredict: 789,
			},
			hit: {
				yesterday: 123,
				today: 456,
				todayPredict: 789,
			}

		}, {
			name: '3引擎',
			id: 3,
			top: false,
			call: {
				yesterday: 123,
				today: 456,
				todayPredict: 789,
			},
			result: {
				yesterday: 123,
				today: 456,
				todayPredict: 789,
			},
			hit: {
				yesterday: 123,
				today: 456,
				todayPredict: 789,
			}

		}, {
			name: '4引擎',
			id: 4,
			top: false,
			call: {
				yesterday: 123,
				today: 456,
				todayPredict: 789,
			},
			result: {
				yesterday: 123,
				today: 456,
				todayPredict: 789,
			},
			hit: {
				yesterday: 123,
				today: 456,
				todayPredict: 789,
			}

		}, {
			name: '5引擎',
			id: 5,
			top: false,
			call: {
				yesterday: 123,
				today: 456,
				todayPredict: 789,
			},
			result: {
				yesterday: 123,
				today: 456,
				todayPredict: 789,
			},
			hit: {
				yesterday: 123,
				today: 456,
				todayPredict: 789,
			}

		}]
	}
})
