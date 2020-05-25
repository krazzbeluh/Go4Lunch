package com.paulleclerc.go4lunch.ui.main.fragments.workmates;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.paulleclerc.go4lunch.model.Workmate;
import com.paulleclerc.go4lunch.repository.WorkmatesRepository;

import java.util.List;

public class WorkmatesViewModel extends AndroidViewModel {
    private final WorkmatesRepository workmatesRepository = new WorkmatesRepository();

    private final MutableLiveData<List<Workmate>> workmates = new MutableLiveData<>();

    public WorkmatesViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Workmate>> getWorkmates() {
        return workmates;
    }

    public void fetchWorkmates() {
        workmatesRepository.fetchWorkmates(getApplication().getApplicationContext(), (success, workmates) -> {
            if (success) this.workmates.setValue(workmates);
        });
    }
}
