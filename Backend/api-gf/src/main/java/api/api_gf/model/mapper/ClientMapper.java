package api.api_gf.model.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import api.api_gf.model.dto.ClientDTO;
import api.api_gf.model.entity.Client;

@Mapper(componentModel = "spring")
public interface ClientMapper {

	ClientMapper INSTANCE = Mappers.getMapper(ClientMapper.class);

	@Mapping(target = "userId", ignore = true)
	@Mapping(target = "businessId", source = "business.businessId")
	@Mapping(target = "invoices", ignore = true)
	ClientDTO toDTO(Client client);

	@Mapping(target = "user.userId", source = "userId")
	@Mapping(target = "business.businessId", source = "businessId")
	@Mapping(target = "invoices", ignore = true)
	Client toEntity(ClientDTO clientDTO);

	@Mapping(target = "userId", ignore = true)
	@Mapping(target = "invoices", ignore = true)
	List<ClientDTO> toDTOs(List<Client> clients);

}
