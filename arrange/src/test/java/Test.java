import com.cyj.arrange.Application;
import com.cyj.arrange.entry.TCiPipelineLog;
import com.cyj.arrange.mapper.TCiPipelineLogMapper;
import org.apache.commons.codec.digest.Md5Crypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Date;

@SpringBootTest(classes = Application.class)
public class Test {

    @Autowired
    TCiPipelineLogMapper tCiPipelineLogMapper;
    @org.junit.jupiter.api.Test
    public void testInsert()
    {
        Date startTime = new Date();
        for (int i=0;i<10;i++)
        {
            TCiPipelineLog pipelineLog = new TCiPipelineLog();
            pipelineLog.setPipelineId(1).setStartTime(startTime);
            System.out.println(tCiPipelineLogMapper.insert(pipelineLog));
        }

    }
}
