package com.jarvins.builder;

import java.util.concurrent.ThreadLocalRandom;

/**
 * 姓枚举
 */
public class NameBuilder {

    private static final String[] FIRST_NAME = {
            "王", "李", "张", "刘", "陈", "杨", "黄", "赵", "吴", "周", "徐", "孙",
            "马", "朱", "胡", "郭", "何", "高", "林", "罗", "郑", "梁", "谢", "宋",
            "唐", "许", "韩", "冯", "邓", "曹", "彭", "曾", "肖", "田", "董", "袁",
            "潘", "于", "蒋", "蔡", "余", "杜", "叶", "程", "苏", "魏", "吕", "丁",
            "任", "沈", "姚", "卢", "姜", "崔", "钟", "谭", "陆", "汪", "范", "金",
            "石", "廖", "贾", "夏", "韦", "付", "方", "白", "邹", "孟", "熊", "秦",
            "邱", "江", "尹", "薛", "闫", "段", "雷", "侯", "龙", "史", "陶", "黎",
            "贺", "顾", "毛", "郝", "龚", "邵", "万", "钱", "严", "覃", "武", "戴",
            "莫", "孔", "向", "汤", "赵", "钱", "孙", "李", "周", "吴", "郑", "王",
            "冯", "陈", "诸", "卫", "蒋", "沈", "韩", "杨", "朱", "秦", "尤", "许",
            "何", "吕", "施", "张", "孔", "曹", "严", "华", "金", "魏", "陶", "姜",
            "戚", "谢", "邹", "喻", "柏", "水", "窦", "章", "云", "苏", "潘", "葛",
            "奚", "范", "彭", "郎", "鲁", "韦", "昌", "马", "苗", "凤", "花", "方",
            "俞", "任", "袁", "柳", "酆", "鲍", "史", "唐", "费", "廉", "岑", "薛",
            "雷", "贺", "倪", "汤", "滕", "殷", "罗", "毕", "郝", "邬", "安", "常",
            "乐", "于", "时", "傅", "皮", "齐", "康", "伍", "余", "元", "卜", "顾",
            "孟", "平", "黄", "和", "穆", "萧", "尹", "姚", "邵", "堪", "汪", "祁",
            "毛", "禹", "狄", "米", "贝", "明", "臧", "计", "伏", "成", "戴", "谈",
            "宋", "茅", "庞", "熊", "纪", "舒", "屈", "项", "祝", "董", "粱", "杜",
            "阮", "蓝", "闵", "席", "季", "麻", "强", "贾", "路", "娄", "危", "江",
            "童", "颜", "郭", "梅", "盛", "林", "刁", "钟", "徐", "邱", "骆", "高",
            "夏", "蔡", "田", "樊", "胡", "凌", "霍", "虞", "万", "支", "柯", "咎",
            "管", "卢", "莫", "经", "房", "裘", "干", "解", "应", "宗", "丁", "宣",
            "贲", "邓", "郁", "单", "杭", "洪", "包", "诸", "左", "石", "崔", "吉",
            "钮", "龚", "欧阳", "端木", "上官", "司马", "东方", "独孤", "南宫", "公孙",
            "慕容", "宇文"};

    private static final String[] WOMAN_SECOND_NAME = {
            "蕊", "薇", "菁", "芝婷", "梦", "岚", "苑", "心钰", "婕", "馨", "瑗", "婷然",
            "琰", "韵", "融", "美娴", "园", "艺", "咏", "轩萍", "卿", "聪", "澜", "鞠薇",
            "纯", "爽", "琬", "宁莎", "茗", "羽", "希", "于芬", "宁", "欣", "飘", "以卉",
            "育", "滢", "馥", "裴芊", "筠", "柔", "竹", "媛欣", "霭", "凝", "晓", "钰美",
            "欢", "霄", "伊", "薇玉", "亚", "宜", "可", "萱若", "姬", "舒", "影", "若妍",
            "荔", "枝", "思", "羽珺", "丽", "芬", "芳", "凤薇", "燕", "莺", "媛", "俊瑛",
            "艳", "珊", "莎", "芳映", "蓉", "眉", "君", "琳彤", "琴", "毓", "悦", "雯一",
            "昭", "冰", "枫", "璎媛", "芸", "菲", "寒", "素语", "锦", "玲", "秋", "娴允",
            "秀", "娟", "英", "娴尚", "华", "慧", "巧", "婧秀", "美", "娜", "静", "英华",
            "淑", "惠", "珠", "婧之", "翠", "雅", "芝", "娴灏", "玉", "萍", "红", "富荷",
            "月", "彩", "春", "竹珑", "菊", "兰", "凤", "雨情", "洁", "梅", "琳", "玥伶",
            "素", "云", "莲", "云秀", "真", "环", "雪", "华嫣", "荣", "爱", "妹", "琪含",
            "霞", "香", "瑞", "秀娴", "凡", "佳", "嘉", "芳叶", "琼", "勤", "珍", "缘蕊",
            "贞", "莉", "桂", "芙洁", "娣", "叶", "璧", "芝艳", "璐", "娅", "琦", "垭媛",
            "晶", "妍", "茜", "妍莉", "黛", "青", "倩", "云霞", "婷", "姣", "婉", "若彩",
            "娴", "瑾", "颖", "惠宥", "露", "瑶", "怡", "欣若", "婵", "雁", "蓓", "薇敏",
            "纨", "仪", "荷", "丹", "洋", "琪"

    };
    private static final String[] MEN_SECOND_NAME = {
            "梁", "栋", "维", "承泽", "启", "克", "伦", "润勋", "翔", "旭", "鹏", "恒杰",
            "泽", "晨", "辰", "浩瀚", "士", "以", "建", "智泽", "家", "致", "树", "修和",
            "炎", "盛", "雄", "建柏", "琛", "钧", "冠", "俊阳", "策", "腾", "楠", "禹涛",
            "榕", "风", "航", " 睿勋", "弘", "义", "兴", "志天", "良", "飞", "彬", "锐翔",
            "富", "和", "鸣", "胜天", "朋", "斌", "行", "荣品", "时", "泰", "博", "兴德",
            "磊", "民", "友", "涵轩", "志", "清", "坚", "建昱", "庆", "若", "德", "海梓",
            "彪", "伟", "刚", "瑞荣", "勇", "毅", "俊", "志诚", "峰", "强", "军", "伟轩",
            "平", "保", "东", "洪铭", "文", "辉", "力", "昊泽", "明", "永", "健", "志锋",
            "世", "广", "海", "一诺", "山", "仁", "波", "展豪", "宁", "福", "生", "杰邦",
            "龙", "元", "全", "哲西", "国", "胜", "学", "泽庆", "祥", "才", "发", "锐翰",
            "武", "新", "利", "佑琪", "顺", "信", "子", "浩厚", "杰", "涛", "昌", "英杰",
            "成", "康", "星", "弘伟", "光", "天", "达", "峻浩", "安", "岩", "中", "德义",
            "茂", "进", "林", "昌盛", "有", "诚", "先", "治´安", "敬", "震", "振", "英勋",
            "壮", "会", "思", "学辉", "群", "豪", "心", "艺皓", "邦", "承", "乐", "杰玮",
            "绍", "功", "松", "明杰", "善", "厚", "裕", "翔宇", "河", "哲", "江", "启松",
            "超", "浩", "亮", "国华", "政", "谦", "亨", "泽轩", "奇", "固", "之", "健豪",
            "轮", "翰", "朗", "言", "伯", "宏", "洋", "琦","均","瀚"
    };

    private static final ThreadLocalRandom RANDOM = ThreadLocalRandom.current();

    public static String build(boolean isMan) {
        String[] arr = isMan ? MEN_SECOND_NAME : WOMAN_SECOND_NAME;
        String firstName = FIRST_NAME[RANDOM.nextInt(99999) % FIRST_NAME.length];
        //是否采用双单名
        if (RANDOM.nextInt(2) == 1) {
            return firstName + buildDoubleName(arr);
        }
        return firstName + arr[RANDOM.nextInt(arr.length)];
    }

    /**
     * 构建2个单字名的双字名
     * 降低名字的重复性
     *
     * @return 名字
     */
    private static String buildDoubleName(String[] arr) {
        int[] indexs = new int[2];
        for (int i = 0; i < 2; i++) {
            int index = RANDOM.nextInt(arr.length);
            indexs[i] = arr[index].length() == 2 ? index - 1 : index;
        }
        return arr[indexs[0]] + arr[indexs[1]];
    }

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            System.out.println(build(false));
        }
    }
}
