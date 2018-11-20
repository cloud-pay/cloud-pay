package com.cloud.pay.common.service;

import java.util.Date;
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
		List<Channel> channels = channelMapper.getChannelList(channel.getChannelCode(), null);
		if(channels != null && channels.size() > 0) {
			log.info("渠道编码{}已存在", channel.getChannelCode());
			throw new RuntimeException("渠道编码" + channel.getChannelCode() + "已存在");
		}
		log.info("修改Channel信息：{}", channel);
		return channelMapper.updateByPrimaryKeySelective(channel);
	}

	public int save(Channel channel) {
		List<Channel> channels = channelMapper.getChannelList(channel.getChannelCode(), null);
		if(channels != null && channels.size() > 0) {
			log.info("渠道编码{}已存在", channel.getChannelCode());
			throw new RuntimeException("渠道编码" + channel.getChannelCode() + "已存在");
		}
		log.info("新增Channel信息：{}", channel);
		return channelMapper.insert(channel);
	}

	public int del(Integer id) {
		log.info("根据ChannelID[{}]删除：{}", id);
		return channelMapper.deleteByPrimaryKey(id);
	}
	
	public Channel selectById(Integer id) {
	    return channelMapper.selectByPrimaryKey(id);
	}
	
	public List<Channel> getchannelList(String channelCode, String channelName){
		return channelMapper.getChannelList(channelCode, channelName);
	}
   
	/**
	 * 根据记账日期获取未初始化对账的渠道信息
	 * @param accountDate
	 * @return
	 */
	public List<Channel> getUnInitChannelList(Date accountDate){
		return channelMapper.getUnInitChannelList(accountDate);
	}
}
