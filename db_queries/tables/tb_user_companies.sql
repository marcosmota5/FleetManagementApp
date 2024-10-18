CREATE TABLE tb_user_companies
(
	id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	user_id INT NOT NULL, 
	company_id INT NOT NULL,
	CONSTRAINT fk_tb_user_companies_tb_users FOREIGN KEY (user_id) REFERENCES tb_users (id) ON DELETE CASCADE, 
	CONSTRAINT fk_tb_user_companies_tb_companies FOREIGN KEY (company_id) REFERENCES tb_companies (id) ON DELETE CASCADE
);