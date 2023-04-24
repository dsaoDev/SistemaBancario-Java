package dao;

import java.util.ArrayList;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import jpaUtil.JPAUtil;
import model.Banco;

public class BancoDAO {
	JPAUtil jpaUtil = new JPAUtil();
	EntityManager em;

	public boolean save(Banco banco) {
		boolean inseriu = false;
		try {
			em = jpaUtil.getEntityManager();
			em.getTransaction().begin();

			em.persist(banco);

			em.getTransaction().commit();

			inseriu = em.contains(banco);
		} catch (Exception e) {
			System.out.println("Alguma coisa deu errado ao tentar inserir dados no banco");
			e.printStackTrace();
			em.getTransaction().rollback();
			em.close();
		}
		return inseriu;

	}

	public Banco searchById(Integer id) {
		Banco banco = new Banco();
		try {
			em = jpaUtil.getEntityManager();
			banco = em.find(Banco.class, id);
		} catch (Exception e) {
			System.out.println("Alguma coisa deu errado ao tentar buscar um banco pelo Id");
			e.printStackTrace();
		}
		return banco;
	}

	public boolean deleteById(int id) {
		Banco banco = new Banco();
		boolean deletou = false;

		try {
			em = jpaUtil.getEntityManager();
			banco = searchById(id);
			if (banco != null) {
				em.getTransaction().begin();

				em.remove(banco);

				em.getTransaction().commit();
				deletou = true;

			}

		} catch (Exception e) {
			System.out.println("Alguma coisa deu errado ao tentar remover dados");
			e.printStackTrace();
			em.getTransaction().rollback();
		} finally {
			em.close();
		}
		return deletou;
	}

	public boolean update(Banco banco) {
		boolean atualizou = false;
		try {
			em = jpaUtil.getEntityManager();
			em.getTransaction().begin();

			em.merge(banco);

			em.getTransaction().commit();

			banco = em.find(Banco.class, banco.getId());
			if (banco != null) {
				atualizou = true;
			}
		} catch (Exception e) {
			System.out.println("Algo deu errado ao tentar atualizar dados");
			e.printStackTrace();
			em.getTransaction().rollback();
		} finally {
			em.close();
		}
		return atualizou;
	}

	@SuppressWarnings("unchecked")
	public ArrayList<Banco> listOfBanks() {
		ArrayList<Banco> bancos = new ArrayList<>();
		try {
			em = jpaUtil.getEntityManager();
			Query query = em.createQuery("SELECT banco FROM Banco banco");
			bancos = (ArrayList<Banco>) query.getResultList();
		} catch (Exception e) {
			System.out.println("Alguma coisa deu errado ao tentar Listar");
			e.printStackTrace();
		} finally {
			em.close();
		}
		return bancos;
	}

}
