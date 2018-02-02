package com.l2f.vitheakids.model;

/**
 * Updated by Soraia Meneses Alarcão on 21/07/2017
 */

public class LoginInfo {
	
	private String greetingMsg;
	private String exListMsg;
	
	public LoginInfo() {
	}

	/**
	 * @return the greetingMsg
	 */
	public String getGreetingMsg() {
		return greetingMsg;
	}
    /**
     * @return the exListMsg
     */
    public String getExListMsg() {
        return exListMsg;
    }


	/**
	 * @param greetingMsg the greetingMsg to set
	 */
	public void setGreetingMsg(String greetingMsg) {
		this.greetingMsg = greetingMsg;
	}
	/**
	 * @param exListMsg the exListMsg to set
	 */
	public void setExListMsg(String exListMsg) {
		this.exListMsg = exListMsg;
	}
}
