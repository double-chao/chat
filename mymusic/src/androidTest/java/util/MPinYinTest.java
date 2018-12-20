package util;

import android.util.Log;

import com.lcc.administrator.utils.MPinYinUtil;

import junit.framework.TestCase;

/**
*  @author lcc
*  created at 2018/12/18
*/
public class MPinYinTest extends TestCase {

    private final String TAG = "MPinYinTest";
    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    public void testGetPinYin() {
        String pinyin1 = MPinYinUtil.getPinYin("超超");
        String pinyin2 = MPinYinUtil.getPinYin("123*");
        String pinyin3 = MPinYinUtil.getPinYin("为*ew");
        String pinyin4 = MPinYinUtil.getPinYin("gj23");
        String pinyin5 = MPinYinUtil.getPinYin("西西435");
        String pinyin6 = MPinYinUtil.getPinYin("&2*啊");

        Log.d(TAG,pinyin1);
        Log.d(TAG,pinyin2);
        Log.d(TAG,pinyin3);
        Log.d(TAG,pinyin4);
        Log.d(TAG,pinyin5);
        Log.d(TAG,pinyin6);
    }

}
