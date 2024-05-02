package com.sep.onlinedeliverysystem.mappers.impl;

import com.sep.onlinedeliverysystem.domain.dto.UserDTO;
import com.sep.onlinedeliverysystem.domain.entities.User;
import com.sep.onlinedeliverysystem.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserMapperImpl implements Mapper<User, UserDTO> {

    private ModelMapper modelMapper;

    public UserMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public UserDTO mapTo(User user) {
        return modelMapper.map(user, UserDTO.class);
    }

    @Override
    public User mapFrom(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }
}
