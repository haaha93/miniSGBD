package manager;

import java.util.ArrayList;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

import bdd.Record;
import bdd.RelDef;
import bdd.RelSchema;
import constant.Constant;
import index.Btree;
import index.Entry;
import index.Rid;

public class HeapFile {

	private RelDef relDef;
	private List<Btree> btrees;

	public HeapFile(RelDef relDef) {
		this.relDef = relDef;
		btrees = new ArrayList<Btree>();
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

	public Btree getBreeWithKey(int key) {
		int index = indexOfTree(key);
		if (index == -1)
			return null;
		return btrees.get(index);
	}

	public int indexOfTree(int key) {
		for (int i = 0; i < btrees.size(); i++)
			if (btrees.get(i).getKey() == key)
				return i;

		return -1;
	}

	public void createHeader() throws IOException {
		ByteBuffer bufferHeader;
		HeaderPageInfo hpi = new HeaderPageInfo();

		hpi.addInfoAt(0, new Info(0, 0));
		DiskManager.createFile(getFileId());
		DiskManager.addPage(getFileId());
		bufferHeader = BufferManager.getPage(getHeaderPage());
		writeHeaderPageInfo(bufferHeader, hpi);
		BufferManager.writePage(getHeaderPage(), bufferHeader);
		BufferManager.freePage(getHeaderPage(), true);
	}

	public void readHeaderPageInfo(ByteBuffer buffer, HeaderPageInfo hpi) {
		buffer.position(0);
		int nbPagesDeDonnees = buffer.getInt();

		for (int i = 0; i < nbPagesDeDonnees; i++) {
			Info info = new Info(buffer.getInt(), buffer.getInt());
			hpi.addInfoAt(i, info);
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

	public void getHeaderPageInfo(HeaderPageInfo hpi) throws IOException {
		ByteBuffer buffer = BufferManager.getPage(getHeaderPage());

		readHeaderPageInfo(buffer, hpi);
		BufferManager.freePage(getHeaderPage(), false);
	}

	public void updateHeaderDataPage(PageId newPageId) throws IOException {
		HeaderPageInfo hpi = new HeaderPageInfo();
		ByteBuffer bufferHeader = BufferManager.getPage(getHeaderPage());
		Info info = new Info(newPageId.getIdx(), getSlotCount());

		readHeaderPageInfo(bufferHeader, hpi);
		hpi.addInfoAt(newPageId.getIdx(), info);
		writeHeaderPageInfo(bufferHeader, hpi);
		BufferManager.writePage(getHeaderPage(), bufferHeader);
		BufferManager.freePage(getHeaderPage(), true);
	}

	public void updateHeaderTakenSlot(PageId pid) throws IOException {
		HeaderPageInfo hpi = new HeaderPageInfo();
		ByteBuffer bufferHeader = BufferManager.getPage(getHeaderPage());

		readHeaderPageInfo(bufferHeader, hpi);
		hpi.incrementSlotAvailableAt(pid.getIdx(), -1);
		writeHeaderPageInfo(bufferHeader, hpi);
		BufferManager.getPage(getHeaderPage());
		BufferManager.writePage(getHeaderPage(), bufferHeader);
		BufferManager.freePage(getHeaderPage(), true);
	}

	public void readPageBitmapInfo(ByteBuffer buffer, PageBitmapInfo pbi) {
		buffer.position(0);

		for (int i = 0; i < getSlotCount(); i++)
			pbi.setValueAtIndexOfSlotsStatus(i, buffer.get());
	}

	public void writePageBitmapInfo(ByteBuffer buffer, PageBitmapInfo pbi) {
		buffer.position(0);

		for (int i = 0; i < getSlotCount(); i++)
			buffer.put(pbi.getValueAtIndexOfSlotsStatus(i));
	}

	public void writeRecordInBuffer(Record record, ByteBuffer buffer, int offset) {
		String type, recordValue;
		int longueur = 0;
		buffer.position(offset);

		for (int i = 0; i < getTypeColumns().size(); i++) {
			recordValue = record.getValueAtIndex(i);

			if (getTypeColumns().get(i).charAt(0) == 'S' || getTypeColumns().get(i).charAt(0) == 's') {
				type = getTypeColumns().get(i).substring(0, 6);
				try {
					longueur = Integer.parseInt(getTypeColumns().get(i).substring(6));
				} catch (NumberFormatException nfe) {
					longueur = Constant.STRINGSIZE;
				}
			} else
				type = getTypeColumns().get(i);

			switch (type.toLowerCase()) {
			case "int":
				buffer.putInt(Integer.parseInt(recordValue));
				break;
			case "float":
				buffer.putFloat(Float.parseFloat(recordValue));
				break;
			case "string":
				if (recordValue.length() >= longueur)
					for (int j = 0; j < longueur; j++)
						buffer.putChar(recordValue.charAt(j));
				else {
					int j = 0;
					for (; j < recordValue.length(); j++)
						buffer.putChar(recordValue.charAt(j));
					for (; j < longueur; j++)
						buffer.putChar(' ');
				}
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

		for (int i = 1; i < hpi.getNbPagesDeDonnees(); i++)
			if (hpi.getInfos().get(i).getNbSlotsAvailable() > 0)
				return new PageId(getFileId(), hpi.getInfos().get(i).getIdxPages());

		return addDataPage();

	}

	public void insertRecordInPage(Record record, PageId pid) throws IOException {
		ByteBuffer buffer = BufferManager.getPage(pid);
		PageBitmapInfo pbi = new PageBitmapInfo(getSlotCount());
		readPageBitmapInfo(buffer, pbi);
		int idx = -1;

		while (pbi.getValueAtIndexOfSlotsStatus(++idx) != 0)
			;

		writeRecordInBuffer(record, buffer, getSlotCount() + idx * getRecordSize());
		pbi.setValueAtIndexOfSlotsStatus(idx, (byte) 1);
		writePageBitmapInfo(buffer, pbi);
		BufferManager.writePage(pid, buffer);
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
		int longueur = 0;
		buffer.position(offset);

		for (int i = 0; i < getTypeColumns().size(); i++) {

			if (getTypeColumns().get(i).charAt(0) == 'S' || getTypeColumns().get(i).charAt(0) == 's') {
				type = getTypeColumns().get(i).substring(0, 6);
				try {
					longueur = Integer.parseInt(getTypeColumns().get(i).substring(6));
				} catch (NumberFormatException nfe) {
					longueur = Constant.STRINGSIZE;
				}
			} else
				type = getTypeColumns().get(i);

			switch (type.toLowerCase()) {
			case "int":
				values.add(i, "" + buffer.getInt() + "");
				break;
			case "float":
				values.add(i, "" + buffer.getFloat() + "");
				break;
			case "string":
				StringBuffer sb = new StringBuffer("");
				for (int j = 0; j < longueur; j++)
					sb.append(buffer.getChar());
				values.add(sb.toString());

				break;
			}
		}

		record.setValues(values);
	}

	public void printAllRecords() throws IOException {
		int recordCompt = 0;
		HeaderPageInfo hpi = new HeaderPageInfo();
		PageBitmapInfo pbi = new PageBitmapInfo(getSlotCount());
		Record record = new Record();

		getHeaderPageInfo(hpi);

		for (int i = 1; i < hpi.getNbPagesDeDonnees(); i++)
			if (hpi.getNbSlotsAvailableAt(i) < getSlotCount()) {
				PageId pi = new PageId(getFileId(), i);
				ByteBuffer buffer = BufferManager.getPage(pi);
				readPageBitmapInfo(buffer, pbi);
				for (int j = 0; j < getSlotCount(); j++)
					if (pbi.getValueAtIndexOfSlotsStatus(j) == 1) {
						readRecordFromBuffer(record, buffer, getSlotCount() + j * getRecordSize());
						System.out.print(++recordCompt + ". ");
						System.out.println(record);
					}
				BufferManager.freePage(pi, false);
			}

		System.out.println("Total records : " + recordCompt);
	}

	public void printAllRecordsWithFilter(int indexColumn, String value) throws IOException {
		int recordCompt = 0;
		HeaderPageInfo hpi = new HeaderPageInfo();
		PageBitmapInfo pbi = new PageBitmapInfo(getSlotCount());
		Record record = new Record();

		getHeaderPageInfo(hpi);

		for (int i = 1; i < hpi.getNbPagesDeDonnees(); i++)
			if (hpi.getNbSlotsAvailableAt(i) < getSlotCount()) {
				PageId pi = new PageId(getFileId(), i);
				ByteBuffer buffer = BufferManager.getPage(pi);
				readPageBitmapInfo(buffer, pbi);
				for (int j = 0; j < getSlotCount(); j++)
					if (pbi.getValueAtIndexOfSlotsStatus(j) == 1) {
						readRecordFromBuffer(record, buffer, getSlotCount() + j * getRecordSize());
						if (record.getValueAtIndex(indexColumn - 1).trim().equals(value)) {
							System.out.print(++recordCompt + ". ");
							System.out.println(record);
						}
					}
				BufferManager.freePage(pi, false);
			}

		System.out.println("Total records : " + recordCompt);

	}

	public void createIndex(int d, int key) throws IOException {
		if (indexOfTree(key) == -1)
			btrees.add(new Btree(d, key));

		int index = indexOfTree(key);
		HeaderPageInfo hpi = new HeaderPageInfo();
		PageBitmapInfo pbi = new PageBitmapInfo(getSlotCount());
		Record record = new Record();

		getHeaderPageInfo(hpi);

		for (int i = 1; i < hpi.getNbPagesDeDonnees(); i++)
			if (hpi.getNbSlotsAvailableAt(i) < getSlotCount()) {
				PageId pi = new PageId(getFileId(), i);
				ByteBuffer buffer = BufferManager.getPage(pi);
				readPageBitmapInfo(buffer, pbi);
				for (int j = 0; j < getSlotCount(); j++)
					if (pbi.getValueAtIndexOfSlotsStatus(j) == 1) {
						int offset = getSlotCount() + j * getRecordSize();
						readRecordFromBuffer(record, buffer, offset);
						String value = record.getValueAtIndex(key);
						Entry entry = btrees.get(index).findEntry(value);
						if (entry == null || entry.getValue() == null)
							btrees.get(index).insertEntry(new Entry(value, new Rid(i, offset)));
						else
							entry.addRid(new Rid(i, offset));
					}
				BufferManager.freePage(pi, false);
			}
	}

	public void selectIndex(int key, String value) throws IOException {
		if (indexOfTree(key) == -1)
			System.out.println("Index does not exist");
		else {
			int index = indexOfTree(key);

			Entry entry = btrees.get(index).findEntry(value);
			int recordCompt = 0;

			if (entry != null)
				for (Rid r : entry.getRids()) {
					PageId pi = new PageId(getFileId(), r.getIdxPage());
					ByteBuffer buffer = BufferManager.getPage(pi);
					Record record = new Record();
					readRecordFromBuffer(record, buffer, r.getOffset());
					System.out.print(++recordCompt + ". ");
					System.out.println(record);
					BufferManager.freePage(pi, false);

				}

			System.out.println("Total records : " + recordCompt);
		}
	}

	public void join(HeapFile hpS, int columnR, int columnS) throws IOException {
		int recordCompt = 0;
		HeaderPageInfo hpiR = new HeaderPageInfo();

		getHeaderPageInfo(hpiR);
		for (int i = 1; i < hpiR.getNbPagesDeDonnees(); i++)
			if (hpiR.getNbSlotsAvailableAt(i) < getSlotCount()) {

				PageId piR = new PageId(getFileId(), i);
				ByteBuffer bufferR = BufferManager.getPage(piR);
				PageBitmapInfo pbiR = new PageBitmapInfo(getSlotCount());
				readPageBitmapInfo(bufferR, pbiR);

				HeaderPageInfo hpiS = new HeaderPageInfo();
				hpS.getHeaderPageInfo(hpiS);

				for (int j = 1; j < hpiS.getNbPagesDeDonnees(); j++)
					if (hpiS.getNbSlotsAvailableAt(j) < hpS.getSlotCount()) {

						PageId piS = new PageId(hpS.getFileId(), j);
						ByteBuffer bufferS = BufferManager.getPage(piS);
						PageBitmapInfo pbiS = new PageBitmapInfo(hpS.getSlotCount());
						hpS.readPageBitmapInfo(bufferS, pbiS);

						for (int k = 0; k < getSlotCount(); k++)
							if (pbiR.getValueAtIndexOfSlotsStatus(k) == 1) {

								Record recordR = new Record();
								readRecordFromBuffer(recordR, bufferR, getSlotCount() + k * getRecordSize());
								String valueR = recordR.getValueAtIndex(columnR);

								for (int l = 0; l < hpS.getSlotCount(); l++)
									if (pbiS.getValueAtIndexOfSlotsStatus(l) == 1) {

										Record recordS = new Record();
										hpS.readRecordFromBuffer(recordS, bufferS,hpS.getSlotCount() + l * hpS.getRecordSize());
										String valueS = recordS.getValueAtIndex(columnS);

										if (valueR.trim().equals(valueS.trim())) {
											System.out.print(++recordCompt + ". ");
											System.out.println(recordR + " " + recordS);
										}
									}
							}
						BufferManager.freePage(piS, false);
					}
				BufferManager.freePage(piR, false);
			}
		System.out.println("Total records : " + recordCompt);
	}

	public void joinindex(HeapFile hpS, int columnR, int columnS) throws IOException {
		Btree btree = hpS.getBreeWithKey(columnS);

		if (btree == null) {
			System.out.println("Index does not exist");
		} else {
			int recordCompt = 0;
			HeaderPageInfo hpiR = new HeaderPageInfo();
			PageBitmapInfo pbiR = new PageBitmapInfo(getSlotCount());
			Record recordR = new Record();

			getHeaderPageInfo(hpiR);
			for (int i = 1; i < hpiR.getNbPagesDeDonnees(); i++)
				if (hpiR.getNbSlotsAvailableAt(i) < getSlotCount()) {
					PageId piR = new PageId(getFileId(), i);
					ByteBuffer bufferR = BufferManager.getPage(piR);
					readPageBitmapInfo(bufferR, pbiR);

					for (int j = 0; j < getSlotCount(); j++)
						if (pbiR.getValueAtIndexOfSlotsStatus(j) == 1) {
							readRecordFromBuffer(recordR, bufferR, getSlotCount() + j * getRecordSize());
							Entry entry = btree.findEntry(recordR.getValueAtIndex(columnR));
							if (entry != null)
								for (Rid r : entry.getRids()) {
									PageId piS = new PageId(hpS.getFileId(), r.getIdxPage());
									ByteBuffer bufferS = BufferManager.getPage(piS);
									Record recordS = new Record();
									hpS.readRecordFromBuffer(recordS, bufferS, r.getOffset());
									System.out.print(++recordCompt + ". ");
									System.out.println(recordR + " " + recordS);
									BufferManager.freePage(piS, false);
								}
						}
					BufferManager.freePage(piR, false);
				}
			System.out.println("Total records : " + recordCompt);
		}
	}
}
