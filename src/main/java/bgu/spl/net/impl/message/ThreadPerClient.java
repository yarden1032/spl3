package bgu.spl.net.impl.message;

import bgu.spl.net.srv.BaseServer;
import bgu.spl.net.srv.BlockingConnectionHandler;

import java.util.function.Supplier;

public class ThreadPerClient extends BaseServer {
    public ThreadPerClient(int port, Supplier protocolFactory, Supplier encdecFactory) {
        super(port, protocolFactory, encdecFactory);
    }

    @Override
    protected void execute(BlockingConnectionHandler handler) {
        new Thread(handler).start();
    }
    protected Thread executeForDB(BlockingConnectionHandler handler) {

              Thread t =  new Thread(handler);
              t.start();
        return t;

    }
    public BlockingConnectionHandler getBlockingConnectionHandler()
    {
        return handler;
    }

}
