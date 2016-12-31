package frlgrd.game;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

public class MainActivity extends AppCompatActivity {
	private static final String BEST_SCORE = "BEST_SCORE";
	private TextView scoreLabel, bestScore;
	private Vibrator vibrator;
	private SharedPreferences preferences;

	private MediaPlayer mediaPlayer;
	private boolean paused = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		preferences = getPreferences(Context.MODE_PRIVATE);
		ImageView background = (ImageView) findViewById(R.id.background);
		GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(background);
		Glide.with(this).load(R.raw.laura).into(imageViewTarget);
		scoreLabel = (TextView) findViewById(R.id.score);
		bestScore = (TextView) findViewById(R.id.bestScore);
		bestScore.setText(String.valueOf(getBestScore()));

		mediaPlayer = MediaPlayer.create(this, R.raw.sound);

		GameView gameView = (GameView) findViewById(R.id.game);
		gameView.setGameEventListener(new GameView.GameEventListener() {
			@Override public void onPoint(int score) {
				if (score > 0 && !paused) {
					mediaPlayer.start();
				}
				scoreLabel.setText(String.valueOf(score));
			}

			@Override public void onFailed(int score) {
				if (!paused) {
					vibrator.vibrate(50);
				}
				if (getBestScore() < score) {
					preferences.edit().putInt(BEST_SCORE, score).apply();
					bestScore.setText(String.valueOf(score));
				}
			}
		});
	}

	private int getBestScore() {
		return preferences.getInt(BEST_SCORE, 0);
	}

	@Override protected void onDestroy() {
		mediaPlayer.stop();
		super.onDestroy();
	}

	@Override protected void onPause() {
		super.onPause();
		paused = true;
	}

	@Override protected void onResume() {
		super.onResume();
		paused = false;
	}
}
