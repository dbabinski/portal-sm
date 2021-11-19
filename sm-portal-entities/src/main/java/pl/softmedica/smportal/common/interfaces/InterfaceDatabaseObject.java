/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.softmedica.smportal.common.interfaces;

import java.util.Comparator;
import javax.persistence.Table;

/**
 *
 * @author chiefu
 */
public interface InterfaceDatabaseObject {

    public Integer getId();

    public static String getTable(Class entityClass) {
        if (entityClass != null && entityClass.getAnnotation(Table.class) != null) {
            String annotation = entityClass.getAnnotation(Table.class).toString();
            //@javax.persistence.Table(schema=, name=wizyta, indexes=[], uniqueConstraints=[], catalog=)
            if (annotation.contains("name=")) {
                annotation = annotation.substring(annotation.indexOf("name=") + 5);
                if (annotation.contains(",")) {
                    annotation = annotation.substring(0, annotation.indexOf(","));
                }
                if (annotation.contains(")")) {
                    annotation = annotation.substring(0, annotation.indexOf(")"));
                }
                return annotation;
            }
        }
        return null;
    }

    public static Comparator<InterfaceDatabaseObject> COMPARATOR_BY_ID = (InterfaceDatabaseObject o1, InterfaceDatabaseObject o2) -> {
        if (o1 != null && o2 != null) {
            if (o1.getId() != null && o2.getId() == null) {
                return -1;
            }
            if (o1.getId() == null && o2.getId() != null) {
                return 1;
            }
            return o1.getId().compareTo(o2.getId());
        } else {
            if (o1 != null) {
                return -1;
            }
            if (o2 != null) {
                return 1;
            }
        }
        return 0;
    };
}
