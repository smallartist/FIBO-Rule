package com.fibo.ddp.common.utils.constant;

public class ApprovalConsts {
    public static final class ApplyStatus {
        public static final int CANCEL = -1;//取消申请
        public static final int WAIT = 0;//待审批
        public static final int PASS = 1;//通过
        public static final int DENY = 2;//拒绝
    }
    public static final class ApprovalType {
        public static final String DECISION_FLOW_VERSION_DEPLOY = "jclbbbs";//决策流版本部署审批
    }
    public static final class ApprovalStatus {
        public static final int DELETE = -1;//取消申请
        public static final int OFF = 0;//待审批
        public static final int ON = 1;//通过
    }
}
