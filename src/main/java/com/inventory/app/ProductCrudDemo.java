package com.inventory.app;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.inventory.entity.Product;
import com.inventory.util.HibernateUtil;

public class ProductCrudDemo {

    public static void main(String[] args) {

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        /* ---------- CREATE ---------- */
        Product p1 = new Product("Tablet", "Android Tablet", 18000, 12);
        session.save(p1);
        System.out.println("Product inserted");

        /* ---------- READ ---------- */
        Product fetched = session.get(Product.class, p1.getId());
        if (fetched != null) {
            System.out.println("Fetched Product: " + fetched.getName());
        }

        /* ---------- UPDATE ---------- */
        fetched.setPrice(17000);
        fetched.setQuantity(10);
        session.merge(fetched);
        System.out.println("Product updated");

        /* ---------- DELETE ---------- */
        session.remove(fetched);
        System.out.println("Product deleted");

        tx.commit();
        session.close();
    }
}
