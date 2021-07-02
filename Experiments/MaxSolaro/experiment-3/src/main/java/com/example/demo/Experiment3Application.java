package com.example.demo;

import java.util.List;
import java.util.Optional;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.example.repo.Pojo;
import com.example.repo.PojoRepository;

@SpringBootApplication
@ComponentScan( "com.example.repo" )

public class Experiment3Application {

	public static void main(String[] args) {
		SpringApplication.run(Experiment3Application.class, args);
	}
	
	@Bean
	public PojoRepository getRepo() {
		return new PojoRepository() {
			
			@Override
			public <S extends Pojo> Optional<S> findOne(Example<S> example) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public <S extends Pojo> Page<S> findAll(Example<S> example, Pageable pageable) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public <S extends Pojo> boolean exists(Example<S> example) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public <S extends Pojo> long count(Example<S> example) {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public <S extends Pojo> S save(S entity) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Optional<Pojo> findById(Long id) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public boolean existsById(Long id) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public void deleteById(Long id) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void deleteAll(Iterable<? extends Pojo> entities) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void deleteAll() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void delete(Pojo entity) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public long count() {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public Page<Pojo> findAll(Pageable pageable) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public <S extends Pojo> S saveAndFlush(S entity) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public <S extends Pojo> List<S> saveAll(Iterable<S> entities) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Pojo getOne(Long id) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public void flush() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public List<Pojo> findAllById(Iterable<Long> ids) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public <S extends Pojo> List<S> findAll(Example<S> example, Sort sort) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public <S extends Pojo> List<S> findAll(Example<S> example) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public List<Pojo> findAll(Sort sort) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public List<Pojo> findAll() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public void deleteInBatch(Iterable<Pojo> entities) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void deleteAllInBatch() {
				// TODO Auto-generated method stub
				
			}
		};
	}

}
