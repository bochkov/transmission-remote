package com.sb.transmissionremote.util;

import java.awt.datatransfer.DataFlavor;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import javax.swing.*;

import com.sb.transmissionremote.model.TrSourceFile;
import com.sb.transmissionremote.model.TrSourceTrash;
import cordelia.client.Client;
import lombok.SneakyThrows;

public final class Drop extends TransferHandler {

    private final AtomicReference<Client> client;

    public Drop(AtomicReference<Client> client) {
        this.client = client;
    }

    @SneakyThrows
    @Override
    public boolean canImport(TransferSupport support) {
        if (!support.isDrop())
            return false;

        return support.isDataFlavorSupported(DataFlavor.javaFileListFlavor);
    }

    @SneakyThrows
    @Override
    public boolean importData(TransferSupport support) {
        List<?> list = (List<?>) support.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
        List<File> files = new ArrayList<>();
        for (Object obj : list) {
            if (obj instanceof File && ((File) obj).getName().toLowerCase().endsWith(".torrent"))
                files.add((File) obj);
        }
        if (files.isEmpty())
            return false;

        new TrSourceTrash(new TrSourceFile(files, Map.of())).add(client.get());
        return true;
    }
}
