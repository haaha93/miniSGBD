package bdd;

import manager.PageId;

public class RelDef {
	
	private RelSchema relSchema;
	private PageId page;
	

	public RelDef(String[] userInput,int page) {
		relSchema = new RelSchema(userInput);
		this.page = new PageId(page, 0);
	}
	
	public String toString(){
		
		return relSchema.toString()+page.toString();
	}

	public RelSchema getRelSchema() {
		return relSchema;
	}

	public void setRelSchema(RelSchema relSchema) {
		this.relSchema = relSchema;
	}

	public PageId getPage() {
		return page;
	}

	public void setPage(PageId page) {
		this.page = page;
	}
	
}
