package com.sergeybochkov.transmissionremote.helpers;

import com.sergeybochkov.transmissionremote.model.rpc.arguments.TorrentField;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import org.jetbrains.annotations.NotNull;

public class TorrentFieldWrap implements Comparable {

    private TorrentField.File file;
    private TorrentField.FileStat fileStat;
    private String name;
    private Integer innerId;

    private IntegerProperty priority;

    public TorrentFieldWrap(Integer id, TorrentField.File file, TorrentField.FileStat fileStat) {
        this.file = file;
        this.fileStat = fileStat;
        innerId = id;
    }

    public TorrentFieldWrap(String name) {
        this.file = null;
        this.fileStat = null;
        this.name = name;
    }

    public Integer getInnerId() {
        return innerId;
    }

    public String getName() {
        if (file == null || name != null)
            return name;
        return file.getName();
    }

    public void setName(String value) {
        name = value;
    }

    public Long getLength() {
        if (file == null)
            return null;
        return file.getLength();
    }

    public Long getBytesComplete() {
        if (file == null)
            return null;
        return file.getBytesCompleted();
    }

    public Boolean getWanted() {
        if (fileStat == null)
            return null;
        return fileStat.getWanted();
    }

    public Integer getPriority() {
        if (fileStat == null)
            return null;
        setPriority(fileStat.getPriority());
        return fileStat.getPriority();
    }

    public void setPriority(Integer value) {
        priorityProperty().set(value);
    }

    public IntegerProperty priorityProperty() {
        if (priority == null)
            priority = new SimpleIntegerProperty(this, "priority");
        return priority;
    }

    enum Sorting {
        BY_ID,
        BY_NAME,
        BY_LENGTH,
        BY_BYTES_COMPLETE,
        BY_WANTED,
        BY_PRIORITY
    }

    private Sorting sort = Sorting.BY_NAME;

    public void setSorting(Sorting sort) {
        this.sort = sort;
    }

    @Override
    public int compareTo(@NotNull Object o) {
        if (o instanceof TorrentFieldWrap) {
            TorrentFieldWrap t = (TorrentFieldWrap) o;
            switch (sort) {
                case BY_NAME:
                    return this.getName().compareTo(t.getName());
            }
        }
        return 1;
    }
}
