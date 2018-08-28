package com.cloud.pay.common.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloud.pay.common.entity.Channel;
import com.cloud.pay.common.mapper.ChannelMapper;

@Service
public class ChannelService {
	
	private Logger log = LoggerFactory.getLogger(ChannelService.class);
	
	@Autowired
	private ChannelMapper channelMapper;
	
	public int update(Channel channel) {
		log.info("修改Channel信息：{}", channel);
		return channelMapper.updateByPrimaryKeySelective(channel);
	}

	public int save(Channel channel) {
		log.info("修改Channel信息：{}", channel);
		return channelMapper.insert(channel);
	}

	public int del(Integer id) {
		log.info("根据ChannelID[{}]删除：{}", id);
		return channelMapper.deleteByPrimaryKey(id);
	}
	
	public List<Channel> getchannelList(String channelCode, String channelName){
		return channelMapper.getChannelList(channelCode, channelName);
	}

}
