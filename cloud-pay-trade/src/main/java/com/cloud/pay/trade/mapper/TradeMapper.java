package com.cloud.pay.trade.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cloud.pay.trade.dto.FeeStatDTO;
import com.cloud.pay.trade.dto.TradeDTO;
import com.cloud.pay.trade.dto.TradeRecordDTO;
import com.cloud.pay.trade.dto.TradeStatDTO;
import com.cloud.pay.trade.entity.Trade;

public interface TradeMapper {
	int deleteByPrimaryKey(Integer id);

	int insert(Trade record);

	int insertSelective(Trade record);

	Trade selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(Trade record);

	int updateByPrimaryKey(Trade record);

	/**
	 * 查询出交易订单存在但是渠道不存在的记录
	 * 
	 * @param reconDate
	 * @return
	 */
	List<TradeDTO> selectLongRecord(String reconDate);

	/**
	 * 查询交易表和渠道订单号相同但其他要素不相等的记录
	 * 
	 * @param reconDate
	 * @return
	 */
	List<TradeDTO> selectExceptionRecord(String reconDate);

	/**
	 * 通过批次号查询
	 * 
	 * @param reconDate
	 * @return
	 */
	List<Trade> selectByBatchNo(@Param("batchNo") String batchNo);

	/**
	 * 通过批次号修改交易时间
	 * 
	 * @author dbnaxlc
	 * @date 2018年9月12日 下午4:16:54
	 * @param record
	 * @return
	 */
	int updateByBatchNo(@Param("tradeTime") Date tradeTime, @Param("status") Integer status,
			@Param("batchNo") String batchNo);

	int updateStatus(Trade record);

	/**
	 * 根据商户ID和订单号查询交易是否存在，如存在则返回orderNo
	 * 
	 * @param merchantId
	 * @param orderNo
	 * @return
	 */
	String selectExist(@Param("merchantId") Integer merchantId, @Param("orderNo") String orderNo);

	/**
	 * 商户交易统计
	 * 
	 * @param merchantName
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	TradeStatDTO tradeStat(@Param("merchantId") Integer merchantId, @Param("orgId") Integer orgId,
			@Param("startTime") Date startTime, @Param("endTime") Date endTime,
			@Param("userMerchantId")Integer userMerchantId,
			@Param("userMerchantType")String userMerchantType);

	/**
	 * 商户垫资交易统计
	 * 
	 * @param merchantName
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	TradeStatDTO loanTradeStat(@Param("merchantId") Integer merchantId, @Param("orgId") Integer orgId,
			@Param("startTime") Date startTime, @Param("endTime") Date endTime,
			@Param("userMerchantId")Integer userMerchantId,
			@Param("userMerchantType")String userMerchantType);

	/**
	 * 交易列表查询
	 * @author dbnaxlc
	 * @date 2018年9月14日 上午11:01:37
	 * @param merchantId
	 * @param orgId
	 * @param orderNo
	 * @param batchNo
	 * @return
	 */
	List<TradeRecordDTO> selectTradeList(@Param("merchantId") Integer merchantId, @Param("orgId") Integer orgId,
		 @Param("orderNo") String orderNo, @Param("batchNo") String batchNo, @Param("loaning") Integer loaning,
			@Param("startTime") Date startTime, @Param("endTime") Date endTime,
			@Param("userMerchantId")Integer userMerchantId,
			@Param("userMerchantType")String userMerchantType);
	
	/**
	 * 商户手续费统计
	 * @author dbnaxlc
	 * @date 2018年9月14日 下午3:11:48
	 * @param merchantId
	 * @param orgId
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	List<FeeStatDTO> selectMerchantFeeStats(@Param("merchantId") Integer merchantId, @Param("orgId") Integer orgId,
				@Param("startTime") Date startTime, @Param("endTime") Date endTime,
				@Param("userMerchantId")Integer userMerchantId,
				@Param("userMerchantType")String userMerchantType);
	
	/**
	 * 机构交易手续费统计
	 * @author dbnaxlc
	 * @date 2018年9月14日 下午4:50:41
	 * @param orgId
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	List<FeeStatDTO> selectOrgTradeFeeStats(@Param("orgId") Integer orgId,
			@Param("startTime") Date startTime, @Param("endTime") Date endTime,
			@Param("userMerchantId")Integer userMerchantId,
			@Param("userMerchantType")String userMerchantType);
	
	/**
	 * 机构下商户交易分润统计
	 * @author dbnaxlc
	 * @date 2018年9月14日 下午4:51:03
	 * @param orgId
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	List<FeeStatDTO> selectMerchantFeeByOrg(@Param("orgId") Integer orgId,
			@Param("startTime") Date startTime, @Param("endTime") Date endTime);
	
	/**
	 * 根据商户号和订单号查询交易信息
	 * @param merchantId
	 * @param orderNo
	 * @return
	 */
	TradeDTO selectTradeByMerIdAndOrderNo(@Param("merchantId")Integer merchantId,@Param("orderNo")String orderNo);
	
	
	/**
	 * 根据商户ID和对账日期获取交易信息
	 * @param merchantId
	 * @param reconDate
	 * @return
	 */
	List<TradeDTO> selectListByMerIdAndReconDate(@Param("merchantId")Integer merchantId,@Param("reconDate")Date reconDate);
	
	/**
	 * 通过批次号修改批量交易状态和确认时间
	 * @param batchNo
	 * @param returnInfo
	 * @param returnCode
	 * @param status
	 * @return
	 */
	int updateStatusByBatchNo(@Param("batchNo") String batchNo, @Param("returnInfo") String returnInfo, 
			@Param("returnCode") String returnCode,@Param("status") Integer status,
			@Param("tradeConfirmTime") Date tradeConfirmTime);
	
	/**
	 * 根据批次号查询渠道入参
	 * @param batchNo
	 * @return
	 */
	List<com.cloud.pay.channel.dto.TradeDTO> selectByBatchNoOrderBySeq(@Param("batchNo") String batchNo);
	
    /**
     * 查询对账后待调账的交易数据
     * @param reconDate
     * @return
     */
	List<Trade>  selectWaitingAdjustTrade(String reconDate);
	
	/**
	 * 汇总对账数据
	 * @param reconDate
	 * @return
	 */
	TradeStatDTO selectAllReconTrade(String reconDate);
}