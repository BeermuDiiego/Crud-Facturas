package api.api_gf.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import api.api_gf.model.entity.InvoiceItem;

public interface InvoiceItemRepository extends JpaRepository<InvoiceItem, Integer> {

}
