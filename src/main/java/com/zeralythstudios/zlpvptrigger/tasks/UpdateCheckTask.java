package com.zeralythstudios.zlpvptrigger.tasks;

import com.zeralythstudios.zlpvptrigger.managers.UpdateChecker;

public class UpdateCheckTask implements Runnable {

    private final UpdateChecker updateChecker;

    public UpdateCheckTask(UpdateChecker updateChecker) {
        this.updateChecker = updateChecker;
    }

    @Override
    public void run() {
        updateChecker.checkForUpdates();
    }
}
