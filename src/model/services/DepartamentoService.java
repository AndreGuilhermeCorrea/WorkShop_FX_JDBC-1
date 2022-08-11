package model.services;

import java.util.ArrayList;
import java.util.List;

import model.entities.Departamento;

public class DepartamentoService {
	
	public List<Departamento> findAll(){
		//dados MOCK mocados
		List<Departamento> list = new ArrayList<>();
		list.add(new Departamento(1, "Livros"));
		list.add(new Departamento(2, "Eletronicos"));
		list.add(new Departamento(3, "Presentes"));
		list.add(new Departamento(4, "Acessórios"));
		list.add(new Departamento(5, "Computadores"));
		return list;
		
	}

}
