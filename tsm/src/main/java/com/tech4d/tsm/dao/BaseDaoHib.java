package com.tech4d.tsm.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;

import com.tech4d.tsm.model.BaseEntity;
import com.tech4d.tsm.model.Flag;

@Transactional
public class BaseDaoHib <T> implements BaseDao <T> {
    private SessionFactory sessionFactory;
    protected Class<T> clazz; 
    
    protected BaseDaoHib (Class<T> clazz) { 
        this.clazz = clazz;              
    }
    
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    /* (non-Javadoc)
     * @see com.tech4d.tsm.dao.BaseDao#setSessionFactory(org.hibernate.SessionFactory)
     */
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /* (non-Javadoc)
     * @see com.tech4d.tsm.dao.BaseDao#save(com.tech4d.tsm.model.BaseEntity)
     */
    public Serializable save(T entity) {
        return this.sessionFactory.getCurrentSession().save(entity);
    }

    /* (non-Javadoc)
     * @see com.tech4d.tsm.dao.BaseDao#saveOrUpdate(com.tech4d.tsm.model.BaseEntity)
     */
    public void saveOrUpdate(T entity) {
        this.sessionFactory.getCurrentSession().saveOrUpdate(entity);
    }
    
    /* (non-Javadoc)
     * @see com.tech4d.tsm.dao.BaseDao#delete(java.lang.Long)
     */
    public void delete(Long id) {
        BaseEntity entity = new Flag();
        entity.setId(id);
        this.sessionFactory.getCurrentSession().delete(entity);
    }

    /* (non-Javadoc)
     * @see com.tech4d.tsm.dao.BaseDao#delete(com.tech4d.tsm.model.BaseEntity)
     */
    public void delete(T entity) {
        this.sessionFactory.getCurrentSession().delete(entity);
    }

    @SuppressWarnings("unchecked")
    public T find(Long id) {
        return (T) this.sessionFactory.getCurrentSession().get(clazz, id);
    }

    @SuppressWarnings("unchecked")
    public List<T> findAll(int maxResults) {
        List<T> list = this.sessionFactory.getCurrentSession().
            createCriteria(clazz).
            setMaxResults(maxResults).
            list();
        return list;
    }
}
