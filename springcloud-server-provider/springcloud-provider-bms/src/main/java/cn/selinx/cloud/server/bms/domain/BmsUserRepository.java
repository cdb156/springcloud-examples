package cn.selinx.cloud.server.bms.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户信息-数据接口
 * @author JiePeng Chen
 * @since 2019/8/7 20:54
 */
@Repository
public interface BmsUserRepository extends JpaRepository<BmsUser,Long> , JpaSpecificationExecutor<BmsUser> {


    /**
     * 根据account查找
     * @param account
     * @return
     */
    List<BmsUser> findByAccount(String account);
}
