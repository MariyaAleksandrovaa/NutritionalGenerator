package app.objects;

public class LocalObj {

	public Integer local_id;
	public String local_name;

	public Integer company_id;
	public String company_name;

	public String name;
	public String direction;

	public LocalObj(int local_id, String local_name, int company_id, String company_name, String name,
			String direction) {
		this.local_id = local_id;
		this.local_name = local_name;
		this.company_id = company_id;
		this.company_name = company_name;
		this.name = name;
		this.direction = direction;
	}
	public LocalObj() {
		
	}

	public Integer getLocal_id() {
		return local_id;
	}

	public void setLocal_id(Integer local_id) {
		this.local_id = local_id;
	}

	public String getLocal_name() {
		return local_name;
	}

	public void setLocal_name(String local_name) {
		this.local_name = local_name;
	}

	public Integer getCompany_id() {
		return company_id;
	}

	public void setCompany_id(Integer company_id) {
		this.company_id = company_id;
	}

	public String getCompany_name() {
		return company_name;
	}

	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

}
