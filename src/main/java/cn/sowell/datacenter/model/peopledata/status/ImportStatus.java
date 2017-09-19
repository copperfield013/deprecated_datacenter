package cn.sowell.datacenter.model.peopledata.status;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;

public class ImportStatus {
	private Integer total;
	private Integer current;
	private boolean breakFlag = false;
	private LinkedList<Message> messages = new LinkedList<ImportStatus.Message>();
	private boolean endFlag = false;
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
	public Integer getCurrent() {
		return current;
	}
	public void setCurrent(Integer current) {
		this.current = current;
	}
	
	public boolean breaked(){
		return breakFlag;
	}
	
	public void breakImport(){
		this.breakFlag = true;
	}
	
	public void setEnded(){
		this.endFlag  = true;
	}
	
	public boolean ended(){
		return endFlag;
	}
	
	public String getCurrentMessage() {
		Message message = messages.getLast();
		if(message != null){
			return message.getMessage();
		}
		return null;
	}
	public void appendMessage(String msg) {
		this.messages.add(new Message(msg));
	}
	
	/**
	 * 获得最后两条消息之间的时间间隙
	 * @return
	 */
	public long lastInterval(){
		Iterator<Message> iterate = this.messages.descendingIterator();
		if(iterate.hasNext()){
			Message last = iterate.next();
			if(iterate.hasNext()){
				Message prev = iterate.next();
				return last.getTime().getTime() - prev.getTime().getTime();
			}
		}
		return 0;
	}
	
	
	class Message{
		private String message;
		private Date createTime = new Date();
		
		public Message(String msg) {
			this.message = msg;
		}
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}
		public Date getTime(){
			return createTime;
		}
	}
}
