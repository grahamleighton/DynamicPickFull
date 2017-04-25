package com.ottouk.pdcu.main.domain;

/**
 * <code>ToteBuild</code> List type. equals (and hashCode) overridden to match
 * on <code>toteItem</code> only.
 * 
 * @author dis065
 * 
 */
public class ToteBuildItem {
	
	/**
	 * 
	 */
	private String toteItem;
	
	/**
	 * 
	 */
	private String eanIndicator;

	
	/**
	 * Default constructor. 
	 */
	public ToteBuildItem() {
	}
	
	/**
	 * Alternate constructor.
	 * 
	 * @param toteItem toteItem
	 * @param eanIndicator eanIndicator
	 */
	public ToteBuildItem(String toteItem, String eanIndicator) {
		this.toteItem = toteItem;
		this.eanIndicator = eanIndicator;
	}
	
	
	/**
	 * @return toteItem
	 */
	public String getToteItem() {
		return toteItem;
	}

	/**
	 * @param toteItem toteItem
	 */
	public void setToteItem(String toteItem) {
		this.toteItem = toteItem;
	}

	/**
	 * @return eanIndicator
	 */
	public String getEanIndicator() {
		return eanIndicator;
	}

	/**
	 * @param eanIndicator eanIndicator
	 */
	public void setEanIndicator(String eanIndicator) {
		this.eanIndicator = eanIndicator;
	}
	
	// Override equals (and hashcode) to match on toteItem

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((toteItem == null) ? 0 : toteItem.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final ToteBuildItem other = (ToteBuildItem) obj;
		if (toteItem == null) {
			if (other.toteItem != null) {
				return false;
			}
		} else if (!toteItem.equals(other.toteItem)) {
			return false;
		}
		return true;
	}
	
}
