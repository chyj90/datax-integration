package com.cyj.arrange.bean;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;

import java.util.List;

@Data
public class Pager {
    List data;

    long pageSize;

    long pageNo;

    long totalPage;

    long totalCount;

    public Pager(IPage iPage)
    {
        this.data = iPage.getRecords();
        this.pageSize = iPage.getSize();
        this.pageNo = iPage.getCurrent();
        this.totalCount = iPage.getTotal();
        this.totalPage = iPage.getPages();
    }
}
