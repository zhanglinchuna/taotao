package com.taotao.manage.service;

import com.taotao.manage.mapper.ItemCatMapper;
import com.taotao.pojo.ItemCat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemCatService {

    @Autowired
    private ItemCatMapper itemCatMapper;

    public List<ItemCat> queryItemCat(Long parentId){
        ItemCat record = new ItemCat();
        record.setParentId(parentId);
        return this.itemCatMapper.select(record);
    }
}
