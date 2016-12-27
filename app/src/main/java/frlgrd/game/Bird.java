package frlgrd.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.view.View;

class Bird extends GameObject {

	private final float gravity;
	private float velocity = 0;
	private float lift;
	private int y;
	private int x;
	private float size;

	private Bitmap matt;
	private RectF hitBox;
	private Matrix matrix = new Matrix();

	private boolean hasLifted = false;
	private BottomTouchListener bottomTouchListener;

	Bird(View parent) {
		super(parent);
		x = parent.getWidth() / 4;
		y = (int) (parent.getHeight() * .75F);
		size = parent.getWidth() / 15;
		hitBox = new RectF(x, y, x + size, y + size);
		Context context = parent.getContext();

		lift = DimensionUtils.toDp(context, 13);
		gravity = DimensionUtils.toDp(context, .5F);
		matt = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
	}

	@Override public void show(Canvas canvas) {
		matrix.setTranslate(x - size / 2, y - size / 2);
		canvas.drawBitmap(matt, matrix, null);
	}

	@Override public void update() {
		if (!hasLifted) return;
		velocity += gravity;
		if (velocity < -lift) {
			velocity = -lift;
		}

		y += velocity;

		if (y < parent.getTop()) {
			y = parent.getTop();
			velocity = 0;
		}
		if (y + size > parent.getBottom()) {
			y = (int) (parent.getBottom() - size);
			velocity = 0;
			if (bottomTouchListener != null) {
				bottomTouchListener.onBottomTouched();
			}
		}
		hitBox.top = y;
		hitBox.bottom = y + size;
	}

	@Override boolean isOutOfView(View parent) {
		return false;
	}

	void lift() {
		hasLifted = true;
		velocity -= gravity * lift;
	}

	void setBottomTouchListener(BottomTouchListener bottomTouchListener) {
		this.bottomTouchListener = bottomTouchListener;
	}

	RectF getHitBox() {
		return hitBox;
	}

	interface BottomTouchListener {
		void onBottomTouched();
	}
}