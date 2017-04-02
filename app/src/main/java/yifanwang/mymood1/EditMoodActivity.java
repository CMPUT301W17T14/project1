package yifanwang.mymood1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

public class EditMoodActivity extends AppCompatActivity {
    private String motion;
    private String trigger;
    private User user;
    private String userName;
    private String social;
    OurMoodApplication app;
    ImageButton addImageButton;
    RadioButton button0;
    RadioButton button1;
    RadioButton button2;
    RadioButton button3;
    RadioButton button4;
    RadioButton button5;
    RadioButton button6;
    RadioButton button7;
    RadioButton button8;
    RadioButton button9;
    RadioButton button10;
    RadioButton button11;
    EditText triggerString;
    private int position;
    private ArrayList<Mood> moodArrayList;
    private Mood mood;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_mood);

        Intent intent = getIntent();
        String target = intent.getStringExtra("MOOD");

        Gson gS = new Gson();
        mood = gS.fromJson(target,Mood.class);
        Log.i("mood:",mood.toString());

        addImageButton = (ImageButton)findViewById(R.id.addImage_bt);
        button0 = (RadioButton)findViewById(R.id.anger);
        button1 = (RadioButton)findViewById(R.id.confusion);
        button2 = (RadioButton)findViewById(R.id.disgust);
        button3 = (RadioButton)findViewById(R.id.fear);
        button4 = (RadioButton)findViewById(R.id.happy);
        button5 = (RadioButton)findViewById(R.id.sad);
        button6 = (RadioButton)findViewById(R.id.shame);
        button7 = (RadioButton)findViewById(R.id.suprise);

        button8 = (RadioButton)findViewById(R.id.alone1);
        button9 = (RadioButton)findViewById(R.id.withother1);
        button10 = (RadioButton)findViewById(R.id.moreone1);
        button11 = (RadioButton)findViewById(R.id.crowd1);
        triggerString = (EditText)findViewById(R.id.triggerView1);
        //app = (OurMoodApplication) getApplication();

        if(mood.getMood().equals("angry")){
            Log.i("motion:",mood.getMood());
            button0.setChecked(true);
        }
        if(mood.getMood().equals("confused")){
            button1.setChecked(true);
        }
        if(mood.getMood().equals("disgust")){
            button2.setChecked(true);
        }
        if(mood.getMood().equals("fear")){
            button3.setChecked(true);
        }
        if(mood.getMood().equals("happy")){
            button4.setChecked(true);
        }
        if(mood.getMood().equals("sad")){
            button5.setChecked(true);
        }
        if(mood.getMood().equals("shame")){
            button6.setChecked(true);
        }
        if(mood.getMood().equals("surprise")){
            button7.setChecked(true);
        }

        if(mood.getMood().equals("alone")){
            button8.setChecked(true);
        }
        if(mood.getSocial()!=null) {
            if (mood.getSocial().equals("with one other people")) {
                button9.setChecked(true);
            }
            if (mood.getSocial().equals("with more than one people")) {
                button10.setChecked(true);
            }
            if (mood.getSocial().equals("crowd")) {
                button11.setChecked(true);
            }
        }
        if(mood.getTrigger()!=null){
            triggerString.setText(mood.getTrigger());
        }



        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EditMoodActivity.this, CameroActivity.class);
                startActivity(i);
            }
        });

        button0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                motion = "angry";
            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                motion = "confused";
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                motion = "disgust";
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                motion = "fear";
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                motion = "happy";
            }
        });
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                motion = "sad";
            }
        });
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                motion = "shame";
            }
        });
        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                motion = "surprise";
            }
        });
        button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                social = "alone";
            }
        });
        button9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                social = "with one other people";
            }
        });
        button10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                social = "with more than one people";
            }
        });
        button11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                social = "crowd";
            }
        });

        trigger = triggerString.getText().toString();

    }

    public void savemood(View view){
        userName = OurMoodApplication.getUsername();
        ElasticsearchController.GetUserTask getUserTask = new ElasticsearchController.GetUserTask();
        getUserTask.execute(userName);
        try {
            user = getUserTask.get();

        } catch (Exception e) {
            Log.i("Error", "Failed to get the User out of the async object");
        }

        //Toast.makeText(this, reason, Toast.LENGTH_SHORT).show();
        moodArrayList = user.getMoodlist();
        for(int i=0;i<moodArrayList.size();i++){
            //Log.i("moodtest",moodArrayList.get(i).toString());
            if(moodArrayList.get(i).toString().equals(mood.toString())){
                moodArrayList.remove(i);
                if (motion!=null){
                    mood.setMood(motion);
                }
                if(trigger!=null) {
                    mood.setTrigger(trigger);
                }
                if(social!=null) {
                    mood.setSocial(social);
                }
                moodArrayList.add(mood);
                ElasticsearchController.AddUserTask addUserTask = new ElasticsearchController.AddUserTask();
                addUserTask.execute(user);
            }
            else{
                Log.i("Error","not working");
            }

        }
        finish();
    }

    public void findlocation(View view){
        Intent intent = new Intent(this,SeeMapActivity.class);
        startActivity(intent);

    }
}
