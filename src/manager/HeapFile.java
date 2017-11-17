package manager;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import bdd.Record;
import bdd.RelDef;
import bdd.RelSchema;

public class HeapFile {

	private RelDef relDef;

	public HeapFile(RelDef relDef) {
		this.relDef = relDef;
	}

	public void createHeader() throws IOException {

		DiskManager.createFile(getFileId());
		DiskManager.addPage(getFileId());
		BufferManager.getPage(getHeaderPage());
		BufferManager.freePage(getHeaderPage(), true);

	}

	public int getFileId() {
		return this.relDef.getHeaderPage().getFileId();
	}

	public int getIdx() {
		return this.relDef.getHeaderPage().getIdx();
	}

	public PageId getHeaderPage() {
		return this.relDef.getHeaderPage();
	}

	public int getSlotCount() {
		return this.relDef.getSlotCount();
	}

	public int getRecordSize() {
		return this.relDef.getRecordSize();
	}

	public RelDef getRelDef() {
		return this.relDef;
	}

	public RelSchema getRelSchema() {
		return this.relDef.getRelSchema();
	}

	public String getRelName() {
		return this.relDef.getRelSchema().getName();
	}

	public int getNbColumns() {
		return this.relDef.getRelSchema().getNbColumns();
	}

	public ArrayList<String> getTypeColumns() {
		return (ArrayList<String>) this.relDef.getRelSchema().getTypeColumns();
	}

	public void readHeaderPageInfo(ByteBuffer buffer, HeaderPageInfo hpi) {
		buffer.position(0);
		hpi.setNbPagesDeDonnees(buffer.getInt());

		for (int i = 0; i < hpi.getNbPagesDeDonnees(); i++) {
			Info info = new Info(buffer.getInt(), buffer.getInt());
			hpi.getInfos().add(i, info);
		}

	}

	public void writeHeaderPageInfo(ByteBuffer buffer, HeaderPageInfo hpi) {
		buffer.position(0);
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
			buffer = BufferManager.getPage(getHeaderPage());
			readHeaderPageInfo(buffer, hpi);
			BufferManager.freePage(getHeaderPage(), false);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void updateHeaderDataPage(PageId newPageId) throws IOException {
		HeaderPageInfo hpi = new HeaderPageInfo();
		PageId headerPage = getHeaderPage();
		ByteBuffer bufferHeader = BufferManager.getPage(headerPage);
		readHeaderPageInfo(bufferHeader, hpi);
		Info info = new Info(getIdx(), getSlotCount());

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
		String type, recordValue;
		int longueur = 0;
		buffer.position(offset);

		for (int i = 0; i < getTypeColumns().size(); i++) {
			recordValue = record.getValueAtIndex(i);

			if (getTypeColumns().get(i).charAt(0) == 'S' || getTypeColumns().get(i).charAt(0) == 's') {
				type = getTypeColumns().get(i).substring(0, 6);
				longueur = Integer.parseInt((getTypeColumns().get(i).substring(6)));
			} else
				type = getTypeColumns().get(i);

			switch (type) {
			case "int":
			case "Int":
			case "INT":
				buffer.putInt(Integer.parseInt(recordValue));
			case "float":
			case "Float":
			case "FLOAT":
				buffer.putFloat(Float.parseFloat(recordValue));
				break;
			case "string":
			case "String":
			case "STRING":
				for (int j = 0; j < longueur; j++)
					buffer.putChar(recordValue.charAt(j));
				break;
			}
		}
	}

	public PageId addDataPage() throws IOException {

		PageId pid = DiskManager.addPage(getFileId());

		updateHeaderDataPage(pid);

		return pid;

	}

	public PageId getFreePage() throws IOException {

		HeaderPageInfo hpi = new HeaderPageInfo();
		getHeaderPageInfo(hpi);

		for (int i = 0 ; i < hpi.getInfos().size() ; i++)
			if (hpi.getInfos().get(i).getNbSlotsAvailable() > 0)
				return new PageId(getFileId(), hpi.getInfos().get(i).getIdxPages());

		return addDataPage();

	}

	public void insertRecordInPage(Record record, PageId pid) throws IOException {
		ByteBuffer buffer = BufferManager.getPage(pid);
		PageBitmapInfo pbi = new PageBitmapInfo(getSlotCount());
		readPageBitmapInfo(buffer, pbi);
		int idx = 0;

		while (pbi.getValueAtIndexOfSlotsStatus(idx++) != 0)
			;

		writeRecordInBuffer(record, buffer, getSlotCount() + idx * getRecordSize());

		writePageBitmapInfo(buffer, pbi);

		pbi.setValueAtIndexOfSlotsStatus(idx, (byte) 1);

		BufferManager.freePage(pid, true);

	}

	public void insertRecord(Record record) throws IOException {
		PageId pid = getFreePage();
		insertRecordInPage(record, pid);
		updateHeaderTakenSlot(pid);
	}

	public void readRecordFromBuffer(Record record, ByteBuffer buffer, int offset) {
		List<String> values = new ArrayList<>();
		String type;
		StringBuffer sb = new StringBuffer("");
		int longueur = 0;
		buffer.position(offset);

		for (int i = 0; i < getTypeColumns().size(); i++) {

			if (getTypeColumns().get(i).charAt(0) == 'S' || getTypeColumns().get(i).charAt(0) == 's') {
				type = getTypeColumns().get(i).substring(0, 6);
				longueur = Integer.parseInt((getTypeColumns().get(i).substring(6)));
			} else
				type = getTypeColumns().get(i);

			switch (type) {
			case "int":
			case "Tnt":
			case "INT":
				values.add(i, "" + buffer.getInt() + "");
			case "float":
			case "Float":
			case "FLOAT":
				values.add(i, "" + buffer.getFloat() + "");
				break;
			case "string":
			case "String":
			case "STRING":
				for (int j = 0; j < longueur; j++)
					sb.append(buffer.getChar());
				values.add(sb.toString());
				sb = new StringBuffer("");
				break;
			}
		}

		record.setValues(values);
	}

	public void printAllRecords() throws IOException {
		int recordCompt = 0;
		HeaderPageInfo hpi = new HeaderPageInfo();
		ByteBuffer buffer;
		PageBitmapInfo pbi = new PageBitmapInfo(getSlotCount());
		Record record = new Record();
		PageId pi = new PageId(getFileId(), 0);

		getHeaderPageInfo(hpi);
		for (int i = 0; i < hpi.getNbPagesDeDonnees(); i++)
			if (hpi.getNbSlotsAvailableAt(i) < getSlotCount()) {
				pi.setIdx(i);
				buffer = BufferManager.getPage(pi);
				readPageBitmapInfo(buffer, pbi);
				for (int j = 0; j < getSlotCount(); j++)
					if (pbi.getValueAtIndexOfSlotsStatus(j) == 1) {
						readRecordFromBuffer(record, buffer, getSlotCount() + j * getRecordSize());
						System.out.println(record);
						recordCompt++;
					}
				BufferManager.freePage(pi, false);
			}
		System.out.println("Total records : " + recordCompt);

	}

	public void printAllRecordsWithFilter(int indexColumn, String value) throws IOException {
		int recordCompt = 0;
		HeaderPageInfo hpi = new HeaderPageInfo();
		ByteBuffer buffer;
		PageBitmapInfo pbi = new PageBitmapInfo(getSlotCount());
		Record record = new Record();
		PageId pi = new PageId(getFileId(), 0);

		getHeaderPageInfo(hpi);
		for (int i = 0; i < hpi.getNbPagesDeDonnees(); i++)
			if (hpi.getNbSlotsAvailableAt(i) < getSlotCount()) {
				pi.setIdx(i);
				buffer = BufferManager.getPage(pi);
				readPageBitmapInfo(buffer, pbi);
				for (int j = 0; j < getSlotCount(); j++)
					if (pbi.getValueAtIndexOfSlotsStatus(j) == 1) {
						readRecordFromBuffer(record, buffer, getSlotCount() + j * getRecordSize());
						if (record.getValueAtIndex(indexColumn).equals(value)) {
							System.out.println(record);
							recordCompt++;
						}
					}
				BufferManager.freePage(pi, false);
			}
		System.out.println("Total records : " + recordCompt);

	}

}
