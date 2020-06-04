package kr.co.tjoeun.colosseum_kotlin.adapters;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import kr.co.tjoeun.colosseum_kotlin.BaseActivity;
import kr.co.tjoeun.colosseum_kotlin.R;
import kr.co.tjoeun.colosseum_kotlin.databinding.ActivityEditReplyBinding;
import kr.co.tjoeun.colosseum_kotlin.utils.ServerUtil;

public class EditReplyActivity extends BaseActivity {

    String topicTitle;
    String mySideTitle;
    int topicId;

//    의견 수정시에는 이 값이 -1이 아니게 변경할 예정.
//     -1 이면 새로 등록하는 경우.
    int replyId = -1;

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
                if(replyId == -1){
                    ServerUtil.postRequestReply(mContext, topicId, input, new ServerUtil.JsonResponseHandler() {
                        @Override
                        public void onResponse(JSONObject json) {
                            Log.d("댓글 달기 응답.",json.toString());

                            try {
                                int code = json.getInt("code");
                                if(code ==200){
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(mContext, "의견이 등록되었습니다.", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    });
                                }
                                else{
                                    final String message = json.getString("message");
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
                else{
//                    댓글을 수정해야 하는 경우
                     ServerUtil.putRequestReply(mContext, replyId, input, new ServerUtil.JsonResponseHandler() {
                         @Override
                         public void onResponse(JSONObject json) {
                             Log.d("의견수정", json.toString());
                         }
                     });
                }

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

//        어느 댓글을 수정할지 id를 받아서 replyId변수에 저장.
        replyId = getIntent().getIntExtra("replyId",-1);

        if(replyId != -1){
            setTitle("의견 수정하기");
        }
        else{
            setTitle("의견 등록하기");
        }

    }
}
