package com.zj.settingview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.SwitchCompat;


public class LSettingItem extends RelativeLayout {
    /*左侧显示文本*/
    private String mLeftText;
    /*左侧图标*/
    private Drawable mLeftIcon;
    /*右侧图标*/
    private Drawable mRightIcon;
    /*左侧显示文本大小*/
    private int mTextSize;
    /*左侧显示文本颜色*/
    private int mTextColor;
    /*右侧显示文本大小*/
    private float mRightTextSize;
    /*右侧显示文本颜色*/
    private int mRightTextColor;

    /*根布局*/
    private RelativeLayout mRootLayout;
    /*左侧文本控件*/
    private TextView mTvLeftText;
    /*右侧文本控件*/
    private TextView mTvRightText;
    /*分割线*/
    private View mUnderLine;
    /*左侧图标控件*/
    private ImageView mIvLeftIcon;
    /*左侧图标大小*/
    private int mLeftIconSzie;
    /*右侧图标控件区域,默认展示图标*/
    private FrameLayout mRightLayout;
    /*右侧图标控件,默认展示图标*/
    private ImageView mIvRightIcon;
    /*右侧图标控件,选择样式图标*/
    private AppCompatCheckBox mRightIcon_check;
    /*右侧图标控件,开关样式图标*/
    private SwitchCompat mRightIcon_switch;

    /*右侧图标展示风格*/
    private int mRightStyle = 0;
    /*选中状态*/
    private boolean mChecked;
    //是否显示下划线
    private boolean isShowUnderLine;
    /*点击事件*/
    private OnLSettingItemClick mOnLSettingItemClick;

    public LSettingItem(Context context) {
        this(context, null);
    }

    public LSettingItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LSettingItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        getCustomStyle(context, attrs);
        //获取到右侧展示风格，进行样式切换
        switchRightStyle(mRightStyle);

        mRightIcon_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mOnLSettingItemClick != null) {
                    mOnLSettingItemClick.click(isChecked);
                }
            }
        });
        mRightIcon_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mOnLSettingItemClick != null) {
                    mOnLSettingItemClick.click(isChecked);
                }
            }
        });
    }

    public void setmOnLSettingItemClick(OnLSettingItemClick mOnLSettingItemClick) {
        this.mOnLSettingItemClick = mOnLSettingItemClick;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    /**
     * 初始化自定义属性
     *
     * @param context
     * @param attrs
     */
    public void getCustomStyle(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LSettingItem);
        //默认显示分割线
        isShowUnderLine = typedArray.getBoolean(R.styleable.LSettingItem_isShowUnderLine, true);
        LayoutParams lineParams = (LayoutParams) mUnderLine.getLayoutParams();
        lineParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        mUnderLine.setLayoutParams(lineParams);

        int n = typedArray.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = typedArray.getIndex(i);
            if (attr == R.styleable.LSettingItem_leftText) {
                mLeftText = typedArray.getString(attr);
                mTvLeftText.setText(mLeftText);
            } else if (attr == R.styleable.LSettingItem_leftIcon) {
                // 左侧图标,有设置就显示，未设置，布局中已经隐藏了
                mLeftIcon = typedArray.getDrawable(attr);
                if (null != mLeftIcon) {
                    mIvLeftIcon.setImageDrawable(mLeftIcon);
                    mIvLeftIcon.setVisibility(VISIBLE);
                }
            } else if (attr == R.styleable.LSettingItem_leftIconSize) {
                if (mLeftIcon != null) {
                    mLeftIconSzie = (int) typedArray.getDimension(attr, 16);
                    LayoutParams layoutParams = (LayoutParams) mIvLeftIcon.getLayoutParams();
                    layoutParams.width = mLeftIconSzie;
                    layoutParams.height = mLeftIconSzie;
                    mIvLeftIcon.setLayoutParams(layoutParams);
                }
            } else if (attr == R.styleable.LSettingItem_leftTextMarginLeft) {
                int leftMargin = (int) typedArray.getDimension(attr, 8);
                LayoutParams layoutParams = (LayoutParams) mTvLeftText.getLayoutParams();
                layoutParams.leftMargin = leftMargin;
                mTvLeftText.setLayoutParams(layoutParams);
            } else if (attr == R.styleable.LSettingItem_rightIcon) {
                // 右侧图标
                mRightIcon = typedArray.getDrawable(attr);
                mIvRightIcon.setImageDrawable(mRightIcon);
            } else if (attr == R.styleable.LSettingItem_leftTextSize) {
                // 默认设置为16sp
                float textSize = typedArray.getFloat(attr, 16);
                mTvLeftText.setTextSize(textSize);
            } else if (attr == R.styleable.LSettingItem_leftTextColor) {
                //文字默认灰色
                mTextColor = typedArray.getColor(attr, Color.LTGRAY);
                mTvLeftText.setTextColor(mTextColor);
            } else if (attr == R.styleable.LSettingItem_rightStyle) {
                mRightStyle = typedArray.getInt(attr, 0);
            } else if (attr == R.styleable.LSettingItem_isShowUnderLine) {
                if (!isShowUnderLine) {
                    mUnderLine.setVisibility(View.GONE);
                } else {
                    mUnderLine.setVisibility(VISIBLE);
                }

            } else if (attr == R.styleable.LSettingItem_android_layout_height) {
                //右侧图标Padding
                int height = (int) typedArray.getLayoutDimension(attr, 52);
                LayoutParams layoutParams = (LayoutParams) mRootLayout.getLayoutParams();
                layoutParams.height = height;
                mRootLayout.setLayoutParams(layoutParams);

            } else if (attr == R.styleable.LSettingItem_rightIconPadding) {
                //右侧图标Padding
                int padding = (int) typedArray.getDimension(attr, 15);
                mIvRightIcon.setPadding(padding, padding, padding, padding);

            } else if (attr == R.styleable.LSettingItem_dividerML) {
                //分割线左侧边距
                if (isShowUnderLine) {
                    int leftMargin = (int) typedArray.getDimension(attr, 15);
                    LayoutParams layoutParams = (LayoutParams) mUnderLine.getLayoutParams();
                    layoutParams.leftMargin = leftMargin;
                    mUnderLine.setLayoutParams(layoutParams);
                }
            } else if (attr == R.styleable.LSettingItem_dividerMR) {
                //分割线右侧边距
                if (isShowUnderLine) {
                    int rightMargin = (int) typedArray.getDimension(attr, 15);
                    LayoutParams layoutParams = (LayoutParams) mUnderLine.getLayoutParams();
                    layoutParams.rightMargin = rightMargin;
                    mUnderLine.setLayoutParams(layoutParams);
                }
            } else if (attr == R.styleable.LSettingItem_dividerHeight) {
                //分割线右侧边距
                if (isShowUnderLine) {
                    int dividerHeight = (int) typedArray.getDimension(attr, 15);
                    LayoutParams layoutParams = (LayoutParams) mUnderLine.getLayoutParams();
                    layoutParams.height = dividerHeight;
                    mUnderLine.setLayoutParams(layoutParams);
                }
            } else if (attr == R.styleable.LSettingItem_isShowRightText) {
                //默认不显示右侧文字
                if (typedArray.getBoolean(attr, false)) {
                    mTvRightText.setVisibility(View.VISIBLE);
                }
            } else if (attr == R.styleable.LSettingItem_rightText) {
                mTvRightText.setText(typedArray.getString(attr));
            } else if (attr == R.styleable.LSettingItem_rightTextSize) {
                // 默认设置为16sp
                mRightTextSize = typedArray.getFloat(attr, 14);
                mTvRightText.setTextSize(mRightTextSize);
            } else if (attr == R.styleable.LSettingItem_rightTextColor) {
                //文字默认灰色
                mRightTextColor = typedArray.getColor(attr, Color.GRAY);
                mTvRightText.setTextColor(mRightTextColor);
            }
        }
        typedArray.recycle();


    }

    /**
     * 根据设定切换右侧展示样式，同时更新点击事件处理方式
     *
     * @param mRightStyle
     */
    private void switchRightStyle(int mRightStyle) {
        switch (mRightStyle) {
            case 0:
                //默认展示样式，只展示一个图标
                mIvRightIcon.setVisibility(View.VISIBLE);
                mRightIcon_check.setVisibility(View.GONE);
                mRightIcon_switch.setVisibility(View.GONE);
                break;
            case 1:
                //隐藏右侧图标
                mRightLayout.setVisibility(View.INVISIBLE);
                break;
            case 2:
                //显示选择框样式
                mIvRightIcon.setVisibility(View.GONE);
                mRightIcon_check.setVisibility(View.VISIBLE);
                mRightIcon_switch.setVisibility(View.GONE);
                break;
            case 3:
                //显示开关切换样式
                mIvRightIcon.setVisibility(View.GONE);
                mRightIcon_check.setVisibility(View.GONE);
                mRightIcon_switch.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void initView(Context context) {
        mRootLayout = (RelativeLayout) View.inflate(context, R.layout.layout_setting_item, null);
        mUnderLine = (View) mRootLayout.findViewById(R.id.underline);
        mTvLeftText = (TextView) mRootLayout.findViewById(R.id.tv_lefttext);
        mTvRightText = (TextView) mRootLayout.findViewById(R.id.tv_righttext);
        mIvLeftIcon = (ImageView) mRootLayout.findViewById(R.id.iv_lefticon);
        mIvRightIcon = (ImageView) mRootLayout.findViewById(R.id.iv_righticon);
        mRightLayout = (FrameLayout) mRootLayout.findViewById(R.id.rightlayout);
        mRightIcon_check = (AppCompatCheckBox) mRootLayout.findViewById(R.id.rightcheck);
        mRightIcon_switch = (SwitchCompat) mRootLayout.findViewById(R.id.rightswitch);
        addView(mRootLayout);

    }

    /**
     * 处理点击事件
     */
    public void onMyClick() {
        switch (mRightStyle) {
            case 0:
            case 1:
                if (null != mOnLSettingItemClick) {
                    mOnLSettingItemClick.click(mChecked);
                }
                break;
            case 2:
                //选择框切换选中状态
                mRightIcon_check.setChecked(!mRightIcon_check.isChecked());
                mChecked = mRightIcon_check.isChecked();
                break;
            case 3:
                //开关切换状态
                mRightIcon_switch.setChecked(!mRightIcon_switch.isChecked());
                mChecked = mRightIcon_check.isChecked();
                break;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    /**
     * 获取根布局对象
     *
     * @return
     */
    public RelativeLayout getmRootLayout() {
        return mRootLayout;
    }

    /**
     * 更改左侧文字
     */
    public void setLeftText(String info) {
        mTvLeftText.setText(info);
    }

    /**
     * 更改右侧文字
     */
    public void setRightText(String info) {
        mTvRightText.setText(info);
    }


    public interface OnLSettingItemClick {
        public void click(boolean isChecked);
    }
}

