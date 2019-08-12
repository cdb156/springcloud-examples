package cn.selinx.cloud.server.bms.domain;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

/**
 * {@link BmsUserRepository } 测试类
 * @author JiePeng Chen
 * @since 2019/8/7 21:02
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class BmsUserRepositoryTest {

    @Autowired
    BmsUserRepository bmsUserRepository;

    @Test
    public void testSave() throws Exception {
        BmsUser bmsUser = new BmsUser();
        bmsUser.setAccount("user");
        bmsUser.setName("测试用户");
        bmsUserRepository.save(bmsUser);

    }

    @Test
    public void testQuery() throws Exception {
        Optional<BmsUser> optionalBmsUser = bmsUserRepository.findById(1L);
        List<BmsUser> bmsUserList = bmsUserRepository.findByAccount("user");
        Assert.assertEquals("user",bmsUserList.get(0).getAccount());
        Assert.assertEquals("user", optionalBmsUser.orElse(new BmsUser()).getAccount());
    }

}
