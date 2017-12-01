package com.kaishengit.dao;

import com.kaishengit.util.Page;
import com.kaishengit.util.RequestQuery;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * Created by hoyt on 2017/11/30.
 */

public abstract class BaseDao<T,PK extends Serializable> {

    @Autowired
    private SessionFactory sessionFactory;

    private Class<T> entityClazz;

    public BaseDao() {

        ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
        entityClazz = (Class<T>) parameterizedType.getActualTypeArguments()[0];
    }

    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    public void save(T entity) {
        getSession().saveOrUpdate(entity);
    }

    public T findById(PK id) {
        return (T) getSession().get(entityClazz,id);
    };

    public void deleteById(PK id) {
        getSession().delete(findById(id));
    }

    public void delete(T entity) {
        getSession().delete(entity);
    }

    public List<T> findAll() {
        Criteria criteria = getSession().createCriteria(entityClazz);
        return criteria.list();
    }

    public List<T> findByPage(Integer start, Integer size) {
        Criteria criteria = getSession().createCriteria(entityClazz);
        criteria.setFirstResult(start);
        criteria.setMaxResults(size);
        return criteria.list();
    }

    public List<T> findByRquestQueryList(List<RequestQuery> requestQueryList, Integer pageNo) {
        Criteria criteria = getSession().createCriteria(entityClazz);
        for (RequestQuery requestQuery : requestQueryList) {
            String paramName = requestQuery.getParameterName();
            String type = requestQuery.getEqualType();
            Object value = requestQuery.getValue();
            criteria.add(createCriterion(paramName,type,value));
        }
        criteria.addOrder(Order.desc("id"));
        criteria.setFirstResult(0);
        criteria.setMaxResults(100);
        return criteria.list();
    }

    public Criterion createCriterion(String paramName,String type, Object value) {
        if (paramName.contains("_or_")) {
            String[] paramNames = paramName.split("_or_");
            Disjunction disjunction = Restrictions.disjunction();
            for (String name : paramNames) {
                disjunction.add(whereBuilder(name,type,value));
            }
            return disjunction;
        } else {
            return whereBuilder(paramName,type,value);
        }
    }

    private Criterion whereBuilder(String paramName,String type, Object value) {
        if ("eq".equalsIgnoreCase(type)) {
            return Restrictions.eq(paramName,value);
        } else if ("like".equalsIgnoreCase(type)) {
            return Restrictions.like(paramName,value.toString(), MatchMode.ANYWHERE);
        } else if ("gt".equalsIgnoreCase(type)) {
            return Restrictions.gt(paramName,value);
        } else if ("ge".equalsIgnoreCase(type)) {
            return Restrictions.ge(paramName,value);
        } else if ("lt".equalsIgnoreCase(type)) {
            return Restrictions.lt(paramName,value);
        } else if ("le".equalsIgnoreCase(type)) {
            return Restrictions.le(paramName,value);
        }
        return null;
    }

    public Page<T> findByRquestQueryListAndPageNo(List<RequestQuery> requestQueryList, Integer pageNo) {
        //1. 计算总记录数（根据查询条件）
            Long count = count(requestQueryList);
        //2. 根据总记录数计算总页数
            Page<T> page = new Page<T>(count.intValue(),15,pageNo);
        //3. 给定页号获取起始行号
            Criteria criteria = getSession().createCriteria(entityClazz);
            conditionBuilder(requestQueryList,criteria);
            criteria.setFirstResult(page.getStart());
            criteria.setMaxResults(15);
        //4. 查询
        List<T> resultList = criteria.list();
        page.setItems(resultList);
        return page;
    }

    public Long count() {
        Criteria criteria = getSession().createCriteria(entityClazz);
        criteria.setProjection(Projections.rowCount());
        return (Long) criteria.uniqueResult();
    }

    public Long count(List<RequestQuery> requestQueryList) {
        Criteria criteria = getSession().createCriteria(entityClazz);
        conditionBuilder(requestQueryList,criteria);
        criteria.setProjection(Projections.rowCount());
        return (Long) criteria.uniqueResult();
    }

    private void conditionBuilder(List<RequestQuery> requestQueryList, Criteria criteria) {
        for (RequestQuery requestQuery : requestQueryList) {
            String paramName = requestQuery.getParameterName();
            String type = requestQuery.getEqualType();
            Object value = requestQuery.getValue();
            criteria.add(createCriterion(paramName,type,value));
        }
    }
}
