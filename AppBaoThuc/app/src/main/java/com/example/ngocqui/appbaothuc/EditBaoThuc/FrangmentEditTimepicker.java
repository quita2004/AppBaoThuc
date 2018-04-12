package com.example.ngocqui.appbaothuc.EditBaoThuc;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.ngocqui.appbaothuc.R;

public class FrangmentEditTimepicker extends Fragment {
    TimePicker timePicker;
    TextView txtChonGio;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_timepicker, container, false);

        timePicker = view.findViewById(R.id.timePicker1);
        txtChonGio = view.findViewById(R.id.textViewChonGio);

        timePicker.setIs24HourView(true);

        return view;
    }
}
