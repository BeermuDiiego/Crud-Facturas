package api.api_gf.model.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import api.api_gf.model.dto.InvoiceItemDTO;
import api.api_gf.model.entity.InvoiceItem;

@Mapper(componentModel = "spring")
public interface InvoiceItemMapper {

	@Mapping(target = "invoiceId", ignore = true)
	@Mapping(target = "itemId", ignore = true)
	InvoiceItemDTO toDTO(InvoiceItem invoiceItem);

	@Mapping(target = "invoice.invoiceId", source = "invoiceId")
	InvoiceItem toEntity(InvoiceItemDTO invoiceItemDTO);

	List<InvoiceItemDTO> toDTOs(List<InvoiceItem> invoiceItems);

	List<InvoiceItem> toEntities(List<InvoiceItemDTO> invoiceItemDTOs);

}
