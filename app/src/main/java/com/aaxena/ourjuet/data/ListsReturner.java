package com.aaxena.ourjuet.data;

import java.util.ArrayList;

public class ListsReturner {
    private ArrayList<com.aaxena.ourjuet.data.AttendenceData> dataArrayList;
    private ArrayList<ArrayList<com.aaxena.ourjuet.data.AttendenceDetails>> detailsArrayList;

    public ListsReturner(ArrayList<com.aaxena.ourjuet.data.AttendenceData> dataArrayList, ArrayList<ArrayList<com.aaxena.ourjuet.data.AttendenceDetails>> detailsArrayList) {
        this.detailsArrayList = detailsArrayList;
        this.dataArrayList = dataArrayList;
    }

    public ListsReturner() {
        this.detailsArrayList = new ArrayList<>();
        this.dataArrayList = new ArrayList<>();
    }

    public ArrayList<com.aaxena.ourjuet.data.AttendenceData> getDataArrayList() {
        return dataArrayList;
    }

    public ArrayList<ArrayList<com.aaxena.ourjuet.data.AttendenceDetails>> getDetailsArrayList() {
        return detailsArrayList;
    }

    public void clear() {
        this.detailsArrayList.clear();
        this.dataArrayList.clear();
    }
}