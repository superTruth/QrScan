package cn.truth.ibdrqlib.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/***************************************************************************************************
 *                                  Copyright (C), Truth.                                      *
 *                                                                              *
 ***************************************************************************************************
 * usage           :
 * Version         : 1
 * Author          : Truth
 * Date            : 2018/8/6
 * Modify          : create file
 **************************************************************************************************/
public class FinderView extends View implements IFinder{
    public FinderView(Context context) {
        super(context);
        init();
    }

    public FinderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FinderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public FinderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private Paint paint;
    private void init(){
        paint = new Paint();
    }

    private Rect finderLocationRect;
    private Rect frame;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        int width = getWidth();
        int height = getHeight();

        int frameWith = Math.min(width, height)*3/4/2;

        frame = new Rect(width/2-frameWith, height/2-frameWith, width/2+frameWith, height/2+frameWith);

        int[] location = new int[2];
        getLocationOnScreen(location);

        finderLocationRect = new Rect(location[0]+frame.left, location[1]+frame.top,
                location[0]+frame.right, location[1]+frame.bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int maskColor = Color.parseColor("#80000000");

        // get widget size
        int width = canvas.getWidth();
        int height = canvas.getHeight();

//        int framWith = width*3/4/2;
//        Rect frame = new Rect(width/2-framWith, height/2-framWith, width/2+framWith, height/2+framWith);

        // draw scan box
        paint.setColor(maskColor);
        canvas.drawRect(0, 0, width, frame.top, paint); //上
        canvas.drawRect(0, frame.top, frame.left, frame.bottom, paint);  //左
        canvas.drawRect(frame.right, frame.top, width, frame.bottom, paint); //右
        canvas.drawRect(0, frame.bottom, width, height, paint);

        // draw box conner
        paint.setColor(Color.GREEN);
        int conerWith = 6;
        int connerLen = 60;
        canvas.drawRect(frame.left-conerWith, frame.top-conerWith, frame.left, frame.top+connerLen, paint);
        canvas.drawRect(frame.left-conerWith, frame.top-conerWith, frame.left+connerLen, frame.top, paint);

        canvas.drawRect(frame.right, frame.top-conerWith, frame.right+conerWith, frame.top+connerLen, paint);
        canvas.drawRect(frame.right-connerLen, frame.top-conerWith, frame.right+conerWith, frame.top, paint);

        canvas.drawRect(frame.left-conerWith, frame.bottom-connerLen, frame.left, frame.bottom+conerWith, paint);
        canvas.drawRect(frame.left-conerWith, frame.bottom, frame.left+connerLen, frame.bottom+conerWith, paint);

        canvas.drawRect(frame.right, frame.bottom-connerLen, frame.right+conerWith, frame.bottom+conerWith, paint);
        canvas.drawRect(frame.right-connerLen, frame.bottom, frame.right+conerWith, frame.bottom+conerWith, paint);

        // draw grid
        paint.setColor(Color.LTGRAY);
        int LinWith = 1;
        canvas.drawRect(frame.left, frame.top+frame.height()/3, frame.right, frame.top+frame.height()/3+LinWith, paint);
        canvas.drawRect(frame.left, frame.top+frame.height()*2/3, frame.right, frame.top+frame.height()*2/3+LinWith, paint);
        canvas.drawRect(frame.left+frame.width()/3, frame.top, frame.left+frame.width()/3+LinWith, frame.bottom, paint);
        canvas.drawRect(frame.left+frame.width()*2/3, frame.top, frame.left+frame.width()*2/3+LinWith, frame.bottom, paint);
    }

    @Override
    public Rect getFinderLocation() {
        return finderLocationRect;
    }
}
