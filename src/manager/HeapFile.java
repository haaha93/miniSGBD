package manager;

import java.io.IOException;
import java.nio.ByteBuffer;

import bdd.RelDef;
import constant.Constant;

public class HeapFile {

	private RelDef relDef;

	public HeapFile(RelDef relDef) {
		this.relDef = relDef;
	}

	// Le Header devra avoir un nom de fichier different des autres noms de
	// fichier relations
	public void createHeader() throws IOException {

		ByteBuffer buffer = ByteBuffer.allocate(Constant.PAGESIZE);

		DiskManager.addPage(relDef.getPage());
		BufferManager.getPage(relDef.getPage());
		DiskManager.writePage(relDef.getPage(), buffer);
		BufferManager.freePage(relDef.getPage(), true);

	}

	public void readHeaderPageInfo(ByteBuffer buffer, HeaderPageInfo hpi) {
		hpi.setNbPagesDeDonnees(buffer.getInt());

		for (int i = 0; i < hpi.getNbPagesDeDonnees(); i++) {
			Info info = new Info(buffer.getInt(), buffer.getInt());
			hpi.getInfos().set(i, info);
		}

	}

	public void writeHeaderPageInfo(ByteBuffer buffer, HeaderPageInfo hpi) {
		buffer.putInt(hpi.getNbPagesDeDonnees());

		for (int i = 0; i < hpi.getNbPagesDeDonnees(); i++) {
			Info info = hpi.getInfos().get(i);
			buffer.putInt(info.getIdxPages());
			buffer.putInt(info.getNbSlotsAvailable());
		}
	}

	public void getHeaderPageInfo(HeaderPageInfo hpi) {
		ByteBuffer buffer;
		try {
			buffer = BufferManager.getPage(this.relDef.getPage());
			readHeaderPageInfo(buffer, hpi);
			BufferManager.freePage(this.relDef.getPage(), false);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void updateHeaderDataPage(PageId newPageId) throws IOException {
		HeaderPageInfo hpi = new HeaderPageInfo();
		PageId headerPage = new PageId(newPageId.getFileId(), 0);
		ByteBuffer bufferHeader = BufferManager.getPage(headerPage);
		readHeaderPageInfo(bufferHeader, hpi);
	
		hpi.incrementNbPagesDeDonnees(1);
		hpi.getInfos().add(new Info(newPageId.getIdx(), this.relDef.getSlotCount()));
		writeHeaderPageInfo(bufferHeader, hpi);
		BufferManager.freePage(headerPage, true);
	}
	
	public void updateHeaderTakenSlot(PageId pid) throws IOException{
		HeaderPageInfo hpi = new HeaderPageInfo();
		PageId headerPage = new PageId(pid.getFileId(), 0);
		ByteBuffer bufferHeader = BufferManager.getPage(headerPage);
		readHeaderPageInfo(bufferHeader, hpi);
		
		hpi.getInfos().get(pid.getIdx()).incrementerNbslotsAvailable(-1);
		writeHeaderPageInfo(bufferHeader, hpi);
		BufferManager.freePage(headerPage, true);
	}
	
	public void readPageBitmapInfo(ByteBuffer buffer, PageBitmapInfo pbi){
		int slot = this.relDef.getSlotCount();
		for(int i =0; i < slot; i++)
			pbi.setValueAtIndexOfSlotsStatus(i, buffer.get(i));
	}
	
	public void writePageBitmapInfo(ByteBuffer buffer, PageBitmapInfo pbi){
		int slot = this.relDef.getSlotCount();
		for(int i =0; i < slot; i++)
			buffer.put(i, pbi.getValueAtIndexOfSlotsStatus(i));
	}

}
