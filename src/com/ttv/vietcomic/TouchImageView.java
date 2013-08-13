package com.ttv.vietcomic;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;

public class TouchImageView extends ImageView {
	public interface OnBitmapChangedListener {

		void onBitmapChanged(Drawable drawable);
	};

	Matrix matrix;

	// We can be in one of these 3 states
	static final int NONE = 0;
	static final int DRAG = 1;
	static final int ZOOM = 2;
	static final float MIN_ZOOM = 1f;
	int mode = NONE;

	// Remember some things for zooming
	PointF last = new PointF();
	PointF start = new PointF();
	protected float minScale = 1f;
	protected float maxScale = 3f;
	protected float[] m;
	protected Matrix mBaseMatrix = new Matrix();
	protected Matrix mSuppMatrix = new Matrix();
	protected final Matrix mDisplayMatrix = new Matrix();
	protected int viewWidth, viewHeight;
	protected static final int CLICK = 3;
	protected float saveScale = 1f;
	protected float mCurrentScaleFactor;
	protected float mScaleFactor = maxScale / 2f;
	protected float origWidth, origHeight;
	protected int oldMeasuredWidth, oldMeasuredHeight;
	protected int mDoubleTapDirection;
	protected int mThisWidth = -1, mThisHeight = -1;
	public Handler mHandler = new Handler();

	protected RectF mBitmapRect = new RectF();
	protected RectF mCenterRect = new RectF();
	protected RectF mScrollRect = new RectF();
	ScaleGestureDetector mScaleDetector;

	GestureDetector mGestureDetector;

	Context context;

	public TouchImageView(Context context) {
		super(context);
		mDoubleTapDirection = 1;
		mCurrentScaleFactor = 1f;
		sharedConstructing(context);

	}

	public TouchImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		sharedConstructing(context);
	}

	private void sharedConstructing(Context context) {
		super.setClickable(true);
		this.context = context;
		mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
		mGestureDetector = new GestureDetector(context,
				new TapToScreenListener());
		matrix = new Matrix();
		m = new float[9];
		setImageMatrix(matrix);
		setScaleType(ScaleType.MATRIX);

		setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				mScaleDetector.onTouchEvent(event);
				mGestureDetector.onTouchEvent(event);
				PointF curr = new PointF(event.getX(), event.getY());

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					last.set(curr);
					start.set(last);
					mode = DRAG;
					break;

				case MotionEvent.ACTION_MOVE:
					if (mode == DRAG) {
						float deltaX = curr.x - last.x;
						float deltaY = curr.y - last.y;
						float fixTransX = getFixDragTrans(deltaX, viewWidth,
								origWidth * saveScale);
						float fixTransY = getFixDragTrans(deltaY, viewHeight,
								origHeight * saveScale);
						matrix.postTranslate(fixTransX, fixTransY);
						fixTrans();
						last.set(curr.x, curr.y);
					}
					break;

				case MotionEvent.ACTION_UP:
					mode = NONE;
					int xDiff = (int) Math.abs(curr.x - start.x);
					int yDiff = (int) Math.abs(curr.y - start.y);
					if (xDiff < CLICK && yDiff < CLICK)
						performClick();
					break;

				case MotionEvent.ACTION_POINTER_UP:
					mode = NONE;
					break;
				}

				setImageMatrix(matrix);
				invalidate();
				return true; // indicate event was handled
			}

		});
	}

	public void setMaxZoom(float x) {
		maxScale = x;
	}

	protected void onZoom(float scale) {
		mCurrentScaleFactor = scale;
	}

	protected float onDoubleTapPost(float scale, float maxZoom) {
		if (mDoubleTapDirection == 1) {
			mDoubleTapDirection = -1;
			// if ((scale + (mScaleFactor * 2)) <= maxZoom) {
			// return scale + mScaleFactor;
			// } else {
			return maxZoom;
			// }
		} else {
			mDoubleTapDirection = 1;
			return 1f;
		}
	}

	private class ScaleListener extends
			ScaleGestureDetector.SimpleOnScaleGestureListener {
		@Override
		public boolean onScaleBegin(ScaleGestureDetector detector) {
			mode = ZOOM;
			return true;
		}

		@Override
		public boolean onScale(ScaleGestureDetector detector) {
			float mScaleFactor = detector.getScaleFactor();
			float origScale = saveScale;
			saveScale *= mScaleFactor;
			if (saveScale > maxScale) {
				saveScale = maxScale;
				mScaleFactor = maxScale / origScale;
			} else if (saveScale < minScale) {
				saveScale = minScale;
				mScaleFactor = minScale / origScale;
			}

			if (origWidth * saveScale <= viewWidth
					|| origHeight * saveScale <= viewHeight)
				matrix.postScale(mScaleFactor, mScaleFactor, viewWidth / 2,
						viewHeight / 2);
			else
				matrix.postScale(mScaleFactor, mScaleFactor,
						detector.getFocusX(), detector.getFocusY());
			fixTrans();
			return true;
		}

	}

	private class TapToScreenListener extends
			GestureDetector.SimpleOnGestureListener {

		// zoom when double click
		@Override
		public boolean onDoubleTap(MotionEvent e) {
			Log.d("touch image view", "double click");
			float scale = getScale();
			float targetScale = scale;
			targetScale = onDoubleTapPost(scale, getMaxZoom());
			targetScale = Math.min(getMaxZoom(),
					Math.max(targetScale, MIN_ZOOM));
			mCurrentScaleFactor = targetScale;
			zoomTo(targetScale, e.getX(), e.getY());
			invalidate();
			return true;
		}

		// protected void zoomTo(float scale) {
		// float cx = getWidth() / 2F;
		// float cy = getHeight() / 2F;
		// zoomTo(scale, cx, cy);
		// }

		private void zoomTo(float scale, float centerX, float centerY) {
			// Log.i(LOG_TAG, "zoomTo");

			if (scale > maxScale)
				scale = maxScale;
			float oldScale = saveScale;
			float deltaScale = scale / oldScale;
			postScale(deltaScale, centerX, centerY);
			onZoom(saveScale);
			center(true, true);
		}

		private void zoomTo(float scale, final float centerX,
				final float centerY, final float durationMs) {
			// Log.i( LOG_TAG, "zoomTo: " + scale + ", " + centerX + ": " +
			// centerY
			// );
			final long startTime = System.currentTimeMillis();
			final float incrementPerMs = (scale - getScale()) / durationMs;
			final float oldScale = getScale();
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					long now = System.currentTimeMillis();
					float currentMs = Math.min(durationMs, now - startTime);
					float target = oldScale + (incrementPerMs * currentMs);
					zoomTo(target, centerX, centerY);
					if (currentMs < durationMs) {
						mHandler.post(this);
					} else {
						// if ( getScale() < 1f ) {}
					}
				}
			});
		}

		@Override
		// single touch to hide and show top layout
		public boolean onSingleTapConfirmed(MotionEvent e) {
			Log.d("touch image view", "single click");
			if (ReadingViewActivity.isShowbar) {
				Log.d("TAG", "touch to hide bar. isshow ====="
						+ ReadingViewActivity.isShowbar);
				ReadingViewActivity.top.setVisibility(View.INVISIBLE);
				ReadingViewActivity.botton.setVisibility(View.INVISIBLE);
				ReadingViewActivity.isShowbar = false;
			} else {
				Log.d("TAG", "touch to show bar, isshow ====="
						+ ReadingViewActivity.isShowbar);
				ReadingViewActivity.top.setVisibility(View.VISIBLE);
				ReadingViewActivity.botton.setVisibility(View.VISIBLE);
				ReadingViewActivity.isShowbar = true;
			}
			return false;
		}
	}

	void fixTrans() {
		matrix.getValues(m);
		float transX = m[Matrix.MTRANS_X];
		float transY = m[Matrix.MTRANS_Y];

		float fixTransX = getFixTrans(transX, viewWidth, origWidth * saveScale);
		float fixTransY = getFixTrans(transY, viewHeight, origHeight
				* saveScale);

		if (fixTransX != 0 || fixTransY != 0)
			matrix.postTranslate(fixTransX, fixTransY);
	}

	float getFixTrans(float trans, float viewSize, float contentSize) {
		float minTrans, maxTrans;

		if (contentSize <= viewSize) {
			minTrans = 0;
			maxTrans = viewSize - contentSize;
		} else {
			minTrans = viewSize - contentSize;
			maxTrans = 0;
		}

		if (trans < minTrans)
			return -trans + minTrans;
		if (trans > maxTrans)
			return -trans + maxTrans;
		return 0;
	}

	float getFixDragTrans(float delta, float viewSize, float contentSize) {
		if (contentSize <= viewSize) {
			return 0;
		}
		return delta;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		viewWidth = MeasureSpec.getSize(widthMeasureSpec);
		viewHeight = MeasureSpec.getSize(heightMeasureSpec);

		//
		// Rescales image on rotation
		//
		if (oldMeasuredHeight == viewWidth && oldMeasuredHeight == viewHeight
				|| viewWidth == 0 || viewHeight == 0)
			return;
		oldMeasuredHeight = viewHeight;
		oldMeasuredWidth = viewWidth;

		if (saveScale == 1) {
			// Fit to screen.

			Drawable drawable = getDrawable();
			if (drawable == null || drawable.getIntrinsicWidth() == 0
					|| drawable.getIntrinsicHeight() == 0)
				return;
			int bmWidth = drawable.getIntrinsicWidth();
			int bmHeight = drawable.getIntrinsicHeight();

			Log.d("bmSize", "bmWidth: " + bmWidth + " bmHeight : " + bmHeight);

			float scaleX = (float) viewWidth / (float) bmWidth;
			float scaleY = (float) viewHeight / (float) bmHeight;
			matrix.setScale(scaleX, scaleY);

			// Center the image
			float redundantYSpace = 0;/*
									 * (float) viewHeight- (scale * (float)
									 * bmHeight);
									 */
			float redundantXSpace = 0;/*
									 * (float) viewWidth - (scale * (float)
									 * bmWidth);
									 */

			redundantYSpace /= (float) 2;
			redundantXSpace /= (float) 2;

			matrix.postTranslate(redundantXSpace, redundantYSpace);

			origWidth = viewWidth - 2 * redundantXSpace;
			origHeight = viewHeight - 2 * redundantYSpace;
			setImageMatrix(matrix);
		}
		fixTrans();
	}


	protected float maxZoom() {
		final Drawable drawable = getDrawable();

		if (drawable == null) {
			return 1F;
		}

		float fw = (float) drawable.getIntrinsicWidth() / (float) mThisWidth;
		float fh = (float) drawable.getIntrinsicHeight() / (float) mThisHeight;
		float max = Math.max(fw, fh) * 8;
		return max;
	}

	public float getMaxZoom() {
		return maxScale;
	}

	private Matrix getImageViewMatrix() {
		mDisplayMatrix.set(mBaseMatrix);
		mDisplayMatrix.postConcat(mSuppMatrix);
		return mDisplayMatrix;
	}

	public Matrix getDisplayMatrix() {
		return new Matrix(mSuppMatrix);
	}

	protected float getValue(Matrix matrix, int whichValue) {
		matrix.getValues(m);
		return m[whichValue];
	}

	protected RectF getBitmapRect() {
		final Drawable drawable = getDrawable();

		if (drawable == null)
			return null;
		Matrix m = getImageViewMatrix();
		mBitmapRect.set(0, 0, drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight());
		m.mapRect(mBitmapRect);
		return mBitmapRect;
	}

	protected float getScale(Matrix matrix) {
		return getValue(matrix, Matrix.MSCALE_X);
	}

	public float getScale() {
		return getScale(mSuppMatrix);
	}

	protected void center(boolean horizontal, boolean vertical) {
		// Log.i(LOG_TAG, "center");
		final Drawable drawable = getDrawable();

		if (drawable == null)
			return;
		RectF rect = getCenter(horizontal, vertical);
		if (rect.left != 0 || rect.top != 0) {
			postTranslate(rect.left, rect.top);
		}
	}

	protected RectF getCenter(boolean horizontal, boolean vertical) {
		final Drawable drawable = getDrawable();

		if (drawable == null)
			return new RectF(0, 0, 0, 0);

		RectF rect = getBitmapRect();
		float height = rect.height();
		float width = rect.width();
		float deltaX = 0, deltaY = 0;
		if (vertical) {
			int viewHeight = getHeight();
			if (height < viewHeight) {
				deltaY = (viewHeight - height) / 2 - rect.top;
			} else if (rect.top > 0) {
				deltaY = -rect.top;
			} else if (rect.bottom < viewHeight) {
				deltaY = getHeight() - rect.bottom;
			}
		}
		if (horizontal) {
			int viewWidth = getWidth();
			if (width < viewWidth) {
				deltaX = (viewWidth - width) / 2 - rect.left;
			} else if (rect.left > 0) {
				deltaX = -rect.left;
			} else if (rect.right < viewWidth) {
				deltaX = viewWidth - rect.right;
			}
		}
		mCenterRect.set(deltaX, deltaY, 0, 0);
		return mCenterRect;
	}

	protected void postTranslate(float deltaX, float deltaY) {
		mSuppMatrix.postTranslate(deltaX, deltaY);
		setImageMatrix(getImageViewMatrix());
	}

	protected void postScale(float scale, float centerX, float centerY) {
		mSuppMatrix.postScale(scale, scale, centerX, centerY);
		setImageMatrix(getImageViewMatrix());
	}

	protected void zoomTo(float scale, float centerX, float centerY) {
		// Log.i(LOG_TAG, "zoomTo");

		if (scale > maxScale)
			scale = maxScale;
		float oldScale = getScale();
		float deltaScale = scale / oldScale;
		postScale(deltaScale, centerX, centerY);
		onZoom(getScale());
		center(true, true);
	}

	public void zoomTo(float scale, final float centerX, final float centerY,
			final float durationMs) {
		// Log.i( LOG_TAG, "zoomTo: " + scale + ", " + centerX + ": " + centerY
		// );
		final long startTime = System.currentTimeMillis();
		final float incrementPerMs = (scale - getScale()) / durationMs;
		final float oldScale = getScale();
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				long now = System.currentTimeMillis();
				float currentMs = Math.min(durationMs, now - startTime);
				float target = oldScale + (incrementPerMs * currentMs);
				zoomTo(target, centerX, centerY);
				if (currentMs < durationMs) {
					mHandler.post(this);
				} else {
					// if ( getScale() < 1f ) {}
				}
			}
		});
	}

}
