package frlgrd.game;

import android.content.Context;
import android.util.TypedValue;

class DimensionUtils {
	static float toDp(Context context, float value) {
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, context.getResources().getDisplayMetrics());
	}
}
