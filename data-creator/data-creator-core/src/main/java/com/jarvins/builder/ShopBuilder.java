package com.jarvins.builder;

import com.jarvins.entity.Commodity;

import java.util.LinkedList;
import java.util.List;

public class ShopBuilder {

    public static List<Shop> build(List<Integer> userIds, List<Commodity> commodities){
        assert userIds.size() == commodities.size();
        List<Shop> list = new LinkedList<>();
        for (int i = 0; i < userIds.size(); i++) {
            Integer id = userIds.get(i);
            Commodity commodity = commodities.get(i);
            list.add(new Shop(id,commodity.getProductName(),commodity.getProductCode(),commodity.getProductPrice()));
        }
        return list;
    }
}
