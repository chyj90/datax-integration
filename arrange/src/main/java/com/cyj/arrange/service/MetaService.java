package com.cyj.arrange.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cyj.arrange.bean.vo.TCfgResolverVO;
import com.cyj.arrange.entry.TCfgDatasource;
import com.cyj.arrange.entry.TCfgResolver;
import com.cyj.arrange.mapper.TCfgDatasourceMapper;
import com.cyj.arrange.mapper.TCfgResolverMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MetaService {

    @Autowired
    TCfgDatasourceMapper datasourceMapper;

    @Autowired
    TCfgResolverMapper resolverMapper;

    public void saveDataSource(TCfgDatasource datasource)
    {
        if (datasource.getSeqId()!=null&&datasource.getSeqId()>0)
        {
            datasourceMapper.updateById(datasource);
        }else {
            datasourceMapper.insert(datasource);
        }
    }

    public void saveResolver(TCfgResolver resolver)
    {
        if (resolver.getSeqId()!=null&&resolver.getSeqId()>0)
        {
            resolverMapper.updateById(resolver);
        }else
        {
            resolverMapper.insert(resolver);
        }
    }

    public List<TCfgDatasource> queryDataSource(Integer owner, String dsName)
    {
        QueryWrapper<TCfgDatasource> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("owner",owner).eq("status",1);
        if (StringUtils.isNotEmpty(dsName))
        {
            queryWrapper.like("ds_name",dsName);
        }
        return datasourceMapper.selectList(queryWrapper);
    }

    public IPage<TCfgResolverVO> queryResolverPager(Integer owner,String resolverName,int pageNo, int pageSize)
    {
        Page<TCfgResolverVO> page = new Page<>(pageNo,pageSize);
        return resolverMapper.selectPageVo(page,resolverName,owner);
    }
}
