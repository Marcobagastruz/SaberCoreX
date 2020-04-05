package me.driftay.core.file.impl;

import me.driftay.core.SaberCore;
import me.driftay.core.file.CustomFile;

import java.io.File;

/**
 * SaberCore - Developed by Driftay.
 * All rights reserved 2020.
 * Creation Date: 3/10/2020
 */
public class FileManager {

    private CustomFile messages = new CustomFile(new File(SaberCore.getInstance().getDataFolder() + "/messages.yml"));

    private CustomFile config = new CustomFile(new File(SaberCore.getInstance().getDataFolder() + "/config.yml"));

    public void setupFiles() {
        messages.setup(true, "");
        config.setup(true, "");
    }

    public CustomFile getMessages() {
        return messages;
    }

    public CustomFile getConfig() {
        return config;
    }
}
