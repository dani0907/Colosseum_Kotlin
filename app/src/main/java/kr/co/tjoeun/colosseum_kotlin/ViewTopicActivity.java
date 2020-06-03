package kr.co.tjoeun.colosseum_kotlin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import kr.co.tjoeun.colosseum_kotlin.adapters.TopicReplyAdapter;
import kr.co.tjoeun.colosseum_kotlin.databinding.ActivityViewTopicBinding;
import kr.co.tjoeun.colosseum_kotlin.datas.Topic;
import kr.co.tjoeun.colosseum_kotlin.utils.ServerUtil;

public class ViewTopicActivity extends BaseActivity {

    ActivityViewTopicBinding binding ;
    int topicId;
    Topic mTopic;
    TopicReplyAdapter mTopicReplyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_view_topic);
        setupEvents();
        setValues();
    }

    @Override
    public void setupEvents() {

        binding.voteToFirstSideBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServerUtil.postRequestVote(mContext, mTopic.getSideList().get(0).getId(), new ServerUtil.JsonResponseHandler() {
                    @Override
                    public void onResponse(JSONObject json) {
                        Log.d("투표응답",json.toString());

                        try {
                            int code = json.getInt("code");
                            if(code ==200){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(mContext, "참여해 주셔서 감사합니다.", Toast.LENGTH_SHORT).show();
                                        getTopicFromServer();
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
        });
        binding.voteToSecondSideBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServerUtil.postRequestVote(mContext, mTopic.getSideList().get(1).getId(), new ServerUtil.JsonResponseHandler() {
                    @Override
                    public void onResponse(JSONObject json) {
                        Log.d("투표응답",json.toString());

                        try {
                            int code = json.getInt("code");
                            if(code ==200){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(mContext, "참여해 주셔서 감사합니다.", Toast.LENGTH_SHORT).show();
                                        getTopicFromServer();
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
        });

    }
    @Override
    public void setValues() {


        topicId = getIntent().getIntExtra("topic_id", -1);

        if(topicId ==-1){
//            TODO - 주제가 제대로 못넘어왔다
            Toast.makeText(mContext, "잘못된 접근입니다.", Toast.LENGTH_SHORT).show();
            finish();
        }

       getTopicFromServer();
        
    }

    void getTopicFromServer(){
        ServerUtil.getRequestTopicById(mContext, topicId, new ServerUtil.JsonResponseHandler() {
            @Override
            public void onResponse(JSONObject json) {
                Log.d("토픽 상세 정보", json.toString());

                try {
                    JSONObject data = json.getJSONObject("data");
                    JSONObject topic = data.getJSONObject("topic");

                    mTopic = Topic.getTopicFromJson(topic);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setTopicvaluesToUi();
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    void setTopicvaluesToUi(){
        binding.topicTitleTxt.setText(mTopic.getTitle());
        Glide.with(mContext).load(mTopic.getImageUrl()).into(binding.topicImg);

        binding.firstSideTitleTxt.setText(mTopic.getSideList().get(0).getTitle());
        binding.secondSideTitleTxt.setText(mTopic.getSideList().get(1).getTitle());

        binding.firstSideVoteCountTxt.setText(String.format("%,d명",mTopic.getSideList().get(0).getVoteCount()));
        binding.secondSideVoteCountTxt.setText(String.format("%,d명",mTopic.getSideList().get(1).getVoteCount()));

        int[] ids = new int[2];
        for(int i=0; i<ids.length; i++){
            ids[i] = mTopic.getSideList().get(i).getId();
        }

        mTopicReplyAdapter = new TopicReplyAdapter(mContext,R.layout.topic_reply_list_item, mTopic.getReplyList(),ids);
        binding.replyListView.setAdapter(mTopicReplyAdapter);

    }
}
