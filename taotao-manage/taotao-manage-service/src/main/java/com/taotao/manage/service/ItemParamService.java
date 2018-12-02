package com.taotao.manage.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.manage.mapper.Item_ParamMapper;
import com.taotao.pojo.ItemParam;
import com.taotao.pojo.Item_Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ItemParamService extends BaseService<ItemParam> {

    @Autowired
    private Item_ParamMapper item_paramMapper;

    public PageInfo<Item_Param> queryPageList(Integer page,Integer rows){
        PageHelper.startPage(page,rows);
        List<Item_Param> item_params = item_paramMapper.queryPageList();
        return new PageInfo<>(item_params);
    }
}
