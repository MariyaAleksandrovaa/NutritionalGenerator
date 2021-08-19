package app.service.company;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.model.Empresa;
import app.repository.CompanyRepository;

@Service
public class CompanyServiceImpl implements CompanyService {

	@Autowired
	public CompanyRepository repoCompany;

	@Override
	public void saveCompany(Empresa empresa) {

		repoCompany.save(empresa);

		System.out.println("Nuevo id: " + empresa.getId_empresa());
		
	}

	@Override
	public boolean isCompanyAlreadyPresent(Empresa empresa) {

		return false;
	}

	public Empresa get(Integer id) throws CompanyNotfound {
		Optional<Empresa> result = repoCompany.findById(id);

		if (result.isPresent()) {
			return result.get();
		}
		throw new CompanyNotfound("No se pudieron encontrar empresas con identificador: " + id);
	}

	public void delete(Integer id) {
		repoCompany.deleteById(id);
	}

}
