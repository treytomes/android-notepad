package com.treytomes.notepad;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "MainActivity";
    private static final String FILE_EXTENSION = ".txt";

    private String _title;
    private String _filename;

    public MainActivity() {
        _title = "";
        _filename = "";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        _title = getTitle().toString();
        setTitleWithFilename();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        // The action bar will automatically handle clicks on the Home/Up button,
        // so long as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_new:
                newFile();
                return true;
            case R.id.action_open:
                openFile();
                return true;
            case R.id.action_save:
                saveFile();
                return true;
            case R.id.action_saveAs:
                saveFileAs();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void newFile() {
        _filename = "";
        ((EditText)findViewById(R.id.edit_text)).setText("");
        setTitleWithFilename();
    }

    private void openFile() {
        List<File> files = FileSystem.getFiles(FILE_EXTENSION);

        final String[] fileNames = new String[files.size()];
        for (int index = 0; index < files.size(); index++) {
            fileNames[index] = files.get(index).getName();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Open File")
                .setItems(fileNames, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        _filename = fileNames[which];
                        File file = new File(FileSystem.getDocumentStoragePath(), _filename);

                        Snackbar.make(findViewById(R.id.edit_text), MessageFormat.format("Opening {0}.", file), Snackbar.LENGTH_LONG).show();

                        try {
                            ((EditText)findViewById(R.id.edit_text)).setText(FileSystem.readAllText(file));
                            setTitleWithFilename();
                        } catch (IOException e) {
                            Dialogs.showMessageBox(MainActivity.this, e.toString());
                        }
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void saveFile() {
        if (_filename.length() == 0) {
            saveFileAs();
        } else {
            File file = new File(FileSystem.getDocumentStoragePath(), _filename);
            Snackbar.make(findViewById(R.id.edit_text), MessageFormat.format("Saving {0}.", file), Snackbar.LENGTH_LONG).show();


            String text = ((EditText)findViewById(R.id.edit_text)).getText().toString();
            try {
                FileSystem.writeAllText(file, text);
            } catch (IOException e) {
                Dialogs.showMessageBox(this, e.getMessage());
            }
        }
    }

    private void saveFileAs() {
        if (FileSystem.isExternalStorageWritable()) {
            Dialogs.showInputBox(this, "Enter filename to save as:", "filename.txt", new IInputTextListener() {
                @Override
                public void onInputTextReceived(String inputText) {
                    if (!inputText.endsWith(FILE_EXTENSION)) {
                        inputText = inputText + FILE_EXTENSION;
                    }

                    _filename = inputText;
                    saveFile();
                    setTitleWithFilename();
                }
            });
        } else {
            Snackbar.make(findViewById(R.id.edit_text), "External storage is not writable.", Snackbar.LENGTH_LONG).show();
        }
    }

    private void setTitleWithFilename() {
        if (_filename.length() == 0) {
            setTitle(_title);
        } else {
            setTitle(MessageFormat.format("{0} - {1}", _title, _filename));
        }
    }
}
