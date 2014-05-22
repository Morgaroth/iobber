package pl.edu.agh.iobber.android.finding;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pl.edu.agh.iobber.R;

public class FindingFragment extends Fragment {

    public FindingFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.finding_fragment_layout, container, false);
        return inflate;
    }
}
