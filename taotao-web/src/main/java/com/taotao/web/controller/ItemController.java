package com.taotao.web.controller;

import com.taotao.pojo.ItemDesc;
import com.taotao.web.bean.Item;
import com.taotao.web.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@RequestMapping("item")
@Controller
public class ItemController {

    @Autowired
    private ItemService itemService;

    @RequestMapping(value = "{itemId}",method = RequestMethod.GET)
    public ModelAndView showDetail(@PathVariable("itemId")Long itemId){
        ModelAndView mv = new ModelAndView("item");
        // 商品的基本数据
        Item item = this.itemService.queryItemById(itemId);
        mv.addObject("item",item);
        // 商品的描述数据
        ItemDesc itemDesc = this.itemService.queryItemDescByItemId(itemId);
        mv.addObject("itemDesc",itemDesc);
        // 商品的规格参数数据
        String html = this.itemService.queryItemParamByItemId(itemId);
        mv.addObject("itemParam",html);
        return mv;
    }
}
