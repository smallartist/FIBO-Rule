package com.fibo.ddp.common.dao.monitor.decisionflow;

import com.fibo.ddp.common.model.monitor.decisionflow.UserInfo;
import com.fibo.ddp.common.utils.constant.monitor.Constants;
import com.spring4all.spring.boot.starter.hbase.api.RowMapper;
import org.apache.hadoop.hbase.util.Bytes;

public class UserInfoHbaseMapper implements RowMapper<UserInfo> {
    private static byte[] NAME = Constants.UserInfoTable.TABLE_NAME.getBytes();
    private static byte[] FAMILY_BASE_INFO = Constants.UserInfoTable.FAMILY_BASE_INFO.getBytes();
    private static byte[] BASEINFO = Constants.UserInfoTable.BASEINFO.getBytes();

    @Override
    public UserInfo mapRow(org.apache.hadoop.hbase.client.Result result, int i) throws Exception {
        UserInfo.BaseInfo baseInfo = new UserInfo.BaseInfo(
                Bytes.toString(result.getValue(FAMILY_BASE_INFO,NAME))
        );

        return new UserInfo(baseInfo);
    }
}
