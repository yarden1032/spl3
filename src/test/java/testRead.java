import bgu.spl.net.Database;
import bgu.spl.net.srv.ReadTXTfile;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

public class testRead {

    @Test
    public void read1()
    {
        String path="src/main/java/bgu/spl/net/Courses.txt";
            boolean b= Database.getInstance().initialize(path);
            assertTrue(b);
            Database d=Database.getInstance();

    }

}
