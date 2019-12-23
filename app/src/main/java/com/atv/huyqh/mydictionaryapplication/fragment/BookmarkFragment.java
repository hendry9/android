package com.atv.huyqh.mydictionaryapplication.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.atv.huyqh.mydictionaryapplication.adapter.BookmarkAdapter;
import com.atv.huyqh.mydictionaryapplication.db_helper.DataBaseHelper;
import com.atv.huyqh.mydictionaryapplication.share.FragmentListener;
import com.atv.huyqh.mydictionaryapplication.share.ListItemListener;
import com.atv.huyqh.mydictionaryapplication.MainActivity;
import com.atv.huyqh.mydictionaryapplication.R;

public class BookmarkFragment extends Fragment {

    private FragmentListener listener;
    private DataBaseHelper dataBaseHelper;

    public BookmarkFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bookmark, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dataBaseHelper = new DataBaseHelper(view.getContext());

        ListView bookmarkList = view.findViewById(R.id.bookmark_list);

        //Get All Work Mark
        final BookmarkAdapter adapter = new BookmarkAdapter(getActivity(), dataBaseHelper.getAllWordFromBookmark());
        bookmarkList.setAdapter(adapter);

        adapter.setOnClickListener(new ListItemListener() {
            @Override
            public void onClickItem(int position) {
                if (listener != null)
                    listener.onItemClick(String.valueOf(adapter.getItem(position)));
            }
        });

        adapter.setOnClickDelListener(new ListItemListener() {
            @Override
            public void onClickItem(int position) {
                adapter.removeItem(position);
                adapter.notifyDataSetChanged();
            }
        });

        ((MainActivity) getActivity()).hideSearchBar();
        getActivity().setTitle("Bookmark");

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void setOnFragmentListener(FragmentListener listener) {
        this.listener = listener;
    }

}
