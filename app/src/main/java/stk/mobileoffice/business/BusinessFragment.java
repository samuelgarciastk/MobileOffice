package stk.mobileoffice.business;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import stk.mobileoffice.R;

public class BusinessFragment extends Fragment {
	private View view;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.business_content, container, false);
		((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("业务");
		return view;
	}
}
