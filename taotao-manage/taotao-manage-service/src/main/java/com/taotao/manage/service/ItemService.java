package com.taotao.manage.service;

import com.github.abel533.entity.Example;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.manage.mapper.ItemMapper;
import com.taotao.pojo.Item;
import com.taotao.pojo.ItemDesc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService extends BaseService<Item> {

    @Autowired
    private ItemDescService itemDescService;

    @Autowired
    private ItemMapper itemMapper;

    // 同一个方法中存在两个事务，根据事务的传播特性，如果当前的事务存在，则另一个事务嵌套当前事务执行
    public void saveItem(Item item,String desc){
        // 设置初始数据
        item.setStatus(1);
        // 强制设置id为null，数据库主键自动增长
        item.setId(null);
        super.save(item);

        // 商品描述表（tb_item_desc）
        ItemDesc itemDesc = new ItemDesc();
        // 商品表（item）主键自增后返回增长后的id值
        itemDesc.setItemId(item.getId());
        itemDesc.setItemDesc(desc);
        // 保存描述数据
        this.itemDescService.save(itemDesc);
    }

    public PageInfo<Item> queryPageList(Integer page,Integer rows){
        Example example = new Example(Item.class);
        example.setOrderByClause("updated DESC");

        // 设置分页参数
        PageHelper.startPage(page,rows);

        List<Item> list = this.itemMapper.selectByExample(example);

        return new PageInfo<>(list);
    }

    public  void updateItem(Item item, String desc){
        // 强制设置不能修改的字段为null
        item.setStatus(null);
        item.setCreated(null);
        super.updateSelective(item);

        // 修改商品描述数据
        ItemDesc itemDesc = new ItemDesc();
        itemDesc.setItemId(item.getId());
        itemDesc.setItemDesc(desc);
        this.itemDescService.updateSelective(itemDesc);
    }
}
