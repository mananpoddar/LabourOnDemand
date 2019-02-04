package com.example.labourondemand;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class AddressSetupFragment extends Fragment {

    private EditText a1, a2, a3, state, city;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setup_address, container, false);

        a1 = view.findViewById(R.id.setup_address_et_address_line_1);
        a2 = view.findViewById(R.id.setup_address_et_address_line_2);
        a3 = view.findViewById(R.id.setup_address_et_address_line_3);
        city = view.findViewById(R.id.setup_address_et_city);
        state = view.findViewById(R.id.setup_address_et_state);

        return view;
    }

    public Boolean isEmpty(){
        if(TextUtils.isEmpty(a1.getText())){
            a1.setError("error");
           return false;
        }
        if(TextUtils.isEmpty(a2.getText())){
            a2.setError("error");
            return false;
        }
        if(TextUtils.isEmpty(a3.getText())){
            a3.setError("error");
            return false;
        }
        if(TextUtils.isEmpty(state.getText())){
            state.setError("error");
            return false;
        }if(TextUtils.isEmpty(city.getText())){
            city.setError("error");
            return false;
        }

        return true;
    }
}
