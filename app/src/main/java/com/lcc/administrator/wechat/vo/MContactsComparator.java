package com.lcc.administrator.wechat.vo;

import java.util.Comparator;

/**
 * 通讯录实体对象比较器
 */

public class MContactsComparator implements Comparator<MContacts> {

    /**
     * 定义比较规则
     * 1、对Contacts Header属性进行排序   按字母的Ascii码  a<b<c<d  不区分大小写
     * 2、“#最大”  排在第一位
     * 3、mContacts > t1  返回一个正数
     */
    @Override
    public int compare(MContacts mContacts, MContacts t1) {

        String mContactsHeader = mContacts.getHeard().toLowerCase();
        String mt1Header = t1.getHeard().toLowerCase();

        if ("#".equals(mContactsHeader)) {
            return 1;
        }
        if ("#".equals(mt1Header)) {
            return -1;
        }
        return mContactsHeader.compareTo(mt1Header);
    }


}
