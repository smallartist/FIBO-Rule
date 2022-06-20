import Vue from 'vue';
import Router from 'vue-router';

Vue.use(Router);

export default new Router({
    routes: [
        {
		    path: '/redirect/:path(.*)',
		    component: () => import('../components/page/redirect/index.vue'),
		},
        {
            path: '/',
            redirect: '/dashboard'
        },
		{
		    path: '/login',
		    component: () => import('../components/page/Login.vue'),
		    meta: { title: '登录' }
		},
        {
            path: '/',
            component: () => import('../components/common/Home.vue'),
            meta: { title: '自述文件' },
            children: [
                {
                    path: '/dashboard',
                    component: () => import('../components/page/Dashboard.vue'),
                    meta: { title: '系统首页' }
                },
                {
                    path: '/engineList',
                    component: () => import('../components/page/EngineListV2.vue'),
                    meta: { title: '引擎列表' }
                },
                {
                    path: '/engine_decision_flow',
                    component: () => import('../components/page/Engine_decision_flow.vue'),
                    meta: { title: '决策流' }
                },
				{
                    path: '/dataFlowEngine',
                    component: () => import('../components/page/dataFlowEngine.vue'),
                    meta: { title: '数据流引擎列表' }
                },{
                    path: '/dataFlowEngineCont',
					name:'dataFlowEngineCont',
                    component: () => import('../components/page/dataFlowEngineCont.vue'),
                    meta: { title: '数据流引擎' }
                },
				{
				    path: '/scorecard',
				    component: () => import('../components/page/scorecard.vue'),
				    meta: { title: '评分卡管理' }
				},
				{
				    path: '/listOperation',
				    component: () => import('../components/page/listOperation.vue'),
				    meta: { title: '集合规则' }
				},{
				    path: '/dataRinse',
				    component: () => import('../components/page/dataRinse.vue'),
				    meta: { title: '数据清洗' }
				},
				{
				    path: '/Knowledge',
				    component: () => import('../components/page/Knowledge.vue'),
				    meta: { title: '复杂规则集' }
				},
                {
                    path: '/groovyRule',
                    component: () => import('../components/page/groovyRule.vue'),
                    meta: { title: '脚本规则集' }
                },{
                    path: '/decisionTable',
                    component: () => import('../components/page/decisionTable.vue'),
                    meta: { title: '决策表' }
                },{
                    path: '/decisionTree',
                    component: () => import('../components/page/decisionTree.vue'),
                    meta: { title: '决策树' }
                },

                {
                    path:'/blackWihiteData',
                    component: () => import('../components/page/blackWhiteManage/blackWihiteData.vue'),
                    meta: { title: '名单库管理' }
                },
 
                {
                    path:'/logManagement',
                    component: () => import('../components/page/systemManagement/logManagement.vue'),
                    meta: { title: '日志管理' }
                },
				{
				    path:'/ruleLog',
				    component: () => import('../components/page/ruleLog.vue'),
				    meta: { title: '规则报表' }
				},
				{
				    path:'/eventLog',
				    component: () => import('../components/page/eventLog.vue'),
				    meta: { title: '事件报表' }
				},
				{
				    path:'/examineCentre',
				    component: () => import('../components/page/examineCentre.vue'),
				    meta: { title: '审批管理' }
				},{
				    path:'/examineCurrent',
				    component: () => import('../components/page/examineCurrent.vue'),
				    meta: { title: '审批流' }
				},{
				    path:'/examineSet',
				    component: () => import('../components/page/examineSet.vue'),
				    meta: { title: '审批设置' }
				},
                {
                    path: '/indexModels',
                    component: () => import('@/components/models/index.vue'),
                    meta: {title: '模型管理'}
                },{
                    path: '/engine_result',
                    component: () => import('@/components/page/Engine_watch.vue'),
                    meta: {title: '决策流监控列表'}
                },{
                    path: '/engine_result_cont/:id',
                    component: () => import('@/components/page/Engine_watch_cont.vue'),
                    meta: {title: '决策流监控'}
                }, 

				{
                    path: '/businessRuleRel',
                    component: () => import('@/components/page/businessRuleRel.vue'),
                    meta: {title: '业务类型'}
                },

				{
                    path: '/tendencyAnalyse',
                    component: () => import('@/components/page/tendencyAnalyse.vue'),
                    meta: {title: '分析中心'}
                },
                {
                    path: '/testModule',
                    component: () => import('../components/page/testModule.vue'),
                    meta: { title: '测试' }
                },
                {
                    path: '/404',
                    component: () => import('../components/page/404.vue'),
                    meta: { title: '404' }
                },
                {
                    path: '/403',
                    component: () => import('../components/page/403.vue'),
                    meta: { title: '403' }
                }
            ]
        },
        
        {
            path: '*',
            redirect: '/404'
        }
    ]
});
