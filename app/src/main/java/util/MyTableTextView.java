package util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

public class MyTableTextView extends TextView {
    // public MyTableTextView(Context context) {
    // super(context);
    // }
    //
    // //由系统调用(带属性+上下文环境构造方法+布局文件中定义样式文件构造方法)
    // public MyTableTextView(Context context, AttributeSet attrs, int defStyle)
    // {
    // super(context, attrs, defStyle);
    // }
    //
    // //重写获取焦点的方法,由系统调用,调用的时候默认就能获取焦点
    // @Override
    // public boolean isFocused() {
    // return true;
    // }

    Paint paint = new Paint();

    public MyTableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        int color = Color.parseColor("#80b9f2");
        // 为边框设置颜色
        paint.setColor(color);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 画TextView的4个边
        canvas.drawLine(0, 0, this.getWidth() - 1, 0, paint);
        canvas.drawLine(0, 0, 0, this.getHeight() - 1, paint);
        canvas.drawLine(this.getWidth() - 1, 0, this.getWidth() - 1,
                this.getHeight() - 1, paint);
        canvas.drawLine(0, this.getHeight() - 1, this.getWidth() - 1,
                this.getHeight() - 1, paint);
    }

}
