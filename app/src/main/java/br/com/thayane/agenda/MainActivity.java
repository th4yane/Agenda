package br.com.thayane.agenda;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Outline;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private  List<Task> task;
    public RecyclerView recyclerView;
    private int year, month, day;
    public String todaysDate;
    private DatabaseController dc;
    private Gson gson;
    public RecyclerViewAdapter adapter;
    public TextView dateText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        this.initLFloatingButtons();

        toolbar = (Toolbar) findViewById(R.id.tbmain);
        toolbar.setTitle("Agenda");
        setSupportActionBar(toolbar);

        gson = new Gson();
        dc = new DatabaseController(getBaseContext());

        //get current date
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("ddMMyyyy");

        if (todaysDate == null) {
            todaysDate = df.format(c.getTime());
        }

        dateText = (TextView) findViewById(R.id.tvDate);
        dateText.setText(todaysDate.substring(0, 2) + "/" + todaysDate.substring(2, 4) + "/" + todaysDate.substring(4, 8));

        task = getTaskList(todaysDate);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);

        adapter = new RecyclerViewAdapter(task, getApplication(), dc, todaysDate);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ItemTouchHelper.Callback callback =
                new SimpleItemTouchHelperCallback(adapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.date_picker:

                year = Integer.parseInt(todaysDate.substring(4, 8));
                month = Integer.parseInt(todaysDate.substring(2, 4)) - 1;
                day = Integer.parseInt(todaysDate.substring(0, 2));

                DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                String nDayOfMonth, nMonthOfYear;

                                if (dayOfMonth < 10) {
                                    nDayOfMonth = "0" + dayOfMonth;
                                } else {
                                    nDayOfMonth = String.valueOf(dayOfMonth);
                                }

                                if (monthOfYear + 1 < 10) {
                                    nMonthOfYear = "0" + (monthOfYear + 1);
                                } else {
                                    nMonthOfYear = String.valueOf(monthOfYear + 1);
                                }

                                todaysDate = nDayOfMonth + nMonthOfYear + String.valueOf(year);
                                task = getTaskList(todaysDate);

                                adapter.setList(task);
                                adapter.date = todaysDate;

                                recyclerView.setAdapter(adapter);
                                dateText.setText(todaysDate.substring(0, 2) + "/" + todaysDate.substring(2, 4) + "/" + todaysDate.substring(4, 8));

                            }
                        }, year, month, day);
                datePickerDialog.show();
                break;

            case R.id.action_second_activity:
                this.finish();

            default:
                break;
        }

        return true;
    }

    public List<Task> getTaskList(String date) {
        String todaysTask;
        List<Task> taskList = null;

        Cursor cursor = dc.returnData(date);

        if (cursor.moveToFirst()) {
            todaysTask = cursor.getString(cursor.getColumnIndexOrThrow(CreateDatabase.DESCRIPTION));
            Type type = new TypeToken<List<Task>>() {
            }.getType();
            taskList = gson.fromJson(todaysTask, type);
        }
        if (taskList != null) {
            return taskList;
        } else {
            return new ArrayList<>();
        }
    }

    private void initLFloatingButtons() {

        final int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 56, getResources().getDisplayMetrics());

        final ViewOutlineProvider viewOutlineProvider = new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setOval(0, 0, size, size);
            }
        };

        ImageButton floatingButton = ((ImageButton) findViewById(R.id.floatingButton));
        floatingButton.setOutlineProvider(viewOutlineProvider);
        floatingButton.setClipToOutline(true);
        floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);

                alert.setTitle("New Task:");

                final EditText input = new EditText(MainActivity.this);
                alert.setView(input);


                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        String ntask = input.getText().toString().trim();

                        if (ntask.isEmpty()) {
                            return;
                        }
                        if (task.isEmpty()) {
                            task.add(new Task(ntask));
                            String inputString = gson.toJson(task);
                            dc.insertData(todaysDate, inputString);
                        } else {
                            task.add(new Task(ntask));
                            String inputString = gson.toJson(task);
                            dc.alterData(todaysDate, inputString);
                        }
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //Canceled
                    }
                });

                alert.show();
            }
        });
    }



}
