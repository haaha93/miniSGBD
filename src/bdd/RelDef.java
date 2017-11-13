package bdd;

import manager.PageId;

public class RelDef {
	
	private RelSchema relSchema;
	private PageId headerPage;
	private int recordSize;
	private int slotCount;
	

	public RelDef(String[] userInput,int page) {
		relSchema = new RelSchema(userInput);
		this.headerPage = new PageId(page, 0);
	}	
	
	public RelDef(RelSchema relSchema, int page, int recordSize, int slotCount) {
		super();
		this.relSchema = relSchema;
		this.headerPage = new PageId(page,0);
		this.recordSize = recordSize;
		this.slotCount=slotCount;
	}



	public String toString(){
		
		return relSchema.toString()+headerPage.toString();
	}

	public RelSchema getRelSchema() {
		return relSchema;
	}

	public void setRelSchema(RelSchema relSchema) {
		this.relSchema = relSchema;
	}

	public int getRecordSize() {
		return recordSize;
	}

	public void setRecordSize(int recordSize) {
		this.recordSize = recordSize;
	}

	public int getSlotCount() {
		return slotCount;
	}

	public void setSlotCount(int slotCount) {
		this.slotCount = slotCount;
	}

	public PageId getHeaderPage() {
		return headerPage;
	}

	public void setHeaderPage(PageId headerPage) {
		this.headerPage = headerPage;
	}

}
