package com.taotao.manage.service;

import com.github.abel533.mapper.Mapper;
import com.taotao.manage.mapper.ItemCatMapper;
import com.taotao.pojo.ItemCat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ItemCatService extends BaseService<ItemCat> {

//    @Autowired
//    private ItemCatMapper itemCatMapper;

//    public List<ItemCat> queryItemCat(Long parentId){
//        ItemCat record = new ItemCat();
//        record.setParentId(parentId);
//        return this.itemCatMapper.select(record);
//    }


}
