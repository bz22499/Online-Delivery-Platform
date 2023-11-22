package com.sep.onlinedeliverysystem.mappers.impl;

import com.sep.onlinedeliverysystem.domain.dto.MenuItemDTO;
import com.sep.onlinedeliverysystem.domain.entities.MenuItem;
import com.sep.onlinedeliverysystem.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class MenuItemMapperImpl implements Mapper<MenuItem, MenuItemDTO> {
    private ModelMapper modelMapper;

    public MenuItemMapperImpl (ModelMapper modelMapper){ this.modelMapper = modelMapper; }

    @Override
    public MenuItemDTO mapTo(MenuItem menuItem) { return modelMapper.map(menuItem, MenuItemDTO.class); }

    @Override
    public MenuItem mapFrom(MenuItemDTO menuItemDTO) { return modelMapper.map(menuItemDTO, MenuItem.class); }
}
