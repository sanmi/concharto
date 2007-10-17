package com.tech4d.tsm.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;

import com.tech4d.tsm.model.kml.Style;

@Transactional
public class StyleDaoHib implements StyleDao {
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.tech4d.tsm.lab.StyleDao#save(com.tech4d.tsm.model.lab.Style)
     */
    public Serializable save(com.tech4d.tsm.model.kml.Style style) {

        return this.sessionFactory.getCurrentSession().save(style);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.tech4d.tsm.lab.StyleDao#delete(com.tech4d.tsm.model.lab.Style)
     */
    public void delete(com.tech4d.tsm.model.kml.Style style) {
        this.sessionFactory.getCurrentSession().delete(style);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.tech4d.tsm.lab.StyleDao#delete(java.lang.Long)
     */
    public void delete(Long id) {
        this.sessionFactory
                .getCurrentSession()
                .createQuery(
                        "delete from TsGeometry where id = (select id from Style where id = ?)")
                .setLong(0, id).executeUpdate();
        this.sessionFactory.getCurrentSession().createQuery(
                "delete Style style where style.id = ?").setLong(0, id)
                .executeUpdate();
    }

    /**
     * Just for testing
     */
    public void deleteAll() {
        this.sessionFactory.getCurrentSession().createQuery(
                "delete from TsGeometry where id in (select id from Style)")
                .executeUpdate();
        this.sessionFactory.getCurrentSession().createQuery(
            "delete from Style").executeUpdate();
    }
     
    /*
     * (non-Javadoc)
     * 
     * @see com.tech4d.tsm.lab.StyleDao#findAll()
     */
    @SuppressWarnings("unchecked")
    public List<Style> findAll() {
        return this.sessionFactory.getCurrentSession().createQuery(
                "select style from Style style").list();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.tech4d.tsm.lab.StyleDao#findById(java.lang.Long)
     */
    public com.tech4d.tsm.model.kml.Style find(Long id) {
        return (com.tech4d.tsm.model.kml.Style) this.sessionFactory.getCurrentSession().get(
                Style.class, id);
    }
}
