package edu.byu.cs.tweeter.server.resources;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.shared.model.domain.Status;

public class StatusResultsPage extends ResultsPage {

    /**
     * The data values returned in this page of results
     */
    private List<Status> statuses;

    public StatusResultsPage() {
        statuses = new ArrayList<>();
    }

    public void addStatus(Status v) {
        statuses.add(v);
    }

    public List<Status> getStatuses() {
        return statuses;
    }

}

