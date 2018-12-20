package com.lcc.administrator.vo;

import java.util.Comparator;
/**
*  @author lcc
*  created at 2018/12/19
 *  通讯录对象比较器
*/

public class MContactsComparator implements Comparator<MContact> {

    /**
     * 定义比较规则
     * 1、对Contacts Header属性进行排序   按字母的Ascii码  a<b<c<d  不区分大小写
     * 2、“#最大”  排在第一位
     * 3、mContacts > t1  返回一个正数
     */
    @Override
    public int compare(MContact mContacts, MContact t1) {

        String mContactsHeader = mContacts.getHeader().toLowerCase();
        String mt1Header = t1.getHeader().toLowerCase();

        if ("#".equals(mContactsHeader)) {
            return 1;
        }
        if ("#".equals(mt1Header)) {
            return -1;
        }
        return mContactsHeader.compareTo(mt1Header);
    }


}
