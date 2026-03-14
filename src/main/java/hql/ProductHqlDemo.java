package hql;
import org.hibernate.Session;
import org.hibernate.query.Query;

import com.inventory.entity.Product;

import java.util.List;

public class ProductHqlDemo {

    // 3a) Sort by price ASC
    public static List<Product> sortByPriceAsc(Session session) {
        return session.createQuery("from Product p order by p.price asc", Product.class)
                .getResultList();
    }

    // 3b) Sort by price DESC
    public static List<Product> sortByPriceDesc(Session session) {
        return session.createQuery("from Product p order by p.price desc", Product.class)
                .getResultList();
    }

    // 4) Sort by quantity highest first
    public static List<Product> sortByQuantityDesc(Session session) {
        return session.createQuery("from Product p order by p.quantity desc", Product.class)
                .getResultList();
    }

    // 5a) Pagination: first 3 products
    public static List<Product> first3(Session session) {
        return session.createQuery("from Product p order by p.id", Product.class)
                .setFirstResult(0)
                .setMaxResults(3)
                .getResultList();
    }

    // 5b) Pagination: next 3 products
    public static List<Product> next3(Session session) {
        return session.createQuery("from Product p order by p.id", Product.class)
                .setFirstResult(3)
                .setMaxResults(3)
                .getResultList();
    }

    // 6a) Count total number of products
    public static long countAll(Session session) {
        return session.createQuery("select count(p) from Product p", Long.class)
                .getSingleResult();
    }

    // 6b) Count products where quantity > 0
    public static long countQtyGtZero(Session session) {
        return session.createQuery("select count(p) from Product p where p.quantity > 0", Long.class)
                .getSingleResult();
    }

    // 6c) Count products grouped by description
    // returns rows: [description, count]
    public static List<Object[]> countGroupedByDescription(Session session) {
        return session.createQuery(
                "select p.description, count(p) from Product p group by p.description",
                Object[].class
        ).getResultList();
    }

    // 6d) Find minimum price
    public static double minPrice(Session session) {
        Double val = session.createQuery("select min(p.price) from Product p", Double.class)
                .getSingleResult();
        return val == null ? 0.0 : val;
    }

    // 6d) Find maximum price
    public static double maxPrice(Session session) {
        Double val = session.createQuery("select max(p.price) from Product p", Double.class)
                .getSingleResult();
        return val == null ? 0.0 : val;
    }

    // 7) GROUP BY description (same as 6c, but you can also list products per group using join fetch later)
    public static List<Object[]> groupByDescription(Session session) {
        return session.createQuery(
                "select p.description, count(p) from Product p group by p.description",
                Object[].class
        ).getResultList();
    }

    // 8) Filter products within price range (WHERE)
    public static List<Product> filterByPriceRange(Session session, double min, double max) {
        return session.createQuery(
                "from Product p where p.price between :min and :max order by p.price",
                Product.class
        )
        .setParameter("min", min)
        .setParameter("max", max)
        .getResultList();
    }

    // 9a) Names starting with letters (LIKE 'Ab%')
    public static List<Product> nameStartsWith(Session session, String prefix) {
        return session.createQuery(
                "from Product p where p.name like :pat order by p.name",
                Product.class
        )
        .setParameter("pat", prefix + "%")
        .getResultList();
    }
 // 9b) Names ending with letters (LIKE '%er')
    public static List<Product> nameEndsWith(Session session, String suffix) {
        return session.createQuery(
                "from Product p where p.name like :pat order by p.name",
                Product.class
        )
        .setParameter("pat", "%" + suffix)
        .getResultList();
    }

    // 9c) Names containing substring anywhere (LIKE '%ous%')
    public static List<Product> nameContains(Session session, String part) {
        return session.createQuery(
                "from Product p where p.name like :pat order by p.name",
                Product.class
        )
        .setParameter("pat", "%" + part + "%")
        .getResultList();
    }

    // 9d) Names with exact length (HQL: length())
    public static List<Product> nameExactLength(Session session, int len) {
        return session.createQuery(
                "from Product p where length(p.name) = :len order by p.name",
                Product.class
        )
        .setParameter("len", len)
        .getResultList();
    }
}