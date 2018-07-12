package com.sbai.bcnews.view.search;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sbai.bcnews.R;
import com.sbai.bcnews.utils.Display;

import java.lang.reflect.Field;

/**
 * Modified by $nishuideyu$ on 2018/7/5
 * <p>
 * Description:
 * </p>
 */
public class SearchEditText extends LinearLayout {

    private Drawable mLeftDrawable;
    private int mSearchTextColor;
    private int mSearchTextSize;
    private int mSearchHintTextColor;
    private Drawable mEditCursorDrawable;
    private String mClearText;
    private int mClearTextColor;
    private int mClearTextSize;

    EditText mSearchEditText;
    TextView mClearContent;

    private OnSearchContentListener mOnSearchContentListener;
    private String mDefaultHintText;


    public interface OnSearchContentListener {

        void onSearchContent(String values);

        void onKeyBoardSearch(String values);
    }


    public SearchEditText(@NonNull Context context) {
        this(context, null);
    }

    public SearchEditText(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        handleAttrs(attrs);
        init();
    }

    private void init() {
        setWillNotDraw(false);
        setGravity(Gravity.CENTER_VERTICAL);
        setOrientation(HORIZONTAL);
        int padding = dp2px(7);
        setPadding(padding, 0, padding, 0);

        mSearchEditText = createSearchEditText();

        mDefaultHintText = getContext().getString(R.string.please_input_antistop);

        setTextSize(mSearchTextSize);
        setTextColor(mSearchTextColor);
        setHintColor(mSearchHintTextColor);
        setHint(mDefaultHintText);

        LayoutParams layoutParams = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.weight = 1;
        layoutParams.setMargins(0, 0, dp2px(5), 0);
        addView(mSearchEditText, layoutParams);


        mClearContent = new TextView(getContext());
        mClearContent.setText(mClearText);
        mClearContent.setTextSize(TypedValue.COMPLEX_UNIT_PX, mClearTextSize);
        mClearContent.setTextColor(mClearTextColor);
        mClearContent.setVisibility(INVISIBLE);
        addView(mClearContent);

        mClearContent.setOnClickListener((v -> {
            mSearchEditText.setText("");
        }));

        mSearchEditText.addTextChangedListener(mTextWatcher);
    }

    private EditText createSearchEditText() {
        EditText editText = new EditText(getContext());
        if (mLeftDrawable != null) {
            editText.setCompoundDrawablesWithIntrinsicBounds(mLeftDrawable, null, null, null);
            editText.setCompoundDrawablePadding(dp2px(10));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            editText.setBackground(null);
        } else {
            editText.setBackgroundDrawable(null);
        }
        editText.setPadding(0, 0, 0, 0);
        editText.setMaxLines(1);
        editText.setGravity(Gravity.CENTER_VERTICAL);
        editText.setSingleLine();
        editText.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        if (mEditCursorDrawable != null) {
            try {
                Field field = TextView.class.getDeclaredField("mCursorDrawableRes");
                field.setAccessible(true);
                field.set(editText, mEditCursorDrawable);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        editText.setOnEditorActionListener(((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (mOnSearchContentListener != null) {
                    if (!TextUtils.isEmpty(editText.getText())) {
                        mOnSearchContentListener.onKeyBoardSearch(editText.getText().toString());
                    } else {
                        if (!mDefaultHintText.equalsIgnoreCase(editText.getHint().toString())) {
                            mOnSearchContentListener.onKeyBoardSearch(editText.getHint().toString());
                        }
                    }
                }
                return true;
            }
            return false;
        }));

        return editText;
    }

    private void handleAttrs(AttributeSet attrs) {
        int defaultTextSize = dp2px(14);
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.SearchEditText);
        mLeftDrawable = typedArray.getDrawable(R.styleable.SearchEditText_leftDrawable);
        mSearchTextColor = typedArray.getColor(R.styleable.SearchEditText_searchEditTextColor, Color.BLACK);
        mSearchTextSize = typedArray.getDimensionPixelOffset(R.styleable.SearchEditText_searchEditTextSize, defaultTextSize);
        mSearchHintTextColor = typedArray.getColor(R.styleable.SearchEditText_searchEditHintTextColor, Color.RED);
        mEditCursorDrawable = typedArray.getDrawable(R.styleable.SearchEditText_searchEditCursorDrawable);
        mClearText = typedArray.getString(R.styleable.SearchEditText_clearText);
        mClearTextColor = typedArray.getColor(R.styleable.SearchEditText_clearTextColor, Color.GRAY);
        mClearTextSize = typedArray.getDimensionPixelOffset(R.styleable.SearchEditText_clearTextSize, defaultTextSize);
        typedArray.recycle();
    }

    private int dp2px(int dp) {
        return (int) Display.dp2Px(dp, getResources());
    }

    public void setHint(CharSequence hint) {
        mSearchEditText.setHint(hint);
    }

    private void setHintColor(int color) {
        mSearchEditText.setHintTextColor(color);
    }

    private void setTextSize(float textSize) {
        mSearchEditText.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
    }

    private void setTextColor(int color) {
        mSearchEditText.setTextColor(color);
    }

    public void setHint(int hintRes) {
        mSearchEditText.setHint(hintRes);
    }

    public void setText(CharSequence text) {
        mSearchEditText.setText(text);
    }

    public void setOnSearchContentListener(OnSearchContentListener onSearchContentListener) {
        mOnSearchContentListener = onSearchContentListener;
    }

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            boolean isEmpty = checkSearchContentIsEmpty();
            if (isEmpty) {
                if (mClearContent.getVisibility() == VISIBLE)
                    mClearContent.setVisibility(INVISIBLE);
            } else {
                if (mClearContent.getVisibility() == INVISIBLE)
                    mClearContent.setVisibility(VISIBLE);
            }

            mSearchEditText.setSelection(s.toString().length());

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (mOnSearchContentListener != null) {
                mOnSearchContentListener.onSearchContent(s.toString());
            }
        }
    };

    private boolean checkSearchContentIsEmpty() {
        return TextUtils.isEmpty(mSearchEditText.getText());
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mOnSearchContentListener = null;
        mSearchEditText.removeTextChangedListener(mTextWatcher);
    }
}
