/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.softmedica.smportal.common.session;

import java.security.Principal;
import java.util.List;
import javax.ejb.PrePassivate;
import javax.persistence.EntityManager;

/**
 *
 * @author chiefu
 * @param <T>
 */
public abstract class AbstractFacade<T> {

    private Class<T> entityClass;
    protected Principal principal = null;
    protected String clientIpAdress = null;

    //--------------------------------------------------------------------------
    // Konstruktor
    //--------------------------------------------------------------------------
    public AbstractFacade(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    @PrePassivate
    protected void prePassivate() {
        principal = null;
    }

    //--------------------------------------------------------------------------
    // Metody publiczne
    //--------------------------------------------------------------------------    
    public Principal getPrincipal() {
        return principal;
    }

    public void setPrincipal(Principal principal) {
        this.principal = principal;
    }

    public String getClientIpAdress() {
        return clientIpAdress;
    }

    public void setClientIpAdress(String clientIpAdress) {
        this.clientIpAdress = clientIpAdress;
    }

    public void create(T entity) {
        getEntityManager().persist(entity);
    }

    public T createManaged(T entity) {
        getEntityManager().persist(entity);
        return getEntityManager().merge(entity);
    }

    /**
     * Zwraca zarządzany egzemplarz encji
     *
     * @param entity
     * @return entity
     */
    public T edit(T entity) {
        return getEntityManager().merge(entity);
    }

    public void remove(T entity) {
        getEntityManager().remove(getEntityManager().merge(entity));
    }

    public T find(Object id) {
        if (id != null) {
            return getEntityManager().find(entityClass, id);
        }
        return null;
    }

    public List<T> findAll() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        return getEntityManager().createQuery(cq).getResultList();
    }

    public List<T> findRange(int[] range) {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    public String decryptText(String text) throws Exception {
        if (text != null) {
            return (String) getEntityManager()
                    .createNativeQuery("SELECT decrypt_txt(:text)")
                    .setParameter("text", text)
                    .getResultList()
                    .stream().findFirst().orElse(null);
        }
        return null;
    }

    //--------------------------------------------------------------------------
    // Metody prywatne
    //--------------------------------------------------------------------------
    protected abstract EntityManager getEntityManager();
}
