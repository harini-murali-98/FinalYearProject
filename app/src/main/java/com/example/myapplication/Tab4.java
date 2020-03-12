package com.example.myapplication;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;


public class Tab4 extends Fragment {
    // TODO: Rename parameter arguments, choose names that match

    TextView r4c1,r4c2,r4c3,r4c4;
    // TODO: Rename and change types of parameters

    private OnFragmentInteractionListener mListener;

    public Tab4() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
         //   mParam1 = getArguments().getString(ARG_PARAM1);
           // mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View RootView = inflater.inflate(R.layout.fragment_tab4, container, false);
        String vulString,detailsString;
        MainActivity ma=(MainActivity) getActivity();
        vulString=ma.vulResult;
        detailsString=ma.detailsResult;

        r4c1=(TextView) RootView.findViewById(R.id.r4c1);
        r4c2=(TextView) RootView.findViewById(R.id.r4c2);
        r4c3=(TextView) RootView.findViewById(R.id.r4c3);
        r4c4=(TextView) RootView.findViewById(R.id.r4c4);


        String vul[]  = vulString.split("\n");
        String details[] = detailsString.split("\n\n");
        r4c1.setText("Clipboard data");
        if(vul[3].equalsIgnoreCase("Yes"))
            r4c2.setText("Vulnerable");
        else
            r4c2.setText("Not Vulnerable");

        if(vul[3].equalsIgnoreCase("Yes"))
            r4c3.setText("Application is reading clipboard and sensitive data could be compromised. Disable the clipboard");
        else
            r4c3.setText("No data leakage");


        r4c4.setText(details[3]);
        return RootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
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

