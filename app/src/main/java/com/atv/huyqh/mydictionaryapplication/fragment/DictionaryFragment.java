package com.atv.huyqh.mydictionaryapplication.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.atv.huyqh.mydictionaryapplication.db_helper.DataBaseHelper;
import com.atv.huyqh.mydictionaryapplication.helper.EndlessScrollListener;
import com.atv.huyqh.mydictionaryapplication.share.FragmentListener;
import com.atv.huyqh.mydictionaryapplication.share.Global;
import com.atv.huyqh.mydictionaryapplication.helper.HideKeyboard;
import com.atv.huyqh.mydictionaryapplication.MainActivity;
import com.atv.huyqh.mydictionaryapplication.R;

import java.util.ArrayList;
import static com.atv.huyqh.mydictionaryapplication.db_helper.DataBaseHelper.TB_EN_VN;
import static com.atv.huyqh.mydictionaryapplication.db_helper.DataBaseHelper.TB_VN_EN;

public class DictionaryFragment extends Fragment {

    private FragmentListener listener;
    ArrayAdapter<String> adapter;
    ListView list_word;
    private ArrayList<String> memSource;
    private DataBaseHelper dataBaseHelper;
    private boolean isSearch = false;

    public DictionaryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dictionary, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dataBaseHelper = new DataBaseHelper(view.getContext());

        list_word = view.findViewById(R.id.list_word);
        final String type = Global.getState(getActivity(), "dic_type");

        if (type != null && type.equals("ev")) {
            memSource = dataBaseHelper.getWord(TB_EN_VN, 0);
        } else {
            memSource = dataBaseHelper.getWord(TB_VN_EN, 0);
        }

        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, memSource);
        list_word.setAdapter(adapter);
        list_word.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HideKeyboard.hideKeyboard(getActivity());
                if (listener != null)
                    listener.onItemClick(memSource.get(position));
            }
        });

        list_word.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                if (isSearch) return true;
                if (type != null && type.equals("ev")) {
                    memSource.addAll(dataBaseHelper.getWord(TB_EN_VN, page));
                } else {
                    memSource.addAll(dataBaseHelper.getWord(TB_VN_EN, page));
                }
                adapter.notifyDataSetChanged();
                return true;
            }
        });

        ((MainActivity) getActivity()).showSearchBar();
    }

    //Search word By value
    public void filterValue(String value, String table) {
        if (value.trim().length() > 0) isSearch = true;
        else {
            isSearch = false;
            return;
        }
        memSource.clear();
        memSource.addAll(dataBaseHelper.search(table, value));
        adapter.notifyDataSetChanged();
    }

    public void setOnFragmentListener(FragmentListener listener) {
        this.listener = listener;
    }

    //Reset Data Source
    public void resetDataSource(String type) {
        memSource.clear();
        if (type.equals("ev")) {
            memSource.addAll(dataBaseHelper.getWord(TB_EN_VN, 0));
        } else {
            memSource.addAll(dataBaseHelper.getWord(TB_VN_EN, 0));
        }
        adapter.notifyDataSetChanged();
    }
}
