package com.professul.professul.util;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageInfo {
    private Integer allPage;
    private Integer curPage;
    private Integer startPage;
    private Integer endPage;

    public PageInfo(Integer page){
        this.curPage=page;
    }
}
