package com.time.memory.gui.sticky;

import java.util.List;

public interface SectionAdapter<T extends SectionAdapter.Section> {

	List<T> getSections();

	public abstract class Section<T extends Section> {

		static final int NO_POSITION = -1;

		private boolean isLast;

		private int state;

		public int end = NO_POSITION;

		public int start;

		public List<T> subsections;

		public Section() {

		}

		public int getState() {
			return state;
		}

		public void setState(int state) {
			this.state = state;
		}

		public Section(int start) {
			this(start, NO_POSITION);
		}

		public Section(int start, int end) {
			this(start, end, null);
		}

		public Section(int start, int end, List<T> subsections) {
			this.start = start;
			this.end = end;
			this.subsections = subsections;
		}

		public boolean isLast() {
			return isLast;
		}

		public void setIsLast(boolean isLast) {
			this.isLast = isLast;
		}

		public int getEnd() {
			return end;
		}

		public Section setEnd(int end) {
			this.end = end;
			return this;
		}

		public SectionLayoutManager.SlmConfig getSlmConfig() {
			return null;
		}

		public int getStart() {
			return start;
		}

		public Section setStart(int start) {
			this.start = start;
			return this;
		}

		public List<T> getSubsections() {
			return subsections;
		}

		public Section setSubsections(List<T> subsections) {
			this.subsections = subsections;
			return this;
		}
	}
}
