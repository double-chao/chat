package com.lcc.administrator.wechat.util;

import android.text.TextUtils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class MPinYinUtil {

    public static String getPinYin(String chinese) {

        String pinYinStr = "";

        if (TextUtils.isEmpty(chinese)) {
            return pinYinStr;
        }

        //设置拼音的输出格式
        HanyuPinyinOutputFormat pinyinOutputFormat = new HanyuPinyinOutputFormat();

        //设置大小写  为大写
        pinyinOutputFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);

        //设置声调  没有声调
        pinyinOutputFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);

        //设置  U或者V   设置为V
        pinyinOutputFormat.setVCharType(HanyuPinyinVCharType.WITH_V);

        //把chinese(用户昵称的名字)转化为字符数组
        char[] charArray = chinese.trim().toCharArray();

        //汉字的正则表达式范围 Unicode
        String reg = "[\\u4e00-\\u9fa5]";

        //遍历字符数组
        for (char tChar : charArray) {
            //把字符数字转化为汉字
            String strChar = Character.toString(tChar);

            //判断该字是汉字
            if (strChar.matches(reg)) {
                try {
                    //文字的输入输出都有异常  要抛出异常
                    String[] strPinYinArray = PinyinHelper.toHanyuPinyinStringArray(tChar, pinyinOutputFormat);

                    //获取第一个字符拼音的首字母    （后面会写一个方法用来比较）
                    pinYinStr += strPinYinArray[0];

                } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {

                }
            } else {//不是汉字
                pinYinStr += strChar;
            }

        }

        return pinYinStr;
    }

}
