package uz.rajabiy.yunus.mypuzzle;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by User on 12/9/2017.
 */

public class SecondActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView timerTextView;
    private Button[][] buttons = new Button[4][4];
    private List<Integer> numberList = new ArrayList<>();
    private boolean started = false;
    private Handler handler;
    private long startMS;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        LinearLayout buttonsContainer = findViewById(R.id.buttonsContainer);
        timerTextView = findViewById(R.id.timerTextView);
        handler = new Handler();
        for (int i = 0; i < 4; i++) {
            LinearLayout rows = (LinearLayout) buttonsContainer.getChildAt(i);
            for (int j = 0; j < 4; j++) {
                Button button = (Button) rows.getChildAt(j);
                buttons[i][j] = button;
                button.setTextSize(20);
                button.setOnClickListener(this);
            }
        }

        for (int i = 1; i <= 15; i++) {
            numberList.add(i);
        }
        restartGame();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mene_second_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_restart: {
                restartGame();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (!started) {
            started = true;
            startTime();
        }
        handleGameButton((Button) v);

        if (GameIsFinished()) {
            Toast.makeText(this, "YOU WIN!!!", Toast.LENGTH_SHORT).show();
            stop();
            started = false;
        }
    }

    private void startTime() {
        startMS = System.currentTimeMillis();
        handler.post(timerTask);
    }

    public void handleGameButton(Button button) {
        int x = 0, y = 0;
        row:
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (buttons[i][j] == button) {
                    x = i;
                    y = j;
                    break row;
                }

            }
        }
        if (x > 0 && buttons[x - 1][y].getText().toString().isEmpty()) {
            buttons[x - 1][y].setText(button.getText());
            button.setText("");
            return;
        }
        if (x < 3 && buttons[x + 1][y].getText().toString().isEmpty()) {
            buttons[x + 1][y].setText(button.getText());
            button.setText("");
            return;
        }
        if (y > 0 && buttons[x][y - 1].getText().toString().isEmpty()) {
            buttons[x][y - 1].setText(button.getText());
            button.setText("");
            return;
        }
        if (y < 3 && buttons[x][y + 1].getText().toString().isEmpty()) {
            buttons[x][y + 1].setText(button.getText());
            button.setText("");

        }

    }

    private void enableButtons(boolean enabled) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                buttons[i][j].setEnabled(enabled);
            }
        }
    }

    private void restartGame() {
        enableButtons(true);
        Collections.shuffle(numberList);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (i == 3 && j == 3) continue;
                buttons[i][j].setText(String.valueOf(numberList.get(4 * i + j)));
            }
        }
        buttons[3][3].setText("");
        startMS = 0L;
        timerTextView.setText(String.valueOf("00:00"));
        startMS = System.currentTimeMillis();
        startTime();


    }

    private boolean GameIsFinished() {
        int k = 1;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (i == 3 && j == 3) continue;
                if (buttons[i][j].getText().toString().isEmpty()) return false;
                if (Integer.parseInt(buttons[i][j].getText().toString()) != k) {
                    return false;
                }
                k++;
            }
        }
        enableButtons(false);
        return true;
    }

    private Runnable timerTask = new Runnable() {
        @Override
        public void run() {

            long ellapsedMs = System.currentTimeMillis() - startMS;
            long min = ellapsedMs / 60_000;
            long sec = ellapsedMs % 60_000 / 1000;
            timerTextView.setText(String.format("%02d:%02d", min, sec));
            System.currentTimeMillis();
            handler.postDelayed(timerTask, 1000);

        }
    };

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public void stop() {
        handler.removeCallbacks(timerTask);
    }
}
