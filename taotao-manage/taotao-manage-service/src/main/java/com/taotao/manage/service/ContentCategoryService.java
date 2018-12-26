package com.taotao.manage.service;

import com.taotao.pojo.ContentCategory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ContentCategoryService extends BaseService<ContentCategory> {

    public ContentCategory saveContentCategory(ContentCategory contentCategory){
        contentCategory.setId(null);
        contentCategory.setIsParent(false);
        contentCategory.setSortOrder(1);
        contentCategory.setStatus(1);
        this.save(contentCategory);

        // 判断父节点的isParent是否为true，如果不是，则修改为true
        ContentCategory parent = this.queryById(contentCategory.getParentId());
        if(!parent.getIsParent()){
            parent.setIsParent(true);
            this.updateSelective(parent);
        }
        return contentCategory;
    }

    public void delete(ContentCategory contentCategory){
        // 查找所有的子节点
        ArrayList<Object> ids = new ArrayList<>();
        ids.add(contentCategory.getId());
        findAllSubNode(contentCategory.getId(),ids);

        // 删除所有子节点
        this.deleteByIds(ContentCategory.class,"id",ids);
        // 判断当前节点的父节点是否还有其它的子节点，如果没有，则将isParent设置为false
        ContentCategory record = new ContentCategory();
        record.setParentId(contentCategory.getParentId());
        List<ContentCategory> list = this.queryListByWhere(record);
        if(null == list || list.isEmpty()){
            ContentCategory parent = new ContentCategory();
            parent.setId(contentCategory.getParentId());
            parent.setIsParent(false);
            this.updateSelective(parent);
        }
    }
    private void findAllSubNode(Long parentId, List<Object> ids) {
        ContentCategory record = new ContentCategory();
        record.setParentId(parentId);
        // 查询得到父节点都为parentId的子节点
        List<ContentCategory> list = this.queryListByWhere(record);
        for(ContentCategory contentCategory:list){
            // 将子节点的id存放到集合中
            ids.add(contentCategory.getId());
            // 递归遍历获取所有子节点id
            findAllSubNode(contentCategory.getId(),ids);
        }
    }
}
