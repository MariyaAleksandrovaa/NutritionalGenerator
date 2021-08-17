package app.service.company;

import app.model.Empresa;

public interface CompanyService {
	
	public void saveCompany(Empresa empresa);
	
	public boolean isCompanyAlreadyPresent(Empresa empresa);
	
	public Empresa get(String cif);
	
	
	
//	public void delete(String cif);

}
