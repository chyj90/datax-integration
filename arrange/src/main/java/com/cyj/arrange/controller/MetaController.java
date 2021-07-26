package com.cyj.arrange.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cyj.arrange.bean.Result;
import com.cyj.arrange.bean.vo.TCfgResolverVO;
import com.cyj.arrange.entry.TCfgDatasource;
import com.cyj.arrange.entry.TCfgResolver;
import com.cyj.arrange.service.MetaService;
import com.cyj.arrange.service.SysService;
import com.cyj.arrange.util.JwtTokenUtil;
import com.cyj.arrange.util.SecurityUtil;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 元数据管理
 */
@RestController
@RequestMapping("/meta")
public class MetaController {

    @Autowired
    MetaService metaService;

    @Autowired
    SysService sysService;

    @PostMapping("/saveDS")
    public Result saveDatasource(@RequestBody String request)
    {
        String userName = SecurityUtil.userName();
        Integer userID = sysService.userID(userName);
        TCfgDatasource datasource = JwtTokenUtil.gson().fromJson(request,new TypeToken<TCfgDatasource>(){}.getType());
        datasource.setOwner(userID);
        metaService.saveDataSource(datasource);
        return new Result().setMessage("保存成功");
    }

    @PostMapping("/saveRs")
    public Result saveResolver(@RequestBody String request)
    {
        String userName = SecurityUtil.userName();
        Integer userID = sysService.userID(userName);
        TCfgResolver resolver = JwtTokenUtil.gson().fromJson(request,new TypeToken<TCfgResolver>(){}.getType());
        resolver.setOwner(userID);
        metaService.saveResolver(resolver);
        return new Result().setMessage("保存成功");
    }

    @GetMapping("/datasources")
    public Result queryDatasources(@RequestParam(value = "name",required = false) String dsName)
    {
        String userName = SecurityUtil.userName();
        Integer userID = sysService.userID(userName);
        List<TCfgDatasource> list =  metaService.queryDataSource(userID,dsName);
        Map<String,Object> rs = new HashMap<>();
        rs.put("data",list);
        return new Result().setMessage(rs);
    }

    @GetMapping("/resolverPager")
    public Result queryResolverPager(@RequestParam(value = "name",required = false) String resolverName,@RequestParam("pageNo") Integer pageNo, @RequestParam("pageSize") Integer pageSize)
    {
        String userName = SecurityUtil.userName();
        Integer userID = sysService.userID(userName);
        IPage<TCfgResolverVO> page = metaService.queryResolverPager(userID,resolverName,pageNo,pageSize);
        return new Result().setMessage(page);
    }
}
