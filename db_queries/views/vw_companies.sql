CREATE VIEW vw_companies
AS
SELECT 
    comp.id AS 'company_id',
    comp.code AS 'company_code',
    comp.name AS 'company_name',
    comp.created_on AS 'company_created_on'
FROM tb_companies AS comp;