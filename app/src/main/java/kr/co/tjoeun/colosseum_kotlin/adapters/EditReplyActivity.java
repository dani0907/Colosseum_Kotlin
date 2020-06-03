package kr.co.tjoeun.colosseum_kotlin.adapters;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.json.JSONObject;

import kr.co.tjoeun.colosseum_kotlin.BaseActivity;
import kr.co.tjoeun.colosseum_kotlin.R;
import kr.co.tjoeun.colosseum_kotlin.databinding.ActivityEditReplyBinding;
import kr.co.tjoeun.colosseum_kotlin.utils.ServerUtil;

public class EditReplyActivity extends BaseActivity {

    String topicTitle;
    String mySideTitle;
    int topicId;

    ActivityEditReplyBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_edit_reply);
        setupEvents();
        setValues();
    }

    @Override
    public void setupEvents() {

        binding.postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = binding.contentExt.getText().toString();
                ServerUtil.postRequestReply(mContext, topicId, input, new ServerUtil.JsonResponseHandler() {
                    @Override
                    public void onResponse(JSONObject json) {
                        Log.d("댓글 달기 응답.",json.toString());
                    }
                });
            }
        });

    }

    @Override
    public void setValues() {

        topicTitle = getIntent().getStringExtra("topicTitle");
        binding.topicTitleTxt.setText(topicTitle);

        mySideTitle = getIntent().getStringExtra("sideTitle");
        binding.sideTitleTxt.setText(mySideTitle);

        topicId = getIntent().getIntExtra("topicId",-1);

    }
}
