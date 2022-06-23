package com.fibo.ddp.datax.realtime.controller.home;




import com.fibo.ddp.common.model.common.ResponseEntityBuilder;
import com.fibo.ddp.common.model.common.ResponseEntityDto;
import com.fibo.ddp.common.service.datax.home.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/DataX/home")
public class DataXHomeController {

    @Autowired
    @Qualifier("dataXService")
    HomeService homeService;


    @PostMapping("/getIndexInfo")
    @ResponseBody
    public ResponseEntityDto getIndexInfo(){
        Map<String, Object> result = homeService.getIndexInfo();
        if (result==null){
            result = new HashMap<>();
        }
        return ResponseEntityBuilder.buildNormalResponse(result);
    }
}
