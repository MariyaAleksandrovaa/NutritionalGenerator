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
	}

	@Override
	public boolean isCompanyAlreadyPresent(Empresa empresa) {

		return false;
	}

	public Empresa get(String cif) {
		Optional<Empresa> result = Optional.ofNullable(repoCompany.findByCif(cif));

		if (result.isPresent()) {
			return result.get();
		}
		return null;
	}

	public void delete(Integer id) {
		repoCompany.deleteById(id);
	}

}
