package com.example.expense_tracker.utilities;

import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.Editable;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import com.example.expense_tracker.R;

public class KeyboardUtils {
    private final Keyboard CustomizedKeyboard;
    private KeyboardView keyboardView;
    private EditText editText;

    public interface OnEnsureListener {
        public void onEnsure();
    }
    public OnEnsureListener onEnsureListener;

    public void setOnEnsureListener(OnEnsureListener onEnsureListener) {
        this.onEnsureListener = onEnsureListener;
    }

    public KeyboardUtils(KeyboardView keyboardView, EditText editText) {
        this.keyboardView = keyboardView;
        this.editText = editText;
        this.editText.setInputType(InputType.TYPE_NULL); // Cancel ejecting the system's own keyboard

        CustomizedKeyboard = new Keyboard(this.editText.getContext(), R.xml.key);
        this.keyboardView.setKeyboard(CustomizedKeyboard);
        this.keyboardView.setEnabled(true);
        this.keyboardView.setPreviewEnabled(false);
        this.keyboardView.setOnKeyboardActionListener(new KeyboardView.OnKeyboardActionListener() {
            @Override
            public void onPress(int primaryCode) {
                // Handle key press event
            }

            @Override
            public void onRelease(int primaryCode) {
                // Handle key release event
            }

            @Override
            public void onKey(int primaryCode, int[] keyCodes) {
                // Handle key event
                Editable editable = editText.getText();
                int start = editText.getSelectionStart();

                switch (primaryCode) {
                    case Keyboard.KEYCODE_DELETE:
                        if(editable != null && editable.length() > 0) {
                            if(start > 0)
                                editable.delete(start-1, start);
                        }
                        break;

                    case Keyboard.KEYCODE_CANCEL:
                        editable.clear();
                        break;

                    case Keyboard.KEYCODE_DONE:
                        onEnsureListener.onEnsure();
                        break;

                    default:
                        editable.insert(start, Character.toString((char)primaryCode));
                        break;
                }
            }

            @Override
            public void onText(CharSequence text) {
                // Handle text event
            }

            @Override
            public void swipeLeft() {
                // Handle swipe left event
            }

            @Override
            public void swipeRight() {
                // Handle swipe right event
            }

            @Override
            public void swipeDown() {
                // Handle swipe down event
            }

            @Override
            public void swipeUp() {
                // Handle swipe up event
            }
        });
    }

    public void showKeyboard() {
        int visibility = keyboardView.getVisibility();
        if(visibility == View.INVISIBLE || visibility == View.GONE) {
            keyboardView.setVisibility(View.VISIBLE);
        }
    }

    public void hideKeyboard() {
        int visibility = keyboardView.getVisibility();
        if(visibility == View.INVISIBLE || visibility == View.GONE) {
            keyboardView.setVisibility(View.VISIBLE);
        }
    }
}
