package com.paulleclerc.go4lunch.closures;

import com.paulleclerc.go4lunch.model.Workmate;

import java.util.List;

public interface FetchWorkmatesCompletion {
    void onComplete(boolean success, List<Workmate> workmates);
}
