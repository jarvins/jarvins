package com.jarvins.builder;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class AddressBuilder {

    private static final String[] PROVINCE = {
            "河北省", "山西省", "辽宁省", "吉林省", "黑龙江省", "江苏省", "浙江省", "安徽省", "福建省",
            "江西省", "山东省", "河南省", "湖北省", "湖南省", "广东省", "海南省", "四川省", "贵州省",
            "云南省", "陕西省", "甘肃省", "青海省", "内蒙古自治区", "广西壮族自治区", "西藏自治区",
            "宁夏回族自治区", "新疆维吾尔自治区", "北京市", "天津市", "上海市", "重庆市"};

    private static final Map<String, String[]> CITY = new HashMap<String, String[]>() {{
        put("河北省",
                new String[]{"石家庄市", "唐山市", "秦皇岛市", "邯郸市", "邢台市",
                        "保定市", "张家口市", "承德市", "沧州市", "廊坊市", "衡水市"});
        put("山西省",
                new String[]{
                        "太原市", "大同市", "阳泉市", "长治市", "晋城市", "朔州市",
                        "晋中市", "运城市", "忻州市", "临汾市", "吕梁市"});
        put("内蒙古自治区",
                new String[]{"呼和浩特市", "包头市", "乌海市", "赤峰市", "通辽市",
                        "鄂尔多斯市", "呼伦贝尔市", "巴彦淖尔市", "乌兰察布市"});
        put("辽宁省",
                new String[]{"沈阳市", "大连市", "鞍山市", "抚顺市", "本溪市", "丹东市",
                        "锦州市", "营口市", "阜新市", "辽阳市", "盘锦市", "铁岭市", "朝阳市", "葫芦岛市"});
        put("吉林省",
                new String[]{"长春市", "吉林市", "四平市", "辽源市", "通化市", "白山市", "松原市", "白城市"});
        put("黑龙江省",
                new String[]{"哈尔滨市", "齐齐哈尔市", "鸡西市", "鹤岗市", "双鸭山市", "大庆市", "伊春市",
                        "佳木斯市", "七台河市", "牡丹江市", "黑河市", "绥化市"});
        put("江苏省",
                new String[]{"南京市", "无锡市", "徐州市", "常州市", "苏州市", "南通市", "连云港市",
                        "淮安市", "盐城市", "扬州市", "镇江市", "泰州市", "宿迁市"});
        put("浙江省",
                new String[]{"杭州市", "宁波市", "温州市", "嘉兴市", "湖州市", "绍兴市", "金华市",
                        "衢州市", "舟山市", "台州市", "丽水市"});
        put("安徽省",
                new String[]{"合肥市", "芜湖市", "蚌埠市", "淮南市", "马鞍山市", "淮北市", "铜陵市",
                        "安庆市", "黄山市", "阜阳市", "宿州市", "滁州市", "六安市", "宣城市", "池州市", "亳州市"});
        put("福建省",
                new String[]{"福州市", "厦门市", "莆田市", "三明市", "泉州市", "漳州市", "南平市", "龙岩市", "宁德市"});
        put("山东省",
                new String[]{"济南市", "青岛市", "淄博市", "枣庄市", "东营市", "烟台市", "潍坊市", "济宁市", "泰安市",
                        "威海市", "日照市", "临沂市", "德州市", "聊城市", "滨州市", "菏泽市"});
        put("河南省",
                new String[]{"郑州市", "开封市", "洛阳市", "平顶山市", "安阳市", "鹤壁市", "新乡市", "焦作市", "濮阳市",
                        "许昌市", "漯河市", "三门峡市", "南阳市", "商丘市", "信阳市", "周口市", "驻马店市"});
        put("湖北省",
                new String[]{"武汉市", "黄石市", "十堰市", "宜昌市", "襄阳市", "鄂州市", "荆门市", "孝感市",
                        "荆州市", "黄冈市", "咸宁市", "随州市"});
        put("湖南省",
                new String[]{"长沙市", "株洲市", "湘潭市", "衡阳市", "邵阳市", "岳阳市", "常德市", "张家界市",
                        "益阳市", "郴州市", "永州市", "怀化市", "娄底市"});
        put("广东省",
                new String[]{"广州市", "韶关市", "深圳市", "珠海市", "汕头市", "佛山市", "江门市", "湛江市", "茂名市",
                        "肇庆市", "惠州市", "梅州市", "汕尾市", "河源市", "阳江市", "清远市", "东莞市", "中山市", "潮州市",
                        "揭阳市", "云浮市"});
        put("广西壮族自治区",
                new String[]{"南宁市", "柳州市", "桂林市", "梧州市", "北海市", "防城港市", "钦州市", "贵港市",
                        "玉林市", "百色市", "贺州市", "河池市", "来宾市", "崇左市"});
        put("海南省",
                new String[]{"海口市", "三亚市", "三沙市", "儋州市"});
        put("四川省",
                new String[]{"成都市", "自贡市", "攀枝花市", "泸州市", "德阳市", "绵阳市", "广元市", "遂宁市",
                        "内江市", "乐山市", "南充市", "眉山市", "宜宾市", "广安市", "达州市", "雅安市", "巴中市", "资阳市"});
        put("贵州省",
                new String[]{"贵阳市", "六盘水市", "遵义市", "安顺市", "毕节市", "铜仁市"});
        put("云南省",
                new String[]{"昆明市", "曲靖市", "玉溪市", "保山市", "昭通市", "丽江市", "普洱市", "临沧市"});
        put("西藏自治区",
                new String[]{"拉萨市", "日喀则市", "昌都市", "林芝市", "山南市", "那曲市"});
        put("陕西省",
                new String[]{"西安市", "铜川市", "宝鸡市", "咸阳市", "渭南市", "延安市", "汉中市", "榆林市", "安康市", "商洛市"});
        put("甘肃省",
                new String[]{"兰州市", "嘉峪关市", "金昌市", "白银市", "天水市", "武威市", "张掖市", "平凉市", "酒泉市", "庆阳市", "定西市", "陇南市"});
        put("青海省",
                new String[]{"西宁市", "海东市"});
        put("江西省",
                new String[]{"南昌市","景德镇市","萍乡市","九江市","抚州市","鹰潭市","赣州市","吉安市","宜春市","新余市","上饶市"});
        put("宁夏回族自治区",
                new String[]{"银川市", "石嘴山市", "吴忠市", "固原市", "中卫市"});
        put("新疆维吾尔自治区",
                new String[]{"乌鲁木齐市", "克拉玛依市", "吐鲁番市", "哈密市"});
        put("北京市", new String[]{"北京市"});
        put("天津市", new String[]{"天津市"});
        put("上海市", new String[]{"上海市"});
        put("重庆市", new String[]{"重庆市"});
    }};

    private static final String[] COMMUNITY = {"君临天下", "华门新都", "凭祥小区", "花门新城", "东城国际", "阳光城",
            "南湖花园", "汇金现代城", "阳光名邸", "东盟国际城", "欧景花园", "中城丽景花园", "光华苑", "永安阁", "方洲丽园",
            "景明苑", "幸福港湾", "瑞华苑小区", "博苑名居", "九里象湖城", "伟梦清水湾", "梦里水乡",
            "江铃瓦良格", "恒大绿洲", "环球花苑", "阳光花园", "世纪花城", "城上城", "君城", "桂花园", "孔雀城", "华城",
            "书香美地", "多彩城", "幸福家园", "中央华府", "新泽苑", "开元小区", "陶居苑", "博世庄园", "乱世韶华", "提香草堂",
            "美然百度城", "达富苑", "宝润苑", "群芳丽景苑", "龙腾捷座", "西富港", "正阳小区", "万福家园", "华龙美树",
            "花木星晨苑", "皓月园", "明珠花园", "灵秀山庄", "双柳新居", "西颐小区", "红叶别墅", "星域轩", "北小园小区",
            "天通苑", "长安家园"};

    private static final String[] HOUSE_NUMBER = {"01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16"};

    private static final ThreadLocalRandom RANDOM = ThreadLocalRandom.current();

    public static Address build(){
        String province = PROVINCE[RANDOM.nextInt(PROVINCE.length)];
        String[] cities = CITY.get(province);
        String city = cities[RANDOM.nextInt(cities.length)];
        String community = COMMUNITY[RANDOM.nextInt(COMMUNITY.length)];
        int dong = RANDOM.nextInt(1,51);
        int floor = RANDOM.nextInt(1,31);
        String houseNumber = HOUSE_NUMBER[RANDOM.nextInt(HOUSE_NUMBER.length)];

        return Address.builder()
                .province(province)
                .city(city)
                .address(String.format("%s-%s-%s-%s-%s%s",province,city,community,dong,floor,houseNumber))
                .build();
    }

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            System.out.println(AddressBuilder.build().getAddress());
        }
    }

}
