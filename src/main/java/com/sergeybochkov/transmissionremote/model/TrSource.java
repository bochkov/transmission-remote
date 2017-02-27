package com.sergeybochkov.transmissionremote.model;

import cordelia.client.TrClient;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface TrSource {

    void add(TrClient client) throws IOException;

    List<File> files();

}
