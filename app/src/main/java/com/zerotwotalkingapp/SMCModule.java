package com.zerotwotalkingapp;

import com.google.firebase.database.Exclude;

public class SMCModule {

    private String nodeKey;

    public SMCModule() {


    }

    public SMCModule(String nodeKey) {
        this.nodeKey = nodeKey;
    }

    @Exclude
    public String getNodeKey() {
        return nodeKey;
    }

    @Exclude
    public void setNodeKey(String nodeKey) {
        this.nodeKey = nodeKey;
    }
}
