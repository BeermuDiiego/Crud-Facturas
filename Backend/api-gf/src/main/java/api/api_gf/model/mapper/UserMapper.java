package api.api_gf.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import api.api_gf.model.dto.UserRegisterDTO;
import api.api_gf.model.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

	UserMapper INSTACE = Mappers.getMapper(UserMapper.class);

	UserRegisterDTO toDto(User user);

	@Mapping(target = "userId", ignore = true)
	@Mapping(target = "role", ignore = true)
	User toEntity(UserRegisterDTO userDTO);

}
