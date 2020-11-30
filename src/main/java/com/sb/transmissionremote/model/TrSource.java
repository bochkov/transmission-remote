package com.sb.transmissionremote.model;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import cordelia.client.Client;

public interface TrSource extends Serializable {

    void add(Client client) throws IOException;

    List<File> files();

}
