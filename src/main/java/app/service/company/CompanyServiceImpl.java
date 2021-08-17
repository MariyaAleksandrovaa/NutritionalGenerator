package app.service.company;

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

}
