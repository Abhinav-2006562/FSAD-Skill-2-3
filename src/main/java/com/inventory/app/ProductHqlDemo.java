package com.inventory.app;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import com.inventory.entity.Product;
import com.inventory.util.HibernateUtil;

public class ProductHqlDemo {

    public static void main(String[] args) {

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        session.save(new Product("Laptop", "Electronics", 75000, 10));
        session.save(new Product("Mobile", "Electronics", 25000, 20));
        session.save(new Product("Headphones", "Accessories", 5000, 30));
        session.save(new Product("Keyboard", "Accessories", 3500, 15));
        session.save(new Product("Mouse", "Accessories", 1200, 25));
        session.save(new Product("Monitor", "Electronics", 15000, 8));

        tx.commit();

   
           //3. SORT BY PRICE

        // a. Ascending
        List<Product> ascPrice =
                session.createQuery("from Product p order by p.price asc", Product.class)
                       .list();

        // b. Descending
        List<Product> descPrice =
                session.createQuery("from Product p order by p.price desc", Product.class)
                       .list();

          // 4. SORT BY QUANTITY (HIGHEST FIRST)
        List<Product> byQuantity =
                session.createQuery("from Product p order by p.quantity desc", Product.class)
                       .list();
           //5. PAGINATION

        // a. First 3 products
        Query<Product> firstPage =
                session.createQuery("from Product", Product.class);
        firstPage.setFirstResult(0);
        firstPage.setMaxResults(3);
        List<Product> firstThree = firstPage.list();

        // b. Next 3 products
        Query<Product> secondPage =
                session.createQuery("from Product", Product.class);
        secondPage.setFirstResult(3);
        secondPage.setMaxResults(3);
        List<Product> nextThree = secondPage.list();

          // 6. AGGREGATE OPERATIONS

        // a. Total products
        Long totalCount =
                session.createQuery("select count(p) from Product p", Long.class)
                       .getSingleResult();

        // b. Products where quantity > 0
        Long availableCount =
                session.createQuery(
                        "select count(p) from Product p where p.quantity > 0",
                        Long.class)
                       .getSingleResult();

        // c. Count products grouped by description
        List<Object[]> countByDesc =
                session.createQuery(
                        "select p.description, count(p) from Product p group by p.description",
                        Object[].class)
                       .list();

        // d. Min & Max price
        Object[] minMax =
                session.createQuery(
                        "select min(p.price), max(p.price) from Product p",
                        Object[].class)
                       .getSingleResult();

          // 7. GROUP BY DESCRIPTION
        List<Object[]> groupByDesc =
                session.createQuery(
                        "select p.description, p.name from Product p group by p.description, p.name",
                        Object[].class)
                       .list();
       
          // 8. FILTER PRODUCTS BY PRICE RANGE
        List<Product> priceRange =
                session.createQuery(
                        "from Product p where p.price between 5000 and 30000",
                        Product.class)
                       .list();

        /* =====================================================
           9. LIKE OPERATIONS
           ===================================================== */

        // a. Names starting with 'M'
        List<Product> startWithM =
                session.createQuery(
                        "from Product p where p.name like 'M%'",
                        Product.class)
                       .list();

        // b. Names ending with 'r'
        List<Product> endWithR =
                session.createQuery(
                        "from Product p where p.name like '%r'",
                        Product.class)
                       .list();

        // c. Names containing 'top'
        List<Product> containsTop =
                session.createQuery(
                        "from Product p where p.name like '%top%'",
                        Product.class)
                       .list();

        // d. Names with exact length (6 characters)
        List<Product> exactLength =
                session.createQuery(
                        "from Product p where length(p.name) = 6",
                        Product.class)
                       .list();

        session.close();
        System.out.println("HQL operations executed successfully");
    }
}
