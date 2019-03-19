package com.example.labourondemand;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static android.support.constraint.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CustomerJobsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CustomerJobsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CustomerJobsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public CustomerJobsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CustomerJobsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CustomerJobsFragment newInstance(String param1, String param2) {
        CustomerJobsFragment fragment = new CustomerJobsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();

        if (bundle != null) {
            customer =  bundle.getParcelable("customer");
            currentService = bundle.getParcelable("service");
            Log.d(TAG, "onCreate: bundle recieved");
        }
    }

    private CustomerFinal customer;
    private ServicesFinal currentService;
    private RecyclerView recyclerView;
    private CustomerJobsAdapter customerJobsAdapter;
    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    private Services services = new Services();
    private TextView noResponse;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_customer_jobs, container, false);

        noResponse = view.findViewById(R.id.customer_dashboard2_tv_no_response);
        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        recyclerView = view.findViewById(R.id.customer_jobs_rv);
        customerJobsAdapter = new CustomerJobsAdapter(getActivity(), currentService);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(customerJobsAdapter);
        recyclerView.setHasFixedSize(false);


        db.collection("services").document(currentService.getServiceId())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@javax.annotation.Nullable DocumentSnapshot snapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {

                        if (e != null) {
                            Log.w(TAG, "listen:error", e);
                            return;
                        }

                        if (snapshot != null && snapshot.exists()) {
                            Log.d(TAG, "Current data: " + snapshot.getData());
                            ServicesFinal updatedService = snapshot.toObject(ServicesFinal.class);
                            ArrayList<LabourerFinal> labourersToBeAdded = updatedService.getLabourers();
                            labourersToBeAdded.removeAll(currentService.getLabourers());
                            for(int i = 0; i < labourersToBeAdded.size(); i++) {
                                customerJobsAdapter.addLabourer(labourersToBeAdded.get(i));
                            }
                        } else {
                            Log.d(TAG, "Current data: null");
                        }

                    }
                });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    Activity mActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof Activity) {
                mActivity = (Activity) context;
        }
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
