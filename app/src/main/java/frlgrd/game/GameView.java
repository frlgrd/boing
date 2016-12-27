package frlgrd.game;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameView extends View implements View.OnClickListener, Bird.BottomTouchListener {
	private Bird bird;
	private List<GameObject> gameObjects = new ArrayList<>();
	private int frameCount = 1;
	private int score = 0;
	private GameEventListener gameEventListener;

	public GameView(Context context) {
		super(context);
		init();
	}

	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	public void setGameEventListener(GameEventListener gameEventListener) {
		this.gameEventListener = gameEventListener;
	}

	private void init() {
		setOnClickListener(this);
	}

	@Override protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (bird == null) {
			bird = new Bird(this);
			bird.setBottomTouchListener(this);
			gameObjects.add(bird);
			gameObjects.add(new Pipe(this));
		}
		if (frameCount % 150 == 0) {
			frameCount = 1;
			gameObjects.add(new Pipe(this));
		}

		for (Iterator<GameObject> iterator = gameObjects.iterator(); iterator.hasNext(); ) {
			GameObject go = iterator.next();
			go.show(canvas);
			go.update();
			if (go instanceof Pipe) {
				Pipe pipe = (Pipe) go;
				if (pipe.isOutOfView(this)) {
					iterator.remove();
				} else if (pipe.isBirdTouching(bird)) {
					pipeTouched(pipe);

				} else if (pipe.winPoint(bird)) {
					scoreUp();
				}
			}
		}
		frameCount++;
		invalidate();
	}

	private void pipeTouched(Pipe pipe) {
		pipe.wasTouched();
		if (gameEventListener != null) {
			gameEventListener.onFailed(score);
			resetScore();
		}
	}

	private void resetScore() {
		score = 0;
		gameEventListener.onPoint(score);
	}

	private void scoreUp() {
		score++;
		if (gameEventListener != null) {
			gameEventListener.onPoint(score);
		}
	}

	@Override public void onClick(View v) {
		if (bird != null) {
			bird.lift();
		}
	}

	@Override public void onBottomTouched() {
		resetScore();
	}

	interface GameEventListener {
		void onPoint(int score);

		void onFailed(int score);
	}
}
