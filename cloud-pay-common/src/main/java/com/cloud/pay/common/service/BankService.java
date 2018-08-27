package com.cloud.pay.common.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloud.pay.common.entity.Bank;
import com.cloud.pay.common.mapper.BankMapper;

@Service
public class BankService {
	
	private Logger log = LoggerFactory.getLogger(BankService.class);
	
	@Autowired
	private BankMapper bankMapper;
	
	public int update(Bank bank) {
		log.info("修改bank信息：{}", bank);
		return bankMapper.updateByPrimaryKeySelective(bank);
	}

	public int save(Bank bank) {
		log.info("修改bank信息：{}", bank);
		return bankMapper.insert(bank);
	}

	public int del(Integer id) {
		log.info("根据bankID[{}]删除：{}", id);
		return bankMapper.deleteByPrimaryKey(id);
	}
	
	public List<Bank> getBankList(String bankCode, String bankName){
		return bankMapper.getBankList(bankCode, bankName);
	}

}
