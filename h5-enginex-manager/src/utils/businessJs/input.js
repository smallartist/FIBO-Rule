import verification  from "../class/index.js"

export  default [
	//  ( Key 显示名称  类型  默认值   是否禁用  枚举  是否折叠   显示fn)
      // new verification("autoTradeType", "下单方式", 'Enum',2,false,[{label:'基差',value:0},{label:'收益率',value:1},{label:'年化收益率',value:2}],true,null),
      new verification("businessName", "业务类型", String, "", false,null,false,null),
      new verification("businessCode", "业务类型编码", String, "", false,null,false,null),
      new verification("businessChildName", "业务子类型", String, "", false,null,false,null),
      new verification("businessChildCode", "业务子类型编码", String, "", false,null,false,null),
      new verification("sendType", "发送方式", 'Enum', "AUTO", false,[{label:'自动',value:'AUTO'},{label:'手动',value:'MANUAL'}],false,null),
      new verification("isUnsubscribe", "是否取消订阅", 'Enum', "NO", false,[{label:'是',value:'YES'},{label:'否',value:'NO'}],false,null),
      new verification("eventType", "事件类型", 'Enum', "NOTICE", false,[{label:'通知',value:'NOTICE'},{label:'待办',value:'PENDING'},{label:'系统类',value:'SYSTEM'}],false,null),
      new verification("backlog", "待办任务", String, "", false,null,false,null),
      new verification("isManualIntervention", "是否需要人工干预", 'Enum', "NO", false,[{label:'是',value:'YES'},{label:'否',value:'NO'}],false,null),
]