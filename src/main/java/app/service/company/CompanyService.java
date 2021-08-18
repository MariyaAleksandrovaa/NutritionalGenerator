package app.service.company;

import app.model.Empresa;

public interface CompanyService {
	
	public void saveCompany(Empresa empresa);
	
	public boolean isCompanyAlreadyPresent(Empresa empresa);
	
	public Empresa get(Integer id) throws CompanyNotfound;

}
