package com.ottouk.pdcu.main.service;

import java.util.ArrayList;
import java.util.List;

import com.ottouk.pdcu.main.domain.ToteBuild;
import com.ottouk.pdcu.main.domain.ToteBuildItem;
import com.ottouk.pdcu.main.utils.Validate;

public class ToteBuildServiceImpl extends GeneralServiceImpl implements ToteBuildService {
	
	private List toteIds = new ArrayList();
	private ToteBuild toteBuild;
	private String toteType;
	
	
	
	public ToteBuildServiceImpl() {
		super();
		toteType = "N";
	}
	

	public boolean checkTotePreviouslyUsed(String toteId) {
		
		if (toteId != null) {
			toteId = toteId.trim();
		}
		
		if (toteIds.contains(toteId)) {
			// Tote already used.
			return returnCode(RC_WARN_TOTE_PREVIOUSLY_USED);
		} else {
			return returnCode(RC_OK);
		}
	}
	
	
	
	public boolean addTote(String toteId) {

		if (toteId != null) {
			toteId = toteId.trim();
		}
		
		if (toteBuild == null) {
			return startTote(toteId);
		} else {
			return endTote(toteId);
		}
	}
	
	
	public boolean addToteAgain(String toteId) {
		
		if (toteId != null) {
			toteId = toteId.trim();
		}
		
		if (toteBuild == null) {
			return startToteAgain(toteId);
		} else {
			return returnCode(RC_ERROR_TOTE_STILL_OPEN);
		}
	}
	
	public boolean addScan(String scan) {
		
		if (scan != null) {
			scan = scan.trim();
		}
		
		if (validToteId(scan)) {
			return addTote(scan);
		} else if (validToteItem(scan)) {
			return addItem(scan);
		} else if (checkMisScan(scan)) {
			return returnCode(RC_WARN_MIS_SCAN);
		} else {
			return returnCode(RC_ERROR_INVALID_SCAN);
		}
	}
	
	private boolean validToteId(String toteId) {
		return ( Validate.mod10Check3131(toteId, TOTE_ID_LENGTH, TOTE_ID_PREFIX) 
				|| Validate.mod10Check3131(toteId, TOTE_ID_LENGTH, "50"));
	}
	
	public boolean validToteItem(String toteItem) {
		return Validate.mod11Check(toteItem, TOTE_ITEM_LENGTH);
	}
	
	private boolean checkMisScan(String scan) {
		return ((scan.length() == TOTE_ID_LENGTH) && !scan.startsWith(TOTE_ID_PREFIX));
	}
	
	private boolean startTote(String toteId) {

		if ( toteType.equalsIgnoreCase("N") && toteIds.contains(toteId)) {
			// Tote already used.
			return returnCode(RC_WARN_TOTE_PREVIOUSLY_USED);
		} else {
			return startToteAgain(toteId);
		}
	}
	
	private boolean startToteAgain(String toteId) {
		
		if (validToteId(toteId)) {
			// Add toteId to save list.
			toteIds.add(toteId);

			// Start new tote.
			toteBuild = new ToteBuild();
			toteBuild.setMessageId(TOTE_BUILD_MESSAGE_ID);
			toteBuild.setUnitId(unitId);
			toteBuild.setOperator(operator);
			toteBuild.setToteId(toteId);
			toteBuild.setToteType(toteType);
			

			return returnCode(RC_OK);
		} else if (checkMisScan(toteId)) {
			return returnCode(RC_WARN_MIS_SCAN);
		} else {
			// Invalid tote.
			return returnCode(RC_ERROR_INVALID_TOTE);
		}
	}
	/**
	 * Ends the tote by sending a tote build message to ther server.
	 * @param toteId Tote ID being built.
	 * @return true if transaction ok.
	 */
	public final boolean endTote(final String toteId) {
	
		if (toteBuild.getToteId().equals(toteId)) {

			boolean transactOK = transact(toteBuild.buildTote());
			if (transactOK) {
				// Tote transacted successfully
				toteBuild = null;
			}
			return transactOK;	// returnCode set by transact() method

		} else {
			return returnCode(RC_ERROR_TOTE_STILL_OPEN);
		}
	}
	
	private boolean addItem(String toteItem) {
		
		
		if (toteBuild.getToteBuildItems() == null) {
			toteBuild.setToteBuildItems(new ArrayList());
		}
		
		ToteBuildItem toteBuildItem = new ToteBuildItem(toteItem, TOTE_BUILD_EAN_INDICATOR);
		
		if (toteBuild.getToteBuildItems().contains(toteBuildItem)) {
			return returnCode(RC_WARN_ITEM_PREVIOUSLY_USED);
		} 
		else
		{
			if (!toteBuild.isToteFull()) {
		//		System.out.println("Adding item");
				toteBuild.getToteBuildItems().add(toteBuildItem);
				if (toteBuild.isToteFull()) {
					return returnCode(RC_WARN_TOTE_FULL);
				} else {
					return returnCode(RC_OK);
				}
			} else {
	//			System.out.println("Max items msg");
				return returnCode(RC_ERROR_MAX_ITEMS_IN_TOTE);
			}
		}
	}
	
	public int getToteItemCount() {
		return (toteBuild == null ? 0 : toteBuild.getItemCount());
	}
	
	public String getToteId() {
		return (toteBuild == null ? null : toteBuild.getToteId());
	}
	public boolean addScanUnlimited(String scan) {
		
		if (scan != null) {
			scan = scan.trim();
		}
		
		if (validToteId(scan)) {
			return addTote(scan);
		} else if (validToteItem(scan)) {
			return addItemUnlimited(scan);
		} else if (checkMisScan(scan)) {
			return returnCode(RC_WARN_MIS_SCAN);
		} else {
			return returnCode(RC_ERROR_INVALID_SCAN);
		}
	}

	/**
	 * Method to empty list of tote ids used.
	 * Mainly to provide functionality to Consolidation activity.
	 */
	public final void clearList() {
		toteIds.clear();
	}

	/**
	 * Sets the tote type for the tote build messages.
	 * @param toteType String either "N" or "C"
	 */
	public void setToteType(String toteType) {
		this.toteType = toteType;
	}

	private boolean addItemUnlimited(String toteItem) {
		
		if (toteBuild.getToteBuildItems() == null) {
			toteBuild.setToteBuildItems(new ArrayList());
		}
		
		ToteBuildItem toteBuildItem = new ToteBuildItem(toteItem, TOTE_BUILD_EAN_INDICATOR);
		
		if (toteBuild.getToteBuildItems().contains(toteBuildItem)) {
			return returnCode(RC_WARN_ITEM_PREVIOUSLY_USED);
		} else {

			toteBuild.getToteBuildItems().add(toteBuildItem);
			return returnCode(RC_OK);			
		}
	}
	
	/**private void getLsize (){
		getLsize();
	}*/	

}
