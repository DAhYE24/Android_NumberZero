package dh_kang.nozero;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

public class ActiStory extends AppCompatActivity {
    ImageView as_imgStory;
    int cnt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acti_story);

        as_imgStory = (ImageView)findViewById(R.id.as_imgStory);

        as_imgStory.setOnTouchListener(new OnSwipeTouchListener(ActiStory.this) {
            public void onSwipeTop() {
            }
            public void onSwipeRight() {
                switch(cnt){
                    case 2:
                        as_imgStory.setBackgroundResource(R.drawable.sample_content2);
                        cnt--;
                        break;
                    case 1:
                        as_imgStory.setBackgroundResource(R.drawable.sample_content1);
                        cnt--;
                        break;
                    case 0:
                        as_imgStory.setBackgroundResource(R.drawable.smaple_contentlogo);
                        break;
                    default:
                        break;
                }
            }
            public void onSwipeLeft() {
                switch(cnt){
                    case 0:
                        as_imgStory.setBackgroundResource(R.drawable.sample_content1);
                        cnt++;
                        break;
                    case 1:
                        as_imgStory.setBackgroundResource(R.drawable.sample_content2);
                        cnt++;
                        break;
                    case 2:
                        as_imgStory.setBackgroundResource(R.drawable.sample_content3);
                        break;
                    default:
                        break;
                }

            }
            public void onSwipeBottom() {
            }

        });
    }
}
