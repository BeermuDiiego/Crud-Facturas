package api.api_gf.model.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import api.api_gf.model.dto.BusinessDTO;
import api.api_gf.model.entity.Business;

@Mapper(componentModel = "spring")
public interface BusinessMapper {

	BusinessMapper INSTANCE = Mappers.getMapper(BusinessMapper.class);

	@Mapping(target = "userId", ignore = true)
	@Mapping(target = "clients", ignore = true)
	BusinessDTO toDto(Business business);

	@Mapping(target = "user.userId", source = "userId")
	@Mapping(target = "clients", ignore = true)
	Business toEntity(BusinessDTO businessDTO);

	@Mapping(target = "userId", ignore = true)
	@Mapping(target = "clients", ignore = true)
	List<BusinessDTO> ToDTOs(List<Business> businesses);

}
