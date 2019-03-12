package com.example.labourondemand;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;


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
            customer = bundle.getParcelable("customer");
        }
    }

    private Customer customer;
    private RecyclerView recyclerView;
    private DashboardAdapter customerDashboardAdapter;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private Services services = new Services();
    private TextView noResponse;

    //vars(demo)
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mFroms = new ArrayList<>();
    private ArrayList<String> mTos = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_customer_jobs, container, false);

        noResponse = view.findViewById(R.id.customer_dashboard2_tv_no_response);
        customer = (Customer) mActivity.getIntent().getExtras().get("customer");
        //services = (Services) getIntent().getExtras().get("services");
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

//        recyclerView = view.findViewById(R.id.customer_jobs_rv);
//        customerDashboardAdapter = new DashboardAdapter(getActivity() ,1,new ArrayList<Labourer>(), services);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        recyclerView.setAdapter(customerDashboardAdapter);
//        recyclerView.setHasFixedSize(false);
        //fetchLabourResponses();

        initText();

        //dummy for presenting
        RecyclerView recyclerView = view.findViewById(R.id.customer_jobs_rv);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(getActivity(), mNames, mFroms, mTos);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    //dummy function
    private void initText() {
        //Log.d(TAG, "initImageBitmaps: preparing bitmaps.");

        mNames.add("Shanthanu");
        mNames.add("Varun");
        mNames.add("Narayan");
        mNames.add("Prajwal");
        mNames.add("Manan");
        mNames.add("Srivatsan");
        mNames.add("dummy 1");
        mNames.add("dummy 2");
        mNames.add("dummy 3");

        mFroms.add("Bombay");
        mFroms.add("Delhi");
        mFroms.add("Udupi");

        mTos.add("Udupi");
        mTos.add("Delhi");
        mTos.add("Bombay");

        for(int i = 0; i < 6; i++) {
            mFroms.add("location" + i);
            mTos.add("location" + (9+i));
        }
    }

    private void fetchLabourResponses() {

        firebaseFirestore.collection("services").document(customer.getCurrentService()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        Log.d("service in dashboard22", customer.getCurrentService());
                        services = documentSnapshot.toObject(Services.class);
                        services.setServiceID(customer.getCurrentService());
                        Log.d("service in dashboard22", services.getAddressLine1()+"!");
                        if(services.getLabourerResponses() != null) {
                            noResponse.setVisibility(View.GONE);
                            customerDashboardAdapter.setServiceAndCustomer(services, customer);
                            Log.d("customerboardAdapter",services.getLabourerResponses().toString());
                            for (final String s : services.getLabourerResponses().keySet()) {
                                firebaseFirestore.collection("labourer").document(s)
                                        .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        Labourer labourer = new Labourer();
                                        labourer = documentSnapshot.toObject(Labourer.class);
                                        labourer.setCurrentServicePrice(services.getLabourerResponses().get(s));
                                        customerDashboardAdapter.addedFromCustomer(labourer);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });
                            }

                        }else{
                            noResponse.setText("No Response from any Labourers");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
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
