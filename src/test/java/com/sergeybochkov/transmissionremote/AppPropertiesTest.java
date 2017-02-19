package com.sergeybochkov.transmissionremote;

import org.junit.Assert;
import org.junit.Test;

public class AppPropertiesTest {

    @Test
    public void testUri() {
        AppProperties props = new AppProperties();
        props.setUrl("http://192.168.55.5:9091/transmission/rpc", "bochkov", "123");
        Assert.assertEquals("http://bochkov:123@192.168.55.5:9091/transmission/rpc", props.uri());
        props.setUrl("https://192.168.55.5:9091/transmission/rpc", "bochkov", "123");
        Assert.assertEquals("https://bochkov:123@192.168.55.5:9091/transmission/rpc", props.uri());
    }

    @Test
    public void testUrl() {
        AppProperties props = new AppProperties();
        props.setUrl("http://192.168.55.5:9091/transmission/rpc", "test", "test");
        Assert.assertEquals("http://192.168.55.5:9091/transmission/rpc", props.url());
    }
}
