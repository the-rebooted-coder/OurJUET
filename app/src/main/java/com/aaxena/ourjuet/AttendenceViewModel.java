package com.aaxena.ourjuet;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.aaxena.ourjuet.data.AttendenceData;

import java.util.List;

import javax.inject.Inject;


public class AttendenceViewModel extends ViewModel {
    private AttendenceRepository mAttendenceRepository;
    private MutableLiveData<Boolean> mIsLoading;
    private MutableLiveData<Integer> mEmptyImage;
    @Inject
    public AttendenceViewModel(AttendenceRepository repository) {
        mAttendenceRepository = repository;
        mIsLoading = new MutableLiveData<>();
        mEmptyImage = new MutableLiveData<>();

//        context = application;
    }

    public LiveData<List<AttendenceData>> getAttendenceData(){


        return Transformations.map(mAttendenceRepository.getAttendenceData(),(list) -> {
            if (mEmptyImage == null){
                mEmptyImage = new MutableLiveData<>();
            }
            if (list.size() == 0){
                mEmptyImage.setValue(R.drawable.sync);
            }else{
                mEmptyImage.setValue(null);
            }
           return list;
        });
    }

    public LiveData<Boolean> getIsLoading() {
        if (mIsLoading == null){
            mIsLoading = new MutableLiveData<>();
        }
        return mIsLoading;
    }

    public void setLoading(){
        mIsLoading.setValue(true);
    }

    public void setLoading(boolean b) {
        mIsLoading.setValue(b);
    }

    public MutableLiveData<Integer> getEmptyImage() {
        return mEmptyImage;
    }
}
