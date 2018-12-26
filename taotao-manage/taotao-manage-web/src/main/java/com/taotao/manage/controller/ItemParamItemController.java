package com.taotao.manage.controller;

import com.taotao.manage.service.ItemParamItemService;
import com.taotao.manage.service.ItemParamService;
import com.taotao.pojo.ItemParam;
import com.taotao.pojo.ItemParamItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.xml.ws.Response;


@RequestMapping("item/param/item")
@Controller
public class ItemParamItemController {

    @Autowired
    private ItemParamItemService itemParamItemService;
    @Autowired
    private ItemParamService itemParamService;

    /**
     * 根据商品id查询商品规格参数数据
     * @param itemId
     * @return
     */
    @RequestMapping(value = "{itemId}",method = RequestMethod.GET)
    public ResponseEntity<ItemParamItem> queryByItemId(@PathVariable("itemId")Long itemId){
        try {
            ItemParamItem record = new ItemParamItem();
            record.setItemId(itemId);
            ItemParamItem itemParamItem = this.itemParamItemService.queryOne(record);
            if(null == itemParamItem){
                // 404
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            // 200
            return ResponseEntity.ok(itemParamItem);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 500
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    /**
     * 根据规格参数d查询规格参数数据
     * @param ItemParamId
     * @return
     */
    @RequestMapping(value = "itemparam/{ItemParamId}",method = RequestMethod.GET)
    public ResponseEntity<ItemParam> queryByItemParamId(@PathVariable("ItemParamId")Long ItemParamId){

        try {
            ItemParam record = new ItemParam();
            record.setId(ItemParamId);
            ItemParam itemParam = this.itemParamService.queryOne(record);
            if(null == itemParam){
                // 404
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            // 200
            return ResponseEntity.ok(itemParam);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 500
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}
