package com.vkejun.wjs.editor;

import android.content.*;
import android.content.res.*;
import android.graphics.*;
import android.os.*;
import android.text.*;
import android.text.style.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import java.util.regex.*;
import org.apache.commons.io.*;
import com.vkejun.wjs.utils.*;

public class TextEditorView extends EditText {

	private Resources res;
	private Handler updateHandler ;
	private int updateDelay,errorLine;
	private boolean textChange = false;
	private boolean modified = true;

	public Pattern line,number,headfile;
	public Pattern string,keyword;
	public Pattern pretreatment,builtin ;
	public Pattern comment,trailingWhiteSpace ;
	private int mode = 0;
	float oldDist;
	float textSize = 0;
	private Paint mPaint = new Paint();
	private OnTextChangedListener onTextChangedListener;
	private Runnable updateThread = new Runnable() {
		@Override
		public void run() {
			Editable edit = getText();
			if (onTextChangedListener != null)
				onTextChangedListener.onTextChanged(edit.toString());
			highlightWithoutChange(edit);
		}
	};
	private Context context;


	public TextEditorView(Context context) {
		super(context, null);
	}

	public TextEditorView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}


	/* 初始化一些参数 */ 
	public void init(Context context) {
		this.context=context;
		setGravity(Gravity.TOP);
		mPaint.setAntiAlias(false);
		mPaint.setColor(Color.argb(200,140,140,140));
		mPaint.setTextSize(dip2px(context,10));
		setPadding((int)dip2px(context,20), 0, 0, 0);
		setHorizontallyScrolling(true);
		updateHandler = new Handler();
		setFilters(new InputFilter[] {inputFilter});
		addTextChangedListener(watcher);
		res = context.getResources();
		initPattern();
	}


	/* 初始化正则匹配的模板 */
	public void initPattern() {
		line = Pattern.compile(".*\\n");
		string = Pattern.compile("\"(\\\"|.)*?\"");
		keyword = Pattern.compile("#\\w{6}+");
		pretreatment = Pattern.compile("[^\"]#\\b(ifdef|ifndef|define|undef|if|else|elif|endif|pragma|"
									   + "error|line)\\b[\\s|\\S].*[^\"]");
		builtin = Pattern.compile("[^\"]\\b(printf|scanf|std::|cout|cin|cerr|clog|endl|template|"
								  + "sizeof)\\b[^\"]");		
		comment = Pattern.compile("/\\*(.|[\r\n])*?(\\*/)|/\\*(.|[\r\n])*|[^\"](?<!:)//.*[^\"]");
		trailingWhiteSpace = Pattern.compile("[\\t ]+$", Pattern.MULTILINE);		 

	}

	@Override
	protected void onDraw(Canvas canvas) {
		int lineHeight = getLineHeight();
        int x = getScrollX();
        int t = getScrollY();
        int b = getHeight() + t;
		Layout layout = getLayout();
        int topLine = layout.getLineForVertical(t);
        int bottomLine = layout.getLineForVertical(b);
        for (int i = topLine; i <= bottomLine + 1; i++) {
			canvas.drawText(String.valueOf(i), x + 5, i * lineHeight, mPaint);
        }
		canvas.translate(0, 0);
		super.onDraw(canvas);
	}

	/*
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public float dip2px(Context context, float dpValue) {
		float scale =context.getResources().getDisplayMetrics().density;
		return dpValue * scale + 0.5f;
	}


	/*
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public float px2dip(Context context, float pxValue) {
		float scale = context.getResources().getDisplayMetrics().density;
		return pxValue / scale + 0.5f;
	}



	/* 对输入的内容进行过滤 */
	private InputFilter inputFilter = new InputFilter(){

		@Override
		public CharSequence filter(CharSequence source, int start, int end,
								   Spanned dest, int dstart, int dend) {
			if (modified && end - start == 1 && start < source.length()
				&& dstart < dest.length()) {
				char c = source.charAt(start);
				if (c == '\n')
					return autoIndent(source, start, end, dest, dstart,dend);
			}
			return source;
		}
	};


	/* 对EditText输入内容进行判断 */
    private TextWatcher watcher = new TextWatcher(){

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // TODO: Implement this method
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // TODO: Implement this method
        }

        @Override
        public void afterTextChanged(Editable edit) {
			cancelUpdate();
			if (!modified)
				return;
			textChange = true;
			updateHandler.postDelayed(updateThread, updateDelay);
        }
	};



	public void setTextHighlighted(CharSequence text) {
		cancelUpdate();

		errorLine = 0;
		textChange = false;

		modified = false;
		setText(highlight(new SpannableStringBuilder(text)));
		modified = true;

		if (onTextChangedListener != null)
			onTextChangedListener.onTextChanged(text.toString());
	}



	public String getCleanText() {
		return trailingWhiteSpace.matcher(getText()).replaceAll("");
	}



	public void refresh() {
		highlightWithoutChange(getText());
	}


	public void cancelUpdate() {
		updateHandler.removeCallbacks(updateThread);
	}


	public void highlightWithoutChange(Editable e) {
		modified = false;
		highlight(e);
		modified = true;
	}


	/* 实现语法高亮 */
	public Editable highlight(Editable edit) {
		try {
			clearSpans(edit);
			if (edit.length() == 0)
				return edit;
			if (errorLine > 0) {
				Matcher m = line.matcher(edit);

				for (int n = errorLine; n-- > 0 && m.find();)
					edit.setSpan(new BackgroundColorSpan(Color.YELLOW), m.start(),
								 m.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
			for (Matcher m = Pattern.compile("#\\w{6}?").matcher(edit); m.find(); ) {
				edit.setSpan(new ForegroundColorSpan(Color.parseColor(edit.subSequence(m.start(),m.end()).toString())), m.start(), m.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
			Iterator<Map.Entry<Pattern, String>> entries = CodeBean.color.map.entrySet().iterator();
			while (entries.hasNext()) {
				Map.Entry<Pattern, String> entry = entries.next();
				for (Matcher m = entry.getKey().matcher(edit); m.find(); ) {
					edit.setSpan(new ForegroundColorSpan(Color.parseColor(entry.getValue())), m.start(), m.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				}
			}
		} catch (Exception ex) {
			Log.e("Editor",ex.toString());
		}
		return edit;
	}


	/* 移除span */
	public void clearSpans(Editable e) {
		ForegroundColorSpan foreSpan[] = e.getSpans(0, e.length(),
													ForegroundColorSpan.class);
		for (int n = foreSpan.length; n-- > 0;)
			e.removeSpan(foreSpan[n]);

		BackgroundColorSpan backSpan[] = e.getSpans(0, e.length(),
													BackgroundColorSpan.class);

		for (int n = backSpan.length; n-- > 0;)
			e.removeSpan(backSpan[n]);

	}

	/* 自动插入内容 */
	public CharSequence autoIndent(CharSequence source, int start, int end,
								   Spanned dest, int dstart, int dend) {
		String indent = "";
		int istart = dstart - 1;
		int iend = -1;

		boolean dataBefore = false;
		int pt = 0;

		for (; istart > -1; --istart) {
			char c = dest.charAt(istart);

			if (c == '\n')
				break;

			if (c != ' ' && c != '\t') {
				if (!dataBefore) {
					if (c == '{' || c == '+' || c == '-' || c == '*'
						|| c == '/' || c == '%' || c == '^' || c == '=')
						--pt;

					dataBefore = true;
				}

				if (c == '(')
					--pt;
				else if (c == ')')
					++pt;
			}
		}

		if (istart > -1) {
			char charAtCursor = dest.charAt(dstart);

			for (iend = ++istart; iend < dend; ++iend) {
				char c = dest.charAt(iend);

				if (charAtCursor != '\n' && c == '/' && iend + 1 < dend
					&& dest.charAt(iend) == c) {
					iend += 2;
					break;
				}

				if (c != ' ' && c != '\t')
					break;
			}

			indent += dest.subSequence(istart, iend);
		}

		if (pt < 0)
			indent += "\t";

		return source + indent;
	}

	/* 跳转到指定行 */
	public boolean gotoLine(int line) {
		--line;

		if (line > getLineCount()){
			setSelection(getText().toString().length());
			return false;
		}

		Layout layout = getLayout();
		setSelection(layout.getLineStart(line), layout.getLineEnd(line));
		return true;
	}

	/* 获得行 行高 当前行 */
	public int getRowHeight() {
		return getLineHeight();
	}

	public int getTotalRows() {
		return getLineCount();
	}

	public int getCurrRow() {
		return getLayout().getLineForOffset(getSelectionStart()) + 1;
	}

	public interface OnTextChangedListener {
		void onTextChanged(String text);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		if (textSize == 0) {
			textSize = this.getTextSize();
		}
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
			case MotionEvent.ACTION_DOWN:
				mode = 1;
				break;
			case MotionEvent.ACTION_UP:
				mode = 0;
				break;
			case MotionEvent.ACTION_POINTER_UP:
				mode -= 1;
				break;
			case MotionEvent.ACTION_POINTER_DOWN:
				oldDist = spacing(event);
				mode += 1;
				break;

			case MotionEvent.ACTION_MOVE:
				if (mode >= 2) {
					float newDist = spacing(event);
					if (newDist > oldDist + 1) {
						zoom(newDist / oldDist);
						oldDist = newDist;
					}
					if (newDist < oldDist - 1) {
						zoom(newDist / oldDist);
						oldDist = newDist;
					}
				}
				break;
		}
		return super.onTouchEvent(event);
	}

	private void zoom(float f) {
		this.setTextSize(textSize *= f);
	}

	private float spacing(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return (float) Math.sqrt(x * x + y * y);
	}

}
