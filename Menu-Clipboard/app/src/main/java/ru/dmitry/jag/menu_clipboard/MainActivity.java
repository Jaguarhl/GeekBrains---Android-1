/**
 * @author Kartsev Dmitry <dek.alpha@mail.ru>
 * @since 12.11.16
 */

/*
 * Universal scalable method for copy into clipboard. 16.11.11
 */

package ru.dmitry.jag.menu_clipboard;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.*;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    TextView clipboardView;
    EditText firstName, lastName, email;
    Toast message;
    ClipboardManager clipboard;

    // copying modes
    enum dataFields { all, fName, lName, eMail };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InitApp();
        setFirstNameLongClick();
        setLastNameLongClick();
        setEmailLongClick();
    }

    private void setEmailLongClick() { // listen to long click for popup menu for email field
        email.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                PopupMenu popupMenu = new PopupMenu(MainActivity.this, email);
                popupMenu.inflate(R.menu.popup_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId())
                        {
                            case R.id.copy_this:
                                LinkedList<EditText> e = new LinkedList<> ();
                                e.add(email);
                                copyFromFields(dataFields.eMail, e);
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                popupMenu.show();
                return false;
            }
        });
    }

    private void setLastNameLongClick() { // listen to long click for popup menu for second name field
        lastName.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                PopupMenu popupMenu = new PopupMenu(MainActivity.this, lastName);
                popupMenu.inflate(R.menu.popup_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId())
                        {
                            case R.id.copy_this:
                                LinkedList<EditText> e = new LinkedList<> ();
                                e.add(lastName);
                                copyFromFields(dataFields.lName, e);
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                popupMenu.show();
                return false;
            }
        });
    }

    private void setFirstNameLongClick() { // listen to long click for popup menu for first name field
        firstName.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                PopupMenu popupMenu = new PopupMenu(MainActivity.this, firstName);
                popupMenu.inflate(R.menu.popup_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId())
                        {
                            case R.id.copy_this:
                                LinkedList<EditText> e = new LinkedList<> ();
                                e.add(firstName);
                                copyFromFields(dataFields.fName, e);
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                popupMenu.show();
                return false;
            }
        });
    }

    private void InitApp() { // init views and clipboard
        clipboardView = (TextView)findViewById(R.id.clipboardView);
        firstName = (EditText) findViewById(R.id.editFirstName);
        lastName = (EditText) findViewById(R.id.editSecondName);
        email = (EditText) findViewById(R.id.editEmail);
        clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.copy_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        LinkedList<EditText> eTxtFields = new LinkedList<> ();
        // Get selected menu
        switch (item.getItemId())
        {
            case R.id.copy_all:
                eTxtFields.add(firstName);
                eTxtFields.add(lastName);
                eTxtFields.add(email);
                copyFromFields(dataFields.all, eTxtFields);
                return true;
            case R.id.copy_name:
                eTxtFields.add(firstName);
                copyFromFields(dataFields.fName, eTxtFields);
                return true;
            case R.id.copy_email:
                eTxtFields.add(email);
                copyFromFields(dataFields.eMail, eTxtFields);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onPasteButtonClick(View view)
    {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);
        clipboardView.setText(item.getText());
    }

    private void showToastMessage(int msg) {
        message = Toast.makeText(getApplicationContext(),
                msg, Toast.LENGTH_LONG);
        message.setGravity(Gravity.CENTER, 0, 0);
        message.show();
    }

    private void copyToClipboard(String name, String text) {
        clipboard = (ClipboardManager)
                getSystemService(Context.CLIPBOARD_SERVICE);

        ClipData clip = ClipData.newPlainText(name, text);
        clipboard.setPrimaryClip(clip);
    }

    private void copyFromFields(dataFields field, LinkedList<EditText> eText) {
        boolean result = true;
        switch (field) {
            case fName:
                if ((eText.get(0).getText().toString().equals("")) && (!eText.isEmpty())) showToastMessage(R.string.msg_fname_not_filled);
                else {
                    copyToClipboard("name", eText.get(0).getText().toString());
                }
                break;
            case lName:
                if ((eText.get(0).getText().toString().equals("")) && (!eText.isEmpty())) showToastMessage(R.string.msg_sname_not_filled);
                else {
                    copyToClipboard("sname", eText.get(0).getText().toString());
                }
                break;
            case eMail:
                if ((eText.get(0).getText().toString().equals("")) && (!eText.isEmpty())) showToastMessage(R.string.msg_email_not_filled);
                else {
                    copyToClipboard("email", eText.get(0).getText().toString());
                }
                break;
            case all:
                String allText = "";
                for(EditText e: eText) {
                    if(e.getText().toString().equals("")) {
                        showToastMessage(R.string.msg_not_filled);
                        result = false;
                    }
                    else {
                        allText += (e.getText().toString() + '\n');
                    }
                }
                if(result) copyToClipboard("all", allText);
                break;
        }
    }
}
