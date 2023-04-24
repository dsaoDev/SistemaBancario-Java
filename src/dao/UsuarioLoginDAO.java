package dao;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import jpaUtil.JPAUtil;
import model.UsuarioLogin;

public class UsuarioLoginDAO {
	JPAUtil jpa = new JPAUtil();
	EntityManager em;

	public UsuarioLogin checarEmail(String email, String senha) {
		UsuarioLogin uLogin = null;
		try {
			em = jpa.getEntityManager();

			TypedQuery<UsuarioLogin> query = em.createQuery(
					"SELECT uLogin FROM UsuarioLogin uLogin WHERE uLogin.email =:email AND uLogin.senha =:senha",
					UsuarioLogin.class);
			query.setParameter("email", email);
			query.setParameter("senha", senha);

			if (query.getSingleResult() != null) {
				uLogin = new UsuarioLogin();
				uLogin.setEmail(query.getSingleResult().getEmail());
				uLogin.setSenha(query.getSingleResult().getSenha());
			}
		} catch (NoResultException nre) {
			System.out.println("Login ou Senha incorretos");

		}
		return uLogin;
	}

	public boolean cadastrar(UsuarioLogin uLogin) {
		boolean cadastrou = false;
		try {
			em = jpa.getEntityManager();

			em.getTransaction().begin();

			em.persist(uLogin);

			em.getTransaction().commit();

			cadastrou = em.contains(uLogin);
		} catch (Exception e) {
			System.out.println("Erro ao cadastrar um Login para usuario");
			e.printStackTrace();
			em.getTransaction().rollback();
		}
		return cadastrou;
	}

}
