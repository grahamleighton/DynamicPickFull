package com.ottouk.pdcu.main.domain;

import com.ottouk.pdcu.main.utils.StringUtils;

/**
 * Domain object for <code>OpCounts</code> interface. Contains all required
 * request and response fields, and methods for the manipulation thereof.
 * 
 * @author dis065
 * 
 */
public class OpCounts extends Header {
	
	// Response fields
	
	/**
	 * Response field. 
	 */
	private String response;
	
	/**
	 * Response field.
	 */
	private String ok;
	
	/**
	 * Response field.
	 */
	private Integer build;
	
    /**
     * Response field.
     */
    private Integer built;
    
    /**
     * Response field.
     */
    private Integer putaway;
    
    /**
     * Response field.
     */
    private Integer totePutawayItems;
    
    /**
     * Response field.
     */
    private Integer consolLocations;
    
    /**
     * Response field.
     */
    private Integer piLocations;
    
    /**
     * Response field.
     */
    private Integer piItems;
    
    /**
     * Response field.
     */
    private Integer consolItems;
    
    //
    
    /**
     * Build opCounts request.
     * 
     * @return opsCount request
     */
    public String buildOpCountsRequest() {
		return buildHeader();
    }
    
    /**
     * Populate response fields.
     * 
     * @param response Comms response
     */
    public void receiveOpCounts(String response) {
    	this.response = StringUtils.getString(response, 0, 3);
    	ok = StringUtils.getString(response, 3, 4);
    	build = StringUtils.getInteger(response, 4, 8);
        built = StringUtils.getInteger(response, 8, 12);
        putaway = StringUtils.getInteger(response, 12, 16);
        totePutawayItems = StringUtils.getInteger(response, 16, 20);
        consolLocations = StringUtils.getInteger(response, 20, 24);
        piLocations = StringUtils.getInteger(response, 24, 28);
        piItems = StringUtils.getInteger(response, 28, 32);
        consolItems = StringUtils.getInteger(response, 32, 36);
    }

    //
    
	/**
	 * @return response
	 */
	public String getResponse() {
		return response;
	}

	/**
	 * @param response response
	 */
	public void setResponse(String response) {
		this.response = response;
	}

	/**
	 * @return ok
	 */
	public String getOk() {
		return ok;
	}

	/**
	 * @param ok ok
	 */
	public void setOk(String ok) {
		this.ok = ok;
	}

	/**
	 * @return build
	 */
	public Integer getBuild() {
		return build;
	}

	/**
	 * @param build build
	 */
	public void setBuild(Integer build) {
		this.build = build;
	}

	/**
	 * @return built
	 */
	public Integer getBuilt() {
		return built;
	}

	/**
	 * @param built built
	 */
	public void setBuilt(Integer built) {
		this.built = built;
	}

	/**
	 * @return putaway
	 */
	public Integer getPutaway() {
		return putaway;
	}

	/**
	 * @param putaway putaway
	 */
	public void setPutaway(Integer putaway) {
		this.putaway = putaway;
	}

	/**
	 * @return totePutawayItems
	 */
	public Integer getTotePutawayItems() {
		return totePutawayItems;
	}

	/**
	 * @param totePutawayItems totePutawayItems
	 */
	public void setTotePutawayItems(Integer totePutawayItems) {
		this.totePutawayItems = totePutawayItems;
	}

	/**
	 * @return consolLocations
	 */
	public Integer getConsolLocations() {
		return consolLocations;
	}

	/**
	 * @param consolLocations consolLocations
	 */
	public void setConsolLocations(Integer consolLocations) {
		this.consolLocations = consolLocations;
	}

	/**
	 * @return piLocations
	 */
	public Integer getPiLocations() {
		return piLocations;
	}

	/**
	 * @param piLocations piLocations
	 */
	public void setPiLocations(Integer piLocations) {
		this.piLocations = piLocations;
	}

	/**
	 * @return piItems
	 */
	public Integer getPiItems() {
		return piItems;
	}

	/**
	 * @param piItems piItems
	 */
	public void setPiItems(Integer piItems) {
		this.piItems = piItems;
	}

	/**
	 * @return consolItems
	 */
	public Integer getConsolItems() {
		return consolItems;
	}

	/**
	 * @param consolItems consolItems
	 */
	public void setConsolItems(Integer consolItems) {
		this.consolItems = consolItems;
	}
    
}
