package com.example.mypersonalandroidproject.fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.mypersonalandroidproject.R;
import com.example.mypersonalandroidproject.database.DatabaseHelper;
import com.example.mypersonalandroidproject.listener.UpdateEventListener;
import com.example.mypersonalandroidproject.model.Event;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UpdateEventDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpdateEventDialogFragment extends DialogFragment {

    public static String TAG = "UpdateEventDialogFragment";
    public static int RESULT_UPDATED_SUCCEED = 1111;

    private DatabaseHelper databaseHelper;

    private TextView tvEventId;
    private EditText etActivityName;
    private EditText etLocation;
    private TextView tvEventDate;
    private TextView tvAttendingTime;
    private EditText etReporterName;
    private Button btnUpdateEvent;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_EVENT = "param_event";
    private static final String ARG_EVENT_LISTENER = "param_event_listener";

    // TODO: Rename and change types of parameters
    private Event event;
    private UpdateEventListener listener;

    public UpdateEventDialogFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment UpdateEventDialogFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UpdateEventDialogFragment newInstance(Event event, UpdateEventListener listener) {
        UpdateEventDialogFragment fragment = new UpdateEventDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_EVENT, event);
        args.putSerializable(ARG_EVENT_LISTENER, listener);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            event = (Event)getArguments().getSerializable(ARG_EVENT);
            listener = (UpdateEventListener)getArguments().getSerializable(ARG_EVENT_LISTENER);
        }

        databaseHelper = new DatabaseHelper(requireContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Drawable drawable = ContextCompat.getDrawable(requireContext(), android.R.color.transparent);
        InsetDrawable inset =  new InsetDrawable(drawable, 20);

        getDialog().getWindow().setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP);
        getDialog().getWindow().setBackgroundDrawable(inset);
        getDialog().setCancelable(true);
        setCancelable(true);
        return inflater.inflate(R.layout.fragment_update_event_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
    }

    @SuppressLint("SetTextI18n")
    private void initView(View view) {
        tvEventId = view.findViewById(R.id.tvEventId);
        etActivityName = view.findViewById(R.id.etActivityName);
        etLocation = view.findViewById(R.id.etLocation);
        tvEventDate = view.findViewById(R.id.tvEventDateUpdateEvent);
        tvAttendingTime = view.findViewById(R.id.tvAttendingTimeUpdateEvent);
        etReporterName = view.findViewById(R.id.etReporterNameUpdateEvent);
        btnUpdateEvent = view.findViewById(R.id.btnUpdateEvent);

        tvEventId.setText(Integer.toString(event.getEventId()));
        etActivityName.setText(event.getActivityName());
        etLocation.setText(event.getLocation());
        tvEventDate.setText(event.getEventDate());
        tvAttendingTime.setText(event.getAttendingTime());
        etReporterName.setText(event.getReporterName());

        tvEventDate.setOnClickListener((v) -> {
            showDatePicker();
        });

        tvAttendingTime.setOnClickListener((v) -> {
            showTimePicker();
        });

        btnUpdateEvent.setOnClickListener((v) -> {
            updateEvent();
        });

        final ImageButton btnClose = view.findViewById(R.id.btnCloseUpdateEvent);
        btnClose.setOnClickListener((v) -> {
            dismiss();
        });

    }

    private void updateEvent() {
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

        this.event = new Event(
                this.event.getEventId(),
                activityName,
                location,
                eventDate,
                attendingTime,
                reporterName
        );

        if(databaseHelper.updateEvent(this.event)) {
            Toast.makeText(requireContext(),"Update succeed!", Toast.LENGTH_SHORT).show();
            // gọi listener để hứng sự kiện đã cập nhật thành công
            // va2 gọi lại nơi @Override
            listener.onUpdated(this.event);
        } else {
            Toast.makeText(requireContext(),"Update failed!", Toast.LENGTH_SHORT).show();
        }
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