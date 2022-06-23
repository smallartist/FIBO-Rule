package com.fibo.ddp.common.service.datax.datainterface.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fibo.ddp.common.dao.authx.system.SysUserMapper;
import com.fibo.ddp.common.dao.canal.TableEnum;
import com.fibo.ddp.common.dao.datax.datainterface.InterfaceMapper;
import com.fibo.ddp.common.model.authx.system.SysUser;
import com.fibo.ddp.common.model.common.enums.ErrorCodeEnum;
import com.fibo.ddp.common.model.common.requestParam.QueryListParam;
import com.fibo.ddp.common.model.datax.datainterface.InterfaceInfo;
import com.fibo.ddp.common.model.datax.datainterface.vo.InterfaceVo;
import com.fibo.ddp.common.service.common.SessionManager;
import com.fibo.ddp.common.service.datax.datainterface.InterfaceService;
import com.fibo.ddp.common.service.redis.RedisManager;
import com.fibo.ddp.common.service.redis.RedisUtils;
import com.fibo.ddp.common.utils.constant.Constants;
import com.fibo.ddp.common.utils.exception.ApiException;
import com.fibo.ddp.common.utils.util.runner.DictVariableUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class InterfaceServiceImpl extends ServiceImpl<InterfaceMapper, InterfaceInfo> implements InterfaceService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    InterfaceMapper interfaceMapper;

    @Autowired
    SysUserMapper sysUserMapper;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired(required = false)
    private AsyncRestTemplate asyncRestTemplate;

    @Autowired
    private RedisManager redisManager;

    // 业务逻辑是否使用缓存
    @Value("${switch.use.cache}")
    private String cacheSwitch;

    @Override
    public String getHttpResponse(InterfaceInfo interfaceInfo) {

        String result = this.getHttpResponse(interfaceInfo, new HashMap<>(), null);
        return result;

//        HttpHeaders httpHeaders = new HttpHeaders();
//        // 设置请求头
//        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
//        // 封装请求体
//        JSONObject body = JSONObject.parseObject(JSONObject.toJSONString(interfaceInfo));
//        // 封装参数和头信息
//        HttpEntity<JSONObject> httpEntity = new HttpEntity(body, httpHeaders);
//        String url = runnerUrl + "/manager/invokeInterface";
//        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, httpEntity, String.class);
//        return responseEntity.getBody();
    }

    @Override
    public Boolean deleteInterfaceInfo(InterfaceVo interfaceVo) {
        interfaceMapper.deleteById(interfaceVo.getId());
        return true;
    }

    @Override
    public InterfaceVo updateInterfaceInfo(InterfaceVo interfaceVo) {
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceVo, interfaceInfo);
        SysUser sysUser = SessionManager.getLoginAccount();
        //设置创建者和修改者id
        interfaceInfo.setModifier(sysUser.getUserId());
        interfaceMapper.updateById(interfaceInfo);
        return interfaceVo;
    }

    @Override
    @Transactional
    public boolean updateStatus(Long[] ids, Integer status) {
        int updateNum = interfaceMapper.updateStatus(ids, status);
        if (updateNum > 0) {
            return true;
        }
        return false;
    }

    //添加接口信息
    @Override
    public InterfaceVo inserInterfaceInfo(InterfaceVo interfaceVo) {
        //拷贝VO到Info对象
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceVo, interfaceInfo);
        SysUser sysUser = SessionManager.getLoginAccount();
        //设置创建者和修改者id
        interfaceInfo.setCreator(sysUser.getUserId());
        interfaceInfo.setModifier(sysUser.getUserId());
        interfaceInfo.setOrganId(sysUser.getOrganId());
        interfaceInfo.setStatus(1);
        //插入并获取insert后实体对象返回id
        boolean save = this.save(interfaceInfo);
        if (!save) {
            throw new ApiException(ErrorCodeEnum.INTERFACE_SAVE_ERROR.getCode(), ErrorCodeEnum.INTERFACE_SAVE_ERROR.getMessage());
        }
        return interfaceVo;
    }

    @Override
    public PageInfo queryByEntity(QueryListParam<InterfaceInfo> interfaceListParam) {
        InterfaceInfo interfaceInfo = interfaceListParam.getEntity();
        Integer pageNo = interfaceListParam.getPageNum();
        Integer pageSize = interfaceListParam.getPageSize();
        if (pageNo > 0 && pageSize > 0) {
            PageHelper.startPage(pageNo, pageSize);
        }
        Wrapper<InterfaceInfo> wrapper = createWrapper(interfaceListParam.getEntity());
        List<InterfaceInfo> interfaceList = interfaceMapper.selectList(wrapper);
        PageInfo pageInfo = new PageInfo(interfaceList);
        //  级联操作完成拼装
        List<InterfaceVo> interfaceVos = new ArrayList<>();
        SysUser sysUser = SessionManager.getLoginAccount();
        for (InterfaceInfo info : interfaceList) {
            InterfaceVo interfaceVo = new InterfaceVo();
            BeanUtils.copyProperties(info, interfaceVo);
            interfaceVo.setUsername(sysUser.getNickName());
            //设置创建者昵称
            interfaceVo.setCreatorName(sysUserMapper.findNickNameById(info.getCreator()));
            //设置修改者昵称
            interfaceVo.setModifierName(sysUserMapper.findNickNameById(info.getModifier()));
            interfaceVos.add(interfaceVo);
        }
        pageInfo.setList(interfaceVos);


        return pageInfo;
    }

    @Override
    public InterfaceVo getInterfaceById(Long id) {
        InterfaceInfo info = interfaceMapper.selectById(id);
        InterfaceVo interfaceVo = new InterfaceVo();
        BeanUtils.copyProperties(info, interfaceVo);
        SysUser sysUser = SessionManager.getLoginAccount();
        interfaceVo.setUsername(sysUser.getNickName());
        //设置创建者昵称
        interfaceVo.setCreatorName(sysUserMapper.findNickNameById(info.getCreator()));
        //设置修改者昵称
        interfaceVo.setModifierName(sysUserMapper.findNickNameById(info.getModifier()));
        return interfaceVo;
    }

    //创建查询器
    private Wrapper<InterfaceInfo> createWrapper(InterfaceInfo query) {
        LambdaQueryWrapper<InterfaceInfo> wrapper = new LambdaQueryWrapper<>();
        if (query != null) {
            if (StringUtils.isNotBlank(query.getName())) {
                wrapper.like(InterfaceInfo::getName, query.getName());
            }
            if (StringUtils.isNotBlank(query.getMethod())) {
                wrapper.eq(InterfaceInfo::getMethod, query.getMethod());
            }
            if (StringUtils.isNotBlank(query.getUrl())) {
                wrapper.eq(InterfaceInfo::getUrl, query.getUrl());
            }
            if (query.getStatus() != null) {
                wrapper.eq(InterfaceInfo::getStatus, query.getStatus());
            } else {
                wrapper.ne(InterfaceInfo::getStatus, -1);
            }
        } else {
            wrapper.ne(InterfaceInfo::getStatus, -1);
        }
        wrapper.orderByDesc(InterfaceInfo::getId);
        return wrapper;
    }

    @Override
    public InterfaceInfo getInterfaceById(Integer id) {
        InterfaceInfo interfaceInfo = null;
        if(Constants.switchFlag.ON.equals(cacheSwitch)){
            String key = RedisUtils.getPrimaryKey(TableEnum.T_FIELD_INTERFACE, id);
            interfaceInfo = redisManager.getByPrimaryKey(key, InterfaceInfo.class);
        } else {
            interfaceInfo = interfaceMapper.selectById(id);
        }
        return interfaceInfo;
    }

    @Override
    public String getHttpResponse(InterfaceInfo interfaceInfo, Map<String, Object> inputParam, Integer callType) {
        if(callType == null){
            callType = 2; // 1为异步，2为同步
        }

        String url = interfaceInfo.getUrl();
        ResponseEntity<String> responseEntity = null;
        ListenableFuture<ResponseEntity<String>> listenableFuture = null;
        String bindParam = interfaceInfo.getBindParam();
        if (StringUtils.isNotBlank(bindParam)){
            JSONObject param = JSON.parseObject(bindParam);
            JSONArray dictVariable = param.getJSONArray("dictVariable");
            for (int i = 0; i < dictVariable.size(); i++) {
                JSONObject jsonObject = dictVariable.getJSONObject(i);
                Object value = DictVariableUtils.getValueFromJsonObject(jsonObject);
                inputParam.put(jsonObject.getString("key"),value);
            }
            JSONArray variable = param.getJSONArray("variable");
            for (int i = 0; i < variable.size(); i++) {
                JSONObject jsonObject = variable.getJSONObject(i);
                if (!inputParam.containsKey(jsonObject.getString("key"))){
                    inputParam.put(jsonObject.getString("key"),jsonObject.get("value"));
                }
            }

        }
        // 请求参数中的变量赋值
        String requestBody = setRequestBodyParams(interfaceInfo.getRequestBody(), inputParam);
        if(HttpMethod.POST.name().equals(interfaceInfo.getMethod())){
            HttpHeaders httpHeaders = new HttpHeaders();
            // 设置请求头
            httpHeaders.setAll(JSONObject.parseObject(interfaceInfo.getRequestHeaders(), Map.class));
            // 封装请求体
            JSONObject body = JSONObject.parseObject(requestBody);
            // 封装参数和头信息
            HttpEntity<JSONObject> httpEntity = new HttpEntity(body, httpHeaders);
            // 发送请求
            if(callType == 2){
                responseEntity = restTemplate.postForEntity(url, httpEntity, String.class);
            } else {
                listenableFuture = asyncRestTemplate.postForEntity(url, httpEntity, String.class);
            }

        } else if(HttpMethod.GET.name().equals(interfaceInfo.getMethod())){
            // 封装uri地址路径变量
            Map<String, Object> uriVariables = new HashMap<>();
            if(StringUtils.isNotBlank(requestBody)){
                uriVariables = JSONObject.parseObject(requestBody, Map.class);
            }
            // 发送请求
            if(callType == 2){
                responseEntity = restTemplate.getForEntity(url, String.class, uriVariables);
            } else {
                listenableFuture = asyncRestTemplate.getForEntity(url, String.class, uriVariables);
            }
        }

        String result = null;
        if(responseEntity != null && HttpStatus.OK.equals(responseEntity.getStatusCode())){
            result = responseEntity.getBody();
        }

        if(listenableFuture != null){
            listenableFuture.addCallback(new ListenableFutureCallback<ResponseEntity<String>>() {
                @Override
                public void onFailure(Throwable throwable) {
                    logger.info("异步接口调用失败", throwable);
                }

                @Override
                public void onSuccess(ResponseEntity<String> stringResponseEntity) {
                    String result = stringResponseEntity.getBody();
                    logger.info("异步接口调用成功，result:{}", result);
                }
            });
        }
        logger.info("【请求接口源完成】 url:{},requestMethod:{}, request:{}, response:{}",url,interfaceInfo.getMethod(),requestBody,result);
        return result;
    }

    private String setRequestBodyParams(String requestBody, Map<String, Object> inputParam){
        if(StringUtils.isBlank(requestBody) || inputParam == null){
            return "";
        }
        // 添加动态参数
        Pattern pattern = Pattern.compile("\\{[a-zA-Z0-9_\u4e00-\u9fa5()（）-]+\\}");
        Matcher matcher = pattern.matcher(requestBody);
        while (matcher.find()) {
            String matchStr = matcher.group(0);
            String param = matchStr.replace("{", "").replace("}", "");
            // 动态参数从变量池获取
            requestBody = requestBody.replace(matchStr, String.valueOf(inputParam.get(param)));
        }
        return requestBody;
    }

    @Override   //解析接口指标
    public String interfaceParseField(String parseStr, String jsonStr) {
        JSONObject json = null;
        try {
            json = JSONObject.parseObject(jsonStr);
        } catch (Exception e) {
            JSONArray jsonArray = JSONArray.parseArray(jsonStr);
            json = jsonArray.getJSONObject(0);
        }

        if(parseStr.contains(".[]")){
            parseStr = parseStr.replace(".[]", "[]");
        }
        String[] strArr = parseStr.split("\\.");
        for(int i = 0; i < strArr.length - 1; i++){
            if(strArr[i].contains("[]")){
                strArr[i] = strArr[i].replace("[]", "");
                JSONArray jsonArray = json.getJSONArray(strArr[i]);
                json = jsonArray.getJSONObject(0);
            }else{
                json = json.getJSONObject(strArr[i]);
            }
        }

        String value = null;
        if(json != null){
            value = json.getString(strArr[strArr.length - 1]);
        }
        return value;
    }

}
