package com.sb.transmissionremote.model;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import cordelia.client.TrClient;

public interface TrSource extends Serializable {

    void add(TrClient client) throws IOException;

    List<File> files();

}
