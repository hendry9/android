package com.atv.huyqh.mydictionaryapplication.fragment;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.atv.huyqh.mydictionaryapplication.db_helper.DataBaseHelper;
import com.atv.huyqh.mydictionaryapplication.share.Global;
import com.atv.huyqh.mydictionaryapplication.MainActivity;
import com.atv.huyqh.mydictionaryapplication.R;
import com.atv.huyqh.mydictionaryapplication.model.Word;

import java.util.Locale;

public class DetailFragment extends Fragment {

    private TextView word_detail, word_meaning;
    private ImageButton btn_mark, btn_spelling;
    private DataBaseHelper dataBaseHelper;
    private String tu;
    private String nghia;
    private TextToSpeech textToSpeech;


    public DetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    //Init Detail Fragment Screen
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final String type = Global.getState(getActivity(), "dic_type");

        dataBaseHelper = new DataBaseHelper(view.getContext());
        tu = getArguments().getString("tu");
        nghia = getArguments().getString("nghia");

        btn_mark = view.findViewById(R.id.btn_mark);
        btn_spelling = view.findViewById(R.id.btn_spelling);

        boolean isMark = dataBaseHelper.isWordMark(new Word(tu, nghia));
        if (isMark) {
            btn_mark.setImageResource(R.drawable.ic_bookmark_fill);
            btn_mark.setTag(1);
        } else {
            btn_mark.setImageResource(R.drawable.ic_bookmark_border);
            btn_mark.setTag(0);
        }

        word_detail = view.findViewById(R.id.work_detail);
        word_detail.setText(tu);
        word_meaning = view.findViewById(R.id.word_meaning);
        word_meaning.setText(nghia);

        ((MainActivity) getActivity()).hideSearchBar();
        getActivity().setTitle("Word Meaning");


        btn_mark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = (int) btn_mark.getTag();
                if (i == 0) {
                    btn_mark.setImageResource(R.drawable.ic_bookmark_fill);
                    btn_mark.setTag(1);
                    dataBaseHelper.addBookmark(new Word(tu, nghia));
                } else if (i == 1) {
                    btn_mark.setImageResource(R.drawable.ic_bookmark_border);
                    btn_mark.setTag(0);
                    dataBaseHelper.removeBookmark(new Word(tu, nghia));
                }
            }
        });

        //Click to hear the spelling
        btn_spelling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speakOut();
            }
        });

        textToSpeech = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    if (type.equals("ev")) {
                        textToSpeech.setLanguage(Locale.UK);
                    } else textToSpeech.setLanguage(Locale.forLanguageTag("vi-VN"));
                }
            }
        });
    }


    private void speakOut() {
        // Text need speech
        String toSpeak = word_detail.getText().toString();
        textToSpeech.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null, null);
    }


}
