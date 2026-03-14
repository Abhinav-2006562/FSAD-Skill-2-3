package com.inventory.app;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import com.inventory.entity.Product;
import com.inventory.util.HibernateUtil;

public class Skill3Main {

    public static void main(String[] args) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            System.out.println("\n=== 3a) Products by Price (ASC) ===");
            List<Product> priceAsc = session
                    .createQuery("from Product p order by p.price asc", Product.class)
                    .getResultList();
            priceAsc.forEach(System.out::println);

            System.out.println("\n=== 3b) Products by Price (DESC) ===");
            List<Product> priceDesc = session
                    .createQuery("from Product p order by p.price desc", Product.class)
                    .getResultList();
            priceDesc.forEach(System.out::println);

            System.out.println("\n=== 4) Products by Quantity (DESC) ===");
            List<Product> qtyDesc = session
                    .createQuery("from Product p order by p.quantity desc", Product.class)
                    .getResultList();
            qtyDesc.forEach(System.out::println);

            System.out.println("\n=== 5a) Pagination: First 3 Products ===");
            List<Product> first3 = session
                    .createQuery("from Product p order by p.id", Product.class)
                    .setFirstResult(0)
                    .setMaxResults(3)
                    .getResultList();
            first3.forEach(System.out::println);

            System.out.println("\n=== 5b) Pagination: Next 3 Products ===");
            List<Product> next3 = session
                    .createQuery("from Product p order by p.id", Product.class)
                    .setFirstResult(3)
                    .setMaxResults(3)
                    .getResultList();
            next3.forEach(System.out::println);

            System.out.println("\n=== 6a) Count: Total Products ===");
            Long totalCount = session
                    .createQuery("select count(p) from Product p", Long.class)
                    .getSingleResult();
            System.out.println("Total products = " + totalCount);

            // 6b) Count products where quantity > 0
            System.out.println("\n=== 6b) Count: Quantity > 0 ===");
            Long qtyGtZero = session
                    .createQuery("select count(p) from Product p where p.quantity > 0", Long.class)
                    .getSingleResult();
            System.out.println("Products with quantity > 0 = " + qtyGtZero);

            // 6c) Count products grouped by description
            System.out.println("\n=== 6c) Count Grouped by Description ===");
            List<Object[]> groupedCounts = session
                    .createQuery("select p.description, count(p) from Product p group by p.description", Object[].class)
                    .getResultList();
            for (Object[] row : groupedCounts) {
                System.out.println("Description = " + row[0] + " | Count = " + row[1]);
            }

            // 6d) Min and Max price
            System.out.println("\n=== 6d) Min & Max Price ===");
            Double minPrice = session
                    .createQuery("select min(p.price) from Product p", Double.class)
                    .getSingleResult();
            Double maxPrice = session
                    .createQuery("select max(p.price) from Product p", Double.class)
                    .getSingleResult();
            System.out.println("Min price = " + minPrice);
            System.out.println("Max price = " + maxPrice);
         // 7) GROUP BY description (same output, explicitly mentioned)
            System.out.println("\n=== 7) GROUP BY Description ===");
            List<Object[]> groupByDesc = session
                    .createQuery("select p.description, count(p) from Product p group by p.description", Object[].class)
                    .getResultList();
            for (Object[] row : groupByDesc) {
                System.out.println("Description = " + row[0] + " | Count = " + row[1]);
            }

            // 8) WHERE filter: price range
            System.out.println("\n=== 8) Filter: Price Between 100 and 1000 ===");
            List<Product> priceRange = session
                    .createQuery("from Product p where p.price between :min and :max order by p.price", Product.class)
                    .setParameter("min", 100.0)
                    .setParameter("max", 1000.0)
                    .getResultList();
            priceRange.forEach(System.out::println);

            // 9) LIKE queries on name
            System.out.println("\n=== 9a) LIKE: Names starting with 'B' ===");
            session.createQuery("from Product p where p.name like :pat order by p.name", Product.class)
                    .setParameter("pat", "B%")
                    .getResultList()
                    .forEach(System.out::println);

            System.out.println("\n=== 9b) LIKE: Names ending with 'er' ===");
            session.createQuery("from Product p where p.name like :pat order by p.name", Product.class)
                    .setParameter("pat", "%er")
                    .getResultList()
                    .forEach(System.out::println);

            System.out.println("\n=== 9c) LIKE: Names containing 'oo' ===");
            session.createQuery("from Product p where p.name like :pat order by p.name", Product.class)
                    .setParameter("pat", "%oo%")
                    .getResultList()
                    .forEach(System.out::println);

            System.out.println("\n=== 9d) Names with exact length = 4 ===");
            session.createQuery("from Product p where length(p.name) = :len order by p.name", Product.class)
                    .setParameter("len", 4)
                    .getResultList()
                    .forEach(System.out::println);

        } finally {
            HibernateUtil.getSessionFactory().close();
        }
    }
}