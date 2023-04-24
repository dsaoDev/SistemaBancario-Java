package dao;

import java.util.ArrayList;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import jpaUtil.JPAUtil;
import model.Conta;

public class ContaDAO {
	JPAUtil jpa = new JPAUtil();
	EntityManager em;

	public boolean save(Conta conta) {
		boolean inseriu = false;
		try {
			em = jpa.getEntityManager();
			em.getTransaction().begin();

			em.persist(conta);

			em.getTransaction().commit();

			inseriu = em.contains(conta);
		} catch (Exception e) {
			System.out.println("Alguma coisa deu errado ao tentar inserir dados");
			e.printStackTrace();
			em.getTransaction().rollback();
		} finally {
			em.close();
		}
		return inseriu;
	}

	public Conta searchById(Integer id) {
		Conta conta = new Conta();
		try {
			em = jpa.getEntityManager();
			conta = em.find(Conta.class, id);
		} catch (Exception e) {
			System.out.println("Erro ao tentar buscar ");
			e.printStackTrace();
		}
		return conta;
	}

	@SuppressWarnings("unchecked")
	public ArrayList<Conta> listOfAccounts() {
		ArrayList<Conta> contas = new ArrayList<>();
		try {
			em = jpa.getEntityManager();
			Query query = em.createQuery("SELECT conta FROM Conta conta");
			contas = (ArrayList<Conta>) query.getResultList();
		} catch (Exception e) {
			System.out.println("Erro ao listar");
			e.printStackTrace();
		} finally {
			em.close();
		}
		return contas;
	}

	public boolean deleteById(Integer id) {
		boolean deletou = false;
		try {
			em = jpa.getEntityManager();
			Conta conta = searchById(id);
			if (conta != null) {
				em.getTransaction().begin();

				em.remove(conta);

				em.getTransaction().commit();
				deletou = true;
			}
		} catch (Exception e) {
			System.out.println("Algo deu errado ao tentar deletar dados");
			e.printStackTrace();
			em.getTransaction().rollback();

		} finally {
			em.close();
		}
		return deletou;
	}

	public boolean update(Conta conta) {
		boolean atualizou = false;
		try {
			em = jpa.getEntityManager();
			em.getTransaction().begin();

			em.merge(conta);

			em.getTransaction().commit();

			conta = em.find(Conta.class, conta.getId());
			if (conta != null) {
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
	
	public void updateForDeposit(Conta conta) {
		boolean atualizou = false;
		try {
			em = jpa.getEntityManager();
			em.getTransaction().begin();

			em.merge(conta);

			em.getTransaction().commit();

			
		} catch (Exception e) {
			System.out.println("Algo deu errado ao tentar atualizar dados");
			e.printStackTrace();
			em.getTransaction().rollback();
		} finally {
			em.close();
		}
		
	}
}
