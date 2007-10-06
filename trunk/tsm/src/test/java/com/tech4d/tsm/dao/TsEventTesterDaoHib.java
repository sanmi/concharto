package com.tech4d.tsm.dao;

import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.transaction.annotation.Transactional;

import com.tech4d.tsm.model.EventSearchText;
import com.tech4d.tsm.model.TsEvent;

@Transactional
public class TsEventTesterDaoHib implements TsEventTesterDao {
    private SessionFactory sessionFactory;
    protected final Log logger = LogFactory.getLog(this.getClass());

    /* (non-Javadoc)
     * @see com.tech4d.tsm.lab.TsEventTesterDao#setSessionFactory(org.hibernate.SessionFactory)
     */
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    /* (non-Javadoc)
     * @see com.tech4d.tsm.lab.TsEventTesterDao#getSessionFactory()
     */
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    /* (non-Javadoc)
     * @see com.tech4d.tsm.lab.TsEventTesterDao#deleteAll()
     */
    public void deleteAll() {
        //TODO -  this is ugly, but it offers the best performance.  There don't
        //seem to be simpler cascade options using HQL delete
        this.sessionFactory.getCurrentSession().createSQLQuery("delete from TsGeometry").executeUpdate();
        this.sessionFactory.getCurrentSession().createSQLQuery("delete from TsEvent_Usertag").executeUpdate();
        this.sessionFactory.getCurrentSession().createSQLQuery("delete from Usertag").executeUpdate();
        this.sessionFactory.getCurrentSession().createSQLQuery("delete from TsEvent_User").executeUpdate();
        this.sessionFactory.getCurrentSession().createSQLQuery("delete from User").executeUpdate();
        this.sessionFactory.getCurrentSession().createSQLQuery("delete from TsEvent").executeUpdate();
//        this.sessionFactory.getCurrentSession().createSQLQuery("delete from EventSearchText").executeUpdate();
        this.sessionFactory.getCurrentSession().createSQLQuery("delete from TimePrimitive").executeUpdate();
        this.sessionFactory.getCurrentSession().createSQLQuery("delete from AuditFieldChange").executeUpdate();
        this.sessionFactory.getCurrentSession().createSQLQuery("delete from AuditEntry").executeUpdate();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.tech4d.tsm.lab.TsEventDao#findAll()
     */
    /* (non-Javadoc)
     * @see com.tech4d.tsm.lab.TsEventTesterDao#findAll()
     */
    @SuppressWarnings("unchecked")
    public List<TsEvent> findAll() {
        return this.sessionFactory.getCurrentSession().createQuery(
                "select tsEvent from TsEvent tsEvent").list();
    }

    /* (non-Javadoc)
     * @see com.tech4d.tsm.lab.TsEventTesterDao#save(java.util.Set)
     */
    public void save(Set<TsEvent> events) {
        
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        int i=0;
        for (TsEvent event : events) {
            i++;
            event.setEventSearchText(new EventSearchText(event));
            session.save(event);
            if ( i % 1000 == 0 ) { //1000, same as the JDBC batch size
                //flush a batch of inserts and release memory:
                session.flush();
                session.clear();
                //logger.debug("saving " + i);
            }
        }
        tx.commit();
        session.close();    
    }

}
