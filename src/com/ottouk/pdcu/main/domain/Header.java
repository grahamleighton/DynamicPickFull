package com.ottouk.pdcu.main.domain;

import com.ottouk.pdcu.main.utils.StringUtils;

/**
 * Abstract class with common domain-object variables and methods.
 * 
 * @author dis065
 *
 */
public abstract class Header {

	/**
	 * Message id.
	 */
	private String messageId;
	
	/**
	 * Unit id.
	 */
	private Integer unitId;
	
	/**
	 * Operator number. 
	 */
	private String operator;
	
	
	/**
	 * Build common domain-object header.
	 * 
	 * @return header string
	 */
	protected String buildHeader() {

		String header = new String();
		header += StringUtils.padField(messageId, 1);
		header += StringUtils.padNumber(unitId, 4);
		header += StringUtils.padNumber(operator, 8);

		return header;
	}
	
	
	/**
	 * Get messageId.
	 * 
	 * @return messageId
	 */
	public String getMessageId() {
		return messageId;
	}

	/**
	 * Set messageId.
	 * 
	 * @param messageId message id
	 */
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	/**
	 * Get unitId.
	 * 
	 * @return unitId
	 */
	public Integer getUnitId() {
		return unitId;
	}

	/**
	 * Set unitId.
	 * 
	 * @param unitId unit id
	 */
	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	/**
	 * Get operator.
	 * 
	 * @return operator
	 */
	public String getOperator() {
		return operator;
	}

	/**
	 * Set operator.
	 * 
	 * @param operator operator number
	 */
	public void setOperator(String operator) {
		this.operator = operator;
	}

}
