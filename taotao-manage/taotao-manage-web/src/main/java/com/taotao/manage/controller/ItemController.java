package com.taotao.manage.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.bean.EasyUIResult;
import com.taotao.manage.service.ItemService;
import com.taotao.pojo.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RequestMapping("item")
@Controller
public class ItemController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ItemController.class);

    private static final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private ItemService itemService;

    /**
     * 新增商品
     * @param item
     * @param desc
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> saveItem(Item item, @RequestParam("desc")String desc,@RequestParam("itemParams")String itemParams){
        // desc是前台新增商品模块中的 商品描述 内容
        // itemParams是前台新增商品模块中的 商品规格 内容
        try {
            if(LOGGER.isInfoEnabled()){
                LOGGER.info("新增商品，item = {}, desc = {} , itemParams = {}",item,desc,itemParams);
            }
            if (StringUtils.isEmpty(item.getTitle())) {
                // 响应400
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            //保存商品的基本数据
            this.itemService.saveItem(item,desc,itemParams);
            if(LOGGER.isInfoEnabled()){
                LOGGER.info("新增商品成功，itemId = {}",item.getId());
            }
            // 成功 201
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            LOGGER.error("新增商品失败！ title = "+item.getTitle()+",cid = "+item.getCid(),e);
        }
        // 出错 500
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    /**
     * 查询商品列表
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<EasyUIResult> queryItemList(
            @RequestParam(value = "page",defaultValue = "1")Integer page,
            @RequestParam(value = "rows",defaultValue = "10")Integer rows){

        try {
            PageInfo<Item> pageInfo = this.itemService.queryPageList(page, rows);
            EasyUIResult easyUIResult = new EasyUIResult(pageInfo.getTotal(),pageInfo.getList());
            return ResponseEntity.ok(easyUIResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 500
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    /**
     * 修改商品信息
     * @param item
     * @param desc
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<Void> updateItem(Item item,@RequestParam("desc") String desc,
                                           @RequestParam("itemParams")String itemParams){
        try {
            if(LOGGER.isInfoEnabled()){
                LOGGER.info("修改商品，item = {}, desc = {} ,itemParams = {}",item,desc,itemParams);
            }
            if (StringUtils.isEmpty(item.getTitle())){
                // 400
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            this.itemService.updateItem(item,desc,itemParams);
            if(LOGGER.isInfoEnabled()){
                LOGGER.info("修改商品成功，itemId = {}",item.getId());
            }
            // 204
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            LOGGER.error("修改商品失败！Title = "+item.getTitle()+",cid = "+item.getCid(),e);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    /**
     * 根据商品id查询商品数据
     * @param itemId
     * @return
     */
    @RequestMapping(value = "{itemId}",method = RequestMethod.GET)
    public ResponseEntity<Item> queryItemById(@PathVariable("itemId")Long itemId){
        try {
            Item item = this.itemService.queryById(itemId);
            if(null == item){
               return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(item);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    /**
     * 更新商品库存
     * @param
     * @return
     */
    @RequestMapping(value = "updateItemNum",method = RequestMethod.POST)
    public ResponseEntity<Void> updateItemById(@RequestBody Map map){
        try {
            String json = objectMapper.writeValueAsString(map);
            Item item = objectMapper.readValue(json, Item.class);
            this.itemService.update(item);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
