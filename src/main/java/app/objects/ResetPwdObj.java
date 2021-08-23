package app.objects;

public class ResetPwdObj {

	public String username;
	public String pwd;
	public String resetPwd;

	public ResetPwdObj(String username, String pwd, String resetPwd) {
		this.username = username;
		this.pwd = pwd;
		this.resetPwd = resetPwd;
	}
	
	public ResetPwdObj() {
		
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getResetPwd() {
		return resetPwd;
	}

	public void setResetPwd(String resetPwd) {
		this.resetPwd = resetPwd;
	}

}
