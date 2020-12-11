package com.jarvins.builder;

public class CommodityBuilder {

    public static final String[] COMMODITY_SEARCH_ITEM = new String[]{
            "女装新品", "商场同款", "仙女连衣裙", "T恤", "衬衫", "时髦外套", "休闲裤", "牛仔裤",
            "无痕文胸", "运动文胸", "潮流家居服", "百搭船袜", "毛呢外套", "羽绒服", "棉服", "丝绒卫衣",
            "毛针织衫", "皮草", "毛衣", "衬衫", "卫衣", "针织衫", "T恤", "短外套", "小西装", "风衣",
            "连衣裙", "半身裙", "A字裙", "荷叶边裙", "大摆裙", "包臀裙", "百褶裙", "长袖连衣裙", "",
            "棉麻连衣裙", "牛仔裙", "蕾丝连衣裙", "真丝连衣裙", "印花连衣裙", "休闲裤", "阔腿裤", "牛仔裤",
            "打底裤", "开叉运动裤", "哈伦裤", "背带裤", "小脚裤", "西装裤", "短裤", "妈妈装", "大码女装",
            "职业套装", "优雅旗袍", "精致礼服", "婚纱唐装", "小码女装", "光面文胸", "美背文胸", "聚拢文胸",
            "大杯文胸", "轻薄塑身", "春夏家居服", "纯棉家居服", "莫代尔家居服", "真丝家居服", "春夏睡裙",
            "男士家居服", "情侣家居服", "性感睡裙", "男士内裤", "女式内裤", "男士内裤多条装", "女士内裤多条装",
            "莫代尔内裤", "羽绒服", "潮流卫衣", "牛仔", "衬衫", "修身夹克", "保暖棉服", "针织衫", "修身西服",
            "秋冬风衣", "束脚裤", "破洞牛仔裤", "跑步鞋", "休闲鞋", "篮球鞋", "自行车", "平衡车", "帆布鞋",
            "运动套装", "运动卫衣", "鱼竿", "冲锋衣", "跑步机", "电动车", "棒球服", "棉衣", "针织开衫", "马甲",
            "短袖T恤", "POLO衫", "短袖衬衫", "背心", "卫衣", "针织衫", "毛衣", "长袖衬衫", "长袖T恤", "小脚裤",
            "9分裤短裤", "休闲裤", "牛仔裤", "针织裤", "西裤", "运动裤", "工装裤", "情侣装", "跑步鞋", "运动鞋",
            "板鞋", "潮鞋", "休闲鞋", "篮球鞋", "足球鞋", "短袖t恤", "运动裤", "运动内衣", "速干t恤", "运动Polo",
            "运动卫衣", "运动套装", "运动短裤", "健身服", "运动茄克", "钓鱼服", "潮流过膝靴", "气质百搭踝靴",
            "永远的帆布鞋", "流行松糕", "底英伦牛津鞋", "休闲鞋", "正装皮鞋", "休闲皮鞋", "板鞋", "户外休闲帆布鞋",
            "运动休闲", "乐福鞋", "豆豆鞋", "布洛克", "帆船鞋", "布鞋", "单鞋", "小白鞋", "帆布鞋", "深口单鞋",
            "浅口单鞋", "平底单鞋", "高跟鞋", "豆豆鞋", "乐福鞋", "牛津鞋", "女士钱包", "单肩包斜跨包", "手提包",
            "手拿包", "腰包", "胸包", "化妆包", "男士钱包", "收纳包", "胸包", "腰包", "手拿包", "手提包", "斜跨包",
            "单肩包", "旅行箱", "万向轮箱", "旅行袋", "女士双肩包", "男士双肩包", "韩版双肩包", "铆钉双肩包", "女士钱包",
            "男士钱包", "面膜", "护肤套装", "乳液", "面霜", "精华液", "护手霜", "爽肤水", "洁面", "眼霜", "身体乳",
            "卸妆", "身体护理", "男士护理精油", "面部喷雾", "T区护理", "去角质补水", "祛痘", "敏感修护",
            "时尚彩妆", "香水", "BB霜", "美容工具", "口红", "隔离粉底", "粉饼", "气垫bb",
            "指甲油", "美甲工具", "眉笔睫毛膏", "眼线", "唇彩", "眼影", "蜜粉彩妆套装",
            "洁面爽肤水", "乳液", "面霜", "面膜", "面部精华", "眼部护理", "防晒", "唇部护理",
            "T区护理", "控油", "洗发水", "进口洗发水", "护发素", "护发精油",
            "牙膏", "进口牙膏", "牙刷", "进口牙刷", "漱口水", "牙线", "牙粉", "口气清新剂", "牙贴", "口腔美白", "假牙清洁",
            "沐浴露", "香皂", "泡澡", "浴盐", "洗手液", "手动剃须刀", "卫生巾", "成人护垫",
            "私处洗液", "卫生棉条", "耳钉", "黄金项链", "婚嫁套饰", "黄金对戒", "足金饰品", "K金饰品",
            "结婚钻戒", "钻石吊坠", "钻石耳饰", "钻石手链", "裸钻定制", "红蓝宝石", "绿宝石",
            "坦桑石", "珍珠项链", "和田玉翡翠摆件", "翡翠手镯", "翡翠项链", "琥珀手链",
            "银饰木手串", "银手镯", "石手链", "开口戒指", "胸针", "手链", "项链", "发饰", "手镯", "耳饰", "戒指",
            "瑞士名表", "欧美手表", "经典国表", "国货精表", "男表", "女表", "情侣表",
            "儿童手表", "机械表", "石英表", "防水表", "运动表", "学生表", "军表", "陶瓷表",
            "太阳镜", "墨镜眼镜框近", "视眼镜", "防辐射眼镜", "老花镜", "司机镜",
            "驾驶镜", "3D眼镜", "电子烟", "蒸汽烟", "水烟", "女士烟", "鼻烟", "雪茄", "小米", "iPhone",
            "魅族", "荣耀", "乐视", "OPPO", "vivo", "三星", "华为", "双卡双待", "大屏", "自拍", "合约机", "笔记本",
            "平板电脑", "台式机", "游戏本", "iPad", "Surface", "智能设备", "智能手表", "智能手环", "VR眼镜", "智能机器人", "显示器",
            "游戏本", "机械键盘", "XBOX", "游戏手柄", "显示器", "机械键盘", "固态硬盘", "CPU", "显卡", "主板", "高速U盘",
            "路由器", "相机", "单反", "摄像机", "自拍神器", "拍立得", "镜头自拍杆", "耳机", "天猫魔盒", "数码影音", "家庭影院",
            "蓝牙耳机", "网络播放器", "文具", "电子书", "书写工具", "智能投影", "打印机", "保险箱", "手机配件",
            "苹果配件", "笔记本配件", "平板配件", "相机配件", "手机车", "载配件", "4K超高清", "智能电视", "超级大屏", "云电视",
            "中央空调", "冷柜", "酒柜", "滚筒洗", "波轮洗", "洗烘一体", "迷你烘干机",
            "烟灶套装", "嵌入式烤箱", "嵌入式蒸箱", "燃气灶", "消毒柜", "洗碗机", "集成灶",
            "电热水器", "燃气热水器", "太阳能", "净水器", "电饭煲", "豆浆机", "电热水壶", "电压力锅", "电磁炉", "养生壶",
            "烤箱", "料理机", "微波炉", "榨汁机", "原汁机", "面包机", "咖啡机", "电炸锅",
            "扫地机器人", "空气净化器", "吸尘器", "除湿机", "取暖器", "挂烫机", "干衣机",
            "剃须刀", "吹风机", "电动牙刷", "体重秤", "理发器", "足浴器", "按摩椅",
            "脱毛器", "吊扇", "冰淇淋机碎冰机", "空调", "客厅成套家具", "餐厅成套家具", "卧室成套家具", "儿童成套家具",
            "沙发", "布艺沙发", "皮艺沙发", "实木沙发", "懒人沙发", "电视柜", "茶几", "鞋柜",
            "单人沙发", "餐桌", "餐椅", "酒柜", "餐边柜",
            "床", "衣柜", "床垫", "简易衣柜", "乳胶床垫", "弹簧床垫", "棕床垫", "实木床", "双人床",
            "电脑桌", "电脑椅", "书桌", "办公桌", "书架", "书柜", "连体书桌",
            "儿童床", "高低床", "儿童床垫", "儿童沙发", "儿童椅", "儿童学习桌", "儿童衣柜",
            "强化复合地板", "瓷砖", "墙纸", "涂料", "瓷砖背景墙", "油漆", "地板", "硅藻泥",
            "浴霸", "集成吊顶", "马桶", "淋浴花洒套装", "浴室柜", "组合水槽套餐", "智能马桶盖",
            "吸顶灯", "吊灯", "水晶吊灯", "筒灯", "射灯", "灯泡", "壁灯", "灯具套餐", "铜灯",
            "接线板", "开关插座", "USB插座", "指纹锁", "工具箱", "监控摄像头", "门锁拉手"
    };
}
