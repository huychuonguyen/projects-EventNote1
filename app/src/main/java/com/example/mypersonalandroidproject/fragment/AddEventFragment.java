package com.example.mypersonalandroidproject.fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.mypersonalandroidproject.R;
import com.example.mypersonalandroidproject.database.DatabaseHelper;
import com.example.mypersonalandroidproject.model.Event;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddEventFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddEventFragment extends Fragment {

    public static String TAG = "AddEventFragment";

    private DatabaseHelper databaseHelper;

    private EditText etActivityName;
    private EditText etLocation;
    private TextView tvEventDate;
    private TextView tvAttendingTime;
    private EditText etReporterName;
    private Button btnAddEvent;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddEventFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddEventFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddEventFragment newInstance(String param1, String param2) {
        AddEventFragment fragment = new AddEventFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        databaseHelper = new DatabaseHelper(requireContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_event, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
    }


    private void initView(View view) {
        etActivityName = view.findViewById(R.id.etActivityName);
        etLocation = view.findViewById(R.id.etLocation);
        tvEventDate = view.findViewById(R.id.tvEventDateAddEvent);
        tvAttendingTime = view.findViewById(R.id.tvAttendingTimeAddEvent);
        etReporterName = view.findViewById(R.id.etReporterName);
        btnAddEvent = view.findViewById(R.id.btnAddEvent);


        tvEventDate.setOnClickListener((v) -> {
            showDatePicker();
        });

        tvAttendingTime.setOnClickListener((v) -> {
            showTimePicker();
        });

        btnAddEvent.setOnClickListener((v) -> {
            addEvent();
        });

    }

    private void showDatePicker() {
        Calendar calendarDatePicker = Calendar.getInstance();
        @SuppressLint("SetTextI18n")
        final DatePickerDialog.OnDateSetListener listener = (v, year, monthOfYear, dayOfMonth) -> {
            String month = normalizeNumber(monthOfYear + 1);
            String day = normalizeNumber(dayOfMonth);

            tvEventDate.setText(month + "/" + day + "/" + year);
        };

        new DatePickerDialog(requireContext(), listener, calendarDatePicker
                .get(Calendar.YEAR), calendarDatePicker.get(Calendar.MONTH),
                calendarDatePicker.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void showTimePicker() {
        Calendar calendarTimePicker = Calendar.getInstance();
        final TimePickerDialog.OnTimeSetListener timePickerListener =  (v, hourOfDay, minute) -> {
            String am_pm;
            int hour = hourOfDay;
            if(hour < 12) {
                am_pm = "AM";
            }
            else {
                am_pm = "PM";
                hour %= 12;
                if(hour == 0)
                    hour = 12;
            }

            String time = normalizeNumber(hour) + ":" + normalizeNumber(minute) + " " + am_pm;
            tvAttendingTime.setText(time);
        };


        int hour = calendarTimePicker.get(Calendar.HOUR);
        int minute = calendarTimePicker.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(
                requireContext(),
                timePickerListener,
                hour,
                minute,
                false);
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    private void addEvent() {
        String activityName  = "";
        String location = "";
        String eventDate = "";
        String attendingTime = "";
        String reporterName = "";

        if(etActivityName.getText().toString().isEmpty()) {
            showAlert("Activity name is required!");
            return;
        } else {
            activityName = etActivityName.getText().toString();
        }

        if(etLocation.getText().toString().isEmpty()) {
            showAlert("ALocation is required!");
            return;
        } else {
            location = etLocation.getText().toString();
        }

        if(tvEventDate.getText().toString().isEmpty()) {
            showAlert("Date is required!");
            return;
        } else {
            eventDate = tvEventDate.getText().toString();
        }

        if(tvAttendingTime.getText().toString().isEmpty()) {
            showAlert("Time of attending is required!");
            return;
        } else {
            attendingTime = tvAttendingTime.getText().toString();
        }

        if(etReporterName.getText().toString().isEmpty()) {
            showAlert("Name of the reporter is required!");
            return;
        } else {
            reporterName = etReporterName.getText().toString();
        }

        Event event = new Event(
                activityName,
                location,
                eventDate,
                attendingTime,
                reporterName
        );

        if(databaseHelper.addEvent(event)) {
            Toast.makeText(requireContext(),"Add succeed!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(requireContext(),"Add failed!", Toast.LENGTH_SHORT).show();
        }
    }

    private void showAlert(String message) {
        new AlertDialog.Builder(requireContext())
                .setMessage(message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with delete operation
                    }
                })
                .show();
    }

    private String normalizeNumber(int number) {
        if(number/10 == 0)
            return "0" + number;
        return Integer.toString(number);
    }
}