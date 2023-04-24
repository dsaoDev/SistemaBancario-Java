package dao;

import java.util.ArrayList;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import jpaUtil.JPAUtil;
import model.Usuario;

public class UsuarioDAO {

	JPAUtil jpa = new JPAUtil();
	EntityManager em;

	public boolean save(Usuario usuario) {
		boolean inseriu = false;
		try {
			em = jpa.getEntityManager();
			em.getTransaction().begin();

			em.persist(usuario);

			em.getTransaction().commit();

			inseriu = em.contains(usuario);

		} catch (Exception e) {
			System.out.println("Alguma coisa deu errado ao tentar inserir dados do Usuario");
			e.printStackTrace();
			em.getTransaction().rollback();
		} finally {
			em.close();
		}
		return inseriu;
	}

	public Usuario searchById(Integer id) {
		Usuario usuario = new Usuario();
		try {
			em = jpa.getEntityManager();
			usuario = em.find(Usuario.class, id);

		} catch (Exception e) {
			System.out.println("Alguma coisa deu errado ao tentar buscar o Id do usuario");
		}
		return usuario;
	}

	@SuppressWarnings("unchecked")
	public ArrayList<Usuario> listOfUsers() {
		ArrayList<Usuario> usuarios = new ArrayList<>();
		try {
			em = jpa.getEntityManager();
			Query query = em.createQuery("SELECT usuario from Usuario usuario");
			usuarios = (ArrayList<Usuario>) query.getResultList();
		} catch (Exception e) {
			System.out.println("Alguma coisa deu errado na Listagem");
			e.printStackTrace();
		} finally {
			em.close();
		}
		return usuarios;
	}

	public boolean deleteById(Integer id) {
		boolean deletou = false;
		try {

			em = jpa.getEntityManager();
			Usuario usuario = searchById(id);

			if (usuario != null) {
				em.getTransaction().begin();
				em.remove(usuario);
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

	public boolean update(Usuario usuario) {
		boolean atualizou = false;
		try {
			em = jpa.getEntityManager();
			em.getTransaction().begin();

			em.merge(usuario);

			em.getTransaction().commit();

			usuario = em.find(Usuario.class, usuario.getId());
			if (usuario != null) {
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

}
