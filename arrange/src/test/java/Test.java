import com.cyj.arrange.Application;
import org.apache.commons.codec.digest.Md5Crypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Date;

@SpringBootTest(classes = Application.class)
public class Test {

    @org.junit.jupiter.api.Test
    public void testInsert()
    {

    }
}
