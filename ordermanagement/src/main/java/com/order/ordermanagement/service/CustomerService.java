package com.order.ordermanagement.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.stereotype.Service;

import com.order.ordermanagement.common.exception.ApiException;
import com.order.ordermanagement.common.exception.CustomerError;
import com.order.ordermanagement.entity.CustomerEntity;
import com.order.ordermanagement.mapper.CustomerMapper;
import com.order.ordermanagement.model.CustomerModel;
import com.order.ordermanagement.model.custom.CustomerCountPerCity;
import com.order.ordermanagement.repo.CustomerRepo;


@Service
public class CustomerService {

	@Autowired
	CustomerRepo customerRepo;
	
	@Autowired
	CustomerMapper customerMapper;
	
	public void addCustomer(CustomerModel customerModel) {
		CustomerEntity customerEntity = customerMapper.convertCustomerModelToCustomerEntity(customerModel);
		customerRepo.save(customerEntity);
	}

	public List<CustomerModel> getCustomers(String sortBy, String orderBy, String name, String email, String city, String state) {
		List<CustomerEntity> customerEntityList = new ArrayList<>();
		if(city != null) {
			customerEntityList = customerRepo.findCustomerByCity(city);
		}else if(state != null) {
			customerEntityList = customerRepo.findCustomerByState(state);
		}else if(name != null) {
			customerEntityList = customerRepo.findCustomerByName(name);
		}else if(email != null) {
			customerEntityList = customerRepo.findCustomerByEmail(email);
		}else if(sortBy != null) {
			switch (sortBy) {
			case "name":
				if (orderBy != null && orderBy.equalsIgnoreCase("desc")) {
					customerEntityList = customerRepo.findAll(Sort.by(Sort.Direction.DESC, sortBy));
				} else {
					customerEntityList = customerRepo.findAll(Sort.by(Sort.Direction.ASC, sortBy));
				}
				break;
			case "namelength":
				customerEntityList = customerRepo.findAllCustomers(JpaSort.unsafe("LENGTH(name)"));
				break;
			default:
				throw new ApiException(CustomerError.CUSTOMER_INVALID_SORT_KEY);
			}
		} else if(sortBy == null && orderBy != null) {
			throw new ApiException(CustomerError.CUSTOMER_SORTBY_PARAM_MISSING);
		} else {
			customerEntityList = customerRepo.findAll();
		}
		return customerMapper.convertCustomerEntityListToCustomerModelList(customerEntityList);
	}

	public CustomerModel getCustomerById(int id) {
		CustomerEntity customerEntity = customerRepo.findById(id)
				.orElseThrow(()-> new ApiException(CustomerError.CUSTOMER_NOT_FOUND));
		return customerMapper.convertCustomerEntityToCustomerModel(customerEntity);
	}
	
	public List<CustomerCountPerCity> getCustomerCount() {
		List<CustomerCountPerCity> customerCountList = customerRepo.findCustomerCountPerCity();
		return customerCountList;
	}

	public void updateCustomer(int id, CustomerModel customerModel) {
		CustomerEntity customerEntity = customerRepo.findById(id)
				.orElseThrow(()-> new ApiException(CustomerError.CUSTOMER_NOT_FOUND));
		CustomerEntity updatedCustomerEntity = customerMapper.mapCustomerModelToCustomerEntity(customerEntity, customerModel);
		customerRepo.save(updatedCustomerEntity);
	}

	public void deleteCustomer(int id) {
		customerRepo.findById(id).orElseThrow(()-> new ApiException(CustomerError.CUSTOMER_NOT_FOUND));
		customerRepo.deleteById(id);
	}
}
