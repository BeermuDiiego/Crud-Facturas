DROP VIEW IF EXISTS get_total_prices_business; 
DROP VIEW IF EXISTS get_total_prices_business_day; 
DROP VIEW IF EXISTS get_total_prices_business_month; 
DROP VIEW IF EXISTS get_total_prices_business_year; 
DROP VIEW IF EXISTS get_total_prices_client; 
DROP VIEW IF EXISTS get_list_top_invoices;



--Obtener el total de las facturas por la empresa--
CREATE VIEW get_total_prices_business AS 
SELECT
	b.id_user AS userId,
	b.id_business AS businessId,
	COUNT(i.id_invoice) AS numInvoices,
    (SELECT COUNT(c.id_client)
		FROM client c
 		WHERE c.id_business = b.id_business) AS numClients,
	SUM(i.total) AS totalPrices,
	SUM(CASE WHEN i.paid = 0 THEN i.total ELSE 0 END) AS totalPricesNoPaid,
	SUM(CASE WHEN i.paid = 1 THEN i.total ELSE 0 END) AS totalPricesPaid
FROM
	business b
LEFT JOIN invoice i ON
	i.id_business = b.id_business
GROUP BY
	b.id_user,
	b.id_business;
	
	--Obtener el total de las facturas por la empresa y dia--
CREATE VIEW get_total_prices_business_day AS 
SELECT
	b.id_user AS userId,
	b.id_business AS businessId,
	i.date_invoice AS invoiceDay,
	SUM(i.total) AS totalPrices,
	SUM(CASE WHEN i.paid = 0 THEN i.total ELSE 0 END) AS totalPricesNoPaid,
	SUM(CASE WHEN i.paid = 1 THEN i.total ELSE 0 END) AS totalPricesPaid
FROM
	business b
LEFT JOIN invoice i ON
	i.id_business = b.id_business
GROUP BY
	b.id_user,
	b.id_business,
	i.date_invoice
ORDER BY
	i.date_invoice ASC;
	
--Obtener el total de las facturas por la empresa y mes--
CREATE VIEW get_total_prices_business_month AS 
SELECT
    b.id_user AS userId,
    b.id_business AS businessId,
    DATE_FORMAT(i.date_invoice, '%Y-%m') AS invoiceMonth,
    SUM(i.total) AS totalPrices,
    SUM(CASE WHEN i.paid = 0 THEN i.total ELSE 0 END) AS totalPricesNoPaid,
    SUM(CASE WHEN i.paid = 1 THEN i.total ELSE 0 END) AS totalPricesPaid
FROM
    business b
LEFT JOIN invoice i ON
    i.id_business = b.id_business
GROUP BY
    b.id_user,
    b.id_business,
    DATE_FORMAT(i.date_invoice, '%Y-%m')
ORDER BY
	DATE_FORMAT(i.date_invoice, '%Y-%m') ASC;
    
    
--Obtener el total de las facturas por la empresa y año--
CREATE VIEW get_total_prices_business_year AS 
SELECT
	b.id_user AS userId,
	b.id_business AS businessId,
	YEAR(i.date_invoice) AS invoiceYear,
	SUM(i.total) AS totalPrices,
	SUM(CASE WHEN i.paid = 0 THEN i.total ELSE 0 END) AS totalPricesNoPaid,
	SUM(CASE WHEN i.paid = 1 THEN i.total ELSE 0 END) AS totalPricesPaid
FROM
	business b
LEFT JOIN invoice i ON
	i.id_business = b.id_business
GROUP BY
	b.id_user,
	b.id_business,
	YEAR(i.date_invoice)
ORDER BY
	YEAR(i.date_invoice) ASC;



--Obtener el total de las facturas por cliente--
CREATE VIEW get_total_prices_client AS 
SELECT
	c.id_user AS userId,
	c.id_business AS businessId,
	c.id_client AS clientId,
	c.name_client AS nameClient,
	c.cif_client AS cifClient,
	COUNT(i.id_invoice) AS numberInvoices,
	SUM(i.total) AS totalInvoices,
	SUM(CASE WHEN i.paid = 0 THEN i.total ELSE 0 END) AS totalInvoicesNoPaid,
	SUM(CASE WHEN i.paid = 1 THEN i.total ELSE 0 END) AS totalInvoicesPaid
FROM
	client c
LEFT JOIN invoice i ON
	c.id_client = i.id_client
GROUP BY
	c.id_user,
	c.id_business,
	c.id_client,
	c.name_client,
	c.cif_client 
ORDER BY
	SUM(i.total) DESC;
	
--Obtener lista de facturas de una empresa--
CREATE VIEW get_list_top_invoices AS
SELECT
	i.id_user AS userId,
	i.id_business AS businessId,
	i.num_invoice AS numInvoice,
	i.date_invoice AS dateInvoice,
	i.total AS totalInvoice
FROM
	invoice i
ORDER BY
	i.total DESC;

