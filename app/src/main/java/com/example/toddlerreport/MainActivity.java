package com.example.toddlerreport;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmAsyncTask;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity implements NewStudentDialog.NewStudentDialogListener,
        AdapterView.OnItemClickListener {

    private Realm realm;
    private RealmAsyncTask realmAsyncTask;

    SwipeMenuListView listView;

    MyHelper helper;
    RealmChangeListener realmChangeListener;

    String studentId;
    String deletedName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = getSharedPreferences("prefs", MODE_PRIVATE);
        boolean firstStart = sharedPreferences.getBoolean("firstStart", true);


        if (firstStart) {
            showStartDialog();
        }

        realm = Realm.getDefaultInstance();
        final RealmResults<DataModel> students = realm.where(DataModel.class).findAll();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        helper = new MyHelper(realm);
        helper.selectFromRealm();

        CustomAdapter adapter = new CustomAdapter(this, helper.justRefresh());
        listView = findViewById(R.id.listView);
        listView.setAdapter(adapter);

        Refresh();

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {

                SwipeMenuItem editItem = new SwipeMenuItem(getApplicationContext());
                editItem.setBackground(new ColorDrawable(Color.rgb(208, 211, 212)));
                editItem.setWidth(170);
                editItem.setIcon(R.drawable.ic_edit);

                SwipeMenuItem deleteItem = new SwipeMenuItem(getApplicationContext());
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
                deleteItem.setWidth(170);
                deleteItem.setIcon(R.drawable.ic_delete);

                menu.addMenuItem(editItem);
                menu.addMenuItem(deleteItem);
            }
        };
        listView.setMenuCreator(creator);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String passName = students.get(position).getName();
                String phoneOne = students.get(position).getPhoneOne();
                String phoneTwo = students.get(position).getPhoneTwo();

                Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                intent.putExtra("studentName", passName);
                intent.putExtra("phoneOne", phoneOne);
                intent.putExtra("phoneTwo", phoneTwo);
                startActivity(intent);

            }
        });

        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {

                switch (index) {

                    case 0:

                        final DataModel student;
                        realm = Realm.getDefaultInstance();

                        studentId = students.get(position).getId();

                        student = realm.where(DataModel.class).equalTo("id", studentId).findFirst();

                        System.out.println(student.name);

                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("  Update Student Info");

                        final EditText studentName = new EditText(MainActivity.this);
                        final EditText phoneOne = new EditText(MainActivity.this);
                        final EditText phoneTwo = new EditText(MainActivity.this);

                        studentName.setText(student.name);
                        phoneOne.setText(student.phoneOne);
                        phoneTwo.setText(student.phoneTwo);


                        builder.setView(studentName);
                        builder.setView(phoneOne);
                        builder.setView(phoneTwo);

                        LinearLayout layoutName = new LinearLayout(MainActivity.this);
                        layoutName.setOrientation(LinearLayout.VERTICAL);
                        layoutName.setPadding(40, 40, 40, 40);
                        layoutName.addView(studentName);
                        layoutName.addView(phoneOne);
                        layoutName.addView(phoneTwo);
                        builder.setView(layoutName);

                        builder.setNegativeButton("Cancel", null)
                                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        realm.beginTransaction();
                                        student.setName(studentName.getText().toString());
                                        student.setPhoneOne(phoneOne.getText().toString());
                                        student.setPhoneTwo(phoneTwo.getText().toString());
                                        realm.commitTransaction();

                                        String studentUpdated = studentName.getText().toString();

                                        Toast.makeText(getApplicationContext(), studentUpdated
                                                + " has been updated.", Toast.LENGTH_SHORT).show();
                                    }
                                });

                        final AlertDialog alert = builder.create();
                        alert.setCancelable(false);
                        alert.setOnShowListener(new DialogInterface.OnShowListener() {
                            @Override
                            public void onShow(DialogInterface dialog) {
                                alert.getButton((AlertDialog.BUTTON_POSITIVE)).setTextColor(Color.BLACK);
                                alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.RED);
                            }
                        });
                        alert.show();
                        break;

                    case 1:

                        deletedName = students.get(position).getName();

                        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(MainActivity.this);
                        alertBuilder.setMessage("Are you sure you want to delete this student?")
                                .setNegativeButton("Cancel", null)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        realm.executeTransaction(new Realm.Transaction() {
                                            @Override
                                            public void execute(Realm realm) {
                                                students.deleteFromRealm(position);
                                            }
                                        });

                                        Toast.makeText(getApplicationContext(), deletedName
                                                + " has been deleted.", Toast.LENGTH_SHORT).show();
                                    }

                                });
                        final AlertDialog alertDialog = alertBuilder.create();
                        alertDialog.setCancelable(false);
                        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                            @Override
                            public void onShow(DialogInterface dialog) {
                                alertDialog.getButton((AlertDialog.BUTTON_POSITIVE)).setTextColor(Color.BLACK);
                                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.RED);
                            }
                        });
                        alertDialog.show();
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        String student = listView.getItemAtPosition(position).toString();

        Toast.makeText(getApplicationContext(), "Clicked " + student, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.add) {

            addStudent();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void addStudent() {
        NewStudentDialog newStudentDialog = new NewStudentDialog();
        newStudentDialog.show(getSupportFragmentManager(), "new student dialog");
        newStudentDialog.setCancelable(false);
    }

    @Override
    public void applyTexts(final String name, final String phoneOne, final String phoneTwo) {

        final String id = UUID.randomUUID().toString();

        realmAsyncTask = realm.executeTransactionAsync(
                new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        DataModel student = realm.createObject(DataModel.class, id);
                        student.setName(name);
                        student.setPhoneOne(phoneOne);
                        student.setPhoneTwo(phoneTwo);
                    }
                },
                new Realm.Transaction.OnSuccess() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(MainActivity.this, name + " has been added.",
                                Toast.LENGTH_SHORT).show();
                    }
                },
                new Realm.Transaction.OnError() {
                    @Override
                    public void onError(Throwable error) {
                        Toast.makeText(MainActivity.this, "ERROR",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void Refresh() {
        realmChangeListener = new RealmChangeListener() {
            @Override
            public void onChange(Object o) {
                CustomAdapter adapter = new CustomAdapter(MainActivity.this, helper.justRefresh());
                listView.setAdapter(adapter);
            }
        };
        realm.addChangeListener(realmChangeListener);
    }

    @Override
    public void onStop() {
        super.onStop();

        if (realmAsyncTask != null && realmAsyncTask.isCancelled()) {
            realmAsyncTask.cancel();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    private void showStartDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Welcome");
        builder.setMessage("Tap the + to add a student. \nSwipe left to edit or delete. " +
                "\nTap a name to fill out a report.");
        builder.setPositiveButton("Got it", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        final AlertDialog alert = builder.create();
        alert.setCancelable(false);
        alert.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
            }
        });
        alert.show();

        SharedPreferences sharedPreferences = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("firstStart", false);
        editor.apply();
    }
}
