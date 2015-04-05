package orienteering.album;
///////////////////////////STUDENT//////////////////////////
///////////// 姓名:林鈺璇 /////////////////////////////////
///////////// 學號:0116224 //////////////////////////////
//////////////////////////////////////////////////////////////////
import java.util.ArrayList;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class MainActivity extends Activity {

    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    OnItemClickListener itemclick = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> av, View v,
                                int position, long id) {


        }
    };
}
