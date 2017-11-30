package com.kaishengit.service;

import com.kaishengit.dao.ProductDao;
import com.kaishengit.pojo.Product;
import com.kaishengit.util.RequestQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by hoyt on 2017/11/29.
 */

@Service
@Transactional(rollbackFor = RuntimeException.class)
public class ProductService {

    @Autowired
    private ProductDao productDao;

    public List<Product> findAll() {
        return  productDao.findByPage(0,100);
    }


    public void saveProduct(Product product) {
        productDao.save(product);
    }

    public Product findById(Integer id) {
        return productDao.findById(id);
    }

    public void editProduct(Product product) {
        productDao.save(product);
    }

    public void delete(Integer id) {
        productDao.deleteById(id);
    }

    public List<Product> findByRequestQuery(List<RequestQuery> requestQueryList) {
        return productDao.findByRquestQueryList(requestQueryList);
    }
}
