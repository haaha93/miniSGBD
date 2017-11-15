package manager;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

import bdd.Record;
import bdd.RelDef;
import constant.Constant;

public class HeapFile {

	private RelDef relDef;

	public HeapFile(RelDef relDef) {
		this.relDef = relDef;
	}

	public void createHeader() throws IOException {
		
		DiskManager.addPage(relDef.getHeaderPage());
		BufferManager.getPage(relDef.getHeaderPage());
		BufferManager.freePage(relDef.getHeaderPage(), true);

	}

	public void readHeaderPageInfo(ByteBuffer buffer, HeaderPageInfo hpi) {
		hpi.setNbPagesDeDonnees(buffer.getInt(0));

		for (int i = 0; i < hpi.getNbPagesDeDonnees(); i++) {
			Info info = new Info(buffer.getInt(), buffer.getInt());
			hpi.getInfos().set(i, info);
		}

	}

	public void writeHeaderPageInfo(ByteBuffer buffer, HeaderPageInfo hpi) {
		buffer.putInt(0, hpi.getNbPagesDeDonnees());

		for (int i = 0; i < hpi.getNbPagesDeDonnees(); i++) {
			Info info = hpi.getInfos().get(i);
			buffer.putInt(info.getIdxPages());
			buffer.putInt(info.getNbSlotsAvailable());
		}
	}

	public void getHeaderPageInfo(HeaderPageInfo hpi) {
		ByteBuffer buffer;
		try {
			buffer = BufferManager.getPage(this.relDef.getHeaderPage());
			readHeaderPageInfo(buffer, hpi);
			BufferManager.freePage(this.relDef.getHeaderPage(), false);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void updateHeaderDataPage(PageId newPageId) throws IOException {
		HeaderPageInfo hpi = new HeaderPageInfo();
		PageId headerPage = this.relDef.getHeaderPage();
		ByteBuffer bufferHeader = BufferManager.getPage(headerPage);
		readHeaderPageInfo(bufferHeader, hpi);
		Info info = new Info(newPageId.getIdx(), this.relDef.getSlotCount());

		hpi.incrementNbPagesDeDonnees(1);
		hpi.addInfo(info);
		writeHeaderPageInfo(bufferHeader, hpi);
		BufferManager.getPage(headerPage);
		DiskManager.writePage(headerPage, bufferHeader);
		BufferManager.freePage(headerPage, true);
	}

	public void updateHeaderTakenSlot(PageId pid) throws IOException {
		HeaderPageInfo hpi = new HeaderPageInfo();
		PageId headerPage = new PageId(pid.getFileId(), 0);
		ByteBuffer bufferHeader = BufferManager.getPage(headerPage);
		readHeaderPageInfo(bufferHeader, hpi);

		hpi.getInfos().get(pid.getIdx()).incrementerNbslotsAvailable(-1);
		writeHeaderPageInfo(bufferHeader, hpi);		
		BufferManager.getPage(pid);
		DiskManager.writePage(pid, bufferHeader);
		BufferManager.freePage(headerPage, true);
	}

	public void readPageBitmapInfo(ByteBuffer buffer, PageBitmapInfo pbi) {
		int slot = this.relDef.getSlotCount();
		for (int i = 0; i < slot; i++)
			pbi.setValueAtIndexOfSlotsStatus(i, buffer.get(i));
	}

	public void writePageBitmapInfo(ByteBuffer buffer, PageBitmapInfo pbi) {
		int slot = this.relDef.getSlotCount();
		for (int i = 0; i < slot; i++)
			buffer.put(i, pbi.getValueAtIndexOfSlotsStatus(i));
		
	}

	public void writeRecordInBuffer(Record record, ByteBuffer buffer, int offset) {
		List<String> typeColumns = this.relDef.getRelSchema().getTypeColumns();
		String type, recordValue;
		int longueur = 0;
		buffer.position(offset);

		for (int i = 0; i < typeColumns.size(); i++) {
			recordValue = record.getValueAtIndex(i);

			if (typeColumns.get(i).charAt(0) == 'S') {
				type = typeColumns.get(i).substring(0, 6);
				longueur = Integer.parseInt((typeColumns.get(i).substring(6)));
			} else
				type = typeColumns.get(i);

			switch (type) {
			case "int":
				buffer.putInt(Integer.parseInt(recordValue));
			case "float":
				buffer.putFloat(Float.parseFloat(recordValue));
				break;
			case "String":
				for (int j = 0; j < longueur; j++)
					buffer.putChar(recordValue.charAt(j));
				break;
			}
		}
	}

	public PageId addDataPage() throws IOException {

		PageId pid = DiskManager.addPage(this.relDef.getHeaderPage());

		updateHeaderDataPage(pid);

		return pid;

	}

	public PageId getFreePage() throws IOException {

		HeaderPageInfo hpi = new HeaderPageInfo();
		getHeaderPageInfo(hpi);

		for (Info i : hpi.getInfos())
			if (i.getNbSlotsAvailable() > 0)
				return new PageId(this.relDef.getHeaderPage().getFileId(), i.getIdxPages());

		return addDataPage();

	}
	
	public void insertRecordInPage(Record record, PageId pid) throws IOException {
		ByteBuffer buffer = BufferManager.getPage(pid);
		PageBitmapInfo pbi = new PageBitmapInfo(this.relDef.getSlotCount());
		readPageBitmapInfo(buffer, pbi);
		int idx = 0;
		
		while (pbi.getValueAtIndexOfSlotsStatus(idx++)!=0);
		
		writeRecordInBuffer(record, buffer, this.relDef.getSlotCount()+idx*this.relDef.getRecordSize());
		
		writePageBitmapInfo(buffer, pbi);
		
		pbi.setValueAtIndexOfSlotsStatus(idx, (byte) 1);
		
		BufferManager.freePage(pid, true);
		
		
	}

	public void insertRecord(Record record) throws IOException {
		PageId pid = getFreePage();
		insertRecordInPage(record, pid);
		updateHeaderTakenSlot(pid);
	}
}
