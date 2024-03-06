package com.example.md4_minitest1.service;

import com.example.md4_minitest1.model.Car;
import org.springframework.stereotype.Service;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

@Service
public class HibernateCarService implements ICarService {
    private static SessionFactory sessionFactory;
    private static EntityManager entityManager;
    static {
        try {
            sessionFactory = new Configuration().configure("hibernate.conf.xml").buildSessionFactory();
            entityManager = sessionFactory.createEntityManager();
        } catch (HibernateException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public List<Car> findAll() {
        String query = "SELECT c from Car as c";
        TypedQuery<Car> typedQuery = entityManager.createQuery(query, Car.class);
        return typedQuery.getResultList();
    }

    @Override
    public Car findByID(int id) {
        String query = "SELECT c from Car as c where c.id = :id";
        TypedQuery<Car> typedQuery = entityManager.createQuery(query,Car.class);
        typedQuery.setParameter("id",id);
        return typedQuery.getSingleResult();
    }

    //Car(id, code, name, producer, price, avatar)
    @Override
    public void save(Car car) {
        Transaction transaction = null;
        Car origin;
        if (car.getId()==0) {
            origin = new Car();
        } else {
            origin = findByID(car.getId());
        }
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            origin.setCode(car.getCode());
            origin.setName(car.getName());
            origin.setProducer(car.getProducer());
            origin.setPrice(car.getPrice());
            origin.setAvatar(car.getAvatar());
            session.saveOrUpdate(origin);
            transaction.commit();
        }catch (Exception e) {
            e.printStackTrace();
            if (transaction!=null) {
                transaction.rollback();
            }
        }
    }

    @Override
    public void remove(int id) {
        Car car = findByID(id);
        if (car != null) {
            Transaction transaction = null;
            try (Session session = sessionFactory.openSession()) {
                transaction = session.beginTransaction();
                session.remove(car);
                transaction.commit();
            } catch (Exception e) {
                e.printStackTrace();
                if (transaction != null) {
                    transaction.rollback();
                }
            }
        }
    }
}




