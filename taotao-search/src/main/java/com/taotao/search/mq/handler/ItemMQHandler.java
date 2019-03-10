package com.taotao.search.mq.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.service.RedisService;
import com.taotao.search.pojo.Item;
import com.taotao.search.service.ItemService;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

public class ItemMQHandler {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Autowired
    private HttpSolrServer httpSolrServer;

    @Autowired
    private ItemService itemService;

    public void execute(String msg){
        try {
            JsonNode jsonNode = MAPPER.readTree(msg);
            Long itemId = jsonNode.get("itemId").asLong();
            String type = jsonNode.get("type").asText();
            if(StringUtils.equals(type,"insert") || StringUtils.equals(type,"update")){
                // 从后台系统中查询商品数据
                Item item = this.itemService.queryItemById(itemId);
                if (null != item){
                    this.httpSolrServer.addBean(item);
                    this.httpSolrServer.commit();
                }
            }else if(StringUtils.equals(type,"delete")){
                // 删除索引数据
                this.httpSolrServer.deleteById(String.valueOf(itemId));
                this.httpSolrServer.commit();
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SolrServerException e) {
            e.printStackTrace();
        }
    }
}
