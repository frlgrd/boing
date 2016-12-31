package frlgrd.game;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.content.res.ResourcesCompat;
import android.view.View;

import java.util.Random;

class Pipe extends GameObject {

	private Paint paint;

	private RectF top, bottom;

	private float speed;

	private boolean passed = false;
	private boolean touched = false;

	Pipe(View parent) {
		super(parent);
		paint = new Paint();
		paint.setStrokeWidth(0);
		paint.setColor(ResourcesCompat.getColor(parent.getResources(), R.color.pipeColor, parent.getContext().getTheme()));
		speed = 5F;
		float pipeWidth = DimensionUtils.toDp(parent.getContext(), 70);
		float space = parent.getHeight() * 0.4F;
		float yPadding = DimensionUtils.toDp(parent.getContext(), 20);
		float entryTop = getRandomEntryTop(parent.getTop() + yPadding, parent.getBottom() - yPadding - space);
		int pipeLeft = parent.getRight();
		float pipeRight = pipeLeft + pipeWidth;
		top = new RectF(
				pipeLeft,
				parent.getTop(),
				pipeRight,
				entryTop
		);
		bottom = new RectF(
				pipeLeft,
				entryTop + space,
				pipeRight,
				parent.getBottom()
		);
	}

	@Override void show(Canvas canvas) {
		canvas.drawRect(top, paint);
		canvas.drawRect(bottom, paint);
	}

	@Override void update() {
		top.left -= speed;
		top.right -= speed;
		bottom.left -= speed;
		bottom.right -= speed;
	}

	@Override boolean isOutOfView(View parent) {
		return top.right < parent.getLeft();
	}

	boolean isMattTouching(Matt matt) {
		if (touched) return false;
		boolean touching = RectF.intersects(top, matt.getHitBox()) || RectF.intersects(matt.getHitBox(), bottom);
		if (touching) {
			touched = true;
		}
		return touching;
	}

	void wasTouched() {
		paint.setColor(ResourcesCompat.getColor(parent.getResources(), R.color.touchedPipeColor, parent.getContext().getTheme()));
	}

	private float getRandomEntryTop(float min, float max) {
		return new Random().nextInt((int) ((max - min) + 1)) + min;
	}

	boolean winPoint(Matt matt) {
		if (touched) return false;
		boolean winPoint = false;
		if (top.right < matt.getHitBox().left && !passed) {
			winPoint = true;
			passed = true;
		}
		return winPoint;
	}
}
