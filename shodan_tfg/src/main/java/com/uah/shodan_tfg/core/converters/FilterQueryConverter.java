package com.uah.shodan_tfg.core.converters;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.uah.shodan_tfg.dataproviders.dao.FilterQuery;
import com.uah.shodan_tfg.entrypoints.dto.FilterQueryDTO;

@Service
public class FilterQueryConverter implements Converter<FilterQueryDTO, FilterQuery> {

    @Override
    public FilterQuery convert(FilterQueryDTO item) {
	FilterQuery filterQuery = new FilterQuery();

	filterQuery.setId(item.getId());
	filterQuery.setQuery(item.getQuery());

	return filterQuery;
    }

    public List<FilterQuery> convert(List<FilterQueryDTO> filtersDTO) {
	List<FilterQuery> filters = new ArrayList<>();
	for (FilterQueryDTO filterDTO : filtersDTO) {
	    FilterQuery filter = convert(filterDTO);
	    filters.add(filter);
	}
	return filters;
    }

    @Override
    public FilterQueryDTO invert(FilterQuery item) {
	FilterQueryDTO filterQueryDTO = FilterQueryDTO.builder().id(item.getId()).query(item.getQuery()).build();

	return filterQueryDTO;
    }

    public List<FilterQueryDTO> invert(List<FilterQuery> filters) {
	List<FilterQueryDTO> filtersDTO = new ArrayList<>();
	for (FilterQuery filter : filters) {
	    FilterQueryDTO filterDTO = invert(filter);
	    filtersDTO.add(filterDTO);
	}
	return filtersDTO;
    }

}
