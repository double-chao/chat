package com.lcc.administrator.wechat.chat;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lcc.administrator.wechat.R;
import com.lcc.administrator.wechat.vo.MContacts;

import java.util.List;

public class SearchActivity extends BaseActivity {
    private EditText et_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initEditText();
    }

    /**
     * 初始化EditText
     */
    public void initEditText() {
        et_search = (EditText) findViewById(R.id.et_search);
//        String key_word=et_search.getText().toString().trim();
        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    // 当按了搜索之后关闭软键盘
                    ((InputMethodManager) et_search.getContext().getSystemService(
                            Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                            SearchActivity.this.getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
//                    List<MContacts> contactsList = MyChatApplication.getInstance().getMContactsDataList();
//                    for(MContacts contacts: contactsList){
//                        if(key_word.equals(contacts.getUsername())){
//
//                        }
//                    }
                    Toast.makeText(SearchActivity.this, "搜到了用户：", Toast.LENGTH_LONG).show();
                    return true;
                }
                return false;
            }
        });

    }

    @Override
    public void setUpTitle() {
        setToolbarTitle("搜索");
    }

    @Override
    public void setUpHomeEnable() {
        setHomeUpEnable(true);
    }

    @Override
    public void setUpNavigationIcon() {

    }
}
