package net.geekstools.floatshort;

import android.app.backup.BackupAgentHelper;
import android.app.backup.BackupManager;
import android.app.backup.RestoreObserver;

public class BackupHandler extends BackupAgentHelper {

    @Override
    public void onCreate() {

    }

    public void requestBackup() {
        BackupManager bm = new BackupManager(this);
        bm.dataChanged();
    }

    public void requestRestore() {
        RestoreObserver restoreObserver = new RestoreObserver() {
            @Override
            public void onUpdate(int nowBeingRestored, String currentPackage) {
                super.onUpdate(nowBeingRestored, currentPackage);
            }
        };

        BackupManager bm = new BackupManager(this);
        bm.requestRestore(restoreObserver);
    }
}
