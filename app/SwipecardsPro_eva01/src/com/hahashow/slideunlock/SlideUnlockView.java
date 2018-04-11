package com.hahashow.slideunlock;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
/**
 * 
 * @author liuziyuan0932
 *
 *https://github.com/liuzhiyuan0932/SlideUnLock
 *
 *	�˴���лԭ������˽��������Դ���¾�
 *
 */


public class SlideUnlockView extends View {
	String namespace = "http://schemas.android.com/apk/res/com.example.swipecardspro_eva01";
	
	
	/**
	 * ���鵱ǰ��״̬
	 */
	public int currentState;
	/**
	 * δ����
	 */
	public static final int STATE_LOCK = 1;
	/**
	 * ����
	 */
	public static final int STATE_UNLOCK = 2;
	/**
	 * ������ק
	 */
	public static final int STATE_MOVING = 3;

	private static final String TAG = "SlideUnlockView";
	/**
	 * ���������ı���ͼƬ
	 */
	private Bitmap slideUnlockBackground;
	/**
	 * �����ͼƬ
	 */
	private Bitmap slideUnlockBlock;
	/**
	 * �������������Ŀ��
	 */
	private int blockBackgoundWidth;
	/**
	 * ������
	 */
	private int blockWidth;
	private int blockHeight;
	/**
	 * ��ָ�ڻ����x��yֵ
	 */
	private float x;
	private float y;
	/**
	 * ��ָ�ڰ���ʱ���Ƿ񰴵��˻�����
	 */
	private boolean downOnBlock;
	
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0) {
				// ���x������0������Ϊ�����û����ƶ�������ˣ�ÿ���ƶ���������Ϊ�������/100
				if (x > 0) {
					x = x - blockBackgoundWidth * 1.0f / 100;
					// ˢ�½���
					postInvalidate();
					// ���ü����ƶ�
					handler.sendEmptyMessageDelayed(0, 10);
				} else {
					handler.removeCallbacksAndMessages(null);
					currentState = STATE_LOCK;
					Log.i(TAG, "state---lock.....");
				}
			}
		};
	};
	
	
	
	
	
	
	
	/**
	 * �����ļ���
	 */
	private OnUnLockListener onUnLockListener;
	
	public SlideUnlockView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		// Ĭ�ϻ�������Ϊδ����״̬
		currentState = STATE_LOCK;
		// ȡ���Զ��������б���ͼƬ
		int slideUnlockBackgroundResource = attrs.getAttributeResourceValue(
				namespace, "slideUnlockBackgroundResource", -1);
		// ȡ���Զ��������л���ͼƬ
		int slideUnlockBlockResource = attrs.getAttributeResourceValue(
				namespace, "slideUnlockBlockResource", -1);
		// ȡ���Զ��������е�ǰ״̬
				// �������״̬��true��˵���Ѿ�����
				/**
				 * ��ȡ���Զ������Եı���ʱ�����ñ���
				 */
		setSlideUnlockBackground(slideUnlockBackgroundResource);
		/**
		 * ��ȡ���Զ������ԵĻ���ʱ�����û����ͼƬ
		 */
		setSlideUnlockBlock(slideUnlockBlockResource);
		/**
		 * ִ��onDraw���������н������
		 */
		postInvalidate();
		
		
		// TODO Auto-generated constructor stub
	}
	
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		// ��һ��ʼ��ʹ�ý�����ͼ���Ƴ���
		canvas.drawBitmap(slideUnlockBackground, 0, 0, null);
		/**
		 * �жϵ�ǰ״̬
		 */
		switch (currentState) {
		// �����δ�������ͽ�������Ƶ������
		case STATE_LOCK:
			canvas.drawBitmap(slideUnlockBlock, 0, 0, null);
			break;
		// �ѽ����������
		case STATE_UNLOCK:
			int unlockX = blockBackgoundWidth - blockWidth;
			canvas.drawBitmap(slideUnlockBlock, unlockX, 0, null);
			break;
		case STATE_MOVING:
			if (x < 0) {
				x = 0;
			} else if (x > blockBackgoundWidth - blockWidth) {
				x = blockBackgoundWidth - blockWidth;
			}
			canvas.drawBitmap(slideUnlockBlock, x, 0, null);
			break;
		default:
			break;
		}
	}
	
	public SlideUnlockView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SlideUnlockView(Context context) {
		this(context, null);
	}
	public void setSlideUnlockBackground(int slideUnlockBackgroundResource) {
		Log.i(TAG, "setSlideUnlockBackground.....");
		slideUnlockBackground = BitmapFactory.decodeResource(getResources(),
				slideUnlockBackgroundResource);
		// ��ȡ����ͼ�Ŀ�͸�
		blockBackgoundWidth = slideUnlockBackground.getWidth();

	}

	public void setSlideUnlockBlock(int slideUnlockBlockResource) {
		Log.i(TAG, "setSlideUnlockBlock.....");
		slideUnlockBlock = BitmapFactory.decodeResource(getResources(),
				slideUnlockBlockResource);
		// ��ȡ����Ŀ�͸�
		blockWidth = slideUnlockBlock.getWidth();
		blockHeight = slideUnlockBlock.getHeight();
	}
	
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		Log.i(TAG, "onMeauser.....");
		setMeasuredDimension(slideUnlockBackground.getWidth(),
				slideUnlockBackground.getHeight());
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		switch (event.getAction()) {
		// ����ָ���µ�ʱ���ж���ָ���µ�λ���Ƿ��ڻ����ϱ�
		case MotionEvent.ACTION_DOWN:

			if (currentState != STATE_MOVING) {
				// �ж�һ�£������ǰ�����ƶ�����ִ�д�������
				// ��ȡ����ڱ��������Ͻǵ�x��yֵ
				x = event.getX();
				y = event.getY();
				// �ȼ������������ĵ��x��y����
				float blockCenterX = blockWidth * 1.0f / 2;
				float blockCenterY = blockHeight * 1.0f / 2;
				downOnBlock = isDownOnBlock(blockCenterX, x, blockCenterY, y);
				Log.i(TAG, "down......................");
				// ����onDraw����
				postInvalidate();

			}
			break;
		case MotionEvent.ACTION_MOVE:
			// �����ָȷ�����ڻ����ϣ�����Ϊ��ʼ��ק����
			if (downOnBlock) {
				// ��ȡ����ڱ��������Ͻǵ�x��yֵ
				x = event.getX();
				y = event.getY();
				currentState = STATE_MOVING;
				Log.i(TAG, "move......................");
				// ����onDraw����
				postInvalidate();
			}
			break;
		case MotionEvent.ACTION_UP:
			if (currentState == STATE_MOVING) {
				// ����ָ̧���ʱ��Ӧ�����û����λ��
				// ˵��δ����
				if (x < blockBackgoundWidth - blockWidth) {
					handler.sendEmptyMessageDelayed(0, 10);
					// ͨ���ص������ѽ���
					onUnLockListener.setUnLocked(false);
				} else {
					currentState = STATE_UNLOCK;
					// ͨ���ص�����δ����
					onUnLockListener.setUnLocked(true);
				}
				downOnBlock = false;
				// ����onDraw����
				postInvalidate();

			}
			break;

		default:
			break;
		}
		return true;
	}
	
	
	/**
	 * ������ָ�Ƿ��������˻�����(Ĭ���ǰ��ջ�����δ�����ĳ�ʼλ���������)
	 */
	public boolean isDownOnBlock(float x1, float x2, float y1, float y2) {
		float sqrt = FloatMath.sqrt(Math.abs(x1 - x2) * Math.abs(x1 - x2)
				+ Math.abs(y1 - y2) * Math.abs(y1 - y2));
		if (sqrt <= blockWidth / 2) {
			return true;
		}
		return false;
	}

	/**
	 * ���ý�������
	 * 
	 * @param onUnLockListener
	 */
	public void setOnUnLockListener(OnUnLockListener onUnLockListener) {
		this.onUnLockListener = onUnLockListener;
	}

	// ����һ�������ļ���
	public interface OnUnLockListener {
		public void setUnLocked(boolean lock);
	}

	/**
	 * ����һ�»�������״̬����֤�´��ܹ�����ʹ��
	 */
	public void reset() {
		currentState = STATE_LOCK;
		postInvalidate();
	}
	
	//�ж���ָ�Ƿ��ڻ���ı��������ƶ�
	public boolean isOnBackground(int x,int y){
		if(x<=slideUnlockBackground.getWidth()&&y<=slideUnlockBackground.getHeight()){
			return true;
		}
		return false;
	}
	
}
