import request from '../utils/request';
// console.log(request)

// export const getV = (params) => request.get(`${window.location.origin}/index.html?time=${new Date().getTime()}`)



export const fetchData = (params) => request.get('./table.json',{params})
// 登录接口
export const getLogin = (params) => request.post('Riskmanage/v2/login/login',params)
// 登出接口
export const getLogout = (params) => request.post('Riskmanage/v2/login/logout',params)
// 查询菜单权限接口
export const getMenus = (params) => request.post('Riskmanage/v2/sysMenu/getMenus',params)
// 查询首页统计信息
export const getIndexInfo = (params) => request.post('Riskmanage/v2/engine/getIndexInfo',params)
// 查询模型列表信息
export const getModelsList = (params) => request.post('Riskmanage/models/getModelsList',params)
// 获取组织下全量指标信息
export const getAllFields = (params) => request.post('Riskmanage/models/getAllFields',params)
// 添加模型
export const saveModel = (params) => request.post('Riskmanage/models/save',params)
// 删除模型
export const deleteModel = (id) => request.delete('Riskmanage/models/delete/' + id)
// 修改模型
export const updateModel = (id, params) => request.put('Riskmanage/models/update/' + id, params)
// 获取模型详细信息
export const getModelDetailInfo = (id) => request.get('Riskmanage/models/getDetailInfo/' + id)

// 查询引擎列表
export const getEngineList = (params) => request.get('Riskmanage/v2/engine/getEngineList',{params})
// 获取UUID
export const getEngineUUID = () => request.get('Riskmanage/v2/engine/getUUID')
// 引擎信息回显
export const engineInitupdate = (id) => request.get('Riskmanage/v2/engine/initupdate/' + id)
// 保存引擎信息
export const updateEngine = (params) => request.put('Riskmanage/v2/engine/update',params)



// 数据源 数据库列表
export const getDataSourcelist = (params) => request.post('/Riskmanage/datasource/getDataSourceList',params)
// 数据源 修改以及创建数据库
export const setDataSource = (params) => request.post('/Riskmanage/datasource/save',params)
// 数据源 删除
export const deleteDataSource = (id) => request.delete('/Riskmanage/datasource/'+id)
// 数据源 修改数据源
export const updataDataSource = (params) => request.post('/Riskmanage/datasource/update',params)


// 指标管理 listTree 获取
export const getfieldListTree = (params) => request.post('/Riskmanage/v2/datamanage/field/newListTree',params)

// 指标管理 指标列表 获取
export const getfieldList = (params) => request.post('/Riskmanage/v2/datamanage/field/list',params)

// 指标管理 指标列表 获取
export const addfieldList = (params) => request.post('/Riskmanage/v2/datamanage/field/addTree',params)
// 指标管理 更新类型 名称
export const updatafieldList = (params) => request.post('/Riskmanage/v2/datamanage/field/updateTree',params)
// 指标管理 更新类型 名称
export const getFieldUser = (params) => request.post('/Riskmanage/v2/datamanage/field/findFieldByUser',params)

// 保存 增加属性
export const getfieldsave = (params) => request.post('/Riskmanage/v2/datamanage/field/save',params)
// 编辑保存
export const updatafield = (params) => request.post('/Riskmanage/v2/datamanage/field/update',params)
// 启用等
export const fieldusing = (params) => request.post('/Riskmanage/v2/datamanage/field/updateStatus',params)
// 指标导入模板下载
export const fielddownTemplate = (params) => request.post('/Riskmanage/v2/datamanage/field/downTemplate',params)
// 指标批量模板上传
export const fieldupdata = (params) => request.post('/Riskmanage/v2/datamanage/field/upload',params)
// 获取指标管理
export const getfieldInfo = (id,params) => request.post('/Riskmanage/v2/datamanage/field/getFieldInfo/'+id,{params})

// 指标文件夹移动
export const updateFieldFolder = (params) => request.post('/Riskmanage/v2/datamanage/field/updateFieldFolder',params)

// ========================================规则管理==========================



// // groovy管理  list获取
// export const getRulesList = (params) => request.post('/Riskmanage/v3/rule/getRuleList',params)
// // groovy管理   删启停
// export const Rulesusing = (params) => request.post('/Riskmanage/v3/rule/updateRuleStatus',params)

// // groovy管理   保存
// export const getrulesave = (params) => request.post('/Riskmanage/v3/rule/addRule',params)
// groovy管理   具体元素内容获取
export const getScriptRuleInfo = (params) => request.post('/Riskmanage/v3/rule/getScriptRule/'+params)
// // groovy管理   修改保存
// export const getRuleUpdata = (params) => request.post('/Riskmanage/v3/rule/updateRule',params)
// groovy管理 获取规则主体
export const getRuleScriptVersionInfo = (id) => request.post('/Riskmanage/v3/ruleScriptVersion/getRuleScriptVersion/'+id.id)
// groovy管理 添加规则版本
export const addRuleScriptVersion = (params) => request.post('/Riskmanage/v3/ruleScriptVersion/addRuleScriptVersion',params)
// groovy管理 更新规则版本状态
export const updateRuleScriptVersionStatus = (params) => request.post('/Riskmanage/v3/ruleScriptVersion/updateRuleScriptVersionStatus   ',params)
// groovy管理 复制规则版本
export const copyRuleScriptVersion = (params) => request.post('/Riskmanage/v3/ruleScriptVersion/copyRuleScriptVersion',params)
// groovy管理 规则版本重命名
export const updateRuleScriptVersion = (params) => request.post('/Riskmanage/v3/ruleScriptVersion/updateRuleScriptVersion',params)







// 规则管理 listTree 获取
export const getRulesListTree = (params) => request.post('/Riskmanage/v2/knowledge/tree/list',params)
// 规则管理  list获取
export const getRulesList = (params) => request.post('/Riskmanage/v3/rule/getRuleList',params)
// 规则管理   删启停
export const Rulesusing = (params) => request.post('/Riskmanage/v3/rule/updateRuleStatus',params)
// 规则管理   文件夹新增
export const addListRules = (params) => request.post('/Riskmanage/v2/knowledge/tree/save',params)
// 规则管理   文件夹删除 修改
export const updataListRules = (params) => request.post('/Riskmanage/v2/knowledge/tree/update',params)
// 规则管理   保存
export const getrulesave = (params) => request.post('/Riskmanage/v3/rule/addRule',params)
// 规则管理   具体元素内容获取
export const getRuleInfo = (id) => request.post('/Riskmanage/v3/rule/getRuleInfo/'+id)
// 规则管理   修改保存
export const getRuleUpdata = (params) => request.post('/Riskmanage/v3/rule/updateRule',params)
// 规则管理 获取规则主体
export const getRuleVersionInfo = (params) => request.post('/Riskmanage/v3/ruleVersion/getRuleVersionInfo/'+params.id)
// 规则管理 添加规则版本
export const addRuleVersion = (params) => request.post('/Riskmanage/v3/ruleVersion/addRuleVersion',params)
// 规则管理 更新规则版本状态
export const updateRuleVersionStatus = (params) => request.post('/Riskmanage/v3/ruleVersion/updateRuleVersionStatus',params)
// 规则管理 复制规则版本
export const copyRuleVersion = (params) => request.post('/Riskmanage/v3/ruleVersion/copyRuleVersion',params)
// 规则管理 规则版本重命名
export const updateRuleVersion = (params) => request.post('/Riskmanage/v3/ruleVersion/updateRuleVersion',params)



// 简单规则管理   保存
export const getEasyRulesave = (params) => request.post('/Riskmanage/v3/rule/addRule',params)
// 简单规则管理   修改保存
export const getEasyRuleUpdata = (params) => request.post('/Riskmanage/v3/rule/updateRule',params)
// 简单规则管理   具体元素内容获取
export const getEasyRuleInfo = (params) => request.post('/Riskmanage/v3/rule/getSimpleRule/'+params.id)
// 简单规则管理   批量上传
export const ruleupdata = (params) => request.post('/Riskmanage/v3/rule/upload',params)
// 规则文件夹移动
export const updateRuleParent = (params) => request.post('/Riskmanage/v3/rule/updateRuleParent',params)







//===============================================评分卡管理===============================
// 评分卡 list 获取
// export const getSCOList = (params) => request.post('/Riskmanage/v2/knowledge/scorecard/list',params)
export const getSCOList = (params) => request.post('/Riskmanage/v3/scorecard/list',params)
// 评分卡 list 删启停
export const updataListSCO = (params) => request.post('/Riskmanage/v3/scorecard/updateStatus',params)
// 评分卡 检测区间是否闭环
export const validateSection = (params) => request.post('/Riskmanage/v3/scorecard/section',params)
// 评分卡 新增列表
export const addSCO = (params) => request.post('/Riskmanage/v3/scorecard/add',params)
// 评分卡 获取评分卡版本
export const getSCOVersion = (params) => request.post('/Riskmanage/v3/scorecard/getScorecardInfo/'+params.id)
// 评分卡 添加评分卡版本
export const addScorecardVersion = (params) => request.post('/Riskmanage/v3/scorecardVersion/addScorecardVersion',params)
// 评分卡 更新版本状态
export const updateScorecardVersionStatus = (params) => request.post('/Riskmanage/v3/scorecardVersion/updateScorecardVersionStatus',params)
// 评分卡 复制版本
export const copyScorecardVersion = (params) => request.post('/Riskmanage/v3/scorecardVersion/copyScorecardVersion',params)
// 评分卡 版本重命名
export const updateScorecardVersion = (params) => request.post('/Riskmanage/v3/scorecardVersion/updateScorecardVersion',params)
// 评分卡 获取评分卡主体
export const getSCOInfo = (params) => request.post('/Riskmanage/v3/scorecardVersion/getScorecardVersionInfo/'+params.id)
// 评分卡 修改列表提交
export const getSCOUpdata = (params) => request.post('/Riskmanage/v3/scorecard/update',params)




//======================================黑名单=======================================

// 黑白名单库管理 首次进来获取名单列表
export const getBlackWhiteList = (params) => request.post('/Riskmanage/v2/datamanage/listmanage/list',params)
// 黑白名单库管理 添加名单库
export const saveBlackWhiteList = (params) => request.post('/Riskmanage/v2/datamanage/listmanage/save',params)
// 黑白名单库管理 编辑名单库
export const updateBlackWhiteList = (params) => request.post('/Riskmanage/v2/datamanage/listmanage/update',params)
// 黑白名单库管理 获取详情
export const getListDbInfo = (id,params) => request.post('/Riskmanage/v2/datamanage/listmanage/getListDbInfo/'+id,params)
// /
export const updateStatus = (params) => request.post('/Riskmanage/v2/datamanage/listmanage/updateStatus',params)
// 导入
export const listmanageUpload = (id,params) => request.post('/Riskmanage/v2/datamanage/listmanage/upload/'+id,params)
//下载模板 
export const listmanageDownTemplate = () => request.post('/Riskmanage/v2/datamanage/listmanage/downTemplate')
// v2/datamanage/listmanage/getListDbData
export const getListDbData = (params) => request.post('/Riskmanage/v2/datamanage/listmanage/getListDbData',params)

// ================================== 系统管理 ===================================
// ---------------------用户管理
// 获取用户列表
export const getUserList = (params) => request.post('/Riskmanage/v2/sysUser/getUserList',params)
// 创建用户  
export const saveUser = (params) => request.post('/Riskmanage/v2/sysUser/save',params)
// 编辑修改 
export const updateUser = (params) => request.post('/Riskmanage/v2/sysUser/update',params)
// 启用、停用、删除  
export const userUpdateStatus = (params) => request.post('/Riskmanage/v2/sysUser/updateStatus',params)
// 获取组织列表
export const getOrganList = (params) => request.post('/Riskmanage/v2/sysOrganization/getOrganList',params)
// 获取已启用的组织列表
export const getAllValidOrgan = (params) => request.post('/Riskmanage/v2/sysOrganization/getAllValidOrgan',params)
// 获取角色 
export const getAllValidRole = (params) => request.post('/Riskmanage/v2/sysRole/getAllValidRole',params)
// 修改密码 
export const updateUserPassword = (params) => request.post('/Riskmanage/v2/sysUser/updatePassword',params)

//----------------------- 角色管理
// 获取角色列表
export const getRoleList = (params) => request.post('/Riskmanage/v2/sysRole/getRoleList',params)
// 修改批量操作  
export const roleUpdateStatus = (params) => request.post('/Riskmanage/v2/sysRole/updateStatus',params)
// 创建角色
export const saveRole = (params) => request.post('/Riskmanage/v2/sysRole/save',params)
// 修改角色
export const updateRole = (params) => request.post('/Riskmanage/v2/sysRole/update',params)
// 获取资源树  
export const getFindTreeList = (params) => request.post('/Riskmanage/v2/sysMenu/findTreeList',params)
// 获取引擎树  
export const getEngineTree = (params) => request.post('/Riskmanage/v2/sysMenu/getEngineTree',params)
// 权限分配资源树保存 
export const insertRoleMenu = (params) => request.post('/Riskmanage/v2/sysMenu/insertRoleMenu',params)
// 权限分配引擎树保存 
export const insertRoleEngine = (params) => request.post('/Riskmanage/v2/sysMenu/insertRoleEngine',params)
// -----------------------资源管理
// 获取资源列表
export const getRsourceMenuList = (params) => request.post('/Riskmanage/v2/sysMenu/getMenuList',params)
// 删除资源 
export const resourceUpdateStatus = (params) => request.post('/Riskmanage/v2/sysMenu/updateStatus',params)
// 新增/修改资源获取的父节点树 
export const getResourceTreeMenu = (params) => request.post('/Riskmanage/v2/sysMenu/getTreeMenu',params)
// 编辑资源 
export const resourceUpdate = (params) => request.post('/Riskmanage/v2/sysMenu/update',params)
// 新增资源 
export const resourceSave = (params) => request.post('/Riskmanage/v2/sysMenu/save',params)
// -----------------------组织管理
// 创建组织
export const saveOrgan = (params) => request.post('/Riskmanage/v2/sysOrganization/save',params)
// 修改组织  
export const updateOrgan = (params) => request.post('/Riskmanage/v2/sysOrganization/update',params)
// 更新状态  
export const updateStatusOrgan = (params) => request.post('/Riskmanage/v2/sysOrganization/updateStatus',params)
// -----------------------日志管理
// 列表
export const getLogList = (params) => request.post('/Riskmanage/v2/sysLog/getLogList',params)






//==========================================决策流==============================

// 决策流 获取版本信息
export const getversion = (id) => request.post('/Riskmanage/v2/engine/version/'+id)
// 决策流 获取节点
export const getNodeList = (id) => request.post('/Riskmanage/v2/decision_flow/getNodeList/'+id)
// 决策流 新增
export const setAddNode = (params) => request.post('/Riskmanage/v2/decision_flow/save',params)
// 决策流 新增黑白
export const setAddNodeHB = (params) => request.post('/Riskmanage/v2/bwListNode/create',params)
// 决策流 移动节点
export const setmoveNode = (params) => request.post('/Riskmanage/v2/decision_flow/updatePropertyForMove',params)
// 决策流 删除节点
export const setdelectNode = (params) => request.post('/Riskmanage/v2/decision_flow/removeNode',params)
// 决策流 删除节点
export const setdelectNodeList = (params) => request.post('/Riskmanage/v2/decision_flow/removeNodeList',params)
// 决策流 连线
export const setupdateProperty = (params) => request.post('/Riskmanage/v2/decision_flow/updateProperty',params)
// 决策流 断线
export const setremoveLink = (params) => request.post('/Riskmanage/v2/decision_flow/removeLink',params)
// 决策流 部署引擎
// export const getVersionDeploy = (id) => request.post('/Riskmanage/v2/engineVersion/deploy/'+id)
// 决策流 部署引擎V2
export const getVersionDeploy = (id) => request.post('/Riskmanage/v2/engineVersion/applyDeploy/'+id)
// 决策流 取消部署引擎
export const getVersionUnDeploy = (id) => request.post('/Riskmanage/v2/engineVersion/undeploy/'+id)
// 决策流 复制版本
export const getCopyVer = (id) => request.post('/Riskmanage/v2/decision_flow/saveVersion/'+id)
// 决策流 复制节点
export const getCopyNode = (params) => request.post('/Riskmanage/v2/decision_flow/copy',params)
// 决策流 清空节点
export const getClearNode = (id) => request.post('/Riskmanage/v2/engineVersion/clear/'+id)
// 决策流 删除版本
export const delectVer = (id) => request.post('/Riskmanage/v2/engineVersion/delete/'+id)
// 决策流 新建版本
export const getAddVer = (id,Vid) => request.post('/Riskmanage/v2/engineVersion/add/'+id+'/'+Vid)
// 决策流 重命名
export const setRenameNode = (params) => request.post('/Riskmanage/v2/decision_flow/renameNode',params)
// 决策流 批量测试模板下载
// export const createSampleData = (params) => request.post('/Riskmanage/v2/engine/createSampleData',params)
// 决策流 json下载
export const getDownloadJsonField = (params) => request.post('/Riskmanage/v2/engine/downloadJsonField',params)
// 批量测试
export const batchTest = (params) => request.post('/Riskmanage/v2/engine/batchTest',params)








//===========================================type N ==================================
//==========type2  规则集

// 规则集 获取规则集 一级树
export const getType2 = (params) => request.post('/Riskmanage/v2/ruleNode/getFolderList',params)
// 规则集 获取规则集 二级树
export const getType2tree = (params) => request.post('/Riskmanage/v2/ruleNode/getRuleDataForEngine',params)
// 规则集 提交规则集
// export const setType2 = (params) => request.post('/Riskmanage/v2/decision_flow/update',params)


//==========type4  评分卡
// 评分卡 获取评分卡
export const getType4 = (params) => request.post('/Riskmanage/v2/cardNode/cardList',params)




//==========type5-6  黑白名单
//  黑白名单 获取黑白名单
export const getType56 = (params) => request.post('/Riskmanage/v2/bwListNode/findBwList',params)



//==========type 7 决策结果
//  决策结果 获取规则集
// export const getType7 = (params) => request.post('/Riskmanage/v2/decision_flow/getFieldOrScorecardForOption',params)



//==========type15  模型
// 模型 获取评分卡
export const getType15 = (params) => request.post('/Riskmanage/modelNode/getModelList',params)



//==========type14  子引擎
// 子引擎 获取子引擎
export const getType14 = (params) => request.post('/Riskmanage/childEngineNode/getEngineList',params)






// 统一提交节点提交
export const setType4 = (params) => request.post('/Riskmanage/v2/decision_flow/update',params)




// =========================交叉决策表=================
// 获取list
export const getDecisionTablesList = (params) => request.post('/Riskmanage/v3/decisionTables/getDecisionTablesList',params)
// 获得 info
export const getDecisionTablesInfo = (id) => request.post('/Riskmanage/v3/decisionTables/getDecisionTablesInfo/'+id)
// 添加列表
export const addDecisionTables = (params) => request.post('/Riskmanage/v3/decisionTables/addDecisionTables',params)
// 更新列表
export const updateDecisionTables = (params) => request.post('/Riskmanage/v3/decisionTables/updateDecisionTables',params)
// 状态更改
export const updateDecisionTablesStatus = (params) => request.post('/Riskmanage/v3/decisionTables/updateDecisionTablesStatus',params)
// 文件夹移动
export const updateDecisionTablesParent = (params) => request.post('/Riskmanage/v3/decisionTables/updateDecisionTablesParent',params)
// 获取 版本
export const getDecisionTablesVersionInfo = (id) => request.post('/Riskmanage/v3/decisionTables/version/getVersionInfo/'+id)
// 添加版本
export const addDecVersion = (params) => request.post('/Riskmanage/v3/decisionTables/version/addVersion',params)
// 复制版本
export const copyDecVersion = (params) => request.post('/Riskmanage/v3/decisionTables/version/copyVersion',params)
// 重命名 版本
export const updateDecVersion = (params) => request.post('/Riskmanage/v3/decisionTables/version/updateVersion',params)
// 删除版本
export const updateDecVersionStatus = (params) => request.post('/Riskmanage/v3/decisionTables/version/updateVersionStatus',params)



// =========================决策树=================

// 获取list
export const getDecisionTreeList = (params) => request.post('/Riskmanage/v3/decisionTree/getDecisionTreeList',params)
// 获得 info
export const getDecisionTreeInfo = (id) => request.post('/Riskmanage/v3/decisionTree/getDecisionTreeInfo/'+id)
// 添加列表
export const addDecisionTree = (params) => request.post('/Riskmanage/v3/decisionTree/addDecisionTree',params)
// 更新列表
export const updateDecisionTree = (params) => request.post('/Riskmanage/v3/decisionTree/updateDecisionTree',params)
// 状态更改
export const updateDecisionTreeStatus = (params) => request.post('/Riskmanage/v3/decisionTree/updateDecisionTreeStatus',params)
// 文件夹移动
export const updateDecisionTreeFolder = (params) => request.post('/Riskmanage/v3/decisionTree/updateDecisionTreeFolder',params)
// 获取 版本
export const getDecisionTreeVersionInfo = (id) => request.post('/Riskmanage/v3/decisionTree/version/getVersionInfo/'+id)
// 添加版本
export const addDecTreeVersion = (params) => request.post('/Riskmanage/v3/decisionTree/version/addVersion',params)
// 复制版本
export const copyDecTreeVersion = (params) => request.post('/Riskmanage/v3/decisionTree/version/copyVersion',params)
// 重命名 版本
export const updateDecTreeVersion = (params) => request.post('/Riskmanage/v3/decisionTree/version/updateVersion',params)
// 删除版本
export const updateDecTreeVersionStatus = (params) => request.post('/Riskmanage/v3/decisionTree/version/updateVersionStatus',params)


// =====================接口管理==============================
// 获取list
export const getInterfaceList = (params) => request.post('/Riskmanage/v3/interface/getInterfaceList',params)
// 新增list
export const addInterface = (params) => request.post('/Riskmanage/v3/interface/addInterface',params)
// 修改list
export const updateInterface = (params) => request.post('/Riskmanage/v3/interface/updateInterface',params)
// 删除list
export const deleteInterface = (params) => request.post('/Riskmanage/v3/interface/deleteInterface',params)
// 测试接口
export const getHttpResponse = (params) => request.post('/Riskmanage/v3/interface/getHttpResponse',params)







// ======================接口管理 ==============================

export const getengineSummaryList = (params) => request.post('/Riskmanage/v3/1',params)
// export const getengineSummaryList = (params) => request.post('/Riskmanage/v3/1',params)









// ======================趋势分析 ==============================

export const getTendencyList = (params) => request.post('/Riskmanage/v3/qvshi1',params)
// export const getengineSummaryList = (params) => request.post('/Riskmanage/v3/1',params)


// =============决策流 - 7.25. 获取决策流条件入参==================
export const getConditionInputParam = (params) => request.post('/Riskmanage/v2/decision_flow/getNodeConditionInputParam',params)




// ======================决策流监控 ==============================
// 获取list
export const getMonitorResults = (params) => request.post('/Riskmanage/v2/monitor/results',params)
// 获取具体数据
export const getMonitorDecisionFlow = (params) => request.post('/Riskmanage/v2/monitor/decisionFlow',params)
// 根据ID获取Node节点
export const getMonitorNode = (params) => request.post('/Riskmanage/v2/monitor/node',params)
// 策略层面接口返回信息  
export const getStrategy = (params) => request.post('/Riskmanage/v2/monitor/strategy',params)
// 获取具体数据
export const getMonitorDecisionFlowMysql = (params) => request.post('/Riskmanage/v2/monitor/decisionFlowMysql',params)
// 根据ID获取Node节点
export const getMonitorNodeMysql = (params) => request.post('/Riskmanage/v2/monitor/nodeMysql',params)
// 策略层面接口返回信息  
export const getStrategyMysql = (params) => request.post('/Riskmanage/v2/monitor/strategyMysql',params)



// ======================趋势分析 ==============================
// 获取list
export const getAnalyseData = (params) => request.post('/Riskmanage/v3/analyse/getData',params)

// 获取总数
export const getAnalyseSummary = (params) => request.post('/Riskmanage/v3/analyse/getEngineSummary',params)

// =======================   审批设置 ======================
// 获取审批设置
 export const approvalConfigGetApprovalList = (params) => request.post('/Riskmanage/v3/approvalConfig/getApprovalList',params)
 // 更改审批设置
 export const approvalConfigUpdateApprovalStatus = (params) => request.post('/Riskmanage/v3/approvalConfig/updateApprovalStatus',params)
 
 
 // ========================   审批管理  ======================
 // 获取审批列表
 export const approvalGetApprovalList = (params) => request.post('/Riskmanage/v3/approval/getApprovalList',params)
 // 更改审批状态
 export const approvalUpdateApplyStatus = (params) => request.post('/Riskmanage/v3/approval/updateApplyStatus',params)
 
 
 // =============================集合操作==============

// 获取list
export const getListOperationList = (params) => request.post('/Riskmanage/v3/listOperation/getListOperationList',params)
// 获得 info
export const getListOperation = (id) => request.post('/Riskmanage/v3/listOperation/getListOperation/'+id)
// 添加列表
export const addListOperation = (params) => request.post('/Riskmanage/v3/listOperation/addListOperation',params)
// 更新列表
export const updateListOperation = (params) => request.post('/Riskmanage/v3/listOperation/updateListOperation',params)
// 状态更改
export const updateListOperationStatus = (params) => request.post('/Riskmanage/v3/listOperation/updateListOperationStatus',params)
// 文件夹移动
export const updateListOperationFolder = (params) => request.post('/Riskmanage/v3/listOperation/updateListOperationFolder',params)


// // 获取 版本
// export const getDecisionTreeVersionInfo = (id) => request.post('/Riskmanage/v3/listOperation/version/getVersionInfo/'+id)
// // 添加版本
export const addlistOperationVersion = (params) => request.post('/Riskmanage/v3/listOperation/version/addVersion',params)
// // 复制版本
export const copyListOperationVersion = (params) => request.post('/Riskmanage/v3/listOperation/version/copyVersion',params)
// // 重命名 版本
export const updatelistOperationVersion = (params) => request.post('/Riskmanage/v3/listOperation/version/updateVersion',params)
// // 删除版本
export const updatelistOperationStatusVersion = (params) => request.post('/Riskmanage/v3/listOperation/version/updateVersionStatus',params)


 // =============================数据清洗==============

// 获取list
export const getDataCleanList = (params) => request.post('/Riskmanage/dataClean/getDataCleanList',params)
// 获得 info
export const getDataClean = (id) => request.post('/Riskmanage/dataClean/getDataClean/'+id)
// 添加列表
export const addDataClean = (params) => request.post('/Riskmanage/dataClean/addDataClean',params)
// 更新列表
export const updateDataClean = (params) => request.post('/Riskmanage/dataClean/updateDataClean',params)
// 状态更改
export const updateDataCleanStatus = (params) => request.post('/Riskmanage/dataClean/updateDataCleanStatus',params)
// 文件夹移动
export const updateDataCleanFolder = (params) => request.post('/Riskmanage/dataClean/updateDataCleanFolder',params)


// // 获取 版本
// export const getDecisionTreeVersionInfo = (id) => request.post('/Riskmanage/v3/dataClean/version/getVersionInfo/'+id)
// // 添加版本
export const addDataCleanVersion = (params) => request.post('/Riskmanage/dataClean/version/addVersion',params)
// // 复制版本
export const copyDataCleanVersion = (params) => request.post('/Riskmanage/dataClean/version/copyVersion',params)
// // 重命名 版本
export const updateDataCleanVersion = (params) => request.post('/Riskmanage/dataClean/version/updateVersion',params)
// // 删除版本
export const updateDataCleanStatusVersion = (params) => request.post('/Riskmanage/dataClean/version/updateVersionStatus',params)



// =====================================类型关联====================
// 获取类型关联
export const businessRuleRelGetList = (params) => request.post('/Riskmanage/businessRuleRel/list',params)
// 添加类型关联
export const businessRuleRelAdd = (params) => request.post('/Riskmanage/businessRuleRel/add',params)
// 删除类型关联
export const businessRuleRelDelete = (params) => request.post('/Riskmanage/businessRuleRel/delete',params)
// 更改类型关联
export const businessRuleRelUpdate = (params) => request.post('/Riskmanage/businessRuleRel/edit',params)

// =====================================规则报表====================
// 获取规则报表
export const getRuleLogList = (params) => request.post('/Riskmanage/cignacmb/getRuleLogList',params)

// =====================================事件报表====================
// 获取事件报表
export const getEventLogList = (params) => request.post('/Riskmanage/cignacmb/getEventLogList',params)
// 事件对应规则输出详情
export const getEventLogDetails = (params) => request.post('/Riskmanage/cignacmb/getEventLogDetails',params)

// ======================================= 获取消息队列============
export const getMqSourceList = (params) => request.post('/Riskmanage/mqSource/getMqSourceList',params)




// =====================================数据流引擎====================
// 获取引擎
export const engineGetList = (params) => request.post('/Riskmanage/v3/engine/getEngineList',params)
// 获取引擎
export const engineGetEngineCode = (params) => request.post('/Riskmanage/v3/engine/getEngineCode',params)
// 删除引擎
export const engineUpdateStatus = (params) => request.post('/Riskmanage/v3/engine/updateStatus',params)
// 删除引擎
export const engineAdd = (params) => request.post('/Riskmanage/v3/engine/addEngine',params)
// 删除引擎
export const engineUpdate = (params) => request.post('/Riskmanage/v3/engine/updateEngine',params)

// =================================数据流引擎内容======================
// 获取数据流引擎的版本信息
export const getDataEngineVersionList = (params) => request.post('/Riskmanage/v3/engineVersion/getEngineVersionList',params)
// 获取数据流引擎的版本info信息
export const getDataEngineVersionInfo = (params) => request.post('/Riskmanage/v3/engineVersionContent/getEngineVersionContentInfo',params)
// 更新数据流引擎数据
export const updateDataEngineVersionInfo = (params) => request.post('/Riskmanage/v3/engineVersionContent/updateEngineVersionContentInfo',params)






export const getV = (params) => request.post('/list/product',params)



