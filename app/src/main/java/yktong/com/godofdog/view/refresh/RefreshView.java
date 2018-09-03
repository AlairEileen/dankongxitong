package yktong.com.godofdog.view.refresh;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrUIHandler;
import in.srain.cube.views.ptr.indicator.PtrIndicator;
import in.srain.cube.views.ptr.indicator.PtrTensionIndicator;

/**
 * Created by vampire on 2017/7/26.
 */

public class RefreshView extends View implements PtrUIHandler {

    private RefreshDrawable mDrawable;
    private PtrFrameLayout mPtrFrameLayout;
    private PtrTensionIndicator mPtrTensionIndicator;

    public RefreshView(Context context) {
        super(context);
        init();
    }

    public RefreshView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RefreshView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setUp(PtrFrameLayout ptrFrameLayout) {
        mPtrFrameLayout = ptrFrameLayout;
        mPtrTensionIndicator = new PtrTensionIndicator();
        mPtrFrameLayout.setPtrIndicator(mPtrTensionIndicator);
    }

    private void init() {
        mDrawable = new RefreshDrawable(this,getContext());
    }

    @Override
    public void onUIReset(PtrFrameLayout frame) {
        mDrawable.resetOriginals();
    }

    @Override
    public void onUIRefreshPrepare(PtrFrameLayout frame) {
    }

    @Override
    public void onUIRefreshBegin(PtrFrameLayout frame) {
        mDrawable.start();
        float percent = mPtrTensionIndicator.getOverDragPercent();
        mDrawable.offsetTopAndBottom(mPtrTensionIndicator.getCurrentPosY());
        mDrawable.setPercent(percent);
        invalidate();
    }

    @Override
    public void onUIRefreshComplete(PtrFrameLayout frame, boolean isHeader) {
        float percent = mPtrTensionIndicator.getOverDragPercent();
        mDrawable.stop();
        mDrawable.offsetTopAndBottom(mPtrTensionIndicator.getCurrentPosY());
        mDrawable.setPercent(percent);
        invalidate();
    }

    @Override
    public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {
        float percent = mPtrTensionIndicator.getOverDragPercent();
        mDrawable.offsetTopAndBottom(mPtrTensionIndicator.getCurrentPosY());
        mDrawable.setPercent(percent);
        invalidate();
    }

    @Override
    public void invalidateDrawable(@NonNull Drawable drawable) {
        if (drawable == mDrawable) {
            invalidate();
        } else {
            super.invalidateDrawable(drawable);
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = mDrawable.getTotalDragDistance() * 5 / 4;
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height + getPaddingTop() + getPaddingBottom(), MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int pl = getPaddingLeft();
        int pt = getPaddingTop();
        mDrawable.setBounds(pl, pt, pl + right - left, pt + bottom - top);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mDrawable.draw(canvas);
        float percent = mPtrTensionIndicator.getOverDragPercent();
    }
}
