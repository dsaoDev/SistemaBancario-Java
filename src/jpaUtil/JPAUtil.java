package jpaUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JPAUtil {
	private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("dev");

	public EntityManager getEntityManager() {
		return emf.createEntityManager();
	}

	public void close() {
		emf.close();
	}

}
