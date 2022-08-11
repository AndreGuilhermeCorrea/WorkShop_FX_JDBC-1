package model.dao;

import java.util.List;

import model.entities.Departamento;
import model.entities.Vendedores;

public interface VendedoresDao {

	void insert(Vendedores obj);

	void update(Vendedores obj);

	void deleteById(Integer id);

	Vendedores findById(Integer id);

	List<Vendedores> findAll();

	List<Vendedores> findByDepartamento(Departamento departamento);
}
