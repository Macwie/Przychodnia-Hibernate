package src.main;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import src.models.*;

public class HibernateUtil {

    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static Session session = null;

    private HibernateUtil() {

    }

    public static Session getInstance() {
        if(session == null)
            newSession();

        return session;
    }

    private static void newSession() {
        session = sessionFactory.openSession();
    }

    private static SessionFactory buildSessionFactory() {
        try {
            return new Configuration()
                    .configure()
                    .addAnnotatedClass(Doctor.class)
                    .addAnnotatedClass(WorkingHours.class)
                    .addAnnotatedClass(Patient.class)
                    .addAnnotatedClass(Address.class)
                    .addAnnotatedClass(Term.class)
                    .addAnnotatedClass(Exemption.class)
                    .addAnnotatedClass(Referral.class)
                    .addAnnotatedClass(Prescription.class)
                    .addAnnotatedClass(Medicine.class)
                    .addAnnotatedClass(Disease.class)
                    .buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static void shutdown() {
        // Close caches and connection pools
        sessionFactory.close();
    }

}