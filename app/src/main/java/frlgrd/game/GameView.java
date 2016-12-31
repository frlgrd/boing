package frlgrd.game;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameView extends View implements View.OnClickListener, Matt.BottomTouchListener {
	private Matt matt;
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
		if (matt == null) {
			matt = new Matt(this);
			matt.setBottomTouchListener(this);
			gameObjects.add(matt);
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
				} else if (pipe.isMattTouching(matt)) {
					pipeTouched(pipe);

				} else if (pipe.winPoint(matt)) {
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
		if (matt != null) {
			matt.lift();
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
