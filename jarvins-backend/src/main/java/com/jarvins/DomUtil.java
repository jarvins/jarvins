package com.jarvins;

import org.dom4j.*;
import org.dom4j.dom.DOMElement;
import org.junit.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class DomUtil {

    public static void main(String[] args) throws DocumentException {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<java version=\"1.7.0_79\" class=\"java.beans.XMLDecoder\">\n" +
                "    <object class=\"java.util.HashMap\">\n" +
                "        <void method=\"put\">\n" +
                "            <string>fd_366e1e1328794a</string>\n" +
                "            <string>招商银行</string>\n" +
                "        </void>\n" +
                "        <void method=\"put\">\n" +
                "            <string>fd_366e1a8548718a</string>\n" +
                "            <object class=\"java.util.ArrayList\">\n" +
                "                <void method=\"add\">\n" +
                "                    <object class=\"java.util.HashMap\">\n" +
                "                        <void method=\"put\">\n" +
                "                            <string>fd_36eb09b27d4632</string>\n" +
                "                            <string>0</string>\n" +
                "                        </void>\n" +
                "                        <void method=\"put\">\n" +
                "                            <string>fdId</string>\n" +
                "                            <string>1764c3d85cbd62bce125a08490bb1927</string>\n" +
                "                        </void>\n" +
                "                        <void method=\"put\">\n" +
                "                            <string>fd_36eb09c7635ae2</string>\n" +
                "                            <double>260.0</double>\n" +
                "                        </void>\n" +
                "                        <void method=\"put\">\n" +
                "                            <string>fd_366e22cf0dbed6</string>\n" +
                "                            <string>员工福利费-团建费</string>\n" +
                "                        </void>\n" +
                "                        <void method=\"put\">\n" +
                "                            <string>fd_36eb09c9687052</string>\n" +
                "                            <double>0.0</double>\n" +
                "                        </void>\n" +
                "                        <void method=\"put\">\n" +
                "                            <string>fd_36eb09f871da8e</string>\n" +
                "                            <double>260.0</double>\n" +
                "                        </void>\n" +
                "                    </object>\n" +
                "                </void>\n" +
                "                <void method=\"add\">\n" +
                "                    <object class=\"java.util.HashMap\">\n" +
                "                        <void method=\"put\">\n" +
                "                            <string>fd_36eb09b27d4632</string>\n" +
                "                            <string>0</string>\n" +
                "                        </void>\n" +
                "                        <void method=\"put\">\n" +
                "                            <string>fdId</string>\n" +
                "                            <string>1764c3d85cb3d22a5e10aef46c2803b1</string>\n" +
                "                        </void>\n" +
                "                        <void method=\"put\">\n" +
                "                            <string>fd_36eb09c7635ae2</string>\n" +
                "                            <double>700.0</double>\n" +
                "                        </void>\n" +
                "                        <void method=\"put\">\n" +
                "                            <string>fd_366e22cf0dbed6</string>\n" +
                "                            <string>员工福利费-团建费</string>\n" +
                "                        </void>\n" +
                "                        <void method=\"put\">\n" +
                "                            <string>fd_36eb09c9687052</string>\n" +
                "                            <double>0.0</double>\n" +
                "                        </void>\n" +
                "                        <void method=\"put\">\n" +
                "                            <string>fd_36eb09f871da8e</string>\n" +
                "                            <double>700.0</double>\n" +
                "                        </void>\n" +
                "                    </object>\n" +
                "                </void>\n" +
                "                <void method=\"add\">\n" +
                "                    <object class=\"java.util.HashMap\">\n" +
                "                        <void method=\"put\">\n" +
                "                            <string>fd_36eb09b27d4632</string>\n" +
                "                            <string>0</string>\n" +
                "                        </void>\n" +
                "                        <void method=\"put\">\n" +
                "                            <string>fdId</string>\n" +
                "                            <string>1764c3d85cbfae0e004da6c48b4a9adb</string>\n" +
                "                        </void>\n" +
                "                        <void method=\"put\">\n" +
                "                            <string>fd_36eb09c7635ae2</string>\n" +
                "                            <double>903.0</double>\n" +
                "                        </void>\n" +
                "                        <void method=\"put\">\n" +
                "                            <string>fd_366e22cf0dbed6</string>\n" +
                "                            <string>员工福利费-团建费</string>\n" +
                "                        </void>\n" +
                "                        <void method=\"put\">\n" +
                "                            <string>fd_36eb09c9687052</string>\n" +
                "                            <double>0.0</double>\n" +
                "                        </void>\n" +
                "                        <void method=\"put\">\n" +
                "                            <string>fd_36eb09f871da8e</string>\n" +
                "                            <double>903.0</double>\n" +
                "                        </void>\n" +
                "                    </object>\n" +
                "                </void>\n" +
                "            </object>\n" +
                "        </void>\n" +
                "        <void method=\"put\">\n" +
                "            <string>fd_369799ea096296</string>\n" +
                "            <double>1863.0</double>\n" +
                "        </void>\n" +
                "        <void method=\"put\">\n" +
                "            <string>fd_371af41a6706c2</string>\n" +
                "            <double>0.0</double>\n" +
                "        </void>\n" +
                "        <void method=\"put\">\n" +
                "            <string>fd_3704f106531f60</string>\n" +
                "            <string>999</string>\n" +
                "        </void>\n" +
                "        <void method=\"put\">\n" +
                "            <string>fd_3672148826fabe</string>\n" +
                "            <double>0.0</double>\n" +
                "        </void>\n" +
                "        <void method=\"put\">\n" +
                "            <string>fd_37b2a703dc9c1c</string>\n" +
                "            <double>1863.0</double>\n" +
                "        </void>\n" +
                "        <void method=\"put\">\n" +
                "            <string>fd_366e1de734a39a</string>\n" +
                "            <object class=\"java.util.HashMap\">\n" +
                "                <void method=\"put\">\n" +
                "                    <string>id</string>\n" +
                "                    <string>164a736b0653cd09d39c3fb44009466f</string>\n" +
                "                </void>\n" +
                "                <void method=\"put\">\n" +
                "                    <string>name</string>\n" +
                "                    <string>数据运营部</string>\n" +
                "                </void>\n" +
                "            </object>\n" +
                "        </void>\n" +
                "        <void method=\"put\">\n" +
                "            <string>fd_37b2a5f3c4a66c</string>\n" +
                "            <double>1863.0</double>\n" +
                "        </void>\n" +
                "        <void method=\"put\">\n" +
                "            <string>fd_371c65eb769b8c</string>\n" +
                "            <string>CNY</string>\n" +
                "        </void>\n" +
                "        <void method=\"put\">\n" +
                "            <string>fd_366e1fa2e5fc5c</string>\n" +
                "            <string>SYFY20201210034</string>\n" +
                "        </void>\n" +
                "        <void method=\"put\">\n" +
                "            <string>fd_3717b8a0c221c2</string>\n" +
                "            <string>M12</string>\n" +
                "        </void>\n" +
                "        <void method=\"put\">\n" +
                "            <string>fd_366e1f7157852e</string>\n" +
                "            <string>北京</string>\n" +
                "        </void>\n" +
                "        <void method=\"put\">\n" +
                "            <string>fd_3673739fc35fe8</string>\n" +
                "            <double>1863.0</double>\n" +
                "        </void>\n" +
                "        <void method=\"put\">\n" +
                "            <string>fd_3677611c930fa8</string>\n" +
                "            <string>&lt;p&gt;业财项目组2020年团建&lt;/p&gt;\n" +
                "</string>\n" +
                "        </void>\n" +
                "        <void method=\"put\">\n" +
                "            <string>fd_3675fe23e2a6d8</string>\n" +
                "            <string>130101</string>\n" +
                "        </void>\n" +
                "        <void method=\"put\">\n" +
                "            <string>fd_366e1a8548718a_index_</string>\n" +
                "            <object class=\"java.util.HashMap\">\n" +
                "                <void method=\"put\">\n" +
                "                    <string>fd_36eb09b27d4632</string>\n" +
                "                    <string>0</string>\n" +
                "                </void>\n" +
                "                <void method=\"put\">\n" +
                "                    <string>fd_36eb09c7635ae2</string>\n" +
                "                    <null/>\n" +
                "                </void>\n" +
                "                <void method=\"put\">\n" +
                "                    <string>fd_366e22cf0dbed6</string>\n" +
                "                    <null/>\n" +
                "                </void>\n" +
                "                <void method=\"put\">\n" +
                "                    <string>fd_36eb09c9687052</string>\n" +
                "                    <double>0.0</double>\n" +
                "                </void>\n" +
                "                <void method=\"put\">\n" +
                "                    <string>fd_36eb09f871da8e</string>\n" +
                "                    <null/>\n" +
                "                </void>\n" +
                "            </object>\n" +
                "        </void>\n" +
                "        <void method=\"put\">\n" +
                "            <string>fd_366e1f77cc57ae</string>\n" +
                "            <string>1</string>\n" +
                "        </void>\n" +
                "        <void method=\"put\">\n" +
                "            <string>fd_3676008f56caca</string>\n" +
                "            <string></string>\n" +
                "        </void>\n" +
                "        <void method=\"put\">\n" +
                "            <string>fd_37754f851fcf44</string>\n" +
                "            <string>2</string>\n" +
                "        </void>\n" +
                "        <void method=\"put\">\n" +
                "            <string>fd_366e1f929151a2</string>\n" +
                "            <string>6214850119707773</string>\n" +
                "        </void>\n" +
                "    </object>\n" +
                "</java>";

        HashMap<String, String> mapping = new HashMap<>();
        mapping.put("fd_366e1e1328794a", "aa");
        mapping.put("fd_36eb09b27d4632", "bb");
        mapping.put("fd_36eb09c7635ae2", "cc");
        mapping.put("fd_366e22cf0dbed6", "dd");

        mapping.put("fd_36eb09c9687052", "ee");
        mapping.put("fd_36eb09f871da8e", "ff");
        mapping.put("fd_369799ea096296", "gg");
        mapping.put("fd_371af41a6706c2", "hh");

        mapping.put("fd_3704f106531f60", "ii");
        mapping.put("fd_3672148826fabe", "jj");
        mapping.put("fd_37b2a703dc9c1c", "kk");
        mapping.put("fd_37b2a5f3c4a66c", "ll");

        mapping.put("fd_371c65eb769b8c", "mm");
        mapping.put("fd_366e1fa2e5fc5c", "nn");
        mapping.put("fd_3717b8a0c221c2", "oo");
        mapping.put("fd_366e1f7157852e", "pp");

        mapping.put("fd_3673739fc35fe8", "qq");
        mapping.put("fd_3677611c930fa8", "rr");
        mapping.put("fd_3675fe23e2a6d8", "ss");
        mapping.put("fd_366e1f77cc57ae", "tt");

        mapping.put("fd_3676008f56caca", "qq");
        mapping.put("fd_37754f851fcf44", "rr");
        mapping.put("fd_366e1f929151a2", "ss");
        mapping.put("fd_366e1de5f14c3e", "tt");



        Element element = DomUtil.loadXml(xml);
        Element res = new DOMElement("billhead");
        List elements = element.elements();
        Assert.assertEquals(elements.size(), 1);
        DomUtil.parse((Element) element.elements().get(0), res, mapping);
        System.out.println(res.getText());
    }


    public static Element loadXml(String xml) throws DocumentException {
        Document document = DocumentHelper.parseText(xml);
        return document.getRootElement();
    }


    public static void parse(Element ele, Element res, Map<String, String> mapping) {
//        List<Element> elements = ele.elements();
//        for (Element element : elements) {
        Optional<Attribute> spec = ele.attributes().stream().filter(e -> ((Attribute) e).getName().equals("class")).findFirst();
        if (spec.isPresent()) {
            switch (spec.get().getValue()) {
                case "java.util.ArrayList":
                    listElementParse(ele, res, mapping);
                    break;
                case "java.util.HashMap":
                    mapElementParse(ele, res, mapping);
            }
        } else {
            simpleElementParse(ele, res, mapping);
        }
//        }
    }

    private static void mapElementParse(Element ele, Element res, Map<String, String> mapping) {
        List<Element> elements = ele.elements();
        for (Element element : elements) {
            parse(element, res, mapping);
        }
    }

    private static void listElementParse(Element ele, Element res, Map<String, String> mapping) {
        Element node = new DOMElement("items");
        List<Element> elements = ele.elements();
        for (Element element : elements) {
            Element item = new DOMElement("item");
            parse(element, item, mapping);
            node.add(item);
        }
        res.add(node);
    }

    private static void simpleElementParse(Element ele, Element res, Map<String, String> mapping) {
        List<Element> elements = ele.elements();
        //只有key-value
        Assert.assertEquals(elements.size(), 2);
        String tag = null;
        Object value = null;
        for (Element element : elements) {
            String data = element.getText();
            if (data != null && data.startsWith("fd_") && mapping.containsKey(data)) {
                tag = mapping.get(data);
            } else {
                value = data;
            }
        }
        if (tag != null) {
            Element e = new DOMElement(tag);
            e.setData(value);
            res.add(e);
        }
    }
}
