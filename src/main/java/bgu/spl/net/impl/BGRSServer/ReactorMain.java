package bgu.spl.net.impl.BGRSServer;

import bgu.spl.net.Database;
import bgu.spl.net.impl.echo.LineMessageEncoderDecoder;
import bgu.spl.net.impl.message.MessageImpl;
import bgu.spl.net.impl.message.ThreadPerClient;
import bgu.spl.net.srv.Reactor;

import java.io.IOException;
import java.util.Map;

public class ReactorMain {
    public static void main (String args[]) {
        String filepath=args[0];
        Database.getInstance().initialize(filepath);
        Database database=Database.getInstance();
        Reactor reactor=new Reactor(20,7777, MessageImpl::new, LineMessageEncoderDecoder::new);
        database.setServer(reactor);
        reactor.serve();



    }
}
