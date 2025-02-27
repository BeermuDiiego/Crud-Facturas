package api.api_gf.model.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import api.api_gf.model.dto.InvoiceDTO;
import api.api_gf.model.entity.Invoice;

@Mapper(componentModel = "spring", uses = { InvoiceItemMapper.class, ClientMapper.class })
public interface InvoiceMapper {

	@Mapping(target = "userId", ignore = true)
	@Mapping(target = "businessId", ignore = true)
	@Mapping(target = "clientId", ignore = true)
	@Mapping(target = "invoicesItems", source = "invoicesItems")
	@Mapping(target = "client", source = "client")
	InvoiceDTO toDTO(Invoice invoice);

	@Mapping(target = "user.userId", source = "userId")
	@Mapping(target = "client.clientId", source = "clientId")
	@Mapping(target = "business.businessId", source = "businessId")
	@Mapping(target = "invoicesItems", source = "invoicesItems")
	Invoice toEntity(InvoiceDTO invoiceDTO);

	List<InvoiceDTO> toDTOs(List<Invoice> invoices);

	List<Invoice> toEntities(List<InvoiceDTO> invoiceDTOs);

}
