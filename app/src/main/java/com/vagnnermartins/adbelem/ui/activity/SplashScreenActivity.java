package com.vagnnermartins.adbelem.ui.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.vagnnermartins.adbelem.R;
import com.vagnnermartins.adbelem.app.App;
import com.vagnnermartins.adbelem.enums.StatusEnum;
import com.vagnnermartins.adbelem.parse.ChurchParse;
import com.vagnnermartins.adbelem.util.NavegacaoUtil;

import java.util.HashSet;
import java.util.List;

public class SplashScreenActivity extends ActionBarActivity {

    private static final int TOTAL_UPDATE = 1;

    private App app;
    private int updated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        init();
        checkStatus(StatusEnum.INICIO);
    }

    private void init() {
        app = (App) getApplication();
    }

    private void checkStatus(StatusEnum statusEnum){
        if(statusEnum == StatusEnum.INICIO){
            updated = 0;
            ChurchParse.findMyChurches(onFindMyChurches());
            checkStatus(StatusEnum.EXECUTANDO);
        }else if(statusEnum == StatusEnum.EXECUTANDO){
        }else if(statusEnum == StatusEnum.EXECUTADO){
            checkStatusExecutado();
        }
    }

    private void checkStatusExecutado() {
        updated++;
        if(updated == TOTAL_UPDATE){
            NavegacaoUtil.navegar(this, MainActivity.class);
            finish();
        }
    }

    private FindCallback<ChurchParse> onFindMyChurches() {
        return new FindCallback<ChurchParse>() {
            @Override
            public void done(List<ChurchParse> result, ParseException e) {
                app.myChurches = result;
                checkStatus(StatusEnum.EXECUTADO);
            }
        };
    }
}