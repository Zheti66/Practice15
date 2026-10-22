package com.example.practice15;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final String FILE_NAME = "document.txt";
    private EditText editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editor = findViewById(R.id.editor);
        Button btnBold = findViewById(R.id.btnBold);
        Button btnItalic = findViewById(R.id.btnItalic);
        Button btnBigger = findViewById(R.id.btnBigger);
        Button btnSmaller = findViewById(R.id.btnSmaller);

        btnBold.setOnClickListener(v -> {
            int start = editor.getSelectionStart();
            int end = editor.getSelectionEnd();
            if (start == end) {
                Toast.makeText(this, "Выделите текст", Toast.LENGTH_SHORT).show();
                return;
            }
            Spannable sp = editor.getText();
            sp.setSpan(new StyleSpan(Typeface.BOLD), start, end,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        });

        btnItalic.setOnClickListener(v -> {
            int start = editor.getSelectionStart();
            int end = editor.getSelectionEnd();
            if (start == end) {
                Toast.makeText(this, "Выделите текст", Toast.LENGTH_SHORT).show();
                return;
            }
            Spannable sp = editor.getText();
            sp.setSpan(new StyleSpan(Typeface.ITALIC), start, end,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        });

        btnBigger.setOnClickListener(v -> changeFontSize(1.15f));

        btnSmaller.setOnClickListener(v -> changeFontSize(0.85f));
    }

    private void changeFontSize(float factor) {
        int start = editor.getSelectionStart();
        int end = editor.getSelectionEnd();
        if (start == end) {
            Toast.makeText(this, "Выделите текст", Toast.LENGTH_SHORT).show();
            return;
        }
        Spannable sp = editor.getText();
        sp.setSpan(new RelativeSizeSpan(factor), start, end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    private File getInternalFile() {
        return new File(getFilesDir(), FILE_NAME);
    }

    public void saveText(View view) {
        File file = getInternalFile();
        try (FileOutputStream fos = new FileOutputStream(file)) {
            String htmlContent = Html.toHtml(editor.getText(), Html.TO_HTML_PARAGRAPH_LINES_CONSECUTIVE);
            fos.write(htmlContent.getBytes());
            Toast.makeText(this, "Файл сохранён", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, "Ошибка: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void openText(View view) {
        File file = getInternalFile();
        if (!file.exists()) {
            Toast.makeText(this, "Файл не найден", Toast.LENGTH_SHORT).show();
            return;
        }
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] bytes = new byte[(int) file.length()];
            int readBytes = fis.read(bytes);
            String text = readBytes > 0 ? new String(bytes, 0, readBytes) : "";
            editor.setText(Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY));
            editor.setSelection(editor.getText().length());
            Toast.makeText(this, "Заметка загружена. Можно редактировать.",
                    Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, "Ошибка: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void clearText(View view) {
        editor.setText("");
    }
}
