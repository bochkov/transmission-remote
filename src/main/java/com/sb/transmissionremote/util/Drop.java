package com.sb.transmissionremote.util;

import java.awt.datatransfer.DataFlavor;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import javax.swing.*;

import com.sb.transmissionremote.model.TrSourceFile;
import com.sb.transmissionremote.model.TrSourceTrash;
import cordelia.client.TrClient;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@RequiredArgsConstructor
public final class Drop extends TransferHandler {

    private final AtomicReference<TrClient> client;

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
            if (obj instanceof File file && file.getName().toLowerCase().endsWith(".torrent"))
                files.add(file);
        }
        if (files.isEmpty())
            return false;

        new TrSourceTrash(
                new TrSourceFile(files, null)
        ).add(client.get());
        return true;
    }
}
