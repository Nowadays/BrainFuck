package com.deodexed.brainfuck;

/**
 * Created by Morgan on 12/09/2016.
 */

public interface RemoteDatabaseListener {

        void onExecSQLfinished(String jsondata,String sciptUsed);
        void onDataUpdated();

}
