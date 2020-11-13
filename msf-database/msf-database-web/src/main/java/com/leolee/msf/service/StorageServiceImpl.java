package com.leolee.msf.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.leolee.msf.config.DataSourceKey;
import com.leolee.msf.config.DynamicDataSourceContextHolder;
import com.leolee.msf.dao.StorageMapper;
import com.leolee.msf.entity.storage.Product;
import com.leolee.msf.service.serviceInterface.StorageService;
import io.seata.core.context.RootContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @ClassName StorageServiceImpl
 * @Description: TODO
 * @Author LeoLee
 * @Date 2020/11/10
 * @Version V1.0
 **/
@Service("storageService")
public class StorageServiceImpl implements StorageService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Autowired
    private StorageMapper storageMapper;

//    @DS("storage")
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    @Override
    public boolean reduceStock(Long productId, Integer amount) throws Exception {

        logger.info("=====================Storage start===================");
        DynamicDataSourceContextHolder.setDataSourceKey(DataSourceKey.STORAGE);
        logger.info("当前 XID: {}", RootContext.getXID());

        //检查库存
        this.checkSrock(productId, amount);

        //扣除库存
        logger.info("开始扣除{}库存{}件", productId, amount);
        Product product = storageMapper.selectById(productId);
        product.setStock(product.getStock() - amount);
        Integer record = storageMapper.updateById(product);
        logger.info("扣减 {} 库存结果:{}", productId, record > 0 ? "操作成功" : "扣减库存失败");

        logger.info("=====================Storage end=====================");
        return record > 0;
    }


    /*
     * 功能描述: <br>
     * 〈检查库存是否足够〉
     * @Param: [productId, requiredAmount]
     * @Return: void
     * @Author: LeoLee
     * @Date: 2020/11/10 22:26
     */
    private void checkSrock(Long productId, Integer requiredAmount) throws Exception {

        logger.info("检查{}库存", productId);

        Product product = storageMapper.selectById(productId);
        if (product.getStock() < requiredAmount) {
            logger.info("商品{}库存不足，当前库存{}", productId, requiredAmount);
            throw new Exception("商品{" + productId + "}库存");
        }
    }
}
