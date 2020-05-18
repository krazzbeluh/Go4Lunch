package com.paulleclerc.go4lunch.ui.main.fragments.workmates;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class WorkmatesViewModel extends AndroidViewModel {
    private MutableLiveData<String> workmates = new MutableLiveData<>();

    public WorkmatesViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<String> getWorkmates() {
        return workmates;
    }
}
