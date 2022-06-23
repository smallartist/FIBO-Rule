package com.fibo.ddp.common.utils.constant.enginex;

public interface EngineContentConst {
    String NODE_LIST_KEY = "nodeList";
    String SOURCE_ID = "sourceId";
    String Event_Dispose = "eventDispose";
    interface DataFlowEngine{
        String NODE_TYPE = "nodeType";
        interface NodeType{
            String BEGIN = "begin";
            String WHERE = "where";
            String OR = "or";
            String WITHIN = "within";
            String NEXT = "next";
            String FOLLOWED_BY = "followedBy";
            String ONE_OR_MORE = "oneOrMore";
            String TIMES = "times";
            String TIMES_OR_MORE = "timesOrMore";
        }
    }
}
