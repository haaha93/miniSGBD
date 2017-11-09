package manager;

public class PageBitmapInfo {
	byte[] slotStatus;

	public PageBitmapInfo(byte[] slotStatus) {
		super();
		this.slotStatus = slotStatus;
	}
	
	public PageBitmapInfo(int size) {
		super();
		slotStatus = new byte[size];
		
	}

	public byte[] getSlotStatus() {
		return slotStatus;
	}

	public void setSlotStatus(byte[] slotStatus) {
		this.slotStatus = slotStatus;
	}
	
	public void setValueAtIndexOfSlotsStatus(int index, byte value){
		slotStatus[index]=value;
	}
	
	public byte getValueAtIndexOfSlotsStatus(int index){
		return slotStatus[index];
	}
}
