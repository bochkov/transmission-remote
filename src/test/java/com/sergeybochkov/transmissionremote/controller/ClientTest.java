package com.sergeybochkov.transmissionremote.controller;

import com.sergeybochkov.transmissionremote.helpers.Settings;
import com.sergeybochkov.transmissionremote.model.rpc.methods.SessionGet;
import com.sergeybochkov.transmissionremote.model.rpc.methods.SessionStats;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ClientTest extends Assert {

    @Before
    public void setUp(){
        Settings.debug = true;
    }

    //@Test
    public void testVariables(){
        String server = "192.168.55.5";
        Integer port = 9091;
        String username = "admin";
        String password = "123";
        Client client = Client.getInstance(server, port, username, password);

        assertEquals(client.getServer(), server);
        assert client.getPort().equals(port);
        assertEquals(client.getUsername(), username);
        assertEquals(client.getPassword(), password);
    }

    //@Test(timeout = 4000)
    public void testValidClient() {
        Client client = Client.getInstance("192.168.55.5", 9091, "admin", "Wfnr112460");
        //assertTrue(client.isConnectionAvailable());

        assertTrue(client.send(new SessionGet()).isSuccess());
        assertTrue(client.send(new SessionStats()).isSuccess());
    }

    //@Test(timeout = 2000)
    public void testInvalidHostClient() {
        Client client = Client.getInstance("192.168.5.1", 9091, "admin", "Wfnr112460");
        //assertFalse(client.isConnectionAvailable());
    }
}
