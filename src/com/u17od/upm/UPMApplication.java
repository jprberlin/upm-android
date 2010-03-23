/*
 * $Id: UPMApplication.java 37 2010-01-27 19:16:42Z Adrian $
 * 
 * Universal Password Manager
 * Copyright (c) 2010 Adrian Smith
 *
 * This file is part of Universal Password Manager.
 *   
 * Universal Password Manager is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * Universal Password Manager is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Universal Password Manager; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package com.u17od.upm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Date;

import android.app.Activity;
import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import com.u17od.upm.database.PasswordDatabase;

/**
 * This class replaces the regular Application class in the application and
 * allows us to store data at the application level.
 */
public class UPMApplication extends Application {

    private PasswordDatabase passwordDatabase;
    private Date timeOfLastSync;
    
    public Date getTimeOfLastSync() {
        return timeOfLastSync;
    }

    public void setTimeOfLastSync(Date timeOfLastSync) {
        this.timeOfLastSync = timeOfLastSync;
    }

    public void setPasswordDatabase(PasswordDatabase passwordDatabase) {
        this.passwordDatabase = passwordDatabase;
    }

    public PasswordDatabase getPasswordDatabase() {
        return passwordDatabase;
    }

    protected void copyFile(File source, File dest, Activity activity) {
        FileChannel sourceChannel = null;
        FileChannel destinationChannel = null;
        try {
            sourceChannel = new FileInputStream(source).getChannel();

            File destFile = null;
            if (dest.isDirectory()) {
                destFile = new File(dest, source.getName());
            } else {
                destFile = dest;
            }

            destinationChannel = new FileOutputStream(destFile).getChannel();
            destinationChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
        } catch (IOException e) {
            Log.e(activity.getClass().getName(), getString(R.string.file_problem), e);
            Toast.makeText(activity, R.string.file_problem, Toast.LENGTH_LONG).show();
        } finally {
            try {
                if (sourceChannel != null) {
                    sourceChannel.close();
                }
                if (destinationChannel != null) {
                    destinationChannel.close();
                }
            } catch (IOException e) {
                Log.e(activity.getClass().getName(), getString(R.string.file_problem), e);
                Toast.makeText(activity, R.string.file_problem, Toast.LENGTH_LONG).show();
            }
        }
    }

}
