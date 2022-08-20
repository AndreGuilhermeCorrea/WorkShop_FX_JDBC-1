package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.VendedoresDao;
import model.entities.Vendedores;

public class VendedoresService {

	private VendedoresDao dao = DaoFactory.createVendedoresDao();

	public List<Vendedores> findAll() {
		return dao.findAll();

		/*
		 * //dados MOCK mocados
		 * 
		 * List<Vendedores> list = new ArrayList<>(); list.add(new Vendedores(1,
		 * "Livros")); list.add(new Vendedores(2, "Eletronicos")); list.add(new
		 * Vendedores(3, "Presentes")); list.add(new Vendedores(4, "Acessórios"));
		 * list.add(new Vendedores(5, "Computadores")); return list;
		 * 
		 */

	}
	
	public void saveOrUpdate(Vendedores obj) {
		//verificar se vai ser inserido um novo Vendedor ou atualizar um ja existente
		//teste se o id é nulo
		if (obj.getId() == null) {
			dao.insert(obj);
		}
		else {
			dao.update(obj);
		}
	}
	
	//método para remover Vendedores do banco
	public void remove(Vendedores obj) {
		dao.deleteById(obj.getId());
	}

}
