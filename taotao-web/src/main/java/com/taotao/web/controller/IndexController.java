package com.taotao.web.controller;

import com.taotao.web.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class IndexController {

    @Autowired
    private IndexService indexService;
    /**
     * taotao首页
     * @return
     */
    @RequestMapping(value = "index",method = RequestMethod.GET)
    public ModelAndView index(){
        ModelAndView mv = new ModelAndView("index");
        // 大广告位数据
        String indexAd1 = indexService.queryIndexAD1();
        mv.addObject("indexAd1",indexAd1);
        // 右上角小广告位数据
        String indexAd2 = indexService.queryIndexAD2();
        mv.addObject("indexAd2",indexAd2);

        return mv;
    }
}
