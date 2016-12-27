package frlgrd.game;

import android.graphics.Canvas;
import android.view.View;

abstract class GameObject {

	View parent;

	GameObject(View parent) {
		this.parent = parent;
	}

	abstract void show(Canvas canvas);

	abstract void update();

	abstract boolean isOutOfView(View parent);
}
