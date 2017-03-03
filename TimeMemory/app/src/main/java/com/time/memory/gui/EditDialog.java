package com.time.memory.gui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.time.memory.R;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:访系统带EditText的Dialog
 * @date 2016/10/25 8:38
 */
public class EditDialog extends Dialog {


	public EditDialog(Context context) {
		super(context);
	}

	public EditDialog(Context context, int theme) {
		super(context, theme);
	}


	public static class Builder {
		private Context context;
		private String title;
		private String message;
		private String positiveButtonText;
		private String negativeButtonText;
		private View contentView;
		private OnEditClickListener positiveButtOnEditClickListener;
		private OnEditClickListener negativeButtOnEditClickListener;
		private EditText input;

		public Builder(Context context) {
			this.context = context;
		}

		public Builder setMessage(String message) {
			this.message = message;
			return this;
		}

		/**
		 * Set the Dialog message from resource
		 *
		 * @return
		 */
		public Builder setMessage(int message) {
			this.message = (String) context.getText(message);
			return this;
		}

		/**
		 * Set the Dialog title from resource
		 *
		 * @param title
		 * @return
		 */
		public Builder setTitle(int title) {
			this.title = (String) context.getText(title);
			return this;
		}

		/**
		 * Set the Dialog title from String
		 *
		 * @param title
		 * @return
		 */

		public Builder setTitle(String title) {
			this.title = title;
			return this;
		}

		public Builder setContentView(View v) {
			this.contentView = v;
			return this;
		}

		/**
		 * Set the positive button resource and it's listener
		 *
		 * @param positiveButtonText
		 * @return
		 */
		public Builder setPositiveButton(int positiveButtonText,
										 OnEditClickListener listener) {
			this.positiveButtonText = (String) context
					.getText(positiveButtonText);
			this.positiveButtOnEditClickListener = listener;
			return this;
		}

		public Builder setPositiveButton(String positiveButtonText,
										 OnEditClickListener listener) {
			this.positiveButtonText = positiveButtonText;
			this.positiveButtOnEditClickListener = listener;
			return this;
		}

		public Builder setNegativeButton(int negativeButtonText,
										 OnEditClickListener listener) {
			this.negativeButtonText = (String) context
					.getText(negativeButtonText);
			this.negativeButtOnEditClickListener = listener;
			return this;
		}

		public Builder setNegativeButton(String negativeButtonText,
										 OnEditClickListener listener) {
			this.negativeButtonText = negativeButtonText;
			this.negativeButtOnEditClickListener = listener;
			return this;
		}

		public EditDialog create() {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			// instantiate the dialog with the custom Theme
			final EditDialog dialog = new EditDialog(context,
					R.style.MyDialogStyleBottom);
			View layout = inflater.inflate(R.layout.activity_editdialog, null);
			dialog.addContentView(layout, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			// set the dialog title
			((TextView) layout.findViewById(R.id.dialog_title_tv)).setText(title);
			input = ((EditText) layout.findViewById(R.id.dialog_input_et));
			input.setText(message);
			// set the confirm button
			if (positiveButtonText != null) {
				((Button) layout.findViewById(R.id.subscription_submit))
						.setText(positiveButtonText);
				if (positiveButtOnEditClickListener != null)
					((Button) layout.findViewById(R.id.subscription_submit))
							.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									positiveButtOnEditClickListener.onClick(
											DialogInterface.BUTTON_NEGATIVE,
											input.getText().toString()
									);
									hideKeyboard();
									dialog.dismiss();
								}
							});
			} else {
				// if no confirm button just set the visibility to GONE
				layout.findViewById(R.id.subscription_submit).setVisibility(
						View.GONE);
			}
			// set the cancel button
			if (negativeButtonText != null) {
				((Button) layout.findViewById(R.id.subscription_exit))
						.setText(negativeButtonText);
				if (negativeButtOnEditClickListener != null) {
					((Button) layout.findViewById(R.id.subscription_exit))
							.setOnClickListener(new View.OnClickListener() {
								public void onClick(View v) {
									negativeButtOnEditClickListener.onClick(
											DialogInterface.BUTTON_NEGATIVE,
											""
									);
									hideKeyboard();
									dialog.dismiss();
								}
							});
				}
			} else {
				// if no confirm button just set the visibility to GONE
				layout.findViewById(R.id.subscription_exit).setVisibility(
						View.GONE);
			}
			dialog.setContentView(layout);
			return dialog;
		}

		public EditDialog show() {
			EditDialog dialog = create();
			dialog.show();
			showKeyboard();
//			if (dialog.isShowing()) {
//				showKeyboard();
//			} else {
//				Timer timer = new Timer();
//				timer.schedule(new TimerTask() {
//					@Override
//					public void run() {
//						showKeyboard();
//					}
//				}, 200);
//			}
			return dialog;
		}

		private void showKeyboard() {
			if (input != null) {
				//设置可获得焦点
				input.setFocusable(true);
				input.setFocusableInTouchMode(true);
				//请求获得焦点
				input.requestFocus();
				//调用系统输入法
				InputMethodManager imm = (InputMethodManager) input.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.showSoftInput(input, InputMethodManager.SHOW_FORCED);

			}
		}

		private void hideKeyboard() {
			InputMethodManager imm = (InputMethodManager) input.getContext()
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			if (imm.isActive()) {
				imm.hideSoftInputFromWindow(input.getApplicationWindowToken(), 0);
			}
		}

	}


	public interface OnEditClickListener {
		public void onClick(int which, String msg);
	}
}
